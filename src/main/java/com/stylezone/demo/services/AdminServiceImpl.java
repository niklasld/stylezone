package com.stylezone.demo.services;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Offer;
import com.stylezone.demo.models.Picture;
import com.stylezone.demo.models.Opening;
import com.stylezone.demo.models.Staff;
import com.stylezone.demo.models.*;
import com.stylezone.demo.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import java.util.logging.Logger;

@Repository
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepo adminRepo;
    @Autowired
    JdbcTemplate template;


    Logger log = Logger.getLogger(AdminServiceImpl.class.getName());

    private static String UPLOADED_FOLDER = "src//main//resources//static//image//upload//";

    //Felix
    private final boolean DEVELOPER_MODE = false;

    //Felix
    @Override
    public Booking findBooking(int bookingId) {
        Booking booking = adminRepo.findBooking(bookingId);
        return booking;
    }

    //Felix
    @Override
    public List<Booking> getBookings(){
        List<Booking> bookings = adminRepo.getBookings();
        return bookings;
    }

    //Felix
    @Override
    public Booking updateBooking(Booking booking){
        booking = adminRepo.updateBooking(booking);
        return booking;
    }

    //Felix
    @Override
    public void deleteBooking(int bookingId){
        adminRepo.deleteBooking(bookingId);
    }

    //Felix
    @Override
    public List<Booking> getSelectedBookings(String date, String timeStart, String timeEnd) {
        if(DEVELOPER_MODE) {
            log.info("BookingService.getSelectedBookings(" + date + ", " + timeStart + ", " + timeEnd + ")");
        }

        List<Booking> temp = adminRepo.getSelectedBookings(date, timeStart, timeEnd);

        if(DEVELOPER_MODE) {
            log.info("temp length: " + temp.size());
        }

        int bookingId;
        String bookingTime, bookingName;
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
            bookingId = 0;

            for (Booking t : temp) {
                if (i == Integer.parseInt(t.getBookingTime().substring(3, 5))) {
                    bookingName = t.getBookingName();
                    bookingId = t.getBookingId();
                }
            }

            if(DEVELOPER_MODE) {
                log.info("start:" + start + ", end:" + end + ", i:" + i);
                log.info("bookingTime:" + bookingTime + ", bookingName:" + bookingName);
            }

            bookings.add(new Booking(bookingId, bookingTime, bookingName));
        }

        return bookings;
    }

    //Felix
    @Override
    public List<BookingGroup> getBookingGroups(String date, String timeStart, String timeEnd) {
        if(DEVELOPER_MODE) {
            log.info("BookingService.getBookingGroups(" + date + ", " + timeStart + ", " + timeEnd + ")");
        }
        List<BookingGroup> temp = adminRepo.getBookingGroups(date, timeStart, timeEnd);
        if(DEVELOPER_MODE) {
            log.info("bookingGroups length" + temp.size());
        }

        int boookingGroupBooked, boookingGroupTotal;
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



            if (boookingGroupTotal > 0) {
                bookingGroups.add(new BookingGroup(bookingGroupStart, bookingGroupEnd, bookingGroupDate, boookingGroupBooked, boookingGroupTotal));
            }

        }

        return bookingGroups;
    }

    //Felix
    @Override
    public Booking createBooking(Booking booking) {

        if (booking.getStaffId() > 0 ||
                !booking.getBookingEmail().equals("") ||
                !booking.getBookingTime().equals("00:00:00") ||
                !booking.getBookingDate().equals("00-00-0000") ||
                booking.getBookingPhone() > 0 ||
                !booking.getBookingName().equals("")) {

            booking = adminRepo.createBooking(booking);

            return booking;
        }
        return null;
    }

    //Felix
    @Override
    public Boolean isBooked(String bookingDate, String bookingTime) {
        Booking booking = adminRepo.isBooked(bookingDate, bookingTime);
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
    public Holiday findHolidayById(int holidayId) {
        Holiday holiday = adminRepo.findHolidayById(holidayId);
        return holiday;
    }

    //Felix
    @Override
    public Holiday findHolidayByDate(String holidayDate) {
        Holiday holiday = adminRepo.findHolidayByDate(holidayDate);
        return holiday;
    }

    //Felix
    @Override
    public Boolean isHolidayByDate(String holidayDate) {
        Holiday holiday = adminRepo.isHolidayByDate(holidayDate);
        if(holiday.getHolidayId() == 1){
            return true;
        } else {
            return false;
        }
    }

    //Felix
    @Override
    public List<Holiday> getHolidays() {
        List<Holiday> holidays = adminRepo.getHolidays();
        return holidays;
    }

    //Niklas
    @Override
    public int hashPassword(String password) {
        int passwordHash = password.hashCode();

        log.info(""+passwordHash);

        return passwordHash;
    }

    //Niklas
    @Override
    public Admin searchUser(Admin admin) {

        Admin adminFound = adminRepo.searchUser(admin);

        return adminFound;
    }

    //Gustav
    @Override
    public void deleteStaffMember(int staffId){
        adminRepo.deleteStaffMember(staffId);
    }

    //Gustav
    @Override
    public Staff createStaffMember(Staff staff){
        staff = adminRepo.createStaffMember(staff);

        return staff;

    }

    //Gustav
    @Override
    public List<Staff> getStaff() {
        List<Staff> staffs = adminRepo.getStaff();
        return staffs;
    }

    //Gustav
    @Override
    public Staff getStaffMember(int staffId) {
        Staff staffs = adminRepo.getStaffMember(staffId);
        return staffs;
    }

    //Gustav
    @Override
    public Staff updateStaff(Staff staff){

        staff = adminRepo.updateStaff(staff);

        return staff;
    }

    //Hasan
    @Override
    public List<Offer> showOffers() {
        List<Offer> offers = adminRepo.showOffers();

        return offers;

    }


    //Hasan
    @Override
    public List<Offer> getOffers() {
        List<Offer> offers = adminRepo.getOffers();

        return offers;
    }

    //Hasan
    @Override
    public Offer createOffer(Offer offer) {
        offer = adminRepo.createOffer(offer);
        return offer;
    }

    //Hasan
    @Override
    public Offer updateOffer(Offer offer) {
        offer = adminRepo.updateOffer(offer);
        return offer;
    }


    //Hasan
    @Override
    public void deleteOffer(int id) {
        String sql = "DELETE FROM Offer WHERE offerId = ?";
        this.template.update(sql, id);
    }

    //Hasan
    @Override
    public Offer findOffer(int id) {
       Offer offer = adminRepo.findOffer(id);
        return offer;
    }

    //Felix
    @Override
    public Opening findOpening(int openingId) {
        Opening opening = adminRepo.findOpening(openingId);
        return opening;
    }

    //Felix
    @Override
    public Opening[] getOpenings() {
        Opening[] openings = adminRepo.getOpenings();
        return openings;
    }

    //Niklas
    @Override
    public Opening saveOpeningHours(Opening opening) {

        opening = returnConvertedOpenings(opening);

        opening = adminRepo.saveOpeningHours(opening);

        return opening;
    }

    //Niklas
    @Override
    public Opening returnConvertedOpenings(Opening opening) {
        String fullTime="";

        if(Integer.parseInt(opening.getOpeningHour())<10) {
            fullTime = "0"+opening.getOpeningHour();
        }
        else if(Integer.parseInt(opening.getOpeningHour())>=10) {
            fullTime = ""+opening.getOpeningHour();
        }
        if(Integer.parseInt(opening.getOpeningMin())<10){
            fullTime = fullTime+":0"+opening.getOpeningMin()+":00";
        }
        else if(Integer.parseInt(opening.getOpeningMin())>=10) {
            fullTime = fullTime+":"+opening.getOpeningMin()+":00";
        }

        opening.setOpeningTime(fullTime);
        fullTime = "";
        if(Integer.parseInt(opening.getClosingHour())<10) {
            fullTime = "0"+opening.getClosingHour();
        }
        else if(Integer.parseInt(opening.getClosingHour())>=10) {
            fullTime = ""+opening.getClosingHour();
        }
        if(Integer.parseInt(opening.getClosingMin())<10){
            fullTime = fullTime+":0"+opening.getClosingMin()+":00";
        }
        else if(Integer.parseInt(opening.getClosingMin())>=10) {
            fullTime = fullTime+":"+opening.getClosingMin()+":00";
        }

        opening.setOpeningClose(fullTime);

        return opening;
    }

    //Niklas
    @Override
    public ArrayList<Integer> getHours() {
        ArrayList<Integer> hours = new ArrayList<>();

        for(int i = 0; i<24; i++) {
            hours.add(i);
        }


        return hours;
    }

    //Gustav & Hasan
    @Override
    public List<Picture> getPictures() {
        List<Picture> pictures = adminRepo.getPictures();
        return pictures;
    }

    //Gustav
    @Override
    public String insertPicture(String picture) {
        picture = adminRepo.insertPicture(picture);
        return picture;
    }

    //Gustav
    //Skriver bytes til fil og specificerer hvor filen åbnes fra og gemt, og retunerer filens originale navn
    public String fileUpload(MultipartFile file) {


        try {
            //gemmer filen som et objekt som typen byte
            byte[] bytes = file.getBytes();
            //Her definerer vi hvor filen skal gemmes
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            insertPicture(""+file.getOriginalFilename());

            return file.getOriginalFilename();

        } catch (IOException e){
            e.printStackTrace();

            return "Error uploading file";
        }
    }

    //Niklas
    public ArrayList<Integer> getMin() {
        ArrayList<Integer> min = new ArrayList<>();

        for(int i = 0; i<=50; i=i+10) {
            min.add(i);
        }

        return min;
    }

    //Niklas
    @Override
    public ArrayList<String> getDays() {
        ArrayList<String> days = new ArrayList<>();

        days.add("mandag");
        days.add("tirsdag");
        days.add("onsdag");
        days.add("torsdag");
        days.add("fredag");
        days.add("lørdag");

        return days;
    }

    //Niklas
    @Override
    public Opening[] convertOpenings() {
        Opening[] fullOpening = getOpenings();
        String chopThis;
        String[] parts;

        for (Opening time : fullOpening) {

                chopThis = time.getOpeningTime();
                parts = chopThis.split(":");

                time.setOpeningHour(parts[0]);
                time.setOpeningMin(parts[1]);

                chopThis = time.getOpeningClose();
                parts = chopThis.split(":");

                time.setClosingHour(parts[0]);
                time.setClosingMin(parts[1]);

        }

        return fullOpening;
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

    //Felix
    @Override
    public void sendMessageMail(Booking booking){
        log.info("sendMessageMail called...");

        String subject = "Vi har en besked til dig angående din tid hos StyleZone den " + booking.getBookingDate() + " kl. " + booking.getBookingTime();
        String mailTo = booking.getBookingEmail();
        String mailText;

        mailText = "Hej " + booking.getBookingName() + "\n\n";
        mailText += "Vi har en besked til dig angående din tid hos StyleZone den " + booking.getBookingDate() + " kl. " + booking.getBookingTime() + "\n\n";
        mailText += "Besked til dig:\n" + booking.getBookingMessage() + "\n\n";
        mailText += "Er der nogle af informationerne der skal ændres eller skal bestillingen skal aflyses, kan StyleZone kontaktes på telefon nummer: +45 2989 7596.\n\n";
        mailText += "Med venlig hilsen.\n";
        mailText += "StyleZone";

        sendEmail(mailText, subject, mailTo);
    }

    //Felix
    @Override
    public void editBookingMail(Booking booking){
        log.info("editBookingMail called...");

        String subject = "Vi har lavet nogle ændringer i din tid hos StyleZone den " + booking.getBookingDate() + " kl. " + booking.getBookingTime() + " for dig.";
        String mailTo = booking.getBookingEmail();
        String mailText;

        mailText = "Hej " + booking.getBookingName() + "\n\n";
        mailText += "Vi har lavet nogle ændringer i din tid hos StyleZone for dig.\n\n";
        mailText += "Du kan se oplysningerne for din tid nedenfor\n\n";
        mailText += "Tid: " + booking.getBookingTime() + "\n";
        mailText += "Dato: " + booking.getBookingDate() + "\n";
        mailText += "Dit tlf nr: +45" + booking.getBookingPhone() + "\n";
        mailText += "Din frisør: " + getStaffMember(booking.getStaffId()).getStaffName() + "\n";
        mailText += "Kommentar til frisøren:\n" + booking.getBookingComment() + "\n\n";
        mailText += "Besked til dig:\n" + booking.getBookingMessage() + "\n\n";
        mailText += "Er der nogle af informationerne der skal ændres eller skal bestillingen skal aflyses, kan StyleZone kontaktes på telefon nummer: +45 2989 7596.\n\n";
        mailText += "Med venlig hilsen.\n";
        mailText += "StyleZone";

        sendEmail(mailText, subject, mailTo);
    }

    //Felix
    @Override
    public void deleteBookingMail(Booking booking){
        log.info("deleteBookingMail called...");

        String subject = "Vi har annulleret din booking hos StyleZone den " + booking.getBookingDate() + " kl. " + booking.getBookingTime() + " for dig.";
        String mailTo = booking.getBookingEmail();
        String mailText;

        mailText = "Hej " + booking.getBookingName() + "\n\n";
        mailText += "Vi har annulleret din booking hos StyleZone for dig.\n\n";
        mailText += "Du kan se oplysningerne for din annulleret booking nedenfor\n\n";
        mailText += "Tid: " + booking.getBookingTime() + "\n";
        mailText += "Dato: " + booking.getBookingDate() + "\n";
        mailText += "Dit tlf nr: +45" + booking.getBookingPhone() + "\n";
        mailText += "Din frisør: " + getStaffMember(booking.getStaffId()).getStaffName() + "\n";
        mailText += "Kommentar til frisøren:\n" + booking.getBookingComment() + "\n\n";
        mailText += "Besked til dig:\n" + booking.getBookingMessage() + "\n\n";
        mailText += "Hvis du har nogle spørgsmål, kan StyleZone kontaktes på telefon nummer: +45 2989 7596.\n\n";
        mailText += "Med venlig hilsen.\n";
        mailText += "StyleZone";

        sendEmail(mailText, subject, mailTo);
    }

    //Felix
    @Override
    public void createBookingMail(Booking booking){
        log.info("createBookingMail called...");

        String subject = "Vi har reserveret en tid hos StyleZone den " + booking.getBookingDate() + " kl. " + booking.getBookingTime() + " for dig.";
        String mailTo = booking.getBookingEmail();
        String mailText;

        mailText = "Hej " + booking.getBookingName() + "\n\n";
        mailText += "Vi har reserveret en tid hos StyleZone for dig.\n\n";
        mailText += "Du kan se oplysningerne for din tid nedenfor\n\n";
        mailText += "Tid: " + booking.getBookingTime() + "\n";
        mailText += "Dato: " + booking.getBookingDate() + "\n";
        mailText += "Dit tlf nr: +45" + booking.getBookingPhone() + "\n";
        mailText += "Din frisør: " + getStaffMember(booking.getStaffId()).getStaffName() + "\n";
        mailText += "Kommentar til frisøren:\n" + booking.getBookingComment() + "\n\n";
        mailText += "Besked til dig:\n" + booking.getBookingMessage() + "\n\n";
        mailText += "Er der nogle af informationerne der skal ændres eller skal bestillingen skal aflyses, kan StyleZone kontaktes på telefon nummer: +45 2989 7596.\n\n";
        mailText += "Med venlig hilsen.\n";
        mailText += "StyleZone";

        sendEmail(mailText, subject, mailTo);
    }

    //Felix
    @Override
    public void sendEmail(String mailText, String subject, String mailTo) {
        //Setting up configurations for the email connection to the Google SMTP server using TLS
        Properties props = new Properties();
        props.put("mail.smtp.host", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        //Establishing a session with required user details
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("stylezone.bestilling@gmail.com", "springboot");
            }
        });
        try {
            //Creating a Message object to set the email content
            MimeMessage msg = new MimeMessage(session);
            //Storing the comma seperated values to email addresses
            String to = "stylezone.bestilling@gmail.com, " + mailTo;
            /*Parsing the String with defualt delimiter as a comma by marking the boolean as true and storing the email
            addresses in an array of InternetAddress objects*/
            InternetAddress[] address = InternetAddress.parse(to, true);
            //Setting the recepients from the address variable
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            msg.setText(mailText);

            msg.setHeader("XPriority", "1");
            Transport.send(msg);
            log.info("Mail has been sent successfully");
        } catch (MessagingException mex) {
            log.info("Unable to send an email" + mex);
        }
    }
}
