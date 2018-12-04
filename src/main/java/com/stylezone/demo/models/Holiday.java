package com.stylezone.demo.models;

public class Holiday {
    private int holidayId;
    private String holidayDate;
    private String holidayName;

    public Holiday() {
    }

    public Holiday(int holidayId, String holidayDate, String holidayName) {
        this.holidayId = holidayId;
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
    }

    public int getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }

    public String getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(String holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    @Override
    public String toString() {
        return "Holiday{" +
                "holidayId=" + holidayId +
                ", holidayDate='" + holidayDate + '\'' +
                ", holidayName='" + holidayName + '\'' +
                '}';
    }
}
