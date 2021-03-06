package com.stylezone.demo.controllers;

import com.stylezone.demo.models.Booking;
import com.stylezone.demo.models.Opening;
import com.stylezone.demo.models.ReCaptchaResponse;
import com.stylezone.demo.models.BookingGroup;
import com.stylezone.demo.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class BookingController {


    public BookingController() {
    }

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    BookingService bookingService;

    private final String REDIRECT = "redirect:/";
    private final String SAVEBOOKING = "saveBooking";
    private final String TIMENOTAVAILABLE = "timeNotAvailable";
    private final String BOOKING = "booking";
    private final String TIMESELECT = "timeSelect";
    private final String INDEX = "index";
    private final String OMOS = "omOs";
    private final String PRISER = "priser";

    Logger log = Logger.getLogger(BookingController.class.getName());

    @GetMapping("/")
    public String index(Model model) {
        log.info("Index called...");

        model.addAttribute("pageTitle", "Forside");
        model.addAttribute("isIndex", true);

        return INDEX;
    }

    //Felix
    @GetMapping("/booking")
    public String booking(Model model) {
        log.info("booking called...");

        String date = bookingService.getDateToday();
        int weekNumber = bookingService.getWeekToday();
        List<BookingGroup> bookingGroups;
        String[] dates = bookingService.getDatesOfWeek();
        Opening[] openings = bookingService.getOpenings();
        String holiday;

        String[] weekdays = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};


        for(int i = 0; i < 6; i++) {
            bookingGroups = bookingService.getBookingGroups(dates[i], openings[i].getOpeningTime(), openings[i].getOpeningClose());
            model.addAttribute(weekdays[i] + "Bookings", bookingGroups);
            model.addAttribute(weekdays[i], dates[i]);
            model.addAttribute("isHoliday_" + weekdays[i], bookingService.isHolidayByDate(dates[i]));
            if (bookingService.isHolidayByDate(dates[i]) == true) {
                holiday = bookingService.findHolidayByDate(dates[i]).getHolidayName();
            } else {
                holiday = "";
            }
            model.addAttribute("holiday_" + weekdays[i], holiday);
        }

        model.addAttribute("sunday", dates[6]);
        model.addAttribute("nextWeek", bookingService.nextWeek());
        model.addAttribute("prevWeek", bookingService.prevWeek());
        model.addAttribute("weekNumber", weekNumber);
        model.addAttribute("pageTitle", "Book tid");

        return BOOKING;
    }

    //Felix
    @PostMapping("/goToDate")
    public String goToDate(@RequestParam("date")String date) {
        log.info("goToDate called...");

        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);

        date = day + "-" + month + "-" + year;

        return REDIRECT + BOOKING + "/" + date;
    }

    //Felix
    @GetMapping("/booking/{day}-{month}-{year}")
    public String bookingDate(@PathVariable("day") int day, @PathVariable("month") int month, @PathVariable("year") int year, Model model) {
        log.info("booking called...");

        String date = day + "-" + month + "-" + year;
        int weekNumber = bookingService.getWeekFromDate(day, month, year);
        List<BookingGroup> bookingGroups;
        String[] dates = bookingService.getDatesOfSelectedWeek(day, month, year);
        Opening[] openings = bookingService.getOpenings();
        String holiday;

        String[] weekdays = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};


        for(int i = 0; i < 6; i++) {
            bookingGroups = bookingService.getBookingGroups(dates[i], openings[i].getOpeningTime(), openings[i].getOpeningClose());
            model.addAttribute(weekdays[i] + "Bookings", bookingGroups);
            model.addAttribute(weekdays[i], dates[i]);
            model.addAttribute("isHoliday_" + weekdays[i], bookingService.isHolidayByDate(dates[i]));
            if (bookingService.isHolidayByDate(dates[i]) == true) {
                holiday = bookingService.findHolidayByDate(dates[i]).getHolidayName();
            } else {
                holiday = "";
            }
            model.addAttribute("holiday_" + weekdays[i], holiday);
        }

        model.addAttribute("sunday", dates[6]);
        model.addAttribute("nextWeek",bookingService.nextWeekFromDate(day, month, year));
        model.addAttribute("prevWeek",bookingService.prevWeekFromDate(day, month, year));
        model.addAttribute("weekNumber",weekNumber);
        model.addAttribute("pageTitle", "Book tid");

        return BOOKING;
    }

    //Felix
    @GetMapping("/timeSelect/{date}/{start}/{end}")
    public String timeSelect(@PathVariable String date, @PathVariable String start, @PathVariable String end, Model model) {
        log.info("timeSelect called...");

        if(start.length() == 4){
            start = "0" + start;
        }

        if(end.length() == 4){
            end = "0" + end;
        }

        List<Booking> bookings = bookingService.getSelectedBookings(date, start, end);
        model.addAttribute("bookings", bookings);
        model.addAttribute("date", date);
        model.addAttribute("pageTitle", "Tider for " + date + ", mellem kl." + start + " - " + end);

        return TIMESELECT;
    }

    //Gustav
    @GetMapping("/saveBooking/{bookingTime}/{bookingDate}")
    public String saveBooking(@PathVariable("bookingTime") String bookingTime, @PathVariable("bookingDate") String bookingDate, Model model){

        log.info("saveBooking getmapping called...");

        if(bookingService.isHolidayByDate(bookingDate) == true || bookingService.isBooked(bookingDate, bookingTime) == true ) {
            model.addAttribute("pageTitle", "Denne tid ikke tilgængelig");

            return TIMENOTAVAILABLE;
        } else if (bookingDate.length() < 10 || bookingTime.length() < 5) {
            model.addAttribute("pageTitle", "Denne tid ikke tilgængelig");

            return TIMENOTAVAILABLE;
        } else {
            model.addAttribute("staffs", bookingService.getStaff());
            model.addAttribute("booking", new Booking());
            model.addAttribute("pageTitle", "Opret ny bookning");

            model.addAttribute("time", bookingTime);
            model.addAttribute("date", bookingDate);

            return SAVEBOOKING;
        }
    }

    //Gustav
    @PostMapping("/saveBooking")
    public String saveBooking(@ModelAttribute Booking booking,
                              @RequestParam("g-recaptcha-response") String captchaResponse){

        log.info("saveBooking postmapping called...");

        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=6LdRxYIUAAAAAJOYs1-rC1md7a8ADQpwKSLoNE0S&response="+captchaResponse;

        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url+params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();

        if(reCaptchaResponse.isSuccess()) {

            booking.setBookingToken(bookingService.generateRandomString());
            log.info("Token: " + booking.getBookingToken());
            bookingService.saveBooking(booking);
            bookingService.sendEmail(booking);
            return REDIRECT+BOOKING;
        } else {

            log.info("Save booking failed!");
            return REDIRECT + SAVEBOOKING + "/" + booking.getBookingTime() + "/" + booking.getBookingDate();
        }

    }

    //Gustav
    @GetMapping("/omOs")
    public String omOs(Model model) {
        log.info("omOs called...");

        model.addAttribute("pageTitle", "Om os");
        model.addAttribute("isAbout", true);

        return OMOS;
    }

    //Gustav
    @GetMapping("/priser")
    public String priser(Model model) {
        log.info("priser called...");

        model.addAttribute("pageTitle", "priser");
        model.addAttribute("isPriser", true);

        return PRISER;
    }

}


