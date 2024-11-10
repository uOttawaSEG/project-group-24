package com.example.eams;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class Event {
    private int eventId;
    private String eventName;
    private String eventDescription;
    private String eventDate;
    private String startTime;
    private String endTime;
    private String eventLocation;
    private boolean requiresApproval;
    private String eventOrganizer;

    public Event(int eventId, String eventName, String eventDescription, String eventDate, String startTime, String endTime, String eventLocation, boolean requiresApproval, String eventOrganizer) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventLocation = eventLocation;
        this.requiresApproval = requiresApproval;
        this.eventOrganizer = eventOrganizer;
    }

    // Getters and Setters for all fields
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public String getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(String eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    // Method to check if the event is a past event
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isPastEvent() {
        // Parse event end time as LocalDateTime for comparison
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime eventEndTime = LocalDateTime.parse(eventDate + " " + endTime, formatter);

        // Get current time in EST (Eastern Standard Time)
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("America/New_York"));

        // Check if the event's end time is before the current time
        return eventEndTime.isBefore(currentTime);
    }

    @Override
    public String toString() {
        // Customize the string representation to show meaningful information
        return eventName + " (" + eventDate + " " + startTime + " - " + endTime + ")";
    }
}
