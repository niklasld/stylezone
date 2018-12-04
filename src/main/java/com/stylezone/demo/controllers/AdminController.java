package com.stylezone.demo.controllers;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
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
            return REDIRECT+SUCCESS;
        }

        log.info("Login failed.");
        return REDIRECT;
    }

    @GetMapping("/success")
    public String success(HttpSession session, Model model) {
        log.info("test"+session.getAttribute("loggedin"));
        model.addAttribute("adminCheck", session.getAttribute("loggedin"));


        return "omOs";
    }



}
