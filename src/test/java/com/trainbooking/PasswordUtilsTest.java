package com.trainbooking;

import com.trainbooking.util.PasswordUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordUtilsTest {

    @Test
    public void testHashAndPasswordVerification() {
        String password = "mySecretPassword123";
        String hashedPassword = PasswordUtils.hashPassword(password);
        
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        
        // Verification succeeds
        assertTrue(PasswordUtils.verifyPassword(password, hashedPassword));
        
        // Verification fails with wrong password
        assertFalse(PasswordUtils.verifyPassword("wrongPassword", hashedPassword));
        assertFalse(PasswordUtils.verifyPassword(null, hashedPassword));
        assertFalse(PasswordUtils.verifyPassword(password, null));
    }
}
