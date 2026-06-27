package com.trainbooking.model;

public class Passenger extends User {
    private int passengerID;
    private int age;
    private String gender;

    public Passenger(int passengerID, int userID, String name, String email, String phone, String password, int age, String gender) {
        super(userID, name, email, phone, password);
        this.passengerID = passengerID;
        this.age = age;
        this.gender = gender;
    }

    public Passenger() {
        super();
    }

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "passengerID=" + passengerID +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", user=" + super.toString() +
                '}';
    }
}
