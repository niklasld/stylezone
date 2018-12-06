package com.stylezone.demo.services;

import com.stylezone.demo.models.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    //Booking
    List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd);
    List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd);

    //Holiday
    Holiday findHolidayById(int holidayId);
    Holiday findHolidayByDate(String holidayDate);
    Boolean isHolidayByDate(String holidayDate);
    List<Holiday> getHolidays();

    //Opening
    Opening findOpening(int openingId);
    Opening[] getOpenings();

    //Admin
    int hashPassword(String password);
    Admin searchUser(Admin admin);

    //Staff
    List<Staff> getStaff();
    Staff getStaffMember(int staffId);
    Staff updateStaff(Staff staff);
    void deleteStaffMember(int staffId);
    Staff createStaffMember(Staff staff);

    //Offers
    List<Offer> getOffers();
    Offer createOffer(Offer offer);
    Offer updateOffer(Offer offer);
    void deleteOffer(int id);
    Offer findOffer(int id);

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

}
