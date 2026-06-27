package com.trainbooking.service;

import com.trainbooking.dto.TrainDTO;
import com.trainbooking.model.Train;
import com.trainbooking.repository.TrainRepository;
import com.trainbooking.util.DBConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class TrainService {
    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public List<TrainDTO> getTrainSchedules() {
        try (Connection conn = DBConnection.getConnection()) {
            List<Train> trains = trainRepository.findAll(conn);
            List<TrainDTO> dtos = new ArrayList<>();
            for (Train t : trains) {
                dtos.add(new TrainDTO(t.getTrainID(), t.getSource(), t.getDestination(), t.getAvailableClasses()));
            }
            return dtos;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to retrieve schedules: " + ex.getMessage(), ex);
        }
    }

    public String getAvailableCoachTypes(String trainID) {
        try (Connection conn = DBConnection.getConnection()) {
            return trainRepository.getAvailableCoachTypes(conn, trainID);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to retrieve coach types: " + ex.getMessage(), ex);
        }
    }
}
