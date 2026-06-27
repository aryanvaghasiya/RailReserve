package com.trainbooking.model;

public class Booking {
    private int bookingId;
    private String trainID;
    private int passengerID;
    private String coachType;
    private int numberOfSeats;

    public Booking(int bookingId, String trainID, int passengerID, String coachType, int numberOfSeats) {
        this.bookingId = bookingId;
        this.trainID = trainID;
        this.passengerID = passengerID;
        this.coachType = coachType;
        this.numberOfSeats = numberOfSeats;
    }

    public Booking(String trainID, int passengerID, String coachType, int numberOfSeats) {
        this.trainID = trainID;
        this.passengerID = passengerID;
        this.coachType = coachType;
        this.numberOfSeats = numberOfSeats;
    }

    public Booking() {
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public String getCoachType() {
        return coachType;
    }

    public void setCoachType(String coachType) {
        this.coachType = coachType;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", trainID='" + trainID + '\'' +
                ", passengerID=" + passengerID +
                ", coachType='" + coachType + '\'' +
                ", numberOfSeats=" + numberOfSeats +
                '}';
    }
}
