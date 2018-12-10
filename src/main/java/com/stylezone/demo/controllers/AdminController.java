package com.stylezone.demo.controllers;

import com.stylezone.demo.models.*;
import com.stylezone.demo.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

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
    private final String BOOKINGADMIN = "bookingAdmin";
    private final String TIMESELECTADMIN = "timeSelectAdmin";
    private final String CREATEBOOKING = "createBooking";
    private final String TIMENOTAVAILABLE = "timeNotAvailable";

    Logger log = Logger.getLogger(AdminController.class.getName());

    @GetMapping("/admin")
    public String admin(){
        return REDIRECT + ADMINLOGIN;
    }

    @GetMapping("/adminLogin")
    public String adminLogin(Model model) {
        log.info("adminLogin GetMapping called...");

        model.addAttribute("pageTitle", "Admin login");
        model.addAttribute("admin", new Admin());

        return ADMINLOGIN;
    }

    @PostMapping("/adminLogin")
    public String adminLogin(@ModelAttribute Admin admin,
                             @RequestParam("g-recaptcha-response") String captchaResponse,
                             HttpSession session) {

        log.info("adminLogin PostMapping called...");

        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=6LeWE30UAAAAAMUpo7seu91Da6DXig-DQxN8YKEQ&response="+captchaResponse;

        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url+params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();

        int adminPW = adminService.hashPassword(admin.getAdminPassword());

        admin.setAdminPassword(""+adminPW);
        Admin adminCheck = adminService.searchUser(admin);

        if(adminCheck.getAdminPassword().equals(admin.getAdminPassword()) && adminCheck.getAdminUsername().equals(admin.getAdminUsername()) && reCaptchaResponse.isSuccess()) {
            log.info("Login is a success");
            session.setAttribute("loggedin", adminCheck);
            return REDIRECT+STAFF;
        }

        log.info("Login failed.");
        return REDIRECT;
    }

    @GetMapping("/editStaff/{staffId}")
    public String editStaff(@PathVariable("staffId")int staffId, Model model){
        log.info("editStaff GetMapping called...");

        Staff staff = adminService.getStaffMember(staffId);

        model.addAttribute("pageTitle", "Rediger personale");
        model.addAttribute("staff",staff );

        return EDITSTAFF;

    }

    @PutMapping("/editStaff")
    public String editStaff(@ModelAttribute Staff staff, Model model){

        adminService.updateStaff(staff);
        log.info("editStaff called..." + staff.getStaffId());
        model.addAttribute("", adminService.getStaff());

        return REDIRECT + STAFF;
    }

    @GetMapping("/staff")
    public String staff(Model model){
        log.info("staff called...");

        model.addAttribute("pageTitle", "Personale");
        List<Staff> staffs = adminService.getStaff();
        model.addAttribute("staffs", staffs);
        model.addAttribute("isStaff", true);

        return STAFF;

    }
    @PostMapping("/staff")
    public String staff(@ModelAttribute Staff staff, Model model){
        log.info("Staff called...");

        model.addAttribute("pageTitle", "Personale");
        model.addAttribute("staffs", adminService.getStaff());

        return STAFF;
    }

    @GetMapping("/deleteStaff/{staffId}")
    public String deleteStaff(@PathVariable("staffId") int staffId, Model model){
        log.info("deleteStaff with called with id :" + staffId );

        model.addAttribute("staff",adminService.getStaffMember(staffId));
        String staffName = adminService.getStaffMember(staffId).getStaffName();
        model.addAttribute("pageTitle", "Delete staff ("+ staffName + ")");

        return DELETESTAFF;

    }
    @PutMapping("/deleteStaff")
    public String deleteStaff(@ModelAttribute Staff staff, Model model){
        log.info("delete confirmed deleting staffmember with Id" + staff.getStaffId());
        int id = staff.getStaffId();

        adminService.deleteStaffMember(id);

        model.addAttribute("staffs", adminService.getStaff() );
        model.addAttribute("pageTitle", "Delete staffMember" );

        return REDIRECT + STAFF;

    }

    @GetMapping("/createStaffMember")
    public String createStaffMember(Model model){
        log.info("CreateStaffMember alled..");

        model.addAttribute("staff", new Staff());
        model.addAttribute("pageTitle", "Create new Staff Member");

        return CREATESTAFFMEMBER;
    }

    @PostMapping("/createStaffMember")
    public String createStaffMember(@ModelAttribute Staff staff, Model model){
        log.info("createStaffMember postmapping called..");

        adminService.createStaffMember(staff);

        model.addAttribute("staff", adminService.getStaff());
        model.addAttribute("pageTitle", "Create staff" );

        return REDIRECT + STAFF;

    }


    @GetMapping("/offer")
    public String offer(Model model) {
        log.info("Index called...");

        List<Offer> offers = adminService.getOffers();
        model.addAttribute("offers", offers);
        model.addAttribute("pageTitle", "offer");
        model.addAttribute("isOffer", true);

        return OFFER;
    }

    @GetMapping("/createOffer")
    public String createOffer(Model model) {
        log.info("createOffer getmapping is been called...");

        model.addAttribute("offer", new Offer());
        model.addAttribute("pageTitle", "Create offer");

        return CREATEOFFER;
    }

    @PostMapping("/createOffer")
    public String createOffer(@ModelAttribute Offer offer, Model model){
        log.info("create Offer postmapping is called");

        log.info("offerName: " + offer.getOfferName() + " offerContent: " + offer.getOfferContent() + " offerStart: " + offer.getOfferStart() + " offerEnd: " + offer.getOfferEnd());

        adminService.createOffer(offer);
        model.addAttribute("Offers", adminService.getOffers());
        model.addAttribute("pageTitle", "Create offer");

        return REDIRECT;
    }

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

    @PostMapping("/createBooking")
    public String createBooking(@ModelAttribute Booking booking,
                              @RequestParam("g-recaptcha-response") String captchaResponse){

        log.info("createBooking postmapping called...");

        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=6LeWE30UAAAAAMUpo7seu91Da6DXig-DQxN8YKEQ&response="+captchaResponse;

        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url+params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();

        if(reCaptchaResponse.isSuccess()) {

            booking.setBookingToken(adminService.generateRandomString());
            log.info("Token: " + booking.getBookingToken());
            adminService.createBookingMail(booking);
            booking.clearBookingMassage();
            adminService.createBooking(booking);
            return REDIRECT+BOOKINGADMIN;
        } else {

            log.info("Save booking failed!");
            return REDIRECT + CREATEBOOKING + "/" + booking.getBookingTime() + "/" + booking.getBookingDate();
        }

    }
}
