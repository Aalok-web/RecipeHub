package com.recipehub.service.impl;

import com.recipehub.service.EmailService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Jakarta Mail-based SMTP Email Service.
 * Transmits secure, branded transactional OTP emails.
 *
 * Configurable via environment variables or JVM system properties:
 * - SMTP_HOST (e.g., smtp.gmail.com, smtp.sendgrid.net, smtp.mailgun.org)
 * - SMTP_PORT (e.g., 587 or 465, defaults to 587)
 * - SMTP_USERNAME (e.g., your-email@gmail.com)
 * - SMTP_PASSWORD (e.g., your app-specific password or API key)
 * - SMTP_FROM (e.g., RecipeHub Kitchen <no-reply@recipehub.com>)
 * - SMTP_STARTTLS (defaults to true)
 * - SMTP_AUTH (defaults to true)
 */
public class SmtpEmailService implements EmailService {
    private static final Logger LOGGER = Logger.getLogger(SmtpEmailService.class.getName());

    @Override
    public boolean isConfigured() {
        String host = getConfig("SMTP_HOST");
        String username = getConfig("SMTP_USERNAME");
        String password = getConfig("SMTP_PASSWORD");
        return host != null && !host.isBlank()
                && username != null && !username.isBlank()
                && password != null && !password.isBlank();
    }

    @Override
    public boolean sendOtpEmail(String recipientEmail, String otpCode) throws Exception {
        if (!isConfigured()) {
            LOGGER.log(Level.WARNING, "SMTP credentials are not configured. To enable live email delivery, please set SMTP_HOST, SMTP_PORT, SMTP_USERNAME, and SMTP_PASSWORD via environment variables or system properties.");
            return false;
        }

        String host = getConfig("SMTP_HOST");
        String port = getConfig("SMTP_PORT", "587");
        String username = getConfig("SMTP_USERNAME");
        String password = getConfig("SMTP_PASSWORD");
        String fromAddress = getConfig("SMTP_FROM", "RecipeHub <no-reply@recipehub.com>");
        String startTls = getConfig("SMTP_STARTTLS", "true");
        String auth = getConfig("SMTP_AUTH", "true");

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", startTls);
        props.put("mail.smtp.connectiontimeout", "6000");
        props.put("mail.smtp.timeout", "6000");

        if ("465".equals(port)) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddress, "RecipeHub Kitchen"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Your RecipeHub Verification Code");

        // Build Multi-part Body (HTML + Plaintext fallback)
        Multipart multipart = new MimeMultipart("alternative");

        // Plaintext version
        MimeBodyPart textPart = new MimeBodyPart();
        String plainText = "Hello from RecipeHub,\n\n"
                + "Your one-time password reset code is: " + otpCode + "\n\n"
                + "This verification code will expire in 5 minutes and can only be used once.\n"
                + "If you did not request a password reset, please ignore this email or review your account security.\n\n"
                + "Warm regards,\n"
                + "The RecipeHub Team";
        textPart.setText(plainText, "utf-8");
        multipart.addBodyPart(textPart);

        // Editorial HTML version
        MimeBodyPart htmlPart = new MimeBodyPart();
        String htmlContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head><meta charset='utf-8'></head>"
                + "<body style='margin:0; padding:32px 16px; background-color:#fcfaf6; font-family:-apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, sans-serif; color:#2d3748;'>"
                + "  <table width='100%' border='0' cellspacing='0' cellpadding='0' style='max-width:540px; margin:0 auto; background:#ffffff; border:1px solid #e7ded4; border-radius:16px; overflow:hidden; box-shadow:0 8px 24px rgba(24,55,40,0.06);'>"
                + "    <tr>"
                + "      <td style='background:#183728; padding:28px 36px; text-align:center;'>"
                + "        <div style='display:inline-block; font-size:24px; font-weight:700; color:#ffffff; letter-spacing:-0.5px;'>"
                + "          <span style='display:inline-block; width:30px; height:30px; background:#d4a373; color:#183728; border-radius:6px; line-height:30px; text-align:center; margin-right:8px; font-weight:800;'>R</span>"
                + "          recipe<span style='color:#d4a373;'>hub</span>"
                + "        </div>"
                + "        <div style='color:#a3b899; font-size:11px; letter-spacing:2px; text-transform:uppercase; margin-top:4px;'>Account Security</div>"
                + "      </td>"
                + "    </tr>"
                + "    <tr>"
                + "      <td style='padding:36px; text-align:center;'>"
                + "        <h2 style='font-family:Georgia, serif; color:#183728; font-size:22px; margin:0 0 12px 0;'>Reset Your Password</h2>"
                + "        <p style='color:#5f6b64; font-size:14px; line-height:1.6; margin:0 0 24px 0;'>"
                + "          We received a request to reset your RecipeHub password. Use the verification code below to proceed:"
                + "        </p>"
                + "        <div style='background:#f4f1ea; border:2px dashed #183728; border-radius:12px; padding:18px 24px; display:inline-block; margin:0 auto 24px auto;'>"
                + "          <span style='font-family:monospace, Courier; font-size:32px; font-weight:800; letter-spacing:8px; color:#183728;'>"
                + otpCode
                + "          </span>"
                + "        </div>"
                + "        <p style='color:#8c6b3e; font-size:13px; font-weight:600; margin:0 0 20px 0; background:#fef8ee; border:1px solid #f9e7cd; border-radius:8px; padding:10px 14px;'>"
                + "          ⏱️ This code expires in <strong>5 minutes</strong> and can only be used once."
                + "        </p>"
                + "        <p style='color:#718096; font-size:12.5px; line-height:1.5; margin:0;'>"
                + "          If you did not request this verification, no changes have been made to your account. You can safely ignore this email."
                + "        </p>"
                + "      </td>"
                + "    </tr>"
                + "    <tr>"
                + "      <td style='background:#faf7f2; border-top:1px solid #eee7dd; padding:18px 36px; text-align:center; font-size:12px; color:#88948d;'>"
                + "        © RecipeHub · Crafted for home cooks everywhere"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</body>"
                + "</html>";
        htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
        multipart.addBodyPart(htmlPart);

        message.setContent(multipart);

        Transport.send(message);
        LOGGER.info("Successfully dispatched OTP verification email to recipient.");
        return true;
    }

    private String getConfig(String key) {
        String val = System.getenv(key);
        if (val == null || val.isBlank()) {
            val = System.getProperty(key);
        }
        return val != null ? val.trim() : null;
    }

    private String getConfig(String key, String fallback) {
        String val = getConfig(key);
        return (val != null && !val.isBlank()) ? val : fallback;
    }
}
