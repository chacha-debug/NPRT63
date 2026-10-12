package com.communityservice.utils;

/** Shared validation rules for registration/profile forms. */
public final class InputValidator {
    private InputValidator() {}

    /** Accepts common South African mobile/landline forms: 0XXXXXXXXX or +27XXXXXXXXX. */
    public static boolean isValidSouthAfricanPhone(String value) {
        if (value == null) return false;
        String normalized = value.replaceAll("[\\s()-]", "");
        return normalized.matches("0[1-8][0-9]{8}") || normalized.matches("\\+27[1-8][0-9]{8}");
    }

    /** Format validation only; this does not claim registry verification. */
    public static boolean isValidSouthAfricanNonProfitRegistration(String value) {
        if (value == null) return false;
        String v = value.trim().toUpperCase().replaceAll("\\s+", " ");
        boolean dsdNpo = v.matches("\\d{3}-\\d{3}\\s*NPO");
        boolean cipcNpc = v.matches("\\d{4}/\\d{6}/08");
        boolean sarsPbo = v.matches("(?:PBO\\s*)?9\\d{9}");
        return dsdNpo || cipcNpc || sarsPbo;
    }

    public static String nonProfitRegistrationHint() {
        return "Use DSD NPO (123-456 NPO), CIPC NPC (2024/123456/08), or SARS PBO (10 digits starting with 9).";
    }
}