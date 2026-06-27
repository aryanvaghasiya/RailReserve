package com.trainbooking.repository;

import com.trainbooking.model.Passenger;
import com.trainbooking.util.PasswordUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PassengerRepository {
    public int savePassenger(Connection conn, Passenger passenger) throws SQLException {
        String insertQuery = "INSERT INTO Passenger (userID, age, gender) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, passenger.getUserID());
            pstmt.setInt(2, passenger.getAge());
            pstmt.setString(3, passenger.getGender());
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("Passenger insert failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("Passenger insert failed, no ID obtained.");
            }
        }
    }

    public Passenger findByEmailAndPassword(Connection conn, String email, String password) throws SQLException {
        String query = "SELECT u.userID, u.name, u.email, u.phone, u.password, u.active, p.passengerID, p.age, p.gender " +
                       "FROM User u JOIN Passenger p ON u.userID = p.userID WHERE u.email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (!PasswordUtils.verifyPassword(password, storedPassword)) {
                        return null;
                    }
                    Passenger passenger = new Passenger();
                    passenger.setUserID(rs.getInt("userID"));
                    passenger.setName(rs.getString("name"));
                    passenger.setEmail(rs.getString("email"));
                    passenger.setPhone(rs.getString("phone"));
                    passenger.setPassword(storedPassword);
                    passenger.setActive(rs.getInt("active"));
                    passenger.setPassengerID(rs.getInt("passengerID"));
                    passenger.setAge(rs.getInt("age"));
                    passenger.setGender(rs.getString("gender"));
                    return passenger;
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
}
