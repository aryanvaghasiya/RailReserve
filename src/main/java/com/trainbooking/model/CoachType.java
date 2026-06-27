package com.trainbooking.model;

public enum CoachType {
    AC,
    SC,
    GE;

    public static CoachType fromString(String value) {
        if (value == null) {
            return null;
        }
        try {
            return CoachType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public String getColumnName() {
        return this.name();
    }
}
