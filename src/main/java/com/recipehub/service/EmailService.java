package com.recipehub.service;

/**
 * Service contract for dispatching transactional emails.
 */
public interface EmailService {
    /**
     * Sends an OTP verification email to the user.
     *
     * @param recipientEmail the destination email address
     * @param otpCode the 6-digit numeric OTP code
     * @return true if the email was dispatched; false otherwise
     * @throws Exception if sending fails due to network/SMTP protocol errors
     */
    boolean sendOtpEmail(String recipientEmail, String otpCode) throws Exception;

    /**
     * Checks whether SMTP configuration properties are fully provided.
     *
     * @return true if host and credentials are set
     */
    boolean isConfigured();
}
