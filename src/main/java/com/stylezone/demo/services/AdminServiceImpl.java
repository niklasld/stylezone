package com.stylezone.demo.services;

import com.stylezone.demo.models.Admin;
import com.stylezone.demo.models.Offer;
import com.stylezone.demo.models.Picture;
import com.stylezone.demo.models.Opening;
import com.stylezone.demo.models.Staff;
import com.stylezone.demo.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepo adminRepo;
    @Autowired
    JdbcTemplate template;


    Logger log = Logger.getLogger(AdminServiceImpl.class.getName());

    private static String UPLOADED_FOLDER = "src//main//resources//static//image//upload//";

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
        offer = adminRepo.updateOffer(offer);
        return offer;
    }


    @Override
    public void deleteOffer(int id) {
        String sql = "DELETE FROM Offer WHERE offerId = ?";
        this.template.update(sql, id);
    }


    @Override
    public Offer findOffer(int id) {
       Offer offer = adminRepo.findOffer(id);
        return offer;
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
    public Opening saveOpeningHours(Opening opening) {

        opening = returnConvertedOpenings(opening);

        opening = adminRepo.saveOpeningHours(opening);

        return opening;
    }

    @Override
    public Opening returnConvertedOpenings(Opening opening) {
        String fullTime="";

        if(Integer.parseInt(opening.getOpeningHour())<10) {
            fullTime = "0"+opening.getOpeningHour();
        }
        else if(Integer.parseInt(opening.getOpeningHour())>=10) {
            fullTime = ""+opening.getOpeningHour();
        }
        if(Integer.parseInt(opening.getOpeningMin())<10){
            fullTime = fullTime+":0"+opening.getOpeningMin()+":00";
        }
        else if(Integer.parseInt(opening.getOpeningMin())>=10) {
            fullTime = fullTime+":"+opening.getOpeningMin()+":00";
        }

        opening.setOpeningTime(fullTime);
        fullTime = "";
        if(Integer.parseInt(opening.getClosingHour())<10) {
            fullTime = "0"+opening.getClosingHour();
        }
        else if(Integer.parseInt(opening.getClosingHour())>=10) {
            fullTime = ""+opening.getClosingHour();
        }
        if(Integer.parseInt(opening.getClosingMin())<10){
            fullTime = fullTime+":0"+opening.getClosingMin()+":00";
        }
        else if(Integer.parseInt(opening.getClosingMin())>=10) {
            fullTime = fullTime+":"+opening.getClosingMin()+":00";
        }

        opening.setOpeningClose(fullTime);

        return opening;
    }

    @Override
    public ArrayList<Integer> getHours() {
        ArrayList<Integer> hours = new ArrayList<>();

        for(int i = 0; i<24; i++) {
            hours.add(i);
        }


        return hours;
    }

    @Override
    public List<Picture> getPictures() {
        List<Picture> pictures = adminRepo.getPictures();
        return pictures;
    }

    @Override
    public String insertPicture(String picture) {
        picture = adminRepo.insertPicture(picture);
        return picture;
    }

    public String fileUpload(MultipartFile file) {


        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            insertPicture(""+file.getOriginalFilename());

            return file.getOriginalFilename();

        } catch (IOException e){
            e.printStackTrace();

            return "Error uploading file";
        }
    }
    public ArrayList<Integer> getMin() {
        ArrayList<Integer> min = new ArrayList<>();

        for(int i = 0; i<=50; i=i+10) {
            min.add(i);
        }

        return min;
    }

    @Override
    public ArrayList<String> getDays() {
        ArrayList<String> days = new ArrayList<>();

        days.add("mandag");
        days.add("tirsdag");
        days.add("onsdag");
        days.add("torsdag");
        days.add("fredag");
        days.add("lørdag");

        return days;
    }

    @Override
    public Opening[] convertOpenings() {
        Opening[] fullOpening = getOpenings();
        String chopThis;
        String[] parts;

        for (Opening time : fullOpening) {

                chopThis = time.getOpeningTime();
                parts = chopThis.split(":");

                time.setOpeningHour(parts[0]);
                time.setOpeningMin(parts[1]);

                chopThis = time.getOpeningClose();
                parts = chopThis.split(":");

                time.setClosingHour(parts[0]);
                time.setClosingMin(parts[1]);

        }

        return fullOpening;
    }

    /*@Override
    public ArrayList<Opening> getTimes() {
        ArrayList<Opening> times = new ArrayList();

        int hour, min;
        String time;
        for(hour = 5; hour<=21; hour++) {
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
    }*/


}
