package com.stylezone.demo.repositories;

import com.stylezone.demo.models.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo {
    //Admin
    Admin checkPassword(Admin admin);
    Admin searchUser(Admin admin);
}
