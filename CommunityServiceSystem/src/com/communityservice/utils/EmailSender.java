package com.communityservice.utils;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Sends verification and password-reset email using JavaMail's SMTP provider.
 *
 * Configuration is supplied through environment variables or Java system
 * properties (-Dname=value). System properties are useful for this NetBeans
 * project because they can be entered under Project Properties > Run > VM Options.
 *
 * Required:
 *   SMTP_HOST      e.g. smtp.gmail.com
 *   SMTP_PORT      e.g. 587
 *   SMTP_USERNAME  full SMTP account username/email
 *   SMTP_PASSWORD  SMTP password or provider app password
 *   SMTP_FROM      sender address (defaults to SMTP_USERNAME)
 */
public final class EmailSender {

    private static final int DEFAULT_PORT = 587;
    private static final int TIMEOUT_MS = 15_000;

    private static volatile String lastError = "";

    private EmailSender() {}

    private static String value(String key) {
        String v = System.getProperty(key);
        if (v == null || v.isBlank()) {
            v = System.getenv(key);
        }
        return (v == null || v.isBlank()) ? null : v.trim();
    }

    private static String host() {
        return value("SMTP_HOST");
    }

    private static int port() {
        String configured = value("SMTP_PORT");
        if (configured == null) return DEFAULT_PORT;
        try {
            int parsed = Integer.parseInt(configured);
            return parsed > 0 && parsed <= 65535 ? parsed : DEFAULT_PORT;
        } catch (NumberFormatException ex) {
            return DEFAULT_PORT;
        }
    }

    private static String username() {
        return value("SMTP_USERNAME");
    }

    private static String password() {
        return value("SMTP_PASSWORD");
    }

    private static String from() {
        String configured = value("SMTP_FROM");
        return configured != null ? configured : username();
    }

    /** Returns true when all required SMTP settings are present. */
    public static boolean isConfigured() {
        return host() != null
                && username() != null
                && password() != null
                && from() != null;
    }

    /**
     * Returns a safe diagnostic summary. Credentials and passwords are never
     * included in the returned value.
     */
    public static String getConfigurationDiagnostic() {
        StringBuilder result = new StringBuilder();
        result.append("SMTP configuration: ");
        result.append("host=").append(host() == null ? "missing" : host());
        result.append(", port=").append(port());
        result.append(", username=").append(username() == null ? "missing" : "configured");
        result.append(", password=").append(password() == null ? "missing" : "configured");
        result.append(", from=").append(from() == null ? "missing" : from());
        return result.toString();
    }

    /** Returns the last safe error description for diagnostics. */
    public static String getLastError() {
        return lastError;
    }

    /**
     * Sends a plain-text email. Returns true only when the SMTP provider accepts
     * the message for delivery.
     */
    public static boolean sendEmail(String to, String subject, String body) {
        lastError = "";

        if (!isConfigured()) {
            lastError = "SMTP configuration is incomplete. " + getConfigurationDiagnostic();
            AppLogger.error("EmailSender: " + lastError);
            return false;
        }

        boolean sent = sendEmailOnPort(to, subject, body, port(), false);
        if (!sent && port() == 587 && lastError.toLowerCase(java.util.Locale.ROOT).contains("could not connect")) {
            AppLogger.info("EmailSender: port 587 unavailable; retrying Gmail SMTP over SSL port 465.");
            sent = sendEmailOnPort(to, subject, body, 465, true);
        }
        return sent;
    }

    private static boolean sendEmailOnPort(String to, String subject, String body, int selectedPort, boolean implicitSsl) {
        final String smtpHost = host();
        final int smtpPort = selectedPort;
        final String smtpUsername = username();
        final String smtpPassword = password();
        final String smtpFrom = from();

        try {
            InternetAddress recipient = new InternetAddress(to, true);
            recipient.validate();
            InternetAddress sender = new InternetAddress(smtpFrom, true);
            sender.validate();

            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", String.valueOf(smtpPort));
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", implicitSsl ? "false" : "true");
            props.put("mail.smtp.starttls.required", implicitSsl ? "false" : "true");
            props.put("mail.smtp.ssl.enable", implicitSsl ? "true" : "false");
            props.put("mail.smtp.connectiontimeout", String.valueOf(TIMEOUT_MS));
            props.put("mail.smtp.timeout", String.valueOf(TIMEOUT_MS));
            props.put("mail.smtp.writetimeout", String.valueOf(TIMEOUT_MS));
            props.put("mail.smtp.ssl.checkserveridentity", "true");
            props.put("mail.smtp.ssl.trust", smtpHost);
            props.put("mail.mime.charset", StandardCharsets.UTF_8.name());
            props.put("mail.debug", "false");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(sender);
            message.setRecipient(Message.RecipientType.TO, recipient);
            message.setSubject(subject, StandardCharsets.UTF_8.name());
            message.setText(body, StandardCharsets.UTF_8.name());

            Transport.send(message);

            AppLogger.info("EmailSender: message accepted by SMTP provider for " + maskEmail(to));
            return true;
        } catch (AddressException ex) {
            lastError = "The recipient or sender email address is invalid.";
            AppLogger.error("EmailSender: " + lastError + " Recipient=" + maskEmail(to));
            return false;
        } catch (MessagingException ex) {
            lastError = classifyMessagingException(ex);
            AppLogger.error("EmailSender: delivery failed for " + maskEmail(to) + ": " + lastError);
            return false;
        } catch (RuntimeException ex) {
            lastError = "Unexpected email-service error: " + safeMessage(ex);
            AppLogger.error("EmailSender: " + lastError);
            return false;
        }
    }

    private static String classifyMessagingException(MessagingException ex) {
        String message = safeMessage(ex);
        String lower = message.toLowerCase(java.util.Locale.ROOT);

        if (lower.contains("authentication") || lower.contains("535") || lower.contains("username and password")) {
            return "SMTP authentication was rejected. Check SMTP_USERNAME and SMTP_PASSWORD; for Gmail, use a Google App Password rather than the normal account password.";
        }
        if (lower.contains("starttls") || lower.contains("530")) {
            return "The SMTP server requires a secure STARTTLS connection, which could not be established.";
        }
        if (lower.contains("connect") || lower.contains("timeout") || lower.contains("unknownhost")) {
            return "Could not connect to the configured Gmail SMTP service. Check the network connection, firewall, antivirus, VPN, or SMTP access settings.";
        }
        if (lower.contains("address") || lower.contains("recipient") || lower.contains("550") || lower.contains("553")) {
            return "The SMTP server rejected the sender or recipient address.";
        }
        return "SMTP delivery failed: " + message;
    }

    private static String safeMessage(Exception ex) {
        String message = ex.getMessage();
        return message == null || message.isBlank() ? ex.getClass().getSimpleName() : message;
    }

    private static String maskEmail(String email) {
        if (email == null || email.isBlank()) return "<unknown>";
        int at = email.indexOf('@');
        if (at <= 1) return "***" + (at >= 0 ? email.substring(at) : "");
        return email.charAt(0) + "***" + email.substring(at);
    }

    public static boolean sendVerificationCode(String to, String code) {
        String subject = "Your Community Service+ verification code";
        String body = "Your verification code is: " + code
                + "\n\nThis code expires in 15 minutes."
                + "\n\nIf you did not create this account, you can safely ignore this email.";
        return sendEmail(to, subject, body);
    }

    public static boolean sendNgoPendingReviewNotice(String to, String organisationName) {
        String subject = "Community Service+ organisation application received";
        String body = "Hello " + (organisationName == null || organisationName.isBlank() ? "there" : organisationName.trim()) + ","
                + "\n\nYour email has been verified and your organisation application has been received."
                + "\nYour application is currently being reviewed by a Community Service+ administrator."
                + "\nYou cannot sign in to the NGO dashboard until the application is approved."
                + "\n\nWe will email you again when a decision has been made. No further action is required right now."
                + "\n\nThank you for joining Community Service+.";
        return sendEmail(to, subject, body);
    }

    public static boolean sendNgoApprovalNotice(String to, String organisationName) {
        String subject = "Community Service+ organisation approved";
        String body = "Hello " + (organisationName == null ? "there" : organisationName) + ","
                + "\n\nYour organisation has been approved by a Community Service+ administrator."
                + "\nYou can now sign in and use the NGO dashboard."
                + "\n\nThank you for joining Community Service+.";
        return sendEmail(to, subject, body);
    }

    public static boolean sendNgoRejectionNotice(String to, String organisationName, String reason) {
        String subject = "Community Service+ organisation registration update";
        String safeReason = (reason == null || reason.isBlank()) ? "No additional reason was provided." : reason.trim();
        String body = "Hello " + (organisationName == null ? "there" : organisationName) + ","
                + "\n\nYour organisation registration was not approved."
                + "\nReason: " + safeReason
                + "\n\nIf you believe this is an error, please contact Community Service+ support.";
        return sendEmail(to, subject, body);
    }

    public static boolean sendPasswordResetCode(String to, String code) {
        String subject = "Your Community Service+ password reset code";
        String body = "Your password reset code is: " + code
                + "\n\nThis code expires in 15 minutes."
                + "\n\nIf you did not request a password reset, you can safely ignore this email.";
        return sendEmail(to, subject, body);
    }
}
