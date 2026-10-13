// ============================================================
// FILE     : InputValidator.java
// PACKAGE  : utils
// PURPOSE  : Validates all console user inputs
//            Prevents invalid data from entering the system
// ============================================================

package utils;

public class InputValidator {

    // ─────────────────────────────────────────
    // Name Validation
    // ─────────────────────────────────────────
    public static boolean isValidName(String name) {
        return name != null
            && !name.trim().isEmpty()
            && name.matches("[a-zA-Z\\s]+")
            && name.trim().length() >= 2;
    }

    // ─────────────────────────────────────────
    // Age Validation
    // ─────────────────────────────────────────
    public static boolean isValidAge(int age) {
        return age >= 1 && age <= 120;
    }

    // ─────────────────────────────────────────
    // Contact Validation — 10-digit number
    // ─────────────────────────────────────────
    public static boolean isValidContact(String contact) {
        return contact != null
            && contact.matches("\\d{10}");
    }

    // ─────────────────────────────────────────
    // Email Validation
    // ─────────────────────────────────────────
    public static boolean isValidEmail(String email) {
        return email != null
            && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    // ─────────────────────────────────────────
    // Menu Choice Validation
    // ─────────────────────────────────────────
    public static boolean isValidMenuChoice(int choice, int min, int max) {
        return choice >= min && choice <= max;
    }

    // ─────────────────────────────────────────
    // Non-empty String Check
    // ─────────────────────────────────────────
    public static boolean isNonEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    // ─────────────────────────────────────────
    // Appointment ID Format Check
    // ─────────────────────────────────────────
    public static boolean isValidAppointmentId(String id) {
        return id != null && id.startsWith("APT-");
    }

    // ─────────────────────────────────────────
    // Patient ID Format Check
    // ─────────────────────────────────────────
    public static boolean isValidPatientId(String id) {
        return id != null && id.startsWith("PAT-");
    }

    // ─────────────────────────────────────────
    // Private Constructor — Utility Class
    // ─────────────────────────────────────────
    private InputValidator() {}
}