package com.stylezone.demo.models;

public class Opening {
    private int openingId;
    private String openingDay;
    private String openingTime;
    private String openingClose;

    public Opening() {
    }

    public Opening(int openingId, String openingDay, String openingTime, String openingClose) {
        this.openingId = openingId;
        this.openingDay = openingDay;
        this.openingTime = openingTime;
        this.openingClose = openingClose;
    }

    public int getOpeningId() {
        return openingId;
    }

    public void setOpeningId(int openingId) {
        this.openingId = openingId;
    }

    public String getOpeningDay() {
        return openingDay;
    }

    public void setOpeningDay(String openingDay) {
        this.openingDay = openingDay;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getOpeningClose() {
        return openingClose;
    }

    public void setOpeningClose(String openingClose) {
        this.openingClose = openingClose;
    }

    @Override
    public String toString() {
        return "Opening{" +
                "openingId=" + openingId +
                ", openingDay='" + openingDay + '\'' +
                ", openingTime=" + openingTime +
                ", openingClose=" + openingClose +
                '}';
    }
}
