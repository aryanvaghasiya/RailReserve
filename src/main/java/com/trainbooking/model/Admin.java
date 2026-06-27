package com.trainbooking.model;

public class Admin extends User {
    private int adminID;

    public Admin(int adminID, int userID, String name, String email, String phone, String password) {
        super(userID, name, email, phone, password);
        this.adminID = adminID;
    }

    public Admin() {
        super();
    }

    public int getAdminID() {
        return this.adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminID=" + adminID +
                ", user=" + super.toString() +
                '}';
    }
}
