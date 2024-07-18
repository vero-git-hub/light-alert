package com.example.lightalert.ui.dashboard;

public class HourStatus {
    private int hour;
    private String topPart;
    private String bottomPart;

    public HourStatus(int hour, String topPart, String bottomPart) {
        this.hour = hour;
        this.topPart = topPart;
        this.bottomPart = bottomPart;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getTopPart() {
        return topPart;
    }

    public void setTopPart(String topPart) {
        this.topPart = topPart;
    }

    public String getBottomPart() {
        return bottomPart;
    }

    public void setBottomPart(String bottomPart) {
        this.bottomPart = bottomPart;
    }

    @Override
    public String toString() {
        return "HourStatus{" +
                "hour=" + hour +
                ", topPart='" + topPart + '\'' +
                ", bottomPart='" + bottomPart + '\'' +
                '}';
    }
}
