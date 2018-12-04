package com.stylezone.demo.services;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Staff;
import org.springframework.stereotype.Service;

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

}
