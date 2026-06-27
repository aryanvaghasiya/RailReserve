package com.trainbooking.util;

import com.trainbooking.model.CoachType;
import java.util.regex.Pattern;

public class Validators {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{7,15}$");
    private static final Pattern TRAIN_ID_PATTERN = Pattern.compile("^[A-Za-z0-9_-]+$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidAge(int age) {
        return age >= 1 && age <= 120;
    }

    public static boolean isValidSeatCount(int seatCount) {
        return seatCount > 0;
    }

    public static boolean isValidTrainId(String trainId) {
        return trainId != null && TRAIN_ID_PATTERN.matcher(trainId).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static void validateEmail(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }
    }

    public static void validatePhone(String phone) {
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number. Use digits only.");
        }
    }

    public static void validateAge(int age) {
        if (!isValidAge(age)) {
            throw new IllegalArgumentException("Invalid age. Age must be between 1 and 120.");
        }
    }

    public static void validateSeatCount(int seatCount) {
        if (!isValidSeatCount(seatCount)) {
            throw new IllegalArgumentException("Seat count must be greater than zero.");
        }
    }

    public static void validateTrainId(String trainId) {
        if (!isValidTrainId(trainId)) {
            throw new IllegalArgumentException("Invalid train ID.");
        }
    }

    public static void validateName(String name) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Name must not be empty.");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
    }

    public static void validateCoachType(String coachType) {
        if (CoachType.fromString(coachType) == null) {
            throw new IllegalArgumentException("Invalid coach type.");
        }
    }
}
