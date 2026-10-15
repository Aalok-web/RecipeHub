package com.recipehub.model;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class PasswordResetOtpTest {

    @Test
    public void testOtpExpiration() {
        long nowMs = System.currentTimeMillis();
        Timestamp pastExpiry = new Timestamp(nowMs - 1000);
        PasswordResetOtp expiredOtp = new PasswordResetOtp("test@recipehub.com", "hash", null, false, 0, 1, new Timestamp(nowMs - 60000), pastExpiry, new Timestamp(nowMs - 60000));
        assertTrue(expiredOtp.isExpired());

        Timestamp futureExpiry = new Timestamp(nowMs + 300000);
        PasswordResetOtp validOtp = new PasswordResetOtp("test@recipehub.com", "hash", null, false, 0, 1, new Timestamp(nowMs), futureExpiry, new Timestamp(nowMs));
        assertFalse(validOtp.isExpired());
    }

    @Test
    public void testResendCooldown() {
        long nowMs = System.currentTimeMillis();
        // Sent 10 seconds ago -> cooldown should block resend
        Timestamp recentSent = new Timestamp(nowMs - 10000);
        PasswordResetOtp cooldownActive = new PasswordResetOtp("test@recipehub.com", "hash", null, false, 0, 1, recentSent, new Timestamp(nowMs + 300000), recentSent);
        assertFalse(cooldownActive.canResend());
        assertTrue(cooldownActive.getRemainingCooldownSeconds() > 0);

        // Sent 70 seconds ago -> cooldown expired, can resend
        Timestamp oldSent = new Timestamp(nowMs - 70000);
        PasswordResetOtp cooldownElapsed = new PasswordResetOtp("test@recipehub.com", "hash", null, false, 0, 1, oldSent, new Timestamp(nowMs + 300000), oldSent);
        assertTrue(cooldownElapsed.canResend());
        assertEquals(0, cooldownElapsed.getRemainingCooldownSeconds());
    }

    @Test
    public void testMaxAttemptsExceeded() {
        long nowMs = System.currentTimeMillis();
        PasswordResetOtp otp = new PasswordResetOtp("test@recipehub.com", "hash", null, false, 4, 1, new Timestamp(nowMs), new Timestamp(nowMs + 300000), new Timestamp(nowMs));
        assertFalse(otp.isMaxAttemptsExceeded());

        otp.setAttempts(5);
        assertTrue(otp.isMaxAttemptsExceeded());
    }

    @Test
    public void testDefaultConstructor() {
        PasswordResetOtp otp = new PasswordResetOtp();
        assertNull(otp.getEmail());
        assertFalse(otp.isVerified());
        assertEquals(0, otp.getAttempts());
    }
}
