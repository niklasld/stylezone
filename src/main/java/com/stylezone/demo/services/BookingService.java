package com.stylezone.demo.services;

import com.stylezone.demo.models.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@Service
public interface BookingService {
    //Booking
    Booking findBooking(int bookingId);
    Booking findBookingByDateTime(String bookingDate, String bookingTime);
    Boolean isBooked(String bookingDate, String bookingTime);
    List<Booking> getBookings();
    Booking saveBooking(Booking booking);
    List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd);
    List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd);
    Booking updateBooking(Booking booking);
    void deleteBooking(int bookingId);

    String generateRandomString();

    //Holiday
    Holiday findHolidayById(int holidayId);
    Holiday findHolidayByDate(String holidayDate);
    Boolean isHolidayByDate(String holidayDate);
    List<Holiday> getHolidays();
    /*Holiday createHoliday(Holiday holiday);
    Holiday updateHoliday(Holiday holiday);
    void deleteHoliday(int holidayId);*/

    //Opening
    Opening findOpening(int openingId);
    Opening[] getOpenings();

    //Mail
    void sendEmail(Booking booking);

    //calender
    int getWeekToday();
    int getWeekFromDate(int day, int month, int year);
    String getDateToday();
    String nextWeek();
    String nextWeekFromDate(int day, int month, int year);
    String prevWeek();
    String prevWeekFromDate(int day, int month, int year);
    String[] getDatesOfWeek();
    String[] getDatesOfSelectedWeek(int day, int month, int year);

    //Staff
    List<Staff> getStaff();
    Staff getStaffMember(int staffId);

}

