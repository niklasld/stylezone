package com.stylezone.demo.services;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Offer;
import com.stylezone.demo.models.Opening;
import com.stylezone.demo.models.Staff;
import com.stylezone.demo.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepo adminRepo;

    Logger log = Logger.getLogger(AdminServiceImpl.class.getName());

    @Override
    public int hashPassword(String password) {
        int passwordHash = password.hashCode();

        log.info(""+passwordHash);

        return passwordHash;
    }

    @Override
    public Admin searchUser(Admin admin) {

        Admin adminFound = adminRepo.searchUser(admin);

        return adminFound;
    }

    @Override
    public void deleteStaffMember(int staffId){
        adminRepo.deleteStaffMember(staffId);
    }

    @Override
    public Staff createStaffMember(Staff staff){
        staff = adminRepo.createStaffMember(staff);

        return staff;

    }
    @Override
    public List<Staff> getStaff() {
        List<Staff> staffs = adminRepo.getStaff();
        return staffs;
    }

    @Override
    public Staff getStaffMember(int staffId) {
        Staff staffs = adminRepo.getStaffMember(staffId);
        return staffs;
    }

    @Override
    public Staff updateStaff(Staff staff){

        staff = adminRepo.updateStaff(staff);

        return staff;
    }

    @Override
    public List<Offer> getOffers() {
        List<Offer> offers = adminRepo.getOffers();

        return offers;
    }

    @Override
    public Offer createOffer(Offer offer) {
        offer = adminRepo.createOffer(offer);
        return offer;
    }

    @Override
    public Offer updateOffer(Offer offer) {
        return null;
    }

    @Override
    public void deleteOffer(int id) {

    }

    @Override
    public Offer findOffer(int id) {
        return null;
    }

    @Override
    public Opening findOpening(int openingId) {
        Opening opening = adminRepo.findOpening(openingId);
        return opening;
    }

    @Override
    public Opening[] getOpenings() {
        Opening[] openings = adminRepo.getOpenings();
        return openings;
    }

    @Override
    public ArrayList<Opening> getTimes() {
        ArrayList<Opening> times = new ArrayList();

        int hour, min;
        String time;
        for(hour = 0; hour<=24; hour++) {
            for (min = 0; min<=50; min=min+10) {
                if(hour < 10 && min == 0) {
                    time = "0" + hour + ":0" + min + ":00";
                }
                else if(hour < 10 && min != 0) {
                    time = "0"+ hour + ":" + min + ":00";
                }
                else {
                    time = "" + hour + ":" + min + ":00";
                }

                times.add(new Opening(time, time));
            }
        }

        return times;
    }
}
