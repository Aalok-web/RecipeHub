package com.recipehub.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Domain model representing an active OTP password reset lifecycle.
 */
public class PasswordResetOtp implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private String otpCode;
    private String resetToken;
    private boolean verified;
    private int attempts;
    private int resendCount;
    private Timestamp lastSentAt;
    private Timestamp expiresAt;
    private Timestamp createdAt;

    public PasswordResetOtp() {}

    public PasswordResetOtp(String email, String otpCode, String resetToken, boolean verified,
                            int attempts, int resendCount, Timestamp lastSentAt,
                            Timestamp expiresAt, Timestamp createdAt) {
        this.email = email;
        this.otpCode = otpCode;
        this.resetToken = resetToken;
        this.verified = verified;
        this.attempts = attempts;
        this.resendCount = resendCount;
        this.lastSentAt = lastSentAt;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }

    public int getResendCount() { return resendCount; }
    public void setResendCount(int resendCount) { this.resendCount = resendCount; }

    public Timestamp getLastSentAt() { return lastSentAt; }
    public void setLastSentAt(Timestamp lastSentAt) { this.lastSentAt = lastSentAt; }

    public Timestamp getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Timestamp expiresAt) { this.expiresAt = expiresAt; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    /**
     * Checks if the OTP has expired (5 minute TTL).
     */
    public boolean isExpired() {
        if (expiresAt == null) return true;
        return System.currentTimeMillis() > expiresAt.getTime();
    }

    /**
     * Checks whether resending the OTP is allowed based on default 60s cooldown.
     */
    public boolean canResend() {
        return canResend(60);
    }

    /**
     * Checks whether resending the OTP is allowed based on cooldown seconds.
     */
    public boolean canResend(long cooldownSeconds) {
        if (lastSentAt == null) return true;
        long elapsedSeconds = (System.currentTimeMillis() - lastSentAt.getTime()) / 1000;
        return elapsedSeconds >= cooldownSeconds;
    }

    /**
     * Calculates remaining seconds before resend is allowed (default 60s cooldown).
     */
    public long getRemainingCooldownSeconds() {
        return getRemainingCooldownSeconds(60);
    }

    /**
     * Calculates remaining seconds before resend is allowed.
     */
    public long getRemainingCooldownSeconds(long cooldownSeconds) {
        if (lastSentAt == null) return 0;
        long elapsedSeconds = (System.currentTimeMillis() - lastSentAt.getTime()) / 1000;
        return Math.max(0, cooldownSeconds - elapsedSeconds);
    }

    /**
     * Checks if the maximum allowed failed attempts have been reached (default 5).
     */
    public boolean isMaxAttemptsExceeded() {
        return isMaxAttemptsExceeded(5);
    }

    /**
     * Checks if the maximum allowed failed attempts have been reached.
     */
    public boolean isMaxAttemptsExceeded(int maxAttempts) {
        return attempts >= maxAttempts;
    }

    @Override
    public String toString() {
        return "PasswordResetOtp{" +
                "email='" + email + '\'' +
                ", verified=" + verified +
                ", attempts=" + attempts +
                ", resendCount=" + resendCount +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
