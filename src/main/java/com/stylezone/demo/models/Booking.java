package com.stylezone.demo.models;

public class Booking {
    private int bookingId;
    private String bookingTime;
    private String bookingDate;
    private String bookingName;
    private String bookingEmail;
    private int bookingPhone;
    private String bookingComment;
    private String bookingMessage;
    private String bookingToken;
    private int staffId;


    public Booking() {
    }

    public Booking(int bookingId) {
        this.bookingId = bookingId;
    }

    public Booking(String bookingTime, String bookingName) {
        this.bookingTime = bookingTime;
        this.bookingName = bookingName;
    }

    public Booking(int bookingId, String bookingTime, String bookingName) {
        this.bookingId = bookingId;
        this.bookingTime = bookingTime;
        this.bookingName = bookingName;
    }

    public Booking(String bookingTime, String bookingDate, String bookingName, String bookingEmail, int bookingPhone, String bookingComment, int staffId) {
        this.bookingTime = bookingTime;
        this.bookingDate = bookingDate;
        this.bookingName = bookingName;
        this.bookingEmail = bookingEmail;
        this.bookingPhone = bookingPhone;
        this.bookingComment = bookingComment;
        this.staffId = staffId;
    }

    public Booking(String bookingTime, String bookingDate, String bookingName, String bookingEmail, int bookingPhone, String bookingComment, String bookingMessage, int staffId) {
        this.bookingTime = bookingTime;
        this.bookingDate = bookingDate;
        this.bookingName = bookingName;
        this.bookingEmail = bookingEmail;
        this.bookingPhone = bookingPhone;
        this.bookingComment = bookingComment;
        this.bookingMessage = bookingMessage;
        this.staffId = staffId;
    }

    public Booking(int bookingId, String bookingTime, String bookingDate, String bookingName, String bookingEmail, int bookingPhone, String bookingComment, int staffId) {

        this.bookingId = bookingId;
        this.bookingTime = bookingTime;
        this.bookingDate = bookingDate;
        this.bookingName = bookingName;
        this.bookingEmail = bookingEmail;
        this.bookingPhone = bookingPhone;
        this.bookingComment = bookingComment;
        this.staffId = staffId;
    }

    public Booking(int bookingId, String bookingTime, String bookingDate, String bookingName, String bookingEmail, int bookingPhone, String bookingComment, String bookingToken, int staffId) {
        this.bookingId = bookingId;
        this.bookingTime = bookingTime;
        this.bookingDate = bookingDate;
        this.bookingName = bookingName;
        this.bookingEmail = bookingEmail;
        this.bookingPhone = bookingPhone;
        this.bookingComment = bookingComment;
        this.bookingToken = bookingToken;
        this.staffId = staffId;
    }

    public Booking(int bookingId, String bookingTime, String bookingDate, String bookingName, String bookingEmail, int bookingPhone, String bookingComment, String bookingMessage, String bookingToken, int staffId) {
        this.bookingId = bookingId;
        this.bookingTime = bookingTime;
        this.bookingDate = bookingDate;
        this.bookingName = bookingName;
        this.bookingEmail = bookingEmail;
        this.bookingPhone = bookingPhone;
        this.bookingComment = bookingComment;
        this.bookingMessage = bookingMessage;
        this.bookingToken = bookingToken;
        this.staffId = staffId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public String getBookingEmail() {
        return bookingEmail;
    }

    public void setBookingEmail(String bookingEmail) {
        this.bookingEmail = bookingEmail;
    }

    public int getBookingPhone() {
        return bookingPhone;
    }

    public void setBookingPhone(int bookingPhone) {
        this.bookingPhone = bookingPhone;
    }

    public String getBookingComment() {
        return bookingComment;
    }

    public void setBookingComment(String bookingComment) {
        this.bookingComment = bookingComment;
    }

    public String getBookingMessage() {
        return bookingMessage;
    }

    public void setBookingMessage(String bookingMessage) {
        this.bookingMessage = bookingMessage;
    }

    public void clearBookingMessage() {
        this.bookingMessage = null;
    }

    public String getBookingToken() {
        return bookingToken;
    }

    public void setBookingToken(String bookingToken) {
        this.bookingToken = bookingToken;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    @Override
    public String   toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", bookingTime='" + bookingTime + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", bookingName='" + bookingName + '\'' +
                ", bookingEmail='" + bookingEmail + '\'' +
                ", bookingPhone=" + bookingPhone +
                ", bookingComment='" + bookingComment + '\'' +
                ", staffId=" +  staffId +
                '}';
    }
}
