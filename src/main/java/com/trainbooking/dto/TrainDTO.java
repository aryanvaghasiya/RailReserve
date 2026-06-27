package com.trainbooking.dto;

import java.util.List;

public class TrainDTO {
    private String trainId;
    private String source;
    private String destination;
    private List<String> availableClasses;

    public TrainDTO(String trainId, String source, String destination, List<String> availableClasses) {
        this.trainId = trainId;
        this.source = source;
        this.destination = destination;
        this.availableClasses = availableClasses;
    }

    public String getTrainId() {
        return trainId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public List<String> getAvailableClasses() {
        return availableClasses;
    }

    @Override
    public String toString() {
        return "TrainDTO{" +
                "trainId='" + trainId + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", availableClasses=" + availableClasses +
                '}';
    }
}
