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
import java.util.ArrayList;
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

    private final String ADMINLOGIN = "adminLogin";
    private final String SUCCESS = "success";
    private final String REDIRECT = "redirect:/";
    private final String EDITSTAFF = "editStaff";
    private final String STAFF = "staff";
    private final String DELETESTAFF = "deleteStaff";
    private final String CREATESTAFFMEMBER = "createStaffMember";
    private final String OFFER = "offer";
    private final String CREATEOFFER = "createOffer";
    private final String EDITOPENINGHOURS = "editOpeningHours";

    Logger log = Logger.getLogger(AdminController.class.getName());

    @GetMapping("/adminLogin")
    public String adminLogin(Model model) {
        log.info("adminLogin GetMapping called...");

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

        List<Staff> staffs = adminService.getStaff();
        model.addAttribute("staffs", staffs);

        return STAFF;

    }
    @PostMapping("/staff")
    public String staff(@ModelAttribute Staff staff, Model model){
        log.info("Staff called...");

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

    @GetMapping("/editOpeningHours")
    public String editOpeningHours(Model model) {
        log.info("Edit opening hours getmapping called...");

        Opening[] opening = adminService.convertOpenings();
        ArrayList<Integer> hours = adminService.getHours();
        ArrayList<Integer> min = adminService.getMin();

        log.info(""+opening[5].getOpeningHour());
        model.addAttribute("opening", opening);
        model.addAttribute("hours",hours);
        model.addAttribute("min", min);

        return EDITOPENINGHOURS;
    }

    @PostMapping("/editOpeningHours")
    public String editOpeningHours(@ModelAttribute Opening opening) {
        log.info("Edit opening hours postmapping called...");


        return EDITOPENINGHOURS;
    }
}
