package com.trainbooking.model;

import java.util.List;

public class Train {
    private String trainID;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private List<String> availableClasses;

    public Train(String trainID, String source, String destination, String departureTime, String arrivalTime, List<String> availableClasses) {
        this.trainID = trainID;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableClasses = availableClasses;
    }

    public Train() {
    }

    public String getTrainID() {
        return trainID;
    }

    public void setTrainID(String trainID) {
        this.trainID = trainID;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<String> getAvailableClasses() {
        return availableClasses;
    }

    public void setAvailableClasses(List<String> availableClasses) {
        this.availableClasses = availableClasses;
    }

    public boolean checkAvailability(String coachType) {
        return availableClasses != null && availableClasses.contains(coachType);
    }

    @Override
    public String toString() {
        return "Train{" +
                "trainID='" + trainID + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", availableClasses=" + availableClasses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Train train = (Train) o;
        return trainID != null ? trainID.equals(train.trainID) : train.trainID == null;
    }

    @Override
    public int hashCode() {
        return trainID != null ? trainID.hashCode() : 0;
    }
}
