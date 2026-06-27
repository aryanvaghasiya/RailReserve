package com.trainbooking.repository;

import com.trainbooking.model.Booking;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    public void saveBooking(Connection conn, Booking booking) throws SQLException {
        String insertQuery = "INSERT INTO Bookings (trainID, passengerID, coachType, numberOfSeats) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, booking.getTrainID());
            pstmt.setInt(2, booking.getPassengerID());
            pstmt.setString(3, booking.getCoachType());
            pstmt.setInt(4, booking.getNumberOfSeats());
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("Booking insert failed, no rows affected.");
            }
        }
    }

    public Booking findBookingById(Connection conn, int bookingId) throws SQLException {
        String query = "SELECT bookingID, trainID, coachType, numberOfSeats, passengerID FROM Bookings WHERE bookingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Booking(
                        rs.getInt("bookingID"),
                        rs.getString("trainID"),
                        rs.getInt("passengerID"),
                        rs.getString("coachType"),
                        rs.getInt("numberOfSeats")
                    );
                }
            }
        }
        return null;
    }

    public void deleteBooking(Connection conn, int bookingId) throws SQLException {
        String deleteQuery = "DELETE FROM Bookings WHERE bookingID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, bookingId);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("No booking found to delete with id " + bookingId);
            }
        }
    }

    public List<Booking> findBookingsByPassengerId(Connection conn, int passengerId) throws SQLException {
        String query = "SELECT bookingID, trainID, coachType, numberOfSeats, passengerID FROM Bookings WHERE passengerID = ?";
        List<Booking> bookings = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, passengerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking(
                        rs.getInt("bookingID"),
                        rs.getString("trainID"),
                        rs.getInt("passengerID"),
                        rs.getString("coachType"),
                        rs.getInt("numberOfSeats")
                    );
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }
}
