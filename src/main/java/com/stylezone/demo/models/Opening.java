package com.stylezone.demo.models;

public class Opening {
    private int openingId;
    private String openingDay;
    private String openingTime;
    private String openingClose;
    private String openingHour, closingHour, openingMin, closingMin;

    public Opening() {
    }

    public Opening(String openingTime, String openingClose) {
        this.openingTime = openingTime;
        this.openingClose = openingClose;
    }

    public Opening(int openingId, String openingDay, String openingTime, String openingClose) {
        this.openingId = openingId;
        this.openingDay = openingDay;
        this.openingTime = openingTime;
        this.openingClose = openingClose;
    }

    public Opening(String openingHour, String closingHour, String openingMin, String closingMin) {
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.openingMin = openingMin;
        this.closingMin = closingMin;
    }


    public String getOpeningHour() {
        return openingHour;
    }

    public void setOpeningHour(String openingHour) {
        this.openingHour = openingHour;
    }

    public String getClosingHour() {
        return closingHour;
    }

    public void setClosingHour(String closingHour) {
        this.closingHour = closingHour;
    }

    public String getOpeningMin() {
        return openingMin;
    }

    public void setOpeningMin(String openingMin) {
        this.openingMin = openingMin;
    }

    public String getClosingMin() {
        return closingMin;
    }

    public void setClosingMin(String closingMin) {
        this.closingMin = closingMin;
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
