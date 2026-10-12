package com.communityservice.tests;

import com.communityservice.utils.EmailValidator;
import com.communityservice.utils.PasswordHasher;

/**
 * Lightweight, dependency-free smoke tests for the pure-logic utility
 * classes (PasswordHasher, EmailValidator).
 *
 * This project can't pull JUnit from Maven Central in some sandboxed build
 * environments, so rather than leave these classes completely untested,
 * this runs plain assertions and prints a pass/fail summary. Run it with:
 *
 *   javac -encoding UTF-8 -d build src/com/communityservice/**\/*.java
 *   java -cp build com.communityservice.tests.SmokeTests
 *
 * Exits with status 0 if everything passes, 1 if anything fails — so it can
 * also be wired into a CI step later without needing a test framework.
 */
public final class SmokeTests {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("Running smoke tests...\n");

        testEmailValidator();
        testPasswordHashing();
        testPasswordStrength();
        testVerificationCodeFormat();

        System.out.println("\n" + passed + " passed, " + failed + " failed.");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testEmailValidator() {
        check("valid email accepted", EmailValidator.isValidEmail("jane.doe@example.com"));
        check("valid email with subdomain accepted", EmailValidator.isValidEmail("user@mail.example.co.za"));
        check("missing @ rejected", !EmailValidator.isValidEmail("jane.doeexample.com"));
        check("missing domain rejected", !EmailValidator.isValidEmail("jane.doe@"));
        check("null rejected", !EmailValidator.isValidEmail(null));
        check("empty string rejected", !EmailValidator.isValidEmail(""));
        check("spaces rejected", !EmailValidator.isValidEmail("jane doe@example.com"));
    }

    private static void testPasswordHashing() {
        String hash1 = PasswordHasher.hashPassword("MyPassw0rd!");
        String hash2 = PasswordHasher.hashPassword("MyPassw0rd!");
        String hash3 = PasswordHasher.hashPassword("Different1!");

        check("hashing is deterministic", hash1 != null && hash1.equals(hash2));
        check("different passwords hash differently", !hash1.equals(hash3));
        check("verifyPassword accepts the correct password", PasswordHasher.verifyPassword("MyPassw0rd!", hash1));
        check("verifyPassword rejects the wrong password", !PasswordHasher.verifyPassword("wrongpass", hash1));
    }

    private static void testPasswordStrength() {
        check("short password rejected", !PasswordHasher.isStrongPassword("Ab1!"));
        check("password without uppercase rejected", !PasswordHasher.isStrongPassword("password1!"));
        check("password without lowercase rejected", !PasswordHasher.isStrongPassword("PASSWORD1!"));
        check("password without digit rejected", !PasswordHasher.isStrongPassword("Password!"));
        check("password without special char rejected", !PasswordHasher.isStrongPassword("Password1"));
        check("strong password accepted", PasswordHasher.isStrongPassword("Str0ng!Pass"));
    }

    private static void testVerificationCodeFormat() {
        String code = PasswordHasher.generateVerificationCode();
        check("verification code is 6 digits", code != null && code.matches("\\d{6}"));

        String token = PasswordHasher.generateResetToken();
        check("reset token is non-empty", token != null && !token.isBlank());
        check("reset token is reasonably long", token != null && token.length() >= 32);
    }

    private static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  [PASS] " + description);
        } else {
            failed++;
            System.out.println("  [FAIL] " + description);
        }
    }
}
