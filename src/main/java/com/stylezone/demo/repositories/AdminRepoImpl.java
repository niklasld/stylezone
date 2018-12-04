package com.stylezone.demo.repositories;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Staff;
import com.stylezone.demo.services.AdminServiceImpl;
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
import java.util.logging.Logger;

@Service
public class AdminRepoImpl implements AdminRepo {
    @Autowired
    JdbcTemplate template;

    Logger log = Logger.getLogger(AdminRepoImpl.class.getName());

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

    @Override
    public Staff getStaffMember(int staffId){
        String sql = "SELECT * FROM Staff WHERE staffId = ?";
        RowMapper<Staff> rowMapper = new BeanPropertyRowMapper<>(Staff.class);

        Staff staff = template.queryForObject(sql,rowMapper, staffId );

        return staff;
    }

    @Override
    public Staff updateStaff(Staff staff){

        String sql = "UPDATE Staff SET staffName =? WHERE staffId = ?";
        String staffName = staff.getStaffName();

        int staffId = staff.getStaffId();
        this.template.update(sql, staffName, staffId);

        return staff;

    }
    @Override
    public void deleteStaffMember(int staffId){
        String sql = "DELETE FROM stylezone.Staff WHERE staffId = ?";
        this.template.update(sql, staffId );
    }

    public Staff createStaffMember(Staff staff){
        String sql = "INSERT INTO Staff VALUE(default, ?)";

        String staffName = staff.getStaffName();

        log.info("createStaffMember called" + staffName);
        this.template.update(sql, staffName);

        return staff;
    }

    @Override
    public List<Staff> getStaff(){

        String sql = "SELECT * FROM Staff";
        return this.template.query(sql, new ResultSetExtractor<List<Staff>>() {

            @Override
            public List<Staff> extractData(ResultSet rs) throws SQLException, DataAccessException{

                int staffId;
                String staffName;
                ArrayList<Staff> staffs = new ArrayList<>();

                while (rs.next()){

                    staffId = rs.getInt("staffId");
                    staffName = rs.getString("staffName");

                    staffs.add(new Staff(staffId, staffName));

                }
                return staffs;
            }

        });
    }
}