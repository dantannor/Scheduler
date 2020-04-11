Calendar App
Author: Daniel Tannor

A webservice which offers the following APIs:
1. Set Meeting - with parameters fromTime, ToTime, meetingTitle
2. Remove Meeting - with parameter fromTime
3. Remove Meeting - with parameter meetingTitle
4. GetNextMeeting - with no parameters

The following rules are applied:
1. A meeting cannot last for more than 2 hours, and for less than 15 minutes
2. Two meetings cannot overlap
3. No meetings on Saturdays
4. Meeting titles are not unique, so removing a meeting by title (API #3) might result in
removing more than one meeting
5. There are up to 40 working hours a week (Sunday to Friday) - any meeting for the same
week exceeding 40h should be rejected with proper error
6. There are up to 10 working hours a day - the last meeting on the same day must end up
to 10 hours after the first meeting started. Trying to set a meeting that contradicts this
should be rejected with proper error
7. Every API must return a proper response that reports what was done

EXECUTION
Simply download the code and run 'CalendarApplication'

Example Requests:

1. Set Meeting
POST http://localhost:8080/api/setMeeting
{
	"title": "Parrot Seed Discussion",
	"fromTime": "2019-04-03T20:56:31.585",
	"toTime": "2019-04-03T21:57:30.585"
}

2. Delete meeting by Date: 2019-05-03T20:56:31.585
DELETE http://localhost:8080/api/removeMeeting/2019-05-03T20:56:31.585

3. Delete by title 'ebay'
DELETE http://localhost:8080/api/removeMeetingByTitle/ebay

4. Get next meeting
GET http://localhost:8080/api/getNextMeeting

Assumptions/Thoughts:

- I did not validate inputs (null, etc.)
- I'm not blocking the ability to set meetings in the past (like Google Calendar) - no strict policy
- It was written to use the method 'Set Meeting', which is not very RESTy, so I am being lenient with the URLs
- I didn't use Java/Spring for a few years and my time on the project is limited, so there aren't tests and runtimes are not as optimal as I would like them to be. Not sure about the 'Spring' way to do things either.
