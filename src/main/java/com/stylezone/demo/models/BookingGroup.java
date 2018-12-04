package com.stylezone.demo.models;

public class BookingGroup {
    private int bookingGroupId, boookingGroupBooked, boookingGroupTotal, bookingGroupFree;
    private String bookingGroupStart, bookingGroupEnd, bookingGroupDate;

    public BookingGroup() {
    }

    public BookingGroup(String bookingGroupStart, int boookingGroupBooked) {
        this.boookingGroupBooked = boookingGroupBooked;
        this.bookingGroupStart = bookingGroupStart;
    }

    public BookingGroup(String bookingGroupStart, String bookingGroupEnd, String bookingGroupDate, int boookingGroupBooked, int boookingGroupTotal) {
        this.bookingGroupStart = bookingGroupStart;
        this.bookingGroupEnd = bookingGroupEnd;
        this.bookingGroupDate = bookingGroupDate;
        this.boookingGroupBooked = boookingGroupBooked;
        this.boookingGroupTotal = boookingGroupTotal;
        this.bookingGroupFree = boookingGroupTotal - boookingGroupBooked;
    }

    public int getBookingGroupId() {
        return bookingGroupId;
    }

    public void setBookingGroupId(int bookingGroupId) {
        this.bookingGroupId = bookingGroupId;
    }

    public String getBookingGroupStart() {
        return bookingGroupStart;
    }

    public void setBookingGroupStart(String bookingGroupStart) {
        this.bookingGroupStart = bookingGroupStart;
    }

    public String getBookingGroupEnd() {
        return bookingGroupEnd;
    }

    public void setBookingGroupEnd(String bookingGroupEnd) {
        this.bookingGroupEnd = bookingGroupEnd;
    }

    public String getBookingGroupDate() {
        return bookingGroupDate;
    }

    public void setBookingGroupDate(String bookingGroupDate) {
        this.bookingGroupDate = bookingGroupDate;
    }

    public int getBoookingGroupBooked() {
        return boookingGroupBooked;
    }

    public void setBoookingGroupBooked(int boookingGroupBooked) {
        this.boookingGroupBooked = boookingGroupBooked;
    }

    public int getBoookingGroupTotal() {
        return boookingGroupTotal;
    }

    public void setBoookingGroupTotal(int boookingGroupTotal) {
        this.boookingGroupTotal = boookingGroupTotal;
    }

    public int getBookingGroupFree() {
        return bookingGroupFree;
    }

    public void setBookingGroupFree(int boookingGroupFree) {
        this.bookingGroupFree = boookingGroupFree;
    }
}
