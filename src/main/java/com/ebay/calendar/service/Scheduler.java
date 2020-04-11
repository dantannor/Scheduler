package com.ebay.calendar.service;

import com.ebay.calendar.ResponseMsg;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * In charge of scheduling meetings
 */
@Component
public class Scheduler {
    private List<Meeting> meetings;
    private static int WEEKLY_LIMIT_MINUTES = 40 * 60;
    private static int DAILY_LIMIT_MINUTES = 10 * 60;

    public Scheduler() {
        this.meetings = new ArrayList<>();
    }

    public ResponseMsg schedule(Meeting meeting) {
        ResponseMsg msg = validate(meeting);
        if(msg != null){
            return msg;
        }

        if (meetings.isEmpty()) {
            meetings.add(meeting);
            return new ResponseMsg(200, "Scheduled " + meeting + "\n\n"+ "All Meetings: " + meetings);
        }

        int pos = -Collections.binarySearch(meetings, meeting);
        if (pos > 0) {
            meetings.add(pos-1, meeting);
            return new ResponseMsg(200, "Scheduled " + meeting + "\n\n"+ "All Meetings: " + meetings);
        } else {
            return new ResponseMsg(400, "Collision with " + meetings.get(-pos));
        }
    }

    private ResponseMsg validate(Meeting meeting) {
        int minutes = meeting.getDurationMinutes();
        if(minutes < 15){
            return new ResponseMsg(400, "Meeting is less than 15 minutes");
        }
        if(minutes > 120){
            return new ResponseMsg(400, "Meeting is longer than 2 hours");
        }

        if(meeting.getFromTime().toLocalDate().getDayOfWeek() == 6 || meeting.getToTime().toLocalDate().getDayOfWeek() == 6){
            return new ResponseMsg(400, "Cannot set meeting on a Saturday");
        }
        if (invalidWeeklyHours(meeting, minutes))
            return new ResponseMsg(400, "Cannot set meeting, exceeds 40 hour work week");

        if(invalidDailyHours(meeting))
            return new ResponseMsg(400, "Cannot set meeting, exceeds 10 hour daily limit");

        return null;
    }

    private boolean invalidDailyHours(Meeting meeting) {
        for (Meeting m: meetings){
            boolean equalDates = meeting.getFromTime().toLocalDate().isEqual(m.getFromTime().toLocalDate());

            boolean exceedsBefore = false;
            boolean exceedsAfter = false;

            if(equalDates){
                exceedsBefore = Minutes.minutesBetween(meeting.getFromTime(), m.getToTime()).getMinutes() > DAILY_LIMIT_MINUTES;
                exceedsAfter = Minutes.minutesBetween(m.getFromTime(), meeting.getToTime()).getMinutes() > DAILY_LIMIT_MINUTES;
            }

            if(exceedsAfter || exceedsBefore){
                return true;
            }
        }

        return false;
    }

    private boolean invalidWeeklyHours(Meeting meeting, int minutes) {
        // Not handling cases where meetings can occupy two different weeks
        final int meetingWeek = meeting.getFromTime().getWeekOfWeekyear();

        int durationSum = minutes;
        // can optimize this to use the binary search, currently O(n)
        for (Meeting m: meetings){
            int week = m.getFromTime().getWeekOfWeekyear();
            if(meetingWeek == week){
                durationSum += m.getDurationMinutes();
            }
        }

        return durationSum > WEEKLY_LIMIT_MINUTES;
    }

    public ResponseMsg remove(String dateStr) {
        DateTime date = DateTime.parse(dateStr);
        // This is a hack so we can use the binary search to find the meeting to remove
        Meeting meeting = new Meeting("Meeting to remove", date.toLocalDateTime(), date.plusSeconds(1).toLocalDateTime());

        if (meetings.isEmpty()) {
            return new ResponseMsg(200, "No meetings to remove " + meeting + "\n\n"+ "All Meetings: " + meetings);
        }

        int pos = -Collections.binarySearch(meetings, meeting);
        if (pos > 0) {
            return new ResponseMsg(400, "No such meeting starts at " + meeting.getFromTime());
        } else {
            Meeting m = meetings.get(-pos);
            meetings.remove(-pos);
            return new ResponseMsg(200, "Removed meeting " + m);
        }
    }

    public ResponseMsg removeByTitle(String name) {
        boolean res = meetings.removeIf(m -> m.getTitle().equals(name));

        if (res){
            return new ResponseMsg(200, "Removed meetings with title " + name);
        } else {
            return new ResponseMsg(200, "Didn't find any meetings with title " + name);
        }
    }

    /**
     * Since we support setting events in the past, 'getNextMeeting' looks for events from now onwards
     * @return
     */
    public ResponseMsg getNextMeeting() {
        Optional<Meeting> res = meetings.stream()
                .filter(m -> m.getFromTime()
                .isAfter(DateTime.now().toLocalDateTime()))
                .findFirst();

        return res.map(meeting -> new ResponseMsg(200, meeting.toString())).orElseGet(() -> new ResponseMsg(200, "No planned meetings"));
    }
}
