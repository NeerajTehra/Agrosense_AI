package com.example.agrosense.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PasswordUtilsTest {

    @Test
    public void testHashPassword_Consistency() {
        String password = "password123";
        String hash1 = PasswordUtils.hashPassword(password);
        String hash2 = PasswordUtils.hashPassword(password);
        
        assertEquals("Same password should produce same hash", hash1, hash2);
    }

    @Test
    public void testHashPassword_Security() {
        String password = "password123";
        String hash = PasswordUtils.hashPassword(password);
        
        assertNotEquals("Hash should not be same as plain password", password, hash);
    }

    @Test
    public void testVerifyPassword() {
        String password = "mypassword";
        String hash = PasswordUtils.hashPassword(password);
        
        assertTrue("Verification should succeed with correct password", PasswordUtils.verifyPassword(password, hash));
    }
}
