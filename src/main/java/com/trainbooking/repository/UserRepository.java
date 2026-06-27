package com.trainbooking.repository;

import com.trainbooking.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    public int saveUser(Connection conn, User user) throws SQLException {
        String insertQuery = "INSERT INTO User (name, email, phone, password, active) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getPassword());
            pstmt.setInt(5, user.getActive());
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("User insert failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                throw new SQLException("User insert failed, no ID obtained.");
            }
        }
    }

    public User findByEmail(Connection conn, String email) throws SQLException {
        String query = "SELECT * FROM User WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("userID"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setPassword(rs.getString("password"));
                    user.setActive(rs.getInt("active"));
                    return user;
                }
            }
        }
        return null;
    }
}
