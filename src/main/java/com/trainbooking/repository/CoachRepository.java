package com.trainbooking.repository;

import com.trainbooking.model.CoachType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoachRepository {
    public int getAvailableSeats(Connection conn, int trainId, CoachType coachType) throws SQLException {
        String query = "SELECT " + coachType.getColumnName() + " FROM Coach WHERE trainID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, trainId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(coachType.getColumnName());
                }
            }
        }
        throw new SQLException("Coach record not found for train id " + trainId);
    }

    public void updateAvailableSeats(Connection conn, int trainId, CoachType coachType, int seatsDelta) throws SQLException {
        String query = "UPDATE Coach SET " + coachType.getColumnName() + " = " + coachType.getColumnName() + " + ? WHERE trainID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, seatsDelta);
            pstmt.setInt(2, trainId);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No coach row updated for train id " + trainId);
            }
        }
    }
}
