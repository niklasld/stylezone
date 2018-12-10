package com.stylezone.demo.repositories;

import com.stylezone.demo.models.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo {
    //Booking
    List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd);
    List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd);
    Booking createBooking(Booking booking);
    Booking isBooked(String bookingDate, String bookingTime);

    //Holiday
    Holiday findHolidayById(int holidayId);
    Holiday findHolidayByDate(String holidayDate);
    Holiday isHolidayByDate(String holidayDate);
    List<Holiday> getHolidays();

    //Admin
    Admin checkPassword(Admin admin);
    Admin searchUser(Admin admin);

    //Staff
    List<Staff> getStaff();
    Staff getStaffMember(int staffId);
    Staff updateStaff(Staff staff);
    void deleteStaffMember(int staffId);
    Staff createStaffMember(Staff staff);

    //Opening
    Opening findOpening(int openingId);
    Opening[] getOpenings();

    //Offers
    Offer findOffer(int offerId);
    List<Offer> getOffers();
    Offer createOffer(Offer offer);
    Offer updateOffer(Offer offer);
}
