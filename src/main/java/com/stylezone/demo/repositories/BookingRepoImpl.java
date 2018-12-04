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

    @Override
    public Booking findBooking(int bookingId) {
        String sql = "SELECT * FROM booking WHERE BookingId = ?";
        RowMapper<Booking> rowMapper = new BeanPropertyRowMapper<>(Booking.class);

        Booking booking = template.queryForObject(sql, rowMapper, bookingId);


        return booking;
    }


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

    @Override
    public List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd) {
        log.info("BookingRepo.getBookingGroups(" + date + ", " + timeStart + ", " + timeEnd + ")");

        String sql = "SELECT HOUR(bookingTime) AS startTime, COUNT(bookingId) AS booked FROM Booking\n" +
                "WHERE bookingDate = STR_TO_DATE(?, '%d-%m-%Y')\n" +
                "AND bookingTime > ?\n" +
                "AND bookingTime < ?\n" +
                "GROUP BY HOUR(bookingTime)";

        return this.template.query(sql, new ResultSetExtractor<List<BookingGroup>>() {

            @Override
            public List<BookingGroup> extractData(ResultSet rs) throws SQLException, DataAccessException {
                int bookingGroupId, boookingGroupBooked, boookingGroupTotal;
                String bookingGroupStart, bookingGroupEnd, bookingGroupDate;
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

    @Override
    public Booking updateBooking(Booking booking) {
        return null;
    }

    @Override
    public void deleteBooking(int bookingId) {

    }

    @Override
    public Holiday findHoliday(int holidayId) {
        String sql = "SELECT * FROM holiday WHERE holidayId = ?";
        RowMapper<Holiday> rowMapper = new BeanPropertyRowMapper<>(Holiday.class);

        Holiday holiday = template.queryForObject(sql, rowMapper, holidayId);


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

    @Override
    public Opening findOpening(int openingId) {
        String sql = "SELECT * FROM opening WHERE openingId = ?";
        RowMapper<Opening> rowMapper = new BeanPropertyRowMapper<>(Opening.class);

        Opening opening = template.queryForObject(sql, rowMapper, openingId);


        return opening;
    }

    @Override
    public List<Opening> getOpenings() {
        String sql = "SELECT * FROM opening";
        return this.template.query(sql, new ResultSetExtractor<List<Opening>>() {

            @Override
            public List<Opening> extractData(ResultSet rs) throws SQLException, DataAccessException {
                int openingId, openingTime, openingClose;
                String openingDay;
                ArrayList<Opening> openings = new ArrayList<>();

                while (rs.next()) {

                    openingId = rs.getInt("openingId");
                    openingDay = rs.getString("openingDay");
                    openingTime = rs.getInt("openingTime");
                    openingClose = rs.getInt("openingClose");

                    openings.add(new Opening(openingId, openingDay, openingTime, openingClose));
                }
                return openings;
            }
        });
    }

    @Override
    public Booking saveBooking(Booking booking) {


        String sql = "INSERT INTO stylezone.Booking VALUES(default,?,STR_TO_DATE(?,'%d-%m-%Y'),?,?,?,?,?)";
        String bookingTime = booking.getBookingTime();
        String bookingDate = booking.getBookingDate();
        String bookingName = booking.getBookingName();
        String bookingEmail = booking.getBookingEmail();
        String bookingComment = booking.getBookingComment();

        int bookingPhone = booking.getBookingPhone();
        int fk_staffId = booking.getStaffId();

        this.template.update(sql, bookingTime, bookingDate, bookingName, bookingEmail, bookingPhone, bookingComment, fk_staffId);

        return booking;

    }
    @Override
    public Offer createOffer(Offer offer) {
        Logger log = Logger.getLogger(BookingServiceImpl.class.getName());

        String sql = "INSERT INTO stylezone.Offer VALUE(default, ?, ?, ?, ?)";
        String offerName = offer.getOfferName();
        String offerContent = offer.getOfferContent();
        String offerStart = offer.getOfferStart();
        String offerEnd = offer.getOfferEnd();

        log.info("create offer" + offerName + offerContent + offerStart + offerEnd );
        this.template.update(sql, offerName, offerContent, offerStart, offerEnd );

        return offer;
    }

    @Override
    public Offer updateOffer(Offer offer) {
        return null;
    }

    @Override
    public Offer findOffer(int offerId) {
        return null;
    }

    @Override
    public List<Offer> getOffers() {
         String sql = "SELECT * FROM Offer";

            // Fra sql til list.
            // Manuelt i stedet.
            return this.template.query(sql, new ResultSetExtractor<List<Offer>>() {
                @Override
                public List<Offer> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    String offerName, offerContent, offerStart, offerEnd;
                    ArrayList<Offer> offers = new ArrayList<>();

                    while (rs.next()) {
                        offerName = rs.getString("offerName");
                        offerContent = rs.getString("offerContent");
                        offerStart = rs.getString("offerStart");
                        offerEnd = rs.getString("offerEnd");

                        offers.add(new Offer(offerName, offerContent, offerStart, offerEnd));
                    }
                    return offers;
                }
            });

        }
    }




