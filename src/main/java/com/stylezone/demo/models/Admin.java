package com.stylezone.demo.models;

public class Admin {
    private String adminUsername;
    private String adminPassword;
    private int adminId;

    public Admin() {
    }

    public Admin(String adminUsername, String adminPassword, int adminId) {
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminId = adminId;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
