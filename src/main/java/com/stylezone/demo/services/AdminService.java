package com.stylezone.demo.services;

import com.stylezone.demo.models.Admin;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    //Admin
    int hashPassword(String password);
    //Admin checkPassword(Admin admin);
    Admin searchUser(Admin admin);

}
