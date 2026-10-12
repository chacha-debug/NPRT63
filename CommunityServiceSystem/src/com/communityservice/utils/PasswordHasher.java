/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.communityservice.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
/**
 *
 * @author Admin
 */
public class PasswordHasher {

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            AppLogger.error("Password hashing failed", e);
            return null;
        }
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedInput = hashPassword(plainPassword);
        return hashedInput != null && hashedInput.equals(hashedPassword);
    }

    public static String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public static String generateResetToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Checks that a password meets minimum strength requirements: at least 8
     * characters, one uppercase letter, one lowercase letter, one digit, and
     * one special character.
     */
    public static boolean isStrongPassword(String password) {
        return getPasswordStrengthError(password) == null;
    }

    /**
     * Returns a human-readable reason the password is too weak, or null if it
     * meets all requirements.
     */
    public static String getPasswordStrengthError(String password) {
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (!password.chars().anyMatch(Character::isUpperCase)) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.chars().anyMatch(Character::isLowerCase)) {
            return "Password must contain at least one lowercase letter.";
        }
        if (!password.chars().anyMatch(Character::isDigit)) {
            return "Password must contain at least one number.";
        }
        if (password.chars().allMatch(Character::isLetterOrDigit)) {
            return "Password must contain at least one special character (e.g. !@#$%).";
        }
        return null;
    }

}
