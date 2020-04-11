package com.ebay.calendar;

import com.ebay.calendar.service.Meeting;
import com.ebay.calendar.service.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Calendar {
    @Autowired
    private Scheduler scheduler;

    @PostMapping(path = "/api/setMeeting", consumes = "application/json", produces = "application/json")
    public ResponseMsg setMeeting(@RequestBody Meeting meeting){
        return scheduler.schedule(meeting);
    }

    @DeleteMapping(path = "/api/removeMeeting/{time}", consumes = "application/json", produces = "application/json")
    public ResponseMsg removeMeeting(@PathVariable String time){
        return scheduler.remove(time);
    }

    @DeleteMapping(path = "/api/removeMeetingByTitle/{title}", consumes = "application/json", produces = "application/json")
    public ResponseMsg removeMeetingByTitle(@PathVariable String title){
        return scheduler.removeByTitle(title);
    }

    @GetMapping(path = "/api/getNextMeeting", consumes = "application/json", produces = "application/json")
    public ResponseMsg getNextMeeting(){
        return scheduler.getNextMeeting();
    }
}
