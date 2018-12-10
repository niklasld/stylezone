package com.stylezone.demo.controllers;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Offer;
import com.stylezone.demo.models.Picture;
import com.stylezone.demo.models.Staff;
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

    private final String ADMINLOGIN = "adminLogin";
    private final String REDIRECT = "redirect:/";
    private final String EDITSTAFF = "editStaff";
    private final String STAFF = "staff";
    private final String DELETESTAFF = "deleteStaff";
    private final String CREATESTAFFMEMBER = "createStaffMember";
    private final String OFFER = "offer";
    private final String CREATEOFFER = "createOffer";
    private final String BILLEDEGALLERI = "billedeGalleri";
    private final String UPLOADLIST = "uploadlist";
    private final String UPLOAD = "upload";
    private final String ADMINDASHBOARD = "adminDashBoard";
    private final String EDITOFFER = "editOffer";
    private final String DELETEOFFER = "deleteOffer";
    private final String OFFERPAGE = "offerPage";
    private final String EDITOPENINGHOURS = "editOpeningHours";

    Logger log = Logger.getLogger(AdminController.class.getName());

    //Niklas
    @GetMapping("/adminLogin")
    public String adminLogin(Model model) {
        log.info("adminLogin GetMapping called...");

        model.addAttribute("admin", new Admin());

        return ADMINLOGIN;
    }

    //Niklas
    @PostMapping("/adminLogin")
    public String adminLogin(@ModelAttribute Admin admin,
                             @RequestParam("g-recaptcha-response") String captchaResponse,
                             HttpSession session) {

        log.info("adminLogin PostMapping called...");

        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=6LeWE30UAAAAAMUpo7seu91Da6DXig-DQxN8YKEQ&response=" + captchaResponse;

        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url + params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();

        int adminPW = adminService.hashPassword(admin.getAdminPassword());

        admin.setAdminPassword("" + adminPW);
        Admin adminCheck = adminService.searchUser(admin);

        if (adminCheck.getAdminPassword().equals(admin.getAdminPassword()) && adminCheck.getAdminUsername().equals(admin.getAdminUsername()) && reCaptchaResponse.isSuccess()) {
            log.info("Login is a success");
            session.setAttribute("loggedin", adminCheck);
            return REDIRECT + STAFF;
        }

        log.info("Login failed.");
        return REDIRECT;
    }

    //Gustav
    @GetMapping("/editStaff/{staffId}")
    public String editStaff(@PathVariable("staffId") int staffId, Model model) {
        log.info("editStaff GetMapping called...");

        Staff staff = adminService.getStaffMember(staffId);

        model.addAttribute("staff", staff);

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

        List<Staff> staffs = adminService.getStaff();
        model.addAttribute("staffs", staffs);

        return STAFF;

    }

    //Gustav
    @PostMapping("/staff")
    public String staff(@ModelAttribute Staff staff, Model model) {
        log.info("Staff called...");

        model.addAttribute("staffs", adminService.getStaff());

        return STAFF;
    }

    //Gustav
    @GetMapping("/deleteStaff/{staffId}")
    public String deleteStaff(@PathVariable("staffId") int staffId, Model model) {
        log.info("deleteStaff with called with id :" + staffId);

        model.addAttribute("staff", adminService.getStaffMember(staffId));
        String staffName = adminService.getStaffMember(staffId).getStaffName();
        model.addAttribute("pageTitle", "Delete staff (" + staffName + ")");

        return DELETESTAFF;

    }

    //Gustav
    @PutMapping("/deleteStaff")
    public String deleteStaff(@ModelAttribute Staff staff, Model model) {
        log.info("delete confirmed deleting staffmember with Id" + staff.getStaffId());
        int id = staff.getStaffId();

        adminService.deleteStaffMember(id);

        model.addAttribute("staffs", adminService.getStaff());
        model.addAttribute("pageTitle", "Delete staffMember");

        return REDIRECT + STAFF;

    }

    //Gustav
    @GetMapping("/createStaffMember")
    public String createStaffMember(Model model) {
        log.info("CreateStaffMember alled..");

        model.addAttribute("staff", new Staff());
        model.addAttribute("pageTitle", "Create new Staff Member");

        return CREATESTAFFMEMBER;
    }

    //Gustav
    @PostMapping("/createStaffMember")
    public String createStaffMember(@ModelAttribute Staff staff, Model model) {
        log.info("createStaffMember postmapping called..");

        adminService.createStaffMember(staff);

        model.addAttribute("staff", adminService.getStaff());
        model.addAttribute("pageTitle", "Create staff");

        return REDIRECT + STAFF;

    }

    //Hasan
    @GetMapping("/offer")
    public String offer(Model model) {
        log.info("offer is called... med en liste af alle offer");

        List<Offer> offers = adminService.getOffers();
        model.addAttribute("offers", offers);
        model.addAttribute("pageTitle", "offer");

        return OFFER;
    }

    //Hasan
    @GetMapping("/createOffer")
    public String createOffer(Model model) {
        log.info("createOffer getmapping is been called...");

        model.addAttribute("offer", new Offer());
        model.addAttribute("pageTitle", "Create offer");

        return CREATEOFFER;
    }

    //Hasan
    @PostMapping("/createOffer")
    public String createOffer(@ModelAttribute Offer offer, Model model) {
        log.info("create Offer postmapping is called");

        log.info("offerName: " + offer.getOfferName() + " offerContent: " + offer.getOfferContent() + " offerStart: " + offer.getOfferStart() + " offerEnd: " + offer.getOfferEnd());

        adminService.createOffer(offer);
        model.addAttribute("Offers", adminService.getOffers());
        model.addAttribute("pageTitle", "Create offer");

        return REDIRECT;
    }

    //Gustav
    @GetMapping("/upload")
    public String upload(Model model) {
        log.info("upload getmapping is called...");
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
    public String uploadlist() {
        log.info("uploadlist getmapping is called...");
        return UPLOADLIST;
    }

    // Gustav & Hasan
    @GetMapping("/billedeGalleri")
    public String billedGalleri(Model model) {
        log.info("billedegalleri getmapping is called...");
        List<Picture> test = adminService.getPictures();
        log.info("billedeGalleri called...");
        log.info("" + test.get(0).getPictureName());
        model.addAttribute("pictures", adminService.getPictures());

        return BILLEDEGALLERI;
    }

    //Gustav
    @GetMapping("adminDashBoard")
    public String adminDashBoard() {
        log.info("adminDashboard getmapping is called...");
        return ADMINDASHBOARD;
    }

    //Hasan
    @GetMapping("/editOffer/{id}")
    public String editOffer ( @PathVariable("id") int id, Model model){
        log.info("Edit Offer is been  called..." + id);

        model.addAttribute("offer", adminService.findOffer(id));

        String offerName = adminService.findOffer(id).getOfferName();
        model.addAttribute("pageTitle", "Edit offer (" + offerName + ")");
        model.addAttribute("offerName", offerName);

        return EDITOFFER;
    }

    //Hasan
    @PutMapping("/editOffer")
    public String editOffer (@ModelAttribute Offer offer, Model model){
        log.info("Edit Offer  putmapping is been  called...");

        adminService.updateOffer(offer);

        model.addAttribute("offers", adminService.getOffers());
        model.addAttribute("pageTitle", "Edit offer");

        return REDIRECT + OFFER;
    }

    //Hasan
    @GetMapping("/deleteOffer/{id}")
    public String deleteOffer (@PathVariable Integer id, Model model){
        log.info("Delete offer med  id: " + id + "?");

        model.addAttribute("offer", adminService.findOffer(id));
        String offerName = adminService.findOffer(id).getOfferName();
        model.addAttribute("pageTitle", "Delete offer (" + offerName + ")");

        return DELETEOFFER;
    }

    //Hasan
    @PutMapping("/deleteOffer")
    public String deleteOffer (@ModelAttribute Offer offer, Model model){
        log.info("delete confirmed deleting offer " + offer.getOfferId());
        int id = offer.getOfferId();

        adminService.deleteOffer(id);

        model.addAttribute("offers", adminService.getOffers());
        model.addAttribute("pageTitle", "Delete offer");

        return REDIRECT + OFFER;
    }

    //Hasan
    @GetMapping("/offerPage")
    public String offerPage (Model model){
        log.info("offerPage is called");
        model.addAttribute("offers", adminService.showOffers());

        return OFFERPAGE;
    }

    //Niklas
    @GetMapping("/editOpeningHours")
    public String editOpeningHours (Model model){
        log.info("Edit opening hours getmapping called...");

        Opening[] opening = adminService.convertOpenings();
        ArrayList<Integer> hours = adminService.getHours();
        ArrayList<Integer> min = adminService.getMin();

        model.addAttribute("openings", opening);
        model.addAttribute("hours", hours);
        model.addAttribute("min", min);

        return EDITOPENINGHOURS;
    }

    //Niklas
    @PutMapping("/editOpeningHours")
    public String editOpeningHours (@ModelAttribute Opening opening){
        log.info("Edit opening hours putmapping called... openingId=" + opening.getOpeningId());

        adminService.saveOpeningHours(opening);

        return REDIRECT + EDITOPENINGHOURS;
    }
}

