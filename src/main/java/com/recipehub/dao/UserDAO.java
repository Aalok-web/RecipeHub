package com.recipehub.dao;

import com.recipehub.model.PasswordResetOtp;
import com.recipehub.model.User;
import java.sql.SQLException;

/**
 * Persistence contract for User management, authentication, profile updates,
 * and OTP-based secure password resets.
 */
public interface UserDAO {
    User findByEmail(String email) throws SQLException;
    User findById(long id) throws SQLException;
    User create(String name, String email, String rawPassword) throws SQLException;
    boolean emailExists(String email) throws SQLException;
    boolean verifyPassword(String rawPassword, String hashedPassword);
    boolean updateProfile(long id, String name, String bio) throws SQLException;
    boolean updatePassword(long id, String newRawPassword) throws SQLException;

    // Secure OTP Password Reset Operations
    PasswordResetOtp createOrUpdateOtp(String email, String rawOtp) throws SQLException;
    PasswordResetOtp getResetOtp(String email) throws SQLException;
    boolean recordOtpFailure(String email) throws SQLException;
    String markOtpVerified(String email) throws SQLException;
    boolean verifyResetToken(String email, String resetToken) throws SQLException;
    boolean resetPasswordWithToken(String email, String resetToken, String newRawPassword) throws SQLException;
}
