package com.stylezone.demo.services;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Offer;
import com.stylezone.demo.models.Picture;
import com.stylezone.demo.models.Opening;
import com.stylezone.demo.models.Staff;
import com.stylezone.demo.models.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.ArrayList;
import java.util.List;

@Service
public interface AdminService {
    //Booking
    Booking findBooking(int bookingId);
    List<Booking> getBookings();
    Booking updateBooking(Booking booking);
    void deleteBooking(int bookingId);
    List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd);
    List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd);
    Booking createBooking(Booking booking);
    Boolean isBooked(String bookingDate, String bookingTime);

    String generateRandomString();

    //Holiday
    Holiday findHolidayById(int holidayId);
    Holiday findHolidayByDate(String holidayDate);
    Boolean isHolidayByDate(String holidayDate);
    List<Holiday> getHolidays();

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
    List<Offer> showOffers();
    Offer createOffer(Offer offer);
    Offer updateOffer(Offer offer);
    void deleteOffer(int id);
    Offer findOffer(int id);
    //Pictures

    List<Picture> getPictures();
    String insertPicture(String picture);
    String fileUpload(MultipartFile file);

    //Opening
    Opening findOpening(int openingId);
    Opening[] getOpenings();
    Opening saveOpeningHours(Opening opening);
    Opening returnConvertedOpenings(Opening opening);

    //ArrayList<Opening> getTimes();
    ArrayList<Integer> getHours();
    ArrayList<Integer> getMin();
    ArrayList<String> getDays();
    Opening[] convertOpenings();

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

    //Mail
    void sendMessageMail(Booking booking);
    void editBookingMail(Booking booking);
    void deleteBookingMail(Booking booking);
    void createBookingMail(Booking booking);
    void sendEmail(String mailText, String subject, String mailTo);

}
