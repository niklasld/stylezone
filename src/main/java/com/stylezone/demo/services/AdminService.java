package com.stylezone.demo.services;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Offer;
import com.stylezone.demo.models.Opening;
import com.stylezone.demo.models.Staff;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface AdminService {
    //Admin
    int hashPassword(String password);
    Admin searchUser(Admin admin);

    //Staff
    List<Staff> getStaff();
    Staff getStaffMember(int staffId);
    Staff updateStaff(Staff staff);
    void deleteStaffMember(int staffId);
    Staff createStaffMember(Staff staff);

    //Offers
    List<Offer> getOffers();
    List<Offer> showOffers();
    Offer createOffer(Offer offer);
    Offer updateOffer(Offer offer);
    void deleteOffer(int id);
    Offer findOffer(int id);

    //Opening
    Opening findOpening(int openingId);
    Opening[] getOpenings();
    Opening saveOpeningHours(Opening opening);
    Opening returnConvertedOpenings(Opening opening);

    //ArrayList<Opening> getTimes();
    ArrayList<Integer> getHours();
    ArrayList<Integer> getMin();
    ArrayList<String> getDays();
    Opening[] convertOpenings();

}
