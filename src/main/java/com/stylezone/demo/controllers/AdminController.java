package com.stylezone.demo.controllers;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Staff;
import com.stylezone.demo.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class AdminController {

    public AdminController() {

    }

    @Autowired
    AdminService adminService;

    private final String ADMINLOGIN = "adminLogin";
    private final String SUCCESS = "success";
    private final String REDIRECT = "redirect:/";
    private final String EDITSTAFF = "editStaff";
    private final String STAFF = "staff";
    private final String DELETESTAFF = "deleteStaff";
    private final String CREATESTAFFMEMBER = "createStaffMember";

    Logger log = Logger.getLogger(AdminController.class.getName());

    @GetMapping("/adminLogin")
    public String adminLogin(Model model) {
        log.info("adminLogin GetMapping called...");

        model.addAttribute("admin", new Admin());

        return ADMINLOGIN;
    }

    @PostMapping("/adminLogin")
    public String adminLogin(@ModelAttribute Admin admin, HttpSession session) {
        log.info("adminLogin PostMapping called...");

        int adminPW = adminService.hashPassword(admin.getAdminPassword());

        admin.setAdminPassword(""+adminPW);
        Admin adminCheck = adminService.searchUser(admin);

        if(adminCheck.getAdminPassword().equals(admin.getAdminPassword()) && adminCheck.getAdminUsername().equals(admin.getAdminUsername())) {
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
}
