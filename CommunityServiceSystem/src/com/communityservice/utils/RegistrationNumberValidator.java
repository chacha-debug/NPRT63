package com.communityservice.utils;

public final class RegistrationNumberValidator {
    private RegistrationNumberValidator() {}

    /**
     * Validates common South African NPO/CIPC references by format only.
     * Actual registration verification remains an administrative/backend process.
     */
    public static boolean isValid(String value) {
        if (value == null) return false;
        String v = value.trim();
        return v.matches("\\d{3}-\\d{3}")
                || v.matches("\\d{4}/\\d{6}/\\d{2}")
                || v.matches("(?i)(PBO\\s*)?[A-Z0-9-]{5,30}");
    }
}
