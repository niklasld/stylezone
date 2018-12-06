package com.stylezone.demo.repositories;

import com.stylezone.demo.models.*;
import com.stylezone.demo.services.AdminServiceImpl;
import com.stylezone.demo.services.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class AdminRepoImpl implements AdminRepo {
    @Autowired
    JdbcTemplate template;

    Logger log = Logger.getLogger(AdminRepoImpl.class.getName());

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
    public Holiday findHolidayById(int holidayId) {
        String sql = "SELECT * FROM stylezone.Holiday WHERE holidayId = ?";
        RowMapper<Holiday> rowMapper = new BeanPropertyRowMapper<>(Holiday.class);

        Holiday holiday = template.queryForObject(sql, rowMapper, holidayId);


        return holiday;
    }

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

    @Override
    public Opening findOpening(int openingId) {
        String sql = "SELECT openingId, openingDay, DATE_FORMAT(openingTime, '%H:%i') AS openingTime, DATE_FORMAT(openingClose, '%H:%i') AS openingClose FROM Opening WHERE openingId = ?";
        RowMapper<Opening> rowMapper = new BeanPropertyRowMapper<>(Opening.class);

        Opening opening = template.queryForObject(sql, rowMapper, openingId);


        return opening;
    }

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

    @Override
    public Admin checkPassword(Admin admin) {
        String sql = "SELECT * FROM Admin WHERE adminUsername = ? AND adminPassword = ?";


        RowMapper<Admin> rowMapper = new BeanPropertyRowMapper<>(Admin.class);

        Admin adminFound = new Admin();

        try {
            adminFound = template.queryForObject(sql, rowMapper, admin.getAdminUsername(), admin.getAdminPassword());
        }
        catch (Exception e) {

            adminFound.setAdminUsername("ErrorAdminNotFound");
            adminFound.setAdminPassword("ErrorPasswordNotFound");
        }

        return adminFound;
    }

    @Override
    public Admin searchUser(Admin admin) {
        String sql = "SELECT * FROM Admin WHERE adminUsername=? AND adminPassword=?";


        // Fra sql til list.
        // Manuelt i stedet.
        return this.template.query(sql, new ResultSetExtractor<Admin>() {
            @Override
            public Admin extractData(ResultSet rs) throws SQLException, DataAccessException {
                int adminId;
                String adminUsername, adminPassword;
                Admin adminFound = new Admin();

                while (rs.next()) {
                    adminId = rs.getInt("adminId");
                    adminUsername = rs.getString("adminUsername");
                    adminPassword = rs.getString("adminPassword");

                    adminFound.setAdminId(adminId);
                    adminFound.setAdminUsername(adminUsername);
                    adminFound.setAdminPassword(adminPassword);
                }
                return adminFound;
            }
        }, admin.getAdminUsername(),admin.getAdminPassword());
    }

    @Override
    public Staff getStaffMember(int staffId){
        String sql = "SELECT * FROM Staff WHERE staffId = ?";
        RowMapper<Staff> rowMapper = new BeanPropertyRowMapper<>(Staff.class);

        Staff staff = template.queryForObject(sql,rowMapper, staffId );

        return staff;
    }

    @Override
    public Staff updateStaff(Staff staff){

        String sql = "UPDATE Staff SET staffName =? WHERE staffId = ?";
        String staffName = staff.getStaffName();

        int staffId = staff.getStaffId();
        this.template.update(sql, staffName, staffId);

        return staff;

    }
    @Override
    public void deleteStaffMember(int staffId){
        String sql = "DELETE FROM stylezone.Staff WHERE staffId = ?";
        this.template.update(sql, staffId );
    }

    public Staff createStaffMember(Staff staff){
        String sql = "INSERT INTO Staff VALUE(default, ?)";

        String staffName = staff.getStaffName();

        log.info("createStaffMember called" + staffName);
        this.template.update(sql, staffName);

        return staff;
    }

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
