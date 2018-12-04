package com.stylezone.demo.repositories;

import com.stylezone.demo.models.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminRepoImpl implements AdminRepo {
    @Autowired
    JdbcTemplate template;

    @Override
    public Admin checkPassword(Admin admin) {
        String sql = "SELECT * FROM Admin WHERE adminUsername = ? AND adminPassword = ?";


        RowMapper<Admin> rowMapper = new BeanPropertyRowMapper<>(Admin.class);

        Admin adminFound = new Admin();

        try {
            adminFound = template.queryForObject(sql, rowMapper, admin.getAdminUsername(), admin.getAdminPassword());
        }
        catch (Exception e) {

            adminFound.setAdminUsername("ErrorAdminNotFound");
            adminFound.setAdminPassword("ErrorPasswordNotFound");
        }

        return adminFound;
    }

    @Override
    public Admin searchUser(Admin admin) {
        String sql = "SELECT * FROM Admin WHERE adminUsername=? AND adminPassword=?";


        // Fra sql til list.
        // Manuelt i stedet.
        return this.template.query(sql, new ResultSetExtractor<Admin>() {
            @Override
            public Admin extractData(ResultSet rs) throws SQLException, DataAccessException {
                int adminId;
                String adminUsername, adminPassword;
                Admin adminFound = new Admin();

                while (rs.next()) {
                    adminId = rs.getInt("adminId");
                    adminUsername = rs.getString("adminUsername");
                    adminPassword = rs.getString("adminPassword");

                    adminFound.setAdminId(adminId);
                    adminFound.setAdminUsername(adminUsername);
                    adminFound.setAdminPassword(adminPassword);
                }
                return adminFound;
            }
        }, admin.getAdminUsername(),admin.getAdminPassword());
    }
}
