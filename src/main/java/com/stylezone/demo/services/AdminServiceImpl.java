package com.stylezone.demo.services;

import com.stylezone.demo.models.*;
import com.stylezone.demo.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepo adminRepo;

    Logger log = Logger.getLogger(AdminServiceImpl.class.getName());

    private final boolean DEVELOPER_MODE = false;

    @Override
    public List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd) {
        if(DEVELOPER_MODE) {
            log.info("BookingService.getSelectedBookings(" + date + ", " + timeStart + ", " + timeEnd + ")");
        }

        List<Booking> temp = adminRepo.getSelectedBookings(date, timeStart, timeEnd);

        if(DEVELOPER_MODE) {
            log.info("temp length: " + temp.size());
        }

        int bookingId, bookingPhone, staffId;
        String bookingTime, bookingDate, bookingName, bookingComment;
        List<Booking> bookings = new ArrayList<>();

        int hour = Integer.parseInt(timeStart.substring(0, 2));
        int start = Integer.parseInt(timeStart.substring(3, 5));
        int end;
        if (Integer.parseInt(timeEnd.substring(3, 5)) < 10) {
            end = 50;
        } else {
            end = Integer.parseInt(timeEnd.substring(3, 5));
            end = end - 10;
        }

        if(DEVELOPER_MODE) {
            log.info("start:" + start + ", end:" + end);
        }

        assert start < end;

        for (int i = start; i <= end; i = i + 10) {

            bookingTime = hour + ":" + i;
            if (i < 1) {
                bookingTime = hour + ":00";
            }

            bookingName = "";

            for (Booking t : temp) {
                if (i == Integer.parseInt(t.getBookingTime().substring(3, 5))) {
                    bookingName = t.getBookingName();
                }
            }

            if(DEVELOPER_MODE) {
                log.info("start:" + start + ", end:" + end + ", i:" + i);
                log.info("bookingTime:" + bookingTime + ", bookingName:" + bookingName);
            }

            bookings.add(new Booking(bookingTime, bookingName));
        }

        return bookings;
    }

    @Override
    public List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd) {
        if(DEVELOPER_MODE) {
            log.info("BookingService.getBookingGroups(" + date + ", " + timeStart + ", " + timeEnd + ")");
        }
        List<BookingGroup> temp = adminRepo.getBookingGroups(date, timeStart, timeEnd);
        if(DEVELOPER_MODE) {
            log.info("bookingGroups length" + temp.size());
        }

        int bookingGroupId, boookingGroupBooked, boookingGroupTotal;
        String bookingGroupStart, bookingGroupEnd, bookingGroupDate;
        List<BookingGroup> bookingGroups = new ArrayList<>();

        for (int i = Integer.parseInt(timeStart.substring(0, 2)); i <= Integer.parseInt(timeEnd.substring(0, 2)); i++) {

            boookingGroupTotal = 6;

            bookingGroupDate = date;

            bookingGroupStart = "" + i + ":00";
            bookingGroupEnd = "" + (i + 1) + ":00";


            if (i == Integer.parseInt(timeStart.substring(0, 2))) {
                int param = Integer.parseInt(timeStart.substring(3, 5));
                switch (param) {
                    case 00:
                        boookingGroupTotal = 6;
                        break;
                    case 10:
                        boookingGroupTotal = 5;
                        bookingGroupStart = "" + i + ":10";
                        break;
                    case 20:
                        boookingGroupTotal = 4;
                        bookingGroupStart = "" + i + ":20";
                        break;
                    case 30:
                        boookingGroupTotal = 3;
                        bookingGroupStart = "" + i + ":30";
                        break;
                    case 40:
                        boookingGroupTotal = 2;
                        bookingGroupStart = "" + i + ":40";
                        break;
                    case 50:
                        boookingGroupTotal = 1;
                        bookingGroupStart = "" + i + ":50";
                        break;
                }
            }

            if (i == Integer.parseInt(timeEnd.substring(0, 2))) {
                int param = Integer.parseInt(timeEnd.substring(3, 5));
                switch (param) {
                    case 00:
                        boookingGroupTotal = 0;
                        break;
                    case 10:
                        boookingGroupTotal = 1;
                        bookingGroupEnd = "" + i + ":10";
                        break;
                    case 20:
                        boookingGroupTotal = 2;
                        bookingGroupEnd = "" + i + ":20";
                        break;
                    case 30:
                        boookingGroupTotal = 3;
                        bookingGroupEnd = "" + i + ":30";
                        break;
                    case 40:
                        boookingGroupTotal = 4;
                        bookingGroupEnd = "" + i + ":40";
                        break;
                    case 50:
                        boookingGroupTotal = 5;
                        bookingGroupEnd = "" + i + ":50";
                        break;
                }


            }

            boookingGroupBooked = 0;

            for (BookingGroup t : temp) {
                if (i == Integer.parseInt(t.getBookingGroupStart().substring(0, 2))) {
                    boookingGroupBooked = t.getBoookingGroupBooked();
                }
            }

            assert boookingGroupBooked <= boookingGroupTotal;

            //log.info("bookingGroupStart:" + bookingGroupStart + ", bookingGroupEnd;" + bookingGroupEnd + ", bookingGroupEnd;" + bookingGroupDate + ", boookingGroupBooked:" + boookingGroupBooked + ", boookingGroupTotal" + boookingGroupTotal);

            if (boookingGroupTotal > 0) {
                bookingGroups.add(new BookingGroup(bookingGroupStart, bookingGroupEnd, bookingGroupDate, boookingGroupBooked, boookingGroupTotal));
            }

        }

        return bookingGroups;
    }

    @Override
    public Holiday findHolidayById(int holidayId) {
        Holiday holiday = adminRepo.findHolidayById(holidayId);
        return holiday;
    }

    @Override
    public Holiday findHolidayByDate(String holidayDate) {
        Holiday holiday = adminRepo.findHolidayByDate(holidayDate);
        return holiday;
    }

    @Override
    public Boolean isHolidayByDate(String holidayDate) {
        Holiday holiday = adminRepo.isHolidayByDate(holidayDate);
        if(holiday.getHolidayId() == 1){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Holiday> getHolidays() {
        List<Holiday> holidays = adminRepo.getHolidays();
        return holidays;
    }

    @Override
    public Opening findOpening(int openingId) {
        Opening opening = adminRepo.findOpening(openingId);
        return opening;
    }

    @Override
    public Opening[] getOpenings() {
        Opening[] openings = adminRepo.getOpenings();
        return openings;
    }

    @Override
    public int hashPassword(String password) {
        int passwordHash = password.hashCode();

        log.info(""+passwordHash);

        return passwordHash;
    }

    /*@Override
    public Admin checkPassword(Admin admin) {

        Admin adminFound = adminRepo.checkPassword(admin);

        return adminFound;
    }*/

    @Override
    public Admin searchUser(Admin admin) {

        Admin adminFound = adminRepo.searchUser(admin);

        return adminFound;
    }

    @Override
    public void deleteStaffMember(int staffId){
        adminRepo.deleteStaffMember(staffId);
    }

    @Override
    public Staff createStaffMember(Staff staff){
        staff = adminRepo.createStaffMember(staff);

        return staff;

    }
    @Override
    public List<Staff> getStaff() {
        List<Staff> staffs = adminRepo.getStaff();
        return staffs;
    }

    @Override
    public Staff getStaffMember(int staffId) {
        Staff staffs = adminRepo.getStaffMember(staffId);
        return staffs;
    }

    @Override
    public Staff updateStaff(Staff staff){

        staff = adminRepo.updateStaff(staff);

        return staff;
    }

    @Override
    public List<Offer> getOffers() {
        List<Offer> offers = adminRepo.getOffers();

        return offers;
    }

    @Override
    public Offer createOffer(Offer offer) {
        offer = adminRepo.createOffer(offer);
        return offer;
    }

    @Override
    public Offer updateOffer(Offer offer) {
        return null;
    }

    @Override
    public void deleteOffer(int id) {

    }

    @Override
    public Offer findOffer(int id) {
        return null;
    }

    public int getWeekToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = new GregorianCalendar();
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        log.info(""+sdf.format(calendar.getTime()));
        return weekOfYear;
    }

    @Override
    public int getWeekFromDate(int day, int month, int year) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        month = month - 1;
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        log.info(""+sdf.format(calendar.getTime()));
        return weekOfYear;
    }

    @Override
    public String getDateToday() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        String today = formatter.format(date);

        return today;
    }

    @Override
    public String nextWeek() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        date = date.plusDays(7);
        String next = formatter.format(date);

        log.info("Next week: "+next);

        return next;
    }

    @Override
    public String nextWeekFromDate(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        date = date.plusDays(7);
        String next = formatter.format(date);

        log.info("Next week: "+next);

        return next;
    }

    @Override
    public String prevWeek() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        date = date.minusDays(7);
        String prev = formatter.format(date);

        log.info("Prev week: "+prev);

        return prev;
    }

    @Override
    public String prevWeekFromDate(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        date = date.minusDays(7);
        String prev = formatter.format(date);

        log.info("Prev week: "+prev);

        return prev;
    }

    @Override
    public String[] getDatesOfWeek() {
        String[] dates = new String[7];

        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");

        LocalDate monday = date;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        date = monday;
        dates[0] = formatter.format(date);

        for (int i = 1; i < 7; i++) {
            date = date.plusDays(1);
            dates[i] = formatter.format(date);
        }

        return dates;
    }

    public String[] getDatesOfSelectedWeek(int day, int month, int year) {
        String[] dates = new String[7];

        log.info("Day: " + day + " Month: " + month + " Year: " + year);

        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");

        log.info(formatter.format(date));

        LocalDate monday = date;
        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }

        date = monday;
        dates[0] = formatter.format(date);

        for (int i = 1; i<7; i++){
            date = date.plusDays(1);
            dates[i] = formatter.format(date);
        }

        return dates;

    }
}
