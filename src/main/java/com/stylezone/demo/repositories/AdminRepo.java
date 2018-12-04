package com.stylezone.demo.repositories;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Offer;
import com.stylezone.demo.models.Staff;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo {
    //Admin
    Admin checkPassword(Admin admin);
    Admin searchUser(Admin admin);

    //Staff
    List<Staff> getStaff();
    Staff getStaffMember(int staffId);
    Staff updateStaff(Staff staff);
    void deleteStaffMember(int staffId);
    Staff createStaffMember(Staff staff);

    //Offers
    Offer findOffer(int offerId);
    List<Offer> getOffers();
    Offer createOffer(Offer offer);
    Offer updateOffer(Offer offer);
}
