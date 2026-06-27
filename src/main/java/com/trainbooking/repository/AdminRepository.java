package com.trainbooking.repository;

import com.trainbooking.model.Admin;
import com.trainbooking.model.Coach;
import com.trainbooking.model.Train;
import com.trainbooking.util.PasswordUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {
    public Admin findByEmailAndPassword(Connection conn, String email, String password) throws SQLException {
        String query = "SELECT u.userID, u.name, u.email, u.phone, u.password, u.active, a.adminID " +
                       "FROM User u JOIN Admin a ON u.userID = a.userID WHERE u.email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (!PasswordUtils.verifyPassword(password, storedPassword)) {
                        return null;
                    }
                    Admin admin = new Admin();
                    admin.setUserID(rs.getInt("userID"));
                    admin.setName(rs.getString("name"));
                    admin.setEmail(rs.getString("email"));
                    admin.setPhone(rs.getString("phone"));
                    admin.setPassword(storedPassword);
                    admin.setActive(rs.getInt("active"));
                    admin.setAdminID(rs.getInt("adminID"));
                    return admin;
                }
            }
        }
        return null;
    }

    public void updateActiveState(Connection conn, int userID, int active) throws SQLException {
        String query = "UPDATE User SET active = ? WHERE userID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, active);
            pstmt.setInt(2, userID);
            pstmt.executeUpdate();
        }
    }

    public boolean updateTrainSchedule(Connection conn, String trainID, String departureTime, String arrivalTime) throws SQLException {
        String updateQuery = "UPDATE Train SET departure = ?, arrival = ? WHERE trainID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, departureTime);
            pstmt.setString(2, arrivalTime);
            pstmt.setString(3, trainID);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean resetCoachSeats(Connection conn, String trainID) throws SQLException {
        String resetSeatsQuery = "UPDATE Coach SET AC = 100, SC = 100, GE = 100 WHERE trainID = (SELECT id FROM Train WHERE TrainID = ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(resetSeatsQuery)) {
            pstmt.setString(1, trainID);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Train> findAllTrains(Connection conn) throws SQLException {
        List<Train> trains = new ArrayList<>();
        String query = "SELECT id, TrainID, route_start, route_end, departure, arrival, coachtypes FROM Train";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
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

    public Coach findCoachByTrainInternalId(Connection conn, int trainInternalId) throws SQLException {
        String coachQuery = "SELECT coachID, trainID, AC, SC, GE FROM Coach WHERE trainID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(coachQuery)) {
            pstmt.setInt(1, trainInternalId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Coach(
                        rs.getInt("coachID"),
                        rs.getInt("trainID"),
                        rs.getInt("AC"),
                        rs.getInt("SC"),
                        rs.getInt("GE")
                    );
                }
            }
        }
        return null;
    }
}
