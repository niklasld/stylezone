

package com.stylezone.demo.services;

import com.stylezone.demo.models.*;
import com.stylezone.demo.repositories.BookingRepo;
import com.stylezone.demo.repositories.BookingRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

import java.time.format.DateTimeFormatter;

@Service
public class BookingServiceImpl implements BookingService {
    Logger log = Logger.getLogger(BookingRepoImpl.class.getName());

    //Felix
    private final boolean DEVELOPER_MODE = true;


    @Autowired
    BookingRepo bookingRepo;

    //Felix
    @Override
    public Booking findBooking(int bookingId) {
        Booking booking = bookingRepo.findBooking(bookingId);
        return booking;
    }

    //Felix
    @Override
    public Booking findBookingByDateTime(String bookingDate, String bookingTime){
        Booking booking = bookingRepo.findBookingByDateTime(bookingDate, bookingTime);
        return booking;
    }

    //Felix
    @Override
    public Boolean isBooked(String bookingDate, String bookingTime) {
        Booking booking = bookingRepo.isBooked(bookingDate, bookingTime);
        if(booking.getBookingId() == 1){
            return true;
        } else {
            return false;
        }
    }

    //Felix
    @Override
    public String generateRandomString(){
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder( 20 );
        for( int i = 0; i < 20; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    //Felix
    @Override
    public List<Booking> getBookings() {
        List<Booking> bookings = bookingRepo.getBookings();
        return bookings;
    }

    //Gustav
    @Override
    public Booking saveBooking(Booking booking) {

        if (booking.getStaffId() > 0 ||
                !booking.getBookingEmail().equals("") ||
                !booking.getBookingTime().equals("00:00:00") ||
                !booking.getBookingDate().equals("00-00-0000") ||
                booking.getBookingPhone() > 0 ||
                !booking.getBookingName().equals("")) {

            booking = bookingRepo.saveBooking(booking);

            return booking;
        }
        return null;
    }

    //Felix
    //Denne metode henter alle ledige og bookede tider for et enkelt time interval
    @Override
    public List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd) {
        if(DEVELOPER_MODE) {
            log.info("BookingService.getSelectedBookings(" + date + ", " + timeStart + ", " + timeEnd + ")");
        }

        List<Booking> temp = bookingRepo.getSelectedBookings(date, timeStart, timeEnd);

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

    //Felix
    //Denne metode henter bookede og ledige tider i grupperede format
    @Override
    public List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd) {
        if(DEVELOPER_MODE) {
            log.info("BookingService.getBookingGroups(" + date + ", " + timeStart + ", " + timeEnd + ")");
        }
        List<BookingGroup> temp = bookingRepo.getBookingGroups(date, timeStart, timeEnd);
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

            log.info("bookingGroupStart:" + bookingGroupStart + ", bookingGroupEnd;" + bookingGroupEnd + ", bookingGroupEnd;" + bookingGroupDate + ", boookingGroupBooked:" + boookingGroupBooked + ", boookingGroupTotal" + boookingGroupTotal);

            if (boookingGroupTotal > 0) {
                bookingGroups.add(new BookingGroup(bookingGroupStart, bookingGroupEnd, bookingGroupDate, boookingGroupBooked, boookingGroupTotal));
            }

        }

        return bookingGroups;
    }

    //Felix
    @Override
    public Booking updateBooking(Booking booking) {
        booking = bookingRepo.updateBooking(booking);
        return booking;
    }

    //Niklas??
    @Override
    public void deleteBooking(int bookingId) {
        bookingRepo.deleteBooking(bookingId);
    }

    //Felix
    @Override
    public Holiday findHolidayById(int holidayId) {
        Holiday holiday = bookingRepo.findHolidayById(holidayId);
        return holiday;
    }

    //Felix
    @Override
    public Holiday findHolidayByDate(String holidayDate) {
        Holiday holiday = bookingRepo.findHolidayByDate(holidayDate);
        return holiday;
    }

    //Felix
    @Override
    public Boolean isHolidayByDate(String holidayDate) {
        Holiday holiday = bookingRepo.isHolidayByDate(holidayDate);
        if(holiday.getHolidayId() == 1){
            return true;
        } else {
            return false;
        }
    }

    //Felix
    @Override
    public List<Holiday> getHolidays() {
        List<Holiday> holidays = bookingRepo.getHolidays();
        return holidays;
    }

    //Felix
    @Override
    public Opening findOpening(int openingId) {
        Opening opening = bookingRepo.findOpening(openingId);
        return opening;
    }

    //Felix
    @Override
    public Opening[] getOpenings() {
        Opening[] openings = bookingRepo.getOpenings();
        return openings;
    }

    //Niklas
    //Denne metode sender en email afsted
    @Override
    public void sendEmail(Booking booking) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("stylezone.bestilling@gmail.com", "springboot");
            }
        });
        try {

            MimeMessage msg = new MimeMessage(session);

            String to = "stylezone.bestilling@gmail.com, " + booking.getBookingEmail();

            InternetAddress[] address = InternetAddress.parse(to, true);

            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("Din tidsbestilling hos StyleZone den " + booking.getBookingDate() + " kl. " + booking.getBookingTime());
            msg.setSentDate(new Date());

            String mailText;

            mailText = "Hej " + booking.getBookingName() + "\n\n";
            mailText += "Tak for din tidsbestilling hos StyleZone.\n\n";
            mailText += "Vi har registreret nedenstående information om din bestilling\n\n";
            mailText += "Tid: " + booking.getBookingTime() + "\n";
            mailText += "Dato: " + booking.getBookingDate() + "\n";
            mailText += "Dit tlf nr: +45" + booking.getBookingPhone() + "\n";
            mailText += "Kommentar: " + booking.getBookingComment() + "\n\n";
            mailText += "Er nogle af informationerne ændres eller bestillingen skal aflyses, kan StyleZone kontaktes på telefon nummer: +45 2989 7596.\n\n";
            mailText += "Med venlig hilsen.\n";
            mailText += "StyleZone";

            msg.setText(mailText);

            msg.setHeader("XPriority", "1");
            Transport.send(msg);
            log.info("Mail has been sent successfully");
        } catch (MessagingException mex) {
            log.info("Unable to send an email" + mex);
        }
    }

    //Felix
    public int getWeekToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = new GregorianCalendar();
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        log.info(""+sdf.format(calendar.getTime()));
        return weekOfYear;
    }

    //Felix
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

    //Felix
    @Override
    public String getDateToday() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        String today = formatter.format(date);

        return today;
    }

    //Felix
    @Override
    public String nextWeek() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        date = date.plusDays(7);
        String next = formatter.format(date);

        log.info("Next week: "+next);

        return next;
    }

    //Felix
    @Override
    public String nextWeekFromDate(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        date = date.plusDays(7);
        String next = formatter.format(date);

        log.info("Next week: "+next);

        return next;
    }

    //Felix
    @Override
    public String prevWeek() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        date = date.minusDays(7);
        String prev = formatter.format(date);

        log.info("Prev week: "+prev);

        return prev;
    }

    //Felix
    @Override
    public String prevWeekFromDate(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        date = date.minusDays(7);
        String prev = formatter.format(date);

        log.info("Prev week: "+prev);

        return prev;
    }

    //Felix
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

    //Felix
    //Henter datoer for en udvalgt uge udvalgt ud fra en dato
    @Override
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

    //Gustav
    @Override
    public List<Staff> getStaff() {
        List<Staff> staffs = bookingRepo.getStaff();
        return staffs;
    }

    //Gustav
    @Override
    public Staff getStaffMember(int staffId) {
        Staff staffs = bookingRepo.getStaffMember(staffId);
        return staffs;
    }


}
