package com.trainbooking.repository;

import com.trainbooking.model.Train;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainRepository {
    public List<Train> findAll(Connection conn) throws SQLException {
        List<Train> trains = new ArrayList<>();
        String query = "SELECT TrainID, route_start, route_end, departure, arrival, coachtypes FROM Train";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Train train = new Train(
                    rs.getString("TrainID"),
                    rs.getString("route_start"),
                    rs.getString("route_end"),
                    rs.getString("departure"),
                    rs.getString("arrival"),
                    List.of(rs.getString("coachtypes").split(",\\s*"))
                );
                trains.add(train);
            }
        }
        return trains;
    }

    public Integer findInternalIdByTrainIdentifier(Connection conn, String trainIdentifier) throws SQLException {
        String query = "SELECT id FROM Train WHERE TrainID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, trainIdentifier);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return null;
    }

    public String getAvailableCoachTypes(Connection conn, String trainIdentifier) throws SQLException {
        String query = "SELECT coachtypes FROM Train WHERE TrainID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, trainIdentifier);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("coachtypes");
                }
            }
        }
        return "";
    }

    public boolean existsByTrainIdentifier(Connection conn, String trainIdentifier) throws SQLException {
        return findInternalIdByTrainIdentifier(conn, trainIdentifier) != null;
    }
}
