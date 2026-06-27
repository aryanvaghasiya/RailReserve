package com.trainbooking.dto;

public class BookingDTO {
    private int bookingId;
    private String trainId;
    private String coachType;
    private int numberOfSeats;

    public BookingDTO(int bookingId, String trainId, String coachType, int numberOfSeats) {
        this.bookingId = bookingId;
        this.trainId = trainId;
        this.coachType = coachType;
        this.numberOfSeats = numberOfSeats;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getTrainId() {
        return trainId;
    }

    public String getCoachType() {
        return coachType;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "bookingId=" + bookingId +
                ", trainId='" + trainId + '\'' +
                ", coachType='" + coachType + '\'' +
                ", numberOfSeats=" + numberOfSeats +
                '}';
    }
}
