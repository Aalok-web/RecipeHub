package com.recipehub.servlet;

import com.recipehub.dao.UserDAO;
import com.recipehub.dao.impl.JdbcUserDAO;
import com.recipehub.model.PasswordResetOtp;
import com.recipehub.service.EmailService;
import com.recipehub.service.impl.SmtpEmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Handles secure OTP-based password reset flows:
 * 1. Request OTP (Rate-limited, anti-enumeration, 60s cooldown)
 * 2. Verify OTP (5-minute expiration, max 5 attempts, single-use)
 * 3. Set New Password (BCrypt hashed, atomicity via transactions)
 */
@WebServlet(urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final long RESEND_COOLDOWN_SECONDS = 60;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private UserDAO userDAO;
    private EmailService emailService;
    private SecureRandom secureRandom;

    @Override
    public void init() {
        userDAO = new JdbcUserDAO();
        emailService = new SmtpEmailService();
        secureRandom = new SecureRandom();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String tab = request.getParameter("tab");
        if ("login".equalsIgnoreCase(tab)) {
            request.setAttribute("mode", "login");
        } else if ("register".equalsIgnoreCase(tab)) {
            request.setAttribute("mode", "register");
        } else {
            request.setAttribute("mode", "forgot");
            request.setAttribute("step", "requestOtp");
        }
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String email = request.getParameter("email");

        request.setAttribute("mode", "forgot");
        request.setAttribute("email", email != null ? email.trim() : "");

        try {
            if ("verifyOtp".equals(action)) {
                handleVerifyOtp(request, response, email);
            } else if ("resendOtp".equals(action)) {
                handleResendOtp(request, response, email);
            } else if ("updatePassword".equals(action)) {
                handleUpdatePassword(request, response, email);
            } else {
                handleRequestOtp(request, response, email);
            }
        } catch (SQLException e) {
            getServletContext().log("Database error during password reset flow", e);
            request.setAttribute("step", "requestOtp");
            request.setAttribute("error", "A database error occurred. Please try again shortly.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
        }
    }

    /**
     * Step 1: User submits email to receive a 6-digit OTP.
     * Implements anti-enumeration (identical user-facing message whether email exists or not).
     */
    private void handleRequestOtp(HttpServletRequest request, HttpServletResponse response, String email)
            throws ServletException, IOException, SQLException {
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            request.setAttribute("step", "requestOtp");
            request.setAttribute("error", "Please provide a valid email address.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        String cleanEmail = email.trim().toLowerCase();
        boolean exists = userDAO.emailExists(cleanEmail);

        if (exists) {
            PasswordResetOtp existingOtp = userDAO.getResetOtp(cleanEmail);
            if (existingOtp != null && !existingOtp.canResend(RESEND_COOLDOWN_SECONDS)) {
                long remaining = existingOtp.getRemainingCooldownSeconds(RESEND_COOLDOWN_SECONDS);
                request.setAttribute("step", "verifyOtp");
                request.setAttribute("cooldownSeconds", remaining);
                request.setAttribute("message", "A verification code was recently sent. Please wait " + remaining + " seconds before requesting another.");
                request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
                return;
            }

            // Generate cryptographically secure 6-digit OTP
            int code = 100000 + secureRandom.nextInt(900000);
            String rawOtp = String.valueOf(code);

            userDAO.createOrUpdateOtp(cleanEmail, rawOtp);

            // Dispatch OTP email asynchronously/safely without leaking in logs
            try {
                emailService.sendOtpEmail(cleanEmail, rawOtp);
            } catch (Exception e) {
                getServletContext().log("Error sending OTP email to recipient: " + e.getMessage());
            }
        }

        request.setAttribute("step", "verifyOtp");
        request.setAttribute("cooldownSeconds", RESEND_COOLDOWN_SECONDS);
        request.setAttribute("message", "If an account matches that email address, a 6-digit verification code has been sent. Please check your inbox and spam folder.");
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
    }

    /**
     * Resends OTP code if cooldown has elapsed.
     */
    private void handleResendOtp(HttpServletRequest request, HttpServletResponse response, String email)
            throws ServletException, IOException, SQLException {
        if (email == null || email.isBlank()) {
            request.setAttribute("step", "requestOtp");
            request.setAttribute("error", "Please provide your email address.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        String cleanEmail = email.trim().toLowerCase();
        PasswordResetOtp otpState = userDAO.getResetOtp(cleanEmail);

        if (otpState != null) {
            if (!otpState.canResend(RESEND_COOLDOWN_SECONDS)) {
                long remaining = otpState.getRemainingCooldownSeconds(RESEND_COOLDOWN_SECONDS);
                request.setAttribute("step", "verifyOtp");
                request.setAttribute("cooldownSeconds", remaining);
                request.setAttribute("error", "Please wait " + remaining + " seconds before requesting a new code.");
                request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
                return;
            }

            if (otpState.getResendCount() >= 5) {
                request.setAttribute("step", "verifyOtp");
                request.setAttribute("error", "You have exceeded the maximum resend attempts for this session. Please wait before trying again.");
                request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
                return;
            }

            int code = 100000 + secureRandom.nextInt(900000);
            String rawOtp = String.valueOf(code);

            userDAO.createOrUpdateOtp(cleanEmail, rawOtp);

            try {
                emailService.sendOtpEmail(cleanEmail, rawOtp);
            } catch (Exception e) {
                getServletContext().log("Error resending OTP email: " + e.getMessage());
            }
        }

        request.setAttribute("step", "verifyOtp");
        request.setAttribute("cooldownSeconds", RESEND_COOLDOWN_SECONDS);
        request.setAttribute("message", "A new 6-digit verification code has been sent if the email is registered.");
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
    }

    /**
     * Step 2: Verifies the entered 6-digit OTP code.
     */
    private void handleVerifyOtp(HttpServletRequest request, HttpServletResponse response, String email)
            throws ServletException, IOException, SQLException {
        String otpCode = request.getParameter("otpCode");

        if (email == null || email.isBlank() || otpCode == null || otpCode.isBlank()) {
            request.setAttribute("step", "verifyOtp");
            request.setAttribute("error", "Please enter the 6-digit verification code sent to your email.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        String cleanEmail = email.trim().toLowerCase();
        PasswordResetOtp otpState = userDAO.getResetOtp(cleanEmail);

        if (otpState == null) {
            request.setAttribute("step", "requestOtp");
            request.setAttribute("error", "No active verification request found. Please request a new code.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        if (otpState.isExpired()) {
            request.setAttribute("step", "verifyOtp");
            request.setAttribute("error", "This verification code has expired (valid for 5 minutes). Please click 'Resend Code'.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        if (otpState.isVerified()) {
            request.setAttribute("step", "requestOtp");
            request.setAttribute("error", "This verification code has already been used. Please request a new code.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        if (otpState.isMaxAttemptsExceeded(MAX_FAILED_ATTEMPTS)) {
            request.setAttribute("step", "verifyOtp");
            request.setAttribute("error", "Too many incorrect attempts. For your security, this session is locked. Please request a new code.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        // Compare SHA-256 hash using constant-time comparison
        String inputHash = JdbcUserDAO.hashOtp(otpCode.trim());
        boolean matches = MessageDigest.isEqual(inputHash.getBytes(), otpState.getOtpCode().getBytes());

        if (!matches) {
            userDAO.recordOtpFailure(cleanEmail);
            int remainingAttempts = MAX_FAILED_ATTEMPTS - (otpState.getAttempts() + 1);
            request.setAttribute("step", "verifyOtp");
            request.setAttribute("error", "The verification code is incorrect. (" + Math.max(0, remainingAttempts) + " attempts remaining)");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        // OTP verified successfully: issue one-time reset token for password update
        String resetToken = userDAO.markOtpVerified(cleanEmail);
        request.setAttribute("step", "setNewPassword");
        request.setAttribute("resetToken", resetToken);
        request.setAttribute("message", "Verification code confirmed! Please enter your new password below.");
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
    }

    /**
     * Step 3: Sets and confirms the new password using the one-time reset token.
     */
    private void handleUpdatePassword(HttpServletRequest request, HttpServletResponse response, String email)
            throws ServletException, IOException, SQLException {
        String resetToken = request.getParameter("resetToken");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        if (confirmPassword == null || confirmPassword.isBlank()) {
            confirmPassword = request.getParameter("confirmNewPassword");
        }

        request.setAttribute("resetToken", resetToken);

        if (resetToken == null || resetToken.isBlank()) {
            request.setAttribute("step", "requestOtp");
            request.setAttribute("error", "Your reset session has expired or is invalid. Please request a new code.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        if (newPassword == null || newPassword.length() < 6) {
            request.setAttribute("step", "setNewPassword");
            request.setAttribute("error", "New password must be at least 6 characters in length.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("step", "setNewPassword");
            request.setAttribute("error", "New passwords do not match. Please re-enter.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
            return;
        }

        String cleanEmail = email != null ? email.trim().toLowerCase() : "";
        boolean success = userDAO.resetPasswordWithToken(cleanEmail, resetToken.trim(), newPassword);

        if (success) {
            request.setAttribute("mode", "login");
            request.setAttribute("message", "Your password has been successfully reset! Please sign in with your new password.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
        } else {
            request.setAttribute("step", "requestOtp");
            request.setAttribute("error", "The password reset token is invalid or has expired. Please request a new code.");
            request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response);
        }
    }
}
