package com.trainbooking.service;

import com.trainbooking.exception.AuthenticationException;
import com.trainbooking.model.Passenger;
import com.trainbooking.repository.PassengerRepository;
import com.trainbooking.repository.UserRepository;
import com.trainbooking.util.DBConnection;
import com.trainbooking.util.PasswordUtils;
import com.trainbooking.util.Validators;
import java.sql.Connection;
import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepository;
    private final PassengerRepository passengerRepository;

    public UserService(UserRepository userRepository, PassengerRepository passengerRepository) {
        this.userRepository = userRepository;
        this.passengerRepository = passengerRepository;
    }

    public Passenger registerPassenger(Passenger passenger) {
        Validators.validateName(passenger.getName());
        Validators.validateEmail(passenger.getEmail());
        Validators.validatePhone(passenger.getPhone());
        Validators.validateAge(passenger.getAge());
        Validators.validatePassword(passenger.getPassword());

        passenger.setPassword(PasswordUtils.hashPassword(passenger.getPassword()));
        passenger.setActive(0);
        
        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                int userId = userRepository.saveUser(conn, passenger);
                passenger.setUserID(userId);
                int passengerId = passengerRepository.savePassenger(conn, passenger);
                passenger.setPassengerID(passengerId);
                conn.commit();
                return passenger;
            } catch (Exception ex) {
                conn.rollback();
                throw new RuntimeException("Unable to register passenger: " + ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Database error during registration: " + ex.getMessage(), ex);
        }
    }

    public Passenger loginPassenger(String email, String password) {
        Validators.validateEmail(email);
        Validators.validatePassword(password);
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            Passenger passenger = passengerRepository.findByEmailAndPassword(conn, email, password);
            if (passenger == null) {
                throw new AuthenticationException("Invalid email or password for passenger.");
            }
            passengerRepository.updateActiveState(conn, passenger.getUserID(), 1);
            passenger.setActive(1);
            conn.commit();
            return passenger;
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Passenger login failed: " + ex.getMessage(), ex);
        }
    }

    public void logoutPassenger(Passenger passenger) {
        if (passenger == null || passenger.getActive() != 1) {
            throw new IllegalStateException("Passenger is not currently logged in.");
        }
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            passengerRepository.updateActiveState(conn, passenger.getUserID(), 0);
            passenger.setActive(0);
            conn.commit();
        } catch (Exception ex) {
            throw new RuntimeException("Passenger logout failed: " + ex.getMessage(), ex);
        }
    }
}
