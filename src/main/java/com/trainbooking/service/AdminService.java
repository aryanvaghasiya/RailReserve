package com.trainbooking.service;

import com.trainbooking.exception.AuthenticationException;
import com.trainbooking.model.Admin;
import com.trainbooking.model.Coach;
import com.trainbooking.model.Train;
import com.trainbooking.repository.AdminRepository;
import com.trainbooking.repository.TrainRepository;
import com.trainbooking.util.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminService {
    private final AdminRepository adminRepository;
    private final TrainRepository trainRepository;

    public AdminService(AdminRepository adminRepository, TrainRepository trainRepository) {
        this.adminRepository = adminRepository;
        this.trainRepository = trainRepository;
    }

    public Admin loginAdmin(String email, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            Admin admin = adminRepository.findByEmailAndPassword(conn, email, password);
            if (admin == null) {
                throw new AuthenticationException("Invalid email or password for admin.");
            }
            adminRepository.updateActiveState(conn, admin.getUserID(), 1);
            admin.setActive(1);
            conn.commit();
            return admin;
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Admin login failed: " + ex.getMessage(), ex);
        }
    }

    public void logoutAdmin(Admin admin) {
        if (admin == null || admin.getActive() != 1) {
            throw new IllegalStateException("Admin is not currently logged in.");
        }
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            adminRepository.updateActiveState(conn, admin.getUserID(), 0);
            admin.setActive(0);
            conn.commit();
        } catch (Exception ex) {
            throw new RuntimeException("Admin logout failed: " + ex.getMessage(), ex);
        }
    }

    public void editTrainSchedule(String trainID, String departureTime, String arrivalTime, boolean resetSeats) {
        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                boolean updated = adminRepository.updateTrainSchedule(conn, trainID, departureTime, arrivalTime);
                if (!updated) {
                    throw new RuntimeException("Train with ID " + trainID + " not found.");
                }
                if (resetSeats) {
                    adminRepository.resetCoachSeats(conn, trainID);
                }
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to edit train schedule: " + ex.getMessage(), ex);
        }
    }

    public void printAdminReport() {
        try (Connection conn = DBConnection.getConnection()) {
            List<Train> trains = adminRepository.findAllTrains(conn);
            System.out.println("\n--- TRAIN SCHEDULING REPORT ---");
            for (Train t : trains) {
                System.out.println("Train ID: " + t.getTrainID());
                System.out.println("Route: " + t.getSource() + " -> " + t.getDestination());
                System.out.println("Departure: " + t.getDepartureTime() + " Arrival: " + t.getArrivalTime());
                
                Integer internalId = trainRepository.findInternalIdByTrainIdentifier(conn, t.getTrainID());
                if (internalId != null) {
                    Coach coach = adminRepository.findCoachByTrainInternalId(conn, internalId);
                    if (coach != null) {
                        System.out.println("Coach Details:");
                        System.out.println("  AC Seats: " + coach.getAcSeats());
                        System.out.println("  SC Seats: " + coach.getScSeats());
                        System.out.println("  GE Seats: " + coach.getGeSeats());
                    }
                }
                System.out.println("------------------------------------------------");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate admin report: " + ex.getMessage(), ex);
        }
    }
}
