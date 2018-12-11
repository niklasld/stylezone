package com.stylezone.demo.repositories;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Offer;

import com.stylezone.demo.models.Picture;
import com.stylezone.demo.models.Opening;
import com.stylezone.demo.models.Staff;
import com.stylezone.demo.models.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo {
    //Booking
    Booking findBooking(int bookingId);
    List<Booking> getBookings();
    Booking updateBooking(Booking booking);
    void deleteBooking(int bookingId);
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

    //Offers
    Offer findOffer(int offerId);
    List<Offer> getOffers();
    List<Offer> showOffers();
    Offer createOffer(Offer offer);
    Offer updateOffer(Offer offer);

    //Pictures
    List<Picture> getPictures();
    String insertPicture(String picture);

    //Opening
    Opening findOpening(int openingId);
    Opening[] getOpenings();
    Opening saveOpeningHours(Opening opening);
}
