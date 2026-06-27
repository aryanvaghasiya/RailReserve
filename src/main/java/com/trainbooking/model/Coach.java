package com.trainbooking.model;

public class Coach {
    private int coachID;
    private int trainID;
    private int acSeats;
    private int scSeats;
    private int geSeats;

    public Coach(int coachID, int trainID, int acSeats, int scSeats, int geSeats) {
        this.coachID = coachID;
        this.trainID = trainID;
        this.acSeats = acSeats;
        this.scSeats = scSeats;
        this.geSeats = geSeats;
    }

    public Coach() {
    }

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public int getTrainID() {
        return trainID;
    }

    public void setTrainID(int trainID) {
        this.trainID = trainID;
    }

    public int getAcSeats() {
        return acSeats;
    }

    public void setAcSeats(int acSeats) {
        this.acSeats = acSeats;
    }

    public int getScSeats() {
        return scSeats;
    }

    public void setScSeats(int scSeats) {
        this.scSeats = scSeats;
    }

    public int getGeSeats() {
        return geSeats;
    }

    public void setGeSeats(int geSeats) {
        this.geSeats = geSeats;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "coachID=" + coachID +
                ", trainID=" + trainID +
                ", acSeats=" + acSeats +
                ", scSeats=" + scSeats +
                ", geSeats=" + geSeats +
                '}';
    }
}
