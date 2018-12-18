package com.stylezone.demo.controllers;

import com.stylezone.demo.models.*;
import com.stylezone.demo.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class AdminController {

    public AdminController() {

    }

    @Autowired
    AdminService adminService;

    @Autowired
    RestTemplate restTemplate;

    private final String REDIRECT = "redirect:/";

    private final String ADMINLOGIN = "adminLogin";
    private final String SUCCESS = "success";
    private final String EDITSTAFF = "editStaff";
    private final String STAFF = "staff";
    private final String DELETESTAFF = "deleteStaff";
    private final String CREATESTAFFMEMBER = "createStaffMember";
    private final String OFFER = "offer";
    private final String CREATEOFFER = "createOffer";
    private final String PORTFOLIO = "portfolio";
    private final String UPLOADLIST = "uploadlist";
    private final String UPLOAD = "upload";
    private final String ADMINDASHBOARD = "adminDashBoard";
    private final String EDITOFFER = "editOffer";
    private final String DELETEOFFER = "deleteOffer";
    private final String TILBUD = "tilbud";
    private final String EDITOPENINGHOURS = "editOpeningHours";
    private final String BOOKINGADMIN = "bookingAdmin";
    private final String BOOKINGINFO = "bookingInfo";
    private final String TIMESELECTADMIN = "timeSelectAdmin";
    private final String CREATEBOOKING = "createBooking";
    private final String EDITBOOKING = "editBooking";
    private final String DELETEBOOKING = "deleteBooking";
    private final String TIMENOTAVAILABLE = "timeNotAvailable";
    private final String SENDMESSAGE = "sendMessage";

    //HttpSession session;




    Logger log = Logger.getLogger(AdminController.class.getName());

    //Felix
    @GetMapping("/admin")
    public String admin(){

        //log.info(""+session.getAttribute("loggedIn"));

        return REDIRECT + ADMINLOGIN;
    }

    //Niklas
    @GetMapping("/adminLogin")
    public String adminLogin(Model model) {
        log.info("adminLogin GetMapping called...");

        model.addAttribute("pageTitle", "Admin login");
        model.addAttribute("admin", new Admin());

        return ADMINLOGIN;
    }

    //Niklas
    @PostMapping("/adminLogin")
    public String adminLogin(@ModelAttribute Admin admin,
                             @RequestParam("g-recaptcha-response") String captchaResponse,
                             HttpSession session,
                             Model model) {

        log.info("adminLogin PostMapping called...");

        model.addAttribute("pageTitle", "Admin login");

        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=6LdRxYIUAAAAAJOYs1-rC1md7a8ADQpwKSLoNE0S&response=" + captchaResponse;

        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url + params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();

        int adminPW = adminService.hashPassword(admin.getAdminPassword());

        admin.setAdminPassword("" + adminPW);
        Admin adminCheck = adminService.searchUser(admin);

        if (adminCheck.getAdminPassword().equals(admin.getAdminPassword()) && adminCheck.getAdminUsername().equals(admin.getAdminUsername()) && reCaptchaResponse.isSuccess()) {
            log.info("Login is a success");
            session.setAttribute("loggedin", adminCheck);
            //session.setAttribute("loggedIn", true);
            return REDIRECT + ADMINDASHBOARD;
        }

        log.info("Login failed.");
        return REDIRECT + ADMINLOGIN;
    }

    //Gustav
    @GetMapping("/editStaff/{staffId}")
    public String editStaff(@PathVariable("staffId") int staffId, Model model) {
        log.info("editStaff GetMapping called...");

        Staff staff = adminService.getStaffMember(staffId);

        model.addAttribute("staff", staff);
        model.addAttribute("pageTitle", "Rediger personale");

        return EDITSTAFF;

    }

    //Gustav
    @PutMapping("/editStaff")
    public String editStaff(@ModelAttribute Staff staff, Model model) {

        adminService.updateStaff(staff);
        log.info("editStaff called..." + staff.getStaffId());
        model.addAttribute("", adminService.getStaff());

        return REDIRECT + STAFF;
    }

    //Gustav
    @GetMapping("/staff")
    public String staff(Model model) {
        log.info("staff called...");

        model.addAttribute("pageTitle", "Personale");
        List<Staff> staffs = adminService.getStaff();
        model.addAttribute("staffs", staffs);
        model.addAttribute("isStaff", true);

        return STAFF;

    }

    //Gustav
    @GetMapping("/deleteStaff/{staffId}")
    public String deleteStaff(@PathVariable("staffId") int staffId, Model model) {
        log.info("deleteStaff with called with id :" + staffId);

        model.addAttribute("staff", adminService.getStaffMember(staffId));
        String staffName = adminService.getStaffMember(staffId).getStaffName();
        model.addAttribute("pageTitle", "Slet personale (" + staffName + ")");

        return DELETESTAFF;

    }

    //Gustav
    @PutMapping("/deleteStaff")
    public String deleteStaff(@ModelAttribute Staff staff, Model model) {
        log.info("delete confirmed deleting staffmember with Id" + staff.getStaffId());
        int id = staff.getStaffId();

        adminService.deleteStaffMember(id);

        model.addAttribute("staffs", adminService.getStaff());

        return REDIRECT + STAFF;

    }

    //Gustav
    @GetMapping("/createStaffMember")
    public String createStaffMember(Model model) {
        log.info("CreateStaffMember alled..");

        model.addAttribute("staff", new Staff());
        model.addAttribute("pageTitle", "Opret personale");

        return CREATESTAFFMEMBER;
    }

    //Gustav
    @PostMapping("/createStaffMember")
    public String createStaffMember(@ModelAttribute Staff staff, Model model) {
        log.info("createStaffMember postmapping called..");

        adminService.createStaffMember(staff);

        model.addAttribute("staff", adminService.getStaff());

        return REDIRECT + STAFF;

    }

    //Hasan
    @GetMapping("/offer")
    public String offer(Model model) {
        log.info("offer is called... med en liste af alle offer");

        List<Offer> offers = adminService.getOffers();
        model.addAttribute("offers", offers);
        model.addAttribute("pageTitle", "Tilbuds admin");
        model.addAttribute("isOffer", true);

        return OFFER;
    }

    //Hasan
    @GetMapping("/createOffer")
    public String createOffer(Model model) {
        log.info("createOffer getmapping is been called...");

        model.addAttribute("offer", new Offer());
        model.addAttribute("pageTitle", "Opret tilbud");

        return CREATEOFFER;
    }

    //Hasan
    @PostMapping("/createOffer")
    public String createOffer(@ModelAttribute Offer offer, Model model) {
        log.info("create Offer postmapping is called");

        log.info("offerName: " + offer.getOfferName() + " offerContent: " + offer.getOfferContent() + " offerStart: " + offer.getOfferStart() + " offerEnd: " + offer.getOfferEnd());

        adminService.createOffer(offer);
        model.addAttribute("Offers", adminService.getOffers());

        return REDIRECT + OFFER;
    }

    //Gustav
    @GetMapping("/upload")
    public String upload(Model model) {
        log.info("upload getmapping is called...");

        model.addAttribute("pageTitle", "Upload billede");
        model.addAttribute("isUpload", true);

        return UPLOAD;
    }

    //Gustav
    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, Model model, Picture picture) {
        log.info("upload postmapping is called...");
        if (file.isEmpty()) {

            redirectAttributes.addFlashAttribute("message", "vælg et billede som du vil uploade til billede galleri siden");
            return "redirect: uploadlist";
        }

        adminService.fileUpload(file);

        redirectAttributes.addFlashAttribute("message", "du har uploadet et nyt billede til galleriet " + file.getOriginalFilename());

        return REDIRECT + UPLOADLIST;
    }

    //Gustav
    @GetMapping("/uploadlist")
    public String uploadlist(Model model) {
        log.info("uploadlist getmapping is called...");


        model.addAttribute("pageTitle", "Upload liste");

        return UPLOADLIST;
    }

    // Gustav & Hasan
    @GetMapping("/portfolio")
    public String portfolio(Model model) {
        log.info("billedegalleri getmapping is called...");
        List<Picture> test = adminService.getPictures();
        log.info("billedeGalleri called...");
        log.info("" + test.get(0).getPictureName());
        model.addAttribute("pictures", adminService.getPictures());
        model.addAttribute("pageTitle", "Portfolio");
        model.addAttribute("isPortfolio", true);

        return PORTFOLIO;
    }

    //Gustav
    @GetMapping("adminDashBoard")
    public String adminDashBoard(Model model) {
        log.info("adminDashboard getmapping is called...");

        model.addAttribute("pageTitle", "Admin dashboard");

        return ADMINDASHBOARD;
    }

    //Hasan
    @GetMapping("/editOffer/{id}")
    public String editOffer ( @PathVariable("id") int id, Model model){
        log.info("Edit Offer is been  called..." + id);

        model.addAttribute("offer", adminService.findOffer(id));

        String offerName = adminService.findOffer(id).getOfferName();
        model.addAttribute("pageTitle", "Rediger tilbud (" + offerName + ")");
        model.addAttribute("offerName", offerName);

        return EDITOFFER;
    }

    //Hasan
    @PutMapping("/editOffer")
    public String editOffer (@ModelAttribute Offer offer, Model model){
        log.info("Edit Offer  putmapping is been  called...");

        adminService.updateOffer(offer);

        model.addAttribute("offers", adminService.getOffers());

        return REDIRECT + OFFER;
    }

    //Hasan
    @GetMapping("/deleteOffer/{id}")
    public String deleteOffer (@PathVariable Integer id, Model model){
        log.info("Delete offer med  id: " + id + "?");

        model.addAttribute("offer", adminService.findOffer(id));
        String offerName = adminService.findOffer(id).getOfferName();
        model.addAttribute("pageTitle", "Slet tilbud (" + offerName + ")");

        return DELETEOFFER;
    }

    //Hasan
    @PutMapping("/deleteOffer")
    public String deleteOffer (@ModelAttribute Offer offer, Model model){
        log.info("delete confirmed deleting offer " + offer.getOfferId());
        int id = offer.getOfferId();

        adminService.deleteOffer(id);

        model.addAttribute("offers", adminService.getOffers());

        return REDIRECT + OFFER;
    }

    //Hasan
    @GetMapping("/tilbud")
    public String tilbud (Model model){
        log.info("offerPage is called");
        model.addAttribute("offers", adminService.showOffers());
        model.addAttribute("pageTitle", "Tilbud");

        model.addAttribute("isTilbud", true);

        return TILBUD;
    }

    //Niklas
    @GetMapping("/editOpeningHours")
    public String editOpeningHours (Model model){
        log.info("Edit opening hours getmapping called...");

        Opening[] opening = adminService.convertOpenings();
        ArrayList<Integer> hours = adminService.getHours();
        ArrayList<Integer> min = adminService.getMin();

        model.addAttribute("pageTitle", "Rediger openings tider");
        model.addAttribute("isOpening", true);

        model.addAttribute("openings", opening);
        model.addAttribute("hours", hours);
        model.addAttribute("min", min);

        return EDITOPENINGHOURS;
    }

    //Niklas
    @PutMapping("/editOpeningHours")
    public String editOpeningHours (@ModelAttribute Opening opening) {
        log.info("Edit opening hours putmapping called... openingId=" + opening.getOpeningId());

        adminService.saveOpeningHours(opening);

        return REDIRECT + EDITOPENINGHOURS;
    }

    //Felix
    @GetMapping("/bookingAdmin")
    public String bookingAdmin(Model model) {
        log.info("booking called...");

        String date = adminService.getDateToday();
        int weekNumber = adminService.getWeekToday();
        List<BookingGroup> bookingGroups;
        String[] dates = adminService.getDatesOfWeek();
        Opening[] openings = adminService.getOpenings();
        String holiday;

        String[] weekdays = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};


        for(int i = 0; i < 6; i++) {
            bookingGroups = adminService.getBookingGroups(dates[i], openings[i].getOpeningTime(), openings[i].getOpeningClose());
            model.addAttribute(weekdays[i] + "Bookings", bookingGroups);
            model.addAttribute(weekdays[i], dates[i]);
            model.addAttribute("isHoliday_" + weekdays[i], adminService.isHolidayByDate(dates[i]));
            if (adminService.isHolidayByDate(dates[i]) == true) {
                holiday = adminService.findHolidayByDate(dates[i]).getHolidayName();
            } else {
                holiday = "";
            }
            model.addAttribute("holiday_" + weekdays[i], holiday);
        }

        model.addAttribute("sunday", dates[6]);
        model.addAttribute("nextWeek", adminService.nextWeek());
        model.addAttribute("prevWeek", adminService.prevWeek());
        model.addAttribute("weekNumber", weekNumber);
        model.addAttribute("pageTitle", "Booking administration");
        model.addAttribute("isBookingAdmin", true);

        //log.info(bookingService.getDateToday());

        return BOOKINGADMIN;
    }

    //Felix
    @PostMapping("/goToDateAdmin")
    public String goToDateAdmin(@RequestParam("date")String date, Model model) {
        log.info("goToDate called...");

        //log.info("Go to date: "+date);

        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);

        date = day + "-" + month + "-" + year;

        //log.info("Go to date formatted: " + date);

        return REDIRECT + BOOKINGADMIN + "/" + date;
    }

    //Felix
    @GetMapping("/bookingAdmin/{day}-{month}-{year}")
    public String bookingAdminDate(@PathVariable("day") int day, @PathVariable("month") int month, @PathVariable("year") int year, Model model) {
        log.info("booking called...");

        String date = day + "-" + month + "-" + year;
        int weekNumber = adminService.getWeekFromDate(day, month, year);
        List<BookingGroup> bookingGroups;
        String[] dates = adminService.getDatesOfSelectedWeek(day, month, year);
        Opening[] openings = adminService.getOpenings();
        String holiday;

        String[] weekdays = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};


        for(int i = 0; i < 6; i++) {
            bookingGroups = adminService.getBookingGroups(dates[i], openings[i].getOpeningTime(), openings[i].getOpeningClose());
            model.addAttribute(weekdays[i] + "Bookings", bookingGroups);
            model.addAttribute(weekdays[i], dates[i]);
            model.addAttribute("isHoliday_" + weekdays[i], adminService.isHolidayByDate(dates[i]));
            if (adminService.isHolidayByDate(dates[i]) == true) {
                holiday = adminService.findHolidayByDate(dates[i]).getHolidayName();
            } else {
                holiday = "";
            }
            model.addAttribute("holiday_" + weekdays[i], holiday);
        }

        model.addAttribute("sunday", dates[6]);
        model.addAttribute("nextWeek",adminService.nextWeekFromDate(day, month, year));
        model.addAttribute("prevWeek",adminService.prevWeekFromDate(day, month, year));
        model.addAttribute("weekNumber",weekNumber);
        model.addAttribute("pageTitle", "Booking administration");
        model.addAttribute("isBookingAdmin", true);

        //log.info(bookingService.getDateToday());

        return BOOKINGADMIN;
    }

    //Felix
    @GetMapping("/timeSelectAdmin/{date}/{start}/{end}")
    public String timeSelect(@PathVariable String date, @PathVariable String start, @PathVariable String end, Model model) {
        log.info("timeSelect called...");

        if(start.length() == 4){
            start = "0" + start;
        }

        if(end.length() == 4){
            end = "0" + end;
        }

        List<Booking> bookings = adminService.getSelectedBookings(date, start, end);
        model.addAttribute("bookings", bookings);
        model.addAttribute("date", date);
        model.addAttribute("pageTitle", "Tider for " + date + ", mellem kl." + start + " - " + end);

        return TIMESELECTADMIN;
    }

    //Felix
    @GetMapping("/createBooking/{bookingTime}/{bookingDate}")
    public String createBooking(@PathVariable("bookingTime") String bookingTime, @PathVariable("bookingDate") String bookingDate, Model model){

        log.info("createBooking getmapping called...");

        if(adminService.isHolidayByDate(bookingDate) == true || adminService.isBooked(bookingDate, bookingTime) == true ) {
            model.addAttribute("pageTitle", "Denne tid ikke tilgængelig");

            return TIMENOTAVAILABLE;
        } else if (bookingDate.length() < 10 || bookingTime.length() < 5) {
            model.addAttribute("pageTitle", "Denne tid ikke tilgængelig");

            return TIMENOTAVAILABLE;
        } else {
            model.addAttribute("staffs", adminService.getStaff());
            model.addAttribute("booking", new Booking());
            model.addAttribute("pageTitle", "Opret ny bookning");

            model.addAttribute("time", bookingTime);
            model.addAttribute("date", bookingDate);

            return CREATEBOOKING;
        }
    }

    //Felix
    @PostMapping("/createBooking")
    public String createBooking(@ModelAttribute Booking booking){

        log.info("createBooking postmapping called...");

        booking.setBookingToken(adminService.generateRandomString());
        log.info("Token: " + booking.getBookingToken());
        adminService.createBooking(booking);
        adminService.createBookingMail(booking);
        return REDIRECT+BOOKINGADMIN;

    }

    //Felix
    @GetMapping("/editBooking/{bookingId}")
    public String createBooking(@PathVariable("bookingId") int bookingId, Model model){

        log.info("editBooking getmapping called...");

        if(bookingId == 0) {
            model.addAttribute("pageTitle", "Denne tid ikke tilgængelig");

            return TIMENOTAVAILABLE;
        } else {
            model.addAttribute("staffs", adminService.getStaff());
            model.addAttribute("booking", adminService.findBooking(bookingId));
            model.addAttribute("pageTitle", "Rediger bookning");

            return EDITBOOKING;
        }
    }

    //Felix
    @GetMapping("/bookingInfo/{bookingId}")
    public String bookingInfo(@PathVariable("bookingId") int bookingId, Model model){

        log.info("bookingInfo getmapping called...");

        Booking booking = adminService.findBooking(bookingId);

        model.addAttribute("staff", adminService.getStaffMember(booking.getStaffId()).getStaffName());
        model.addAttribute("booking", booking);
        model.addAttribute("pageTitle", "Booking info");

        return BOOKINGINFO;
    }

    //Felix
    @PutMapping("/editBooking")
    public String editStaff(@ModelAttribute Booking booking, Model model){

        log.info("editBooking putmapping called..." + booking.getBookingId());
        adminService.updateBooking(booking);
        adminService.editBookingMail(booking);
        model.addAttribute("", adminService.getBookings());

        return REDIRECT + BOOKINGADMIN;
    }

    @GetMapping("/sendMessage/{bookingId}")
    public String sendMessage(@PathVariable("bookingId") int bookingId, Model model){

        model.addAttribute("id", bookingId);
        model.addAttribute("name", adminService.findBooking(bookingId).getBookingName());
        model.addAttribute("pageTitle", "Send besked");

        return SENDMESSAGE;
    }

    //Felix
    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam("bookingId")int bookingId, @RequestParam("bookingMessage")String bookingMessage, Model model) {
        log.info("sendMessage PostMapping called...");

        Booking booking = adminService.findBooking(bookingId);
        booking.setBookingMessage(bookingMessage);

        adminService.sendMessageMail(booking);


        return REDIRECT + BOOKINGADMIN;
    }

    //Felix
    @GetMapping("/deleteBooking/{bookingId}")
    public String deleteBooking(@PathVariable("bookingId") int bookingId, Model model){
        log.info("deleteBooking GetMapping called...");

        Booking booking = adminService.findBooking(bookingId);
        model.addAttribute("booking", booking);
        model.addAttribute("pageTitle", "Slet booking for " + booking.getBookingName());

        return DELETEBOOKING;
    }

    //Felix
    @DeleteMapping("/deleteBooking")
    public String deleteBooking(@ModelAttribute Booking booking, Model model){
        log.info("deleteBooking DeleteMapping called...");

        Booking booking_ = adminService.findBooking(booking.getBookingId());
        booking_.setBookingMessage(booking.getBookingMessage());
        adminService.deleteBookingMail(booking_);
        adminService.deleteBooking(booking.getBookingId());

        return REDIRECT + BOOKINGADMIN;
    }
}

