package com.ebay.calendar.service;

import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;

/**
 * Meetings are based on date time to support year round schedules
 */
public class Meeting implements Comparable<Meeting>{

    private String title;

    private LocalDateTime fromTime;

    private LocalDateTime toTime;

    public Meeting() {
    }

    public Meeting(String title, LocalDateTime fromTime, LocalDateTime toTime) {
        this.title = title;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    /**
     * Compares meeting based on intervals and then according to the 'fromTime'
     * @param that
     * @return
     */
    public int compareTo(Meeting that) {
        Interval meetingInterval = new Interval(fromTime.toDateTime(), toTime.toDateTime());
        Interval otherInterval = new Interval(that.getFromTime().toDateTime(), that.getToTime().toDateTime());

        if(meetingInterval.overlaps(otherInterval))
            return 0;

        return this.getFromTime().compareTo(that.getFromTime());
    }

    public int getDurationMinutes(){
        return Minutes.minutesBetween(fromTime, toTime).getMinutes();
    }

    @Override
    public String toString() {
        return "meeting '" + title + "' from " + fromTime +
                " to " + toTime;
    }
}
