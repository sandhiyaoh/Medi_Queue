// ============================================================
// FILE     : DateTimeUtils.java
// PACKAGE  : utils
// PURPOSE  : Handles all date/time formatting,
//            timestamp generation, and wait time calculation
// ============================================================

package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    // ─────────────────────────────────────────
    // Formatters
    // ─────────────────────────────────────────
    private static final DateTimeFormatter FULL_FORMAT  =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter TIME_FORMAT  =
        DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final DateTimeFormatter DATE_FORMAT  =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TOKEN_FORMAT =
        DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    // ─────────────────────────────────────────
    // Get Current Timestamps
    // ─────────────────────────────────────────
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(FULL_FORMAT);
    }

    public static String getCurrentTime() {
        return LocalDateTime.now().format(TIME_FORMAT);
    }

    public static String getCurrentDate() {
        return LocalDateTime.now().format(DATE_FORMAT);
    }

    public static String getTokenTimestamp() {
        return LocalDateTime.now().format(TOKEN_FORMAT);
    }

    // ─────────────────────────────────────────
    // ID Generators
    // ─────────────────────────────────────────

    // e.g., PAT-20240101123045-001
    public static String generatePatientId(int count) {
        return String.format("PAT-%s-%03d", getTokenTimestamp(), count);
    }

    // e.g., DOC-20240101123045-001
    public static String generateDoctorId(int count) {
        return String.format("DOC-%s-%03d", getTokenTimestamp(), count);
    }

    // e.g., APT-20240101123045-001
    public static String generateAppointmentId(int count) {
        return String.format("APT-%s-%03d", getTokenTimestamp(), count);
    }

    // e.g., TKN-GEN-20240101123045-001
    public static String generateTokenId(String deptCode, int count) {
        return String.format("TKN-%s-%s-%03d", deptCode, getTokenTimestamp(), count);
    }

    // ─────────────────────────────────────────
    // Wait Time Calculator
    // ─────────────────────────────────────────

    // Each patient = 10 mins average consultation
    public static int calculateWaitTime(int queuePosition) {
        return queuePosition * 10;
    }

    // Emergency wait time = 5 mins per position
    public static int calculateEmergencyWaitTime(int queuePosition) {
        return queuePosition * 5;
    }

    // VIP wait time = 7 mins per position
    public static int calculateVIPWaitTime(int queuePosition) {
        return queuePosition * 7;
    }

    // ─────────────────────────────────────────
    // Private Constructor — Utility Class
    // ─────────────────────────────────────────
    private DateTimeUtils() {}
}