package com.example.eams;

public class Event {
    private String title;
    private String description;
    private String date;
    private String startTime;
    private String endTime;
    private String location;
    private boolean requiresApproval;
    private String organizerId;

    public Event(String title, String description, String date, 
                 String startTime, String endTime, String location, 
                 boolean requiresApproval, String organizerId) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.requiresApproval = requiresApproval;
        this.organizerId = organizerId;
    }

    // Add getters for all fields
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getLocation() { return location; }
    public boolean getRequiresApproval() { return requiresApproval; }
    public String getOrganizerId() { return organizerId; }
} 