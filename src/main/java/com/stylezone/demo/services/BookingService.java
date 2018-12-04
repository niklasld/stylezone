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
    List<Booking> getBookings();
    Booking saveBooking(Booking booking);
    List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd);
    List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd);
    Booking updateBooking(Booking booking);
    void deleteBooking(int bookingId);

    //Holiday
    Holiday findHoliday(int holidayId);
    List<Holiday> getHolidays();
    /*Holiday createHoliday(Holiday holiday);
    Holiday updateHoliday(Holiday holiday);
    void deleteHoliday(int holidayId);*/

    //Opening
    Opening findOpening(int openingId);
    List<Opening> getOpenings();
    //Mail
    void sendEmail(Booking booking);

    //calender
    public int getWeekToday();
    public String getDateToday();
    public String[] getDatesOfWeek();

    //Offers
    List<Offer> getOffers();
    Offer createOffer(Offer offer);
    Offer updateOffer(Offer offer);
    void deleteOffer(int id);
    Offer findOffer(int id);
}
