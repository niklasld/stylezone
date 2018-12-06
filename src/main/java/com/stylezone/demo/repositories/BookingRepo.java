package com.stylezone.demo.repositories;

import com.stylezone.demo.models.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface BookingRepo {
    //Booking
    Booking findBooking(int bookingId);
    Booking findBookingByDateTime(String bookingDate, String bookingTime);
    Booking isBooked(String bookingDate, String bookingTime);
    List<Booking> getBookings();
    Booking saveBooking(Booking booking);
    List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd);
    List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd);
    Booking updateBooking(Booking booking);
    void deleteBooking(int bookingId);

    //Holiday
    Holiday findHolidayById(int holidayId);
    Holiday findHolidayByDate(String holidayDate);
    Holiday isHolidayByDate(String holidayDate);
    List<Holiday> getHolidays();
    /*Holiday createHoliday(Holiday holiday);
    Holiday updateHoliday(Holiday holiday);
    void deleteHoliday(int holidayId);*/

    //Opening
    Opening findOpening(int openingId);
    Opening[] getOpenings();

    //Staff
    List<Staff> getStaff();
    Staff getStaffMember(int staffId);

}
