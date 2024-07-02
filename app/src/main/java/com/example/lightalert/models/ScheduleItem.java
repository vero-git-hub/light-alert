package com.example.lightalert.models;

public class ScheduleItem {

    private String day;
    private String timeSlot;
    private String status;

    public ScheduleItem(String day, String timeSlot, String status) {
        this.day = day;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
