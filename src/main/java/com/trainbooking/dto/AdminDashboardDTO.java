package com.trainbooking.dto;

import java.util.List;

public class AdminDashboardDTO {
    private List<TrainDTO> trains;
    private List<String> coachReports;

    public AdminDashboardDTO(List<TrainDTO> trains, List<String> coachReports) {
        this.trains = trains;
        this.coachReports = coachReports;
    }

    public List<TrainDTO> getTrains() {
        return trains;
    }

    public List<String> getCoachReports() {
        return coachReports;
    }

    @Override
    public String toString() {
        return "AdminDashboardDTO{" +
                "trains=" + trains +
                ", coachReports=" + coachReports +
                '}';
    }
}
