package com.trainbooking;

import com.trainbooking.util.Validators;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidatorsTest {

    @Test
    public void testEmailValidation() {
        assertTrue(Validators.isValidEmail("test@example.com"));
        assertTrue(Validators.isValidEmail("user.name+tag@domain.co.in"));
        assertFalse(Validators.isValidEmail("invalid-email"));
        assertFalse(Validators.isValidEmail("@domain.com"));
        assertFalse(Validators.isValidEmail("user@domain"));
    }

    @Test
    public void testPhoneValidation() {
        assertTrue(Validators.isValidPhone("1234567890"));
        assertTrue(Validators.isValidPhone("9876543"));
        assertFalse(Validators.isValidPhone("123")); // too short
        assertFalse(Validators.isValidPhone("phone123")); // letters
    }

    @Test
    public void testAgeValidation() {
        assertTrue(Validators.isValidAge(25));
        assertTrue(Validators.isValidAge(1));
        assertTrue(Validators.isValidAge(120));
        assertFalse(Validators.isValidAge(0));
        assertFalse(Validators.isValidAge(121));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmailThrowsException() {
        Validators.validateEmail("invalid-email");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidatePasswordThrowsException() {
        Validators.validatePassword("123"); // too short
    }
}
