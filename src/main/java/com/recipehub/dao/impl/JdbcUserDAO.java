package com.recipehub.dao.impl;

import com.recipehub.dao.UserDAO;
import com.recipehub.model.PasswordResetOtp;
import com.recipehub.model.User;
import com.recipehub.util.Database;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;

/**
 * JDBC implementation of UserDAO.
 * Features salted BCrypt password hashing, SHA-256 hashed OTP verification,
 * ACID transaction management, and rate-limited token lifecycles.
 */
public class JdbcUserDAO implements UserDAO {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public User findByEmail(String email) throws SQLException {
        if (email == null || email.isBlank()) {
            return null;
        }
        String sql = "SELECT id, name, email, password_hash, bio, role, created_at FROM users WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public User findById(long id) throws SQLException {
        String sql = "SELECT id, name, email, password_hash, bio, role, created_at FROM users WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public User create(String name, String email, String rawPassword) throws SQLException {
        String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
        String sql = "INSERT INTO users (name, email, password_hash, bio, role) VALUES (?, ?, ?, '', 'USER')";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name.trim());
            stmt.setString(2, email.trim().toLowerCase());
            stmt.setString(3, hash);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    return findById(id);
                }
            }
        }
        return findByEmail(email);
    }

    @Override
    public boolean emailExists(String email) throws SQLException {
        if (email == null || email.isBlank()) {
            return false;
        }
        String sql = "SELECT 1 FROM users WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null || hashedPassword.isBlank()) {
            return false;
        }
        try {
            return BCrypt.checkpw(rawPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateProfile(long id, String name, String bio) throws SQLException {
        if (id <= 0 || name == null || name.isBlank()) {
            return false;
        }
        String sql = "UPDATE users SET name = ?, bio = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name.trim());
            stmt.setString(2, bio != null ? bio.trim() : "");
            stmt.setLong(3, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updatePassword(long id, String newRawPassword) throws SQLException {
        if (id <= 0 || newRawPassword == null || newRawPassword.length() < 6) {
            return false;
        }
        String hash = BCrypt.hashpw(newRawPassword, BCrypt.gensalt(12));
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hash);
            stmt.setLong(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // =========================================================================
    // OTP-Based Secure Password Reset
    // =========================================================================

    @Override
    public PasswordResetOtp createOrUpdateOtp(String email, String rawOtp) throws SQLException {
        if (email == null || email.isBlank() || rawOtp == null) {
            return null;
        }
        String cleanEmail = email.trim().toLowerCase();
        String hashedOtp = hashOtp(rawOtp);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + (5 * 60 * 1000)); // 5 minute TTL

        PasswordResetOtp existing = getResetOtp(cleanEmail);

        try (Connection conn = Database.getConnection()) {
            if (existing != null) {
                String updateSql = "UPDATE password_resets SET otp_code = ?, reset_token = NULL, " +
                        "verified = FALSE, attempts = 0, resend_count = resend_count + 1, " +
                        "last_sent_at = ?, expires_at = ? WHERE LOWER(email) = LOWER(?)";
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setString(1, hashedOtp);
                    stmt.setTimestamp(2, now);
                    stmt.setTimestamp(3, expiresAt);
                    stmt.setString(4, cleanEmail);
                    stmt.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO password_resets (email, otp_code, reset_token, verified, " +
                        "attempts, resend_count, last_sent_at, expires_at) VALUES (?, ?, NULL, FALSE, 0, 1, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, cleanEmail);
                    stmt.setString(2, hashedOtp);
                    stmt.setTimestamp(3, now);
                    stmt.setTimestamp(4, expiresAt);
                    stmt.executeUpdate();
                }
            }
        }
        return getResetOtp(cleanEmail);
    }

    @Override
    public PasswordResetOtp getResetOtp(String email) throws SQLException {
        if (email == null || email.isBlank()) {
            return null;
        }
        String sql = "SELECT email, otp_code, reset_token, verified, attempts, resend_count, " +
                "last_sent_at, expires_at, created_at FROM password_resets WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim().toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new PasswordResetOtp(
                            rs.getString("email"),
                            rs.getString("otp_code"),
                            rs.getString("reset_token"),
                            rs.getBoolean("verified"),
                            rs.getInt("attempts"),
                            rs.getInt("resend_count"),
                            rs.getTimestamp("last_sent_at"),
                            rs.getTimestamp("expires_at"),
                            rs.getTimestamp("created_at")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean recordOtpFailure(String email) throws SQLException {
        if (email == null || email.isBlank()) {
            return false;
        }
        String sql = "UPDATE password_resets SET attempts = attempts + 1 WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim().toLowerCase());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public String markOtpVerified(String email) throws SQLException {
        if (email == null || email.isBlank()) {
            return null;
        }
        // Generate a 64-character cryptographically secure token for one-time password update
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String resetToken = bytesToHex(randomBytes);

        String sql = "UPDATE password_resets SET verified = TRUE, reset_token = ?, otp_code = 'USED', attempts = 0 WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resetToken);
            stmt.setString(2, email.trim().toLowerCase());
            int rows = stmt.executeUpdate();
            return rows > 0 ? resetToken : null;
        }
    }

    @Override
    public boolean verifyResetToken(String email, String resetToken) throws SQLException {
        if (email == null || resetToken == null || resetToken.isBlank()) {
            return false;
        }
        String sql = "SELECT expires_at FROM password_resets WHERE LOWER(email) = LOWER(?) AND reset_token = ? AND verified = TRUE";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim().toLowerCase());
            stmt.setString(2, resetToken.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp expiresAt = rs.getTimestamp("expires_at");
                    return expiresAt != null && expiresAt.after(new Timestamp(System.currentTimeMillis()));
                }
            }
        }
        return false;
    }

    @Override
    public boolean resetPasswordWithToken(String email, String resetToken, String newRawPassword) throws SQLException {
        if (!verifyResetToken(email, resetToken)) {
            return false;
        }
        String newHash = BCrypt.hashpw(newRawPassword, BCrypt.gensalt(12));
        String updateSql = "UPDATE users SET password_hash = ? WHERE LOWER(email) = LOWER(?)";
        String deleteSql = "DELETE FROM password_resets WHERE LOWER(email) = LOWER(?)";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement updStmt = conn.prepareStatement(updateSql)) {
                    updStmt.setString(1, newHash);
                    updStmt.setString(2, email.trim().toLowerCase());
                    int rows = updStmt.executeUpdate();
                    if (rows > 0) {
                        try (PreparedStatement delStmt = conn.prepareStatement(deleteSql)) {
                            delStmt.setString(1, email.trim().toLowerCase());
                            delStmt.executeUpdate();
                        }
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public static String hashOtp(String otp) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(otp.trim().getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available in JVM", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("bio"),
                rs.getString("role"),
                rs.getTimestamp("created_at")
        );
    }
}
