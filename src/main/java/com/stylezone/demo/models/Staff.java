package com.stylezone.demo.models;

public class Staff {

    int staffId;
    String staffName;

    public Staff(){
    }

    public Staff(int staffId, String staffName) {
        this.staffId = staffId;
        this.staffName = staffName;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}