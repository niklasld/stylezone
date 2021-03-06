package com.stylezone.demo.repositories;

import com.stylezone.demo.models.*;
import com.stylezone.demo.services.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.logging.Logger;
import java.util.*;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository

public class BookingRepoImpl implements BookingRepo {
    Logger log = Logger.getLogger(BookingRepoImpl.class.getName());


    @Autowired
    JdbcTemplate template;

    //Felix
    @Override
    public Booking findBooking(int bookingId) {
        String sql = "SELECT * FROM stylezone.Booking WHERE BookingId = ?";
        RowMapper<Booking> rowMapper = new BeanPropertyRowMapper<>(Booking.class);

        Booking booking = template.queryForObject(sql, rowMapper, bookingId);


        return booking;
    }

    //Felix
    @Override
    public Booking findBookingByDateTime(String bookingDate, String bookingTime) {
        String sql = "SELECT * FROM stylezone.Booking WHERE bookingDate = STR_TO_DATE(?, '%d-%m-%Y') AND bookingTime = ?";
        RowMapper<Booking> rowMapper = new BeanPropertyRowMapper<>(Booking.class);

        Booking booking = template.queryForObject(sql, rowMapper, bookingDate, bookingTime);

        return booking;
    }

    //Felix
    @Override
    public Booking isBooked(String bookingDate, String bookingTime) {
        String sql = "SELECT COUNT(bookingId) AS bookingId FROM stylezone.Booking WHERE bookingDate = STR_TO_DATE(?, '%d-%m-%Y') AND bookingTime = ?";
        RowMapper<Booking> rowMapper = new BeanPropertyRowMapper<>(Booking.class);

        Booking booking = template.queryForObject(sql, rowMapper, bookingDate, bookingTime);

        return booking;
    }

    //Felix
    @Override
    public List<Booking> getBookings() {
        String sql = "SELECT * FROM Booking";
        return this.template.query(sql, new ResultSetExtractor<List<Booking>>() {

            @Override
            public List<Booking> extractData(ResultSet rs) throws SQLException, DataAccessException {
                int bookingId, bookingPhone, staffId;
                String bookingTime, bookingDate, bookingName, bookingComment, bookingEmail;
                ArrayList<Booking> bookings = new ArrayList<>();

                while (rs.next()) {
                    bookingId = rs.getInt("bookingId");
                    bookingPhone = rs.getInt("bookingPhone");
                    staffId = rs.getInt("fk_staffId");
                    bookingTime = rs.getString("bookingTime");
                    bookingDate = rs.getString("bookingDate");
                    bookingName = rs.getString("bookingName");
                    bookingEmail = rs.getString("bookingEmail");
                    bookingComment = rs.getString("bookingComment");

                    bookings.add(new Booking(bookingId, bookingTime, bookingDate, bookingName, bookingEmail, bookingPhone, bookingComment, staffId));
                }
                return bookings;
            }
        });
    }

    //Felix
    //Her henter vi alle bookninger fra et enklet time interval i databasen
    @Override
    public List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd) {
        log.info("BookingRepo.getSelectedBookings(" + date + ", " + timeStart + ", " + timeEnd + ")");

        String sql = "SELECT * FROM Booking\n" +
                "WHERE bookingDate = STR_TO_DATE(?, '%d-%m-%Y')\n" +
                "AND bookingTime >= ?\n" +
                "AND bookingTime < ?\n" +
                "ORDER BY bookingTime ASC";
        return this.template.query(sql, new ResultSetExtractor<List<Booking>>() {

            @Override
            public List<Booking> extractData(ResultSet rs) throws SQLException, DataAccessException {
                int bookingId, bookingPhone, staffId;
                String bookingTime, bookingDate, bookingName, bookingEmail, bookingComment;
                ArrayList<Booking> bookings = new ArrayList<>();

                while (rs.next()) {
                    bookingId = rs.getInt("bookingId");
                    bookingPhone = rs.getInt("bookingPhone");
                    staffId = rs.getInt("fk_staffId");
                    bookingTime = rs.getString("bookingTime");
                    bookingDate = rs.getString("bookingDate");
                    bookingName = rs.getString("bookingName");
                    bookingEmail = rs.getString("bookingEmail");
                    bookingComment = rs.getString("bookingComment");

                    bookings.add(new Booking(bookingId, bookingTime, bookingDate, bookingName, bookingEmail, bookingPhone, bookingComment, staffId));
                }
                return bookings;
            }
        }, date, timeStart, timeEnd);
    }

    //Felix
    //Her henter vi alle bookninger for enkel dag inden for et bestemt tidsinterval, grupperet i time intervaler.
    @Override
    public List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd) {

        log.info("BookingRepo.getBookingGroups(" + date + ", " + timeStart + ", " + timeEnd + ")");

        String sql = "SELECT HOUR(bookingTime) AS startTime, COUNT(bookingId) AS booked FROM Booking\n" +
                "WHERE bookingDate = STR_TO_DATE(?, '%d-%m-%Y')\n" +
                "AND bookingTime >= ?\n" +
                "AND bookingTime < ?\n" +
                "GROUP BY HOUR(bookingTime)";

        return this.template.query(sql, new ResultSetExtractor<List<BookingGroup>>() {

            @Override
            public List<BookingGroup> extractData(ResultSet rs) throws SQLException, DataAccessException {
                int boookingGroupBooked;
                String bookingGroupStart;
                List<BookingGroup> bookingGroups = new ArrayList<>();

                while (rs.next()) {
                    bookingGroupStart = rs.getString("startTime");
                    bookingGroupStart = bookingGroupStart + ":00";
                    boookingGroupBooked = rs.getInt("booked");

                    bookingGroups.add(new BookingGroup(bookingGroupStart, boookingGroupBooked));
                }
                return bookingGroups;
            }
        }, date, timeStart, timeEnd);
    }

    //Felix
    @Override
    public Booking updateBooking(Booking booking) {
        return null;
    }

    //Felix
    @Override
    public void deleteBooking(int bookingId) {

    }

    //Felix
    @Override
    public Holiday findHolidayById(int holidayId) {
        String sql = "SELECT * FROM stylezone.Holiday WHERE holidayId = ?";
        RowMapper<Holiday> rowMapper = new BeanPropertyRowMapper<>(Holiday.class);

        Holiday holiday = template.queryForObject(sql, rowMapper, holidayId);


        return holiday;
    }

    //Felix
    @Override
    public Holiday findHolidayByDate(String holidayDate) {
        String sql = "SELECT * FROM stylezone.Holiday WHERE holidayDate = STR_TO_DATE(?, '%d-%m-%Y')";
        RowMapper<Holiday> rowMapper = new BeanPropertyRowMapper<>(Holiday.class);

        Holiday holiday = template.queryForObject(sql, rowMapper, holidayDate);


        return holiday;
    }

    @Override
    public Holiday isHolidayByDate(String holidayDate) {
        String sql = "SELECT COUNT(holidayId) AS holidayId FROM stylezone.Holiday WHERE holidayDate = STR_TO_DATE(?, '%d-%m-%Y')";
        RowMapper<Holiday> rowMapper = new BeanPropertyRowMapper<>(Holiday.class);

        Holiday holiday = template.queryForObject(sql, rowMapper, holidayDate);

        return holiday;
    }

    @Override
    public List<Holiday> getHolidays() {
        String sql = "SELECT * FROM Holiday";
        return this.template.query(sql, new ResultSetExtractor<List<Holiday>>() {

            @Override
            public List<Holiday> extractData(ResultSet rs) throws SQLException, DataAccessException {
                int holidayId;
                String holidayDate, holidayName;
                ArrayList<Holiday> holidays = new ArrayList<>();

                while (rs.next()) {
                    holidayId = rs.getInt("holidayId");
                    holidayDate = rs.getString("holidayDate");
                    holidayName = rs.getString("holidayName");


                    holidays.add(new Holiday(holidayId, holidayDate, holidayName));
                }
                return holidays;
            }
        });
    }

    //Felix
    @Override
    public Opening findOpening(int openingId) {
        String sql = "SELECT openingId, openingDay, DATE_FORMAT(openingTime, '%H:%i') AS openingTime, DATE_FORMAT(openingClose, '%H:%i') AS openingClose FROM Opening WHERE openingId = ?";
        RowMapper<Opening> rowMapper = new BeanPropertyRowMapper<>(Opening.class);

        Opening opening = template.queryForObject(sql, rowMapper, openingId);


        return opening;
    }

    //Felix
    @Override
    public Opening[] getOpenings() {
        String sql = "SELECT openingId, openingDay, DATE_FORMAT(openingTime, '%H:%i') AS openingTime, DATE_FORMAT(openingClose, '%H:%i') AS openingClose FROM Opening";
        return this.template.query(sql, new ResultSetExtractor<Opening[]>() {

            @Override
            public Opening[] extractData(ResultSet rs) throws SQLException, DataAccessException {
                int openingId;
                String openingDay, openingTime, openingClose;
                Opening[] openings = new Opening[7];
                int i = 0;

                while (rs.next()) {

                    openingId = rs.getInt("openingId");
                    openingDay = rs.getString("openingDay");
                    openingTime = rs.getString("openingTime");
                    openingClose = rs.getString("openingClose");

                    openings[i] = new Opening(openingId, openingDay, openingTime, openingClose);
                    i++;
                }
                return openings;
            }
        });
    }

    //Gustav
    @Override
    public Booking saveBooking(Booking booking) {


        String sql = "INSERT INTO stylezone.Booking VALUES(default,?,STR_TO_DATE(?,'%d-%m-%Y'),?,?,?,?,?,?)";
        String bookingTime = booking.getBookingTime();
        String bookingDate = booking.getBookingDate();
        String bookingName = booking.getBookingName();
        String bookingEmail = booking.getBookingEmail();
        String bookingComment = booking.getBookingComment();
        String bookingToken = booking.getBookingToken();

        int bookingPhone = booking.getBookingPhone();
        int fk_staffId = booking.getStaffId();

        this.template.update(sql, bookingTime, bookingDate, bookingName, bookingEmail, bookingPhone, bookingComment, bookingToken, fk_staffId);

        return booking;

    }

    //Gustav
    @Override
    public Staff getStaffMember(int staffId){
        String sql = "SELECT * FROM Staff WHERE staffId = ?";
        RowMapper<Staff> rowMapper = new BeanPropertyRowMapper<>(Staff.class);

        Staff staff = template.queryForObject(sql,rowMapper, staffId );

        return staff;
    }

    //Gustav
    @Override
    public List<Staff> getStaff(){

        String sql = "SELECT * FROM Staff";
        return this.template.query(sql, new ResultSetExtractor<List<Staff>>() {

            @Override
            public List<Staff> extractData(ResultSet rs) throws SQLException, DataAccessException{

                int staffId;
                String staffName;
                ArrayList<Staff> staffs = new ArrayList<>();

                while (rs.next()){

                    staffId = rs.getInt("staffId");
                    staffName = rs.getString("staffName");

                    staffs.add(new Staff(staffId, staffName));

                }
                return staffs;
            }

        });
    }
}

