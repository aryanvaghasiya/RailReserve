package com.trainbooking.service;

import com.trainbooking.dto.BookingDTO;
import com.trainbooking.exception.BookingException;
import com.trainbooking.model.Booking;
import com.trainbooking.model.CoachType;
import com.trainbooking.repository.BookingRepository;
import com.trainbooking.repository.CoachRepository;
import com.trainbooking.repository.TrainRepository;
import com.trainbooking.util.DBConnection;
import com.trainbooking.util.Validators;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private final BookingRepository bookingRepository;
    private final CoachRepository coachRepository;
    private final TrainRepository trainRepository;

    public BookingService(BookingRepository bookingRepository, CoachRepository coachRepository, TrainRepository trainRepository) {
        this.bookingRepository = bookingRepository;
        this.coachRepository = coachRepository;
        this.trainRepository = trainRepository;
    }

    public void confirmBooking(BookingDTO bookingDto, int passengerId) {
        Validators.validateTrainId(bookingDto.getTrainId());
        Validators.validateCoachType(bookingDto.getCoachType());
        Validators.validateSeatCount(bookingDto.getNumberOfSeats());

        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                Integer trainInternalId = trainRepository.findInternalIdByTrainIdentifier(conn, bookingDto.getTrainId());
                if (trainInternalId == null) {
                    throw new BookingException("Train not found: " + bookingDto.getTrainId());
                }

                CoachType coachType = CoachType.fromString(bookingDto.getCoachType());
                if (coachType == null) {
                    throw new BookingException("Invalid coach type: " + bookingDto.getCoachType());
                }

                int availableSeats = coachRepository.getAvailableSeats(conn, trainInternalId, coachType);
                if (availableSeats < bookingDto.getNumberOfSeats()) {
                    throw new BookingException("Not enough seats available in " + coachType + " coach.");
                }

                coachRepository.updateAvailableSeats(conn, trainInternalId, coachType, -bookingDto.getNumberOfSeats());
                
                Booking booking = new Booking(
                    bookingDto.getTrainId(),
                    passengerId,
                    bookingDto.getCoachType(),
                    bookingDto.getNumberOfSeats()
                );
                bookingRepository.saveBooking(conn, booking);
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        } catch (Exception ex) {
            throw new BookingException("Unable to confirm booking: " + ex.getMessage(), ex);
        }
    }

    public void cancelBooking(int bookingId) {
        try (Connection conn = DBConnection.getConnection()) {
            try {
                conn.setAutoCommit(false);
                Booking booking = bookingRepository.findBookingById(conn, bookingId);
                if (booking == null) {
                    throw new BookingException("No booking found with ID: " + bookingId);
                }

                Integer trainInternalId = trainRepository.findInternalIdByTrainIdentifier(conn, booking.getTrainID());
                if (trainInternalId == null) {
                    throw new BookingException("Train not found for booking ID: " + bookingId);
                }

                CoachType coachType = CoachType.fromString(booking.getCoachType());
                if (coachType == null) {
                    throw new BookingException("Invalid coach type: " + booking.getCoachType());
                }

                coachRepository.updateAvailableSeats(conn, trainInternalId, coachType, booking.getNumberOfSeats());
                bookingRepository.deleteBooking(conn, bookingId);
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        } catch (Exception ex) {
            throw new BookingException("Unable to cancel booking: " + ex.getMessage(), ex);
        }
    }

    public List<BookingDTO> getBookingsForPassenger(int passengerId) {
        try (Connection conn = DBConnection.getConnection()) {
            List<Booking> bookings = bookingRepository.findBookingsByPassengerId(conn, passengerId);
            List<BookingDTO> dtos = new ArrayList<>();
            for (Booking b : bookings) {
                dtos.add(new BookingDTO(b.getBookingId(), b.getTrainID(), b.getCoachType(), b.getNumberOfSeats()));
            }
            return dtos;
        } catch (Exception ex) {
            throw new RuntimeException("Unable to fetch bookings for passenger: " + ex.getMessage(), ex);
        }
    }
}
