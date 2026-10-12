package com.communityservice.utils;

public final class PhoneValidator {
    private PhoneValidator() {}

    /** Accepts common South African formats such as 0821234567 or +27 82 123 4567. */
    public static boolean isValidSouthAfricanPhone(String phone) {
        if (phone == null) return false;
        String value = phone.trim();
        if (value.matches("\\+27\\s?\\d{2}\\s?\\d{3}\\s?\\d{4}")) return true;
        if (value.matches("0\\d{2}\\s?\\d{3}\\s?\\d{4}")) return true;
        return false;
    }
}
