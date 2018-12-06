package com.stylezone.demo.controllers;

import com.stylezone.demo.models.Booking;
import com.stylezone.demo.models.Offer;
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

    @GetMapping("/booking")
    public String booking(Model model) {
        log.info("booking called...");

        String date = bookingService.getDateToday();
        int weekNumber = bookingService.getWeekToday();
        List<BookingGroup> bookingGroups;
        String[] dates = bookingService.getDatesOfWeek();
        Opening[] openings = bookingService.getOpenings();

        bookingGroups = bookingService.getBookingGroups(dates[0], openings[0].getOpeningTime(), openings[0].getOpeningClose());
        model.addAttribute("mondayBookings", bookingGroups);
        model.addAttribute("monday", dates[0]);
        bookingGroups = bookingService.getBookingGroups(dates[1], openings[1].getOpeningTime(), openings[1].getOpeningClose());
        model.addAttribute("tuesdayBookings", bookingGroups);
        model.addAttribute("tuesday", dates[1]);
        bookingGroups = bookingService.getBookingGroups(dates[2], openings[2].getOpeningTime(), openings[2].getOpeningClose());
        model.addAttribute("wednesdayBookings", bookingGroups);
        model.addAttribute("wednesday", dates[2]);
        bookingGroups = bookingService.getBookingGroups(dates[3], openings[3].getOpeningTime(), openings[3].getOpeningClose());
        model.addAttribute("thursdayBookings", bookingGroups);
        model.addAttribute("thursday", dates[3]);
        bookingGroups = bookingService.getBookingGroups(dates[4], openings[4].getOpeningTime(), openings[4].getOpeningClose());
        model.addAttribute("fridayBookings", bookingGroups);
        model.addAttribute("friday", dates[4]);
        bookingGroups = bookingService.getBookingGroups(dates[5], openings[5].getOpeningTime(), openings[5].getOpeningClose());
        model.addAttribute("saturdayBookings", bookingGroups);
        model.addAttribute("saturday", dates[5]);

        model.addAttribute("sunday", dates[6]);
        model.addAttribute("nextWeek",bookingService.nextWeek());
        model.addAttribute("prevWeek",bookingService.prevWeek());
        model.addAttribute("weekNumber",weekNumber);
        model.addAttribute("pageTitle", "Book tid");

        //log.info(bookingService.getDateToday());

        return BOOKING;
    }

    @PostMapping("/goToDate")
    public String goToDate(@RequestParam("date")String date, Model model) {

        //log.info("Go to date: "+date);

        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);

        date = day + "-" + month + "-" + year;

        //log.info("Go to date formatted: " + date);

        return REDIRECT + BOOKING + "/" + date;
    }


    @GetMapping("/booking/{day}-{month}-{year}")
    public String bookingDate(@PathVariable("day") int day, @PathVariable("month") int month, @PathVariable("year") int year, Model model) {
        log.info("booking called...");

        String date = day + "-" + month + "-" + year;
        int weekNumber = bookingService.getWeekFromDate(day, month, year);
        List<BookingGroup> bookingGroups;
        String[] dates = bookingService.getDatesOfSelectedWeek(day, month, year);
        Opening[] openings = bookingService.getOpenings();


        bookingGroups = bookingService.getBookingGroups(dates[0], openings[0].getOpeningTime(), openings[0].getOpeningClose());
        model.addAttribute("mondayBookings", bookingGroups);
        model.addAttribute("monday", dates[0]);
        bookingGroups = bookingService.getBookingGroups(dates[1], openings[1].getOpeningTime(), openings[1].getOpeningClose());
        model.addAttribute("tuesdayBookings", bookingGroups);
        model.addAttribute("tuesday", dates[1]);
        bookingGroups = bookingService.getBookingGroups(dates[2], openings[2].getOpeningTime(), openings[2].getOpeningClose());
        model.addAttribute("wednesdayBookings", bookingGroups);
        model.addAttribute("wednesday", dates[2]);
        bookingGroups = bookingService.getBookingGroups(dates[3], openings[3].getOpeningTime(), openings[3].getOpeningClose());
        model.addAttribute("thursdayBookings", bookingGroups);
        model.addAttribute("thursday", dates[3]);
        bookingGroups = bookingService.getBookingGroups(dates[4], openings[4].getOpeningTime(), openings[4].getOpeningClose());
        model.addAttribute("fridayBookings", bookingGroups);
        model.addAttribute("friday", dates[4]);
        bookingGroups = bookingService.getBookingGroups(dates[5], openings[5].getOpeningTime(), openings[5].getOpeningClose());
        model.addAttribute("saturdayBookings", bookingGroups);
        model.addAttribute("saturday", dates[5]);

        model.addAttribute("sunday", dates[6]);
        model.addAttribute("nextWeek",bookingService.nextWeekFromDate(day, month, year));
        model.addAttribute("prevWeek",bookingService.prevWeekFromDate(day, month, year));
        model.addAttribute("weekNumber",weekNumber);
        model.addAttribute("pageTitle", "Book tid");

        //log.info(bookingService.getDateToday());

        return BOOKING;
    }

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








    @GetMapping("/saveBooking/{bookingTime}/{bookingDate}")
    public String saveBooking(@PathVariable("bookingTime") String bookingTime, @PathVariable("bookingDate") String bookingDate, Model model){

        log.info("saveBooking getmapping called...");

        model.addAttribute("booking", new Booking());

        model.addAttribute("time", bookingTime);
        model.addAttribute("date", bookingDate);


        return SAVEBOOKING;
    }

    @PostMapping("/saveBooking")
    public String saveBooking(@ModelAttribute Booking booking,
                              @RequestParam("g-recaptcha-response") String captchaResponse){

        log.info("saveBooking postmapping called...");

        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=6LeWE30UAAAAAMUpo7seu91Da6DXig-DQxN8YKEQ&response="+captchaResponse;

        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url+params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();

        if(reCaptchaResponse.isSuccess()) {

            bookingService.saveBooking(booking);
            bookingService.sendEmail(booking);
            return REDIRECT+BOOKING;
        }


        return SAVEBOOKING;

    }

    @GetMapping("/omOs")
    public String omOs() {

        return OMOS;
    }

    @GetMapping("/priser")
    public String priser(){


        return PRISER;
    }

}


