// ============================================================
// FILE     : ConsolePrinter.java
// PACKAGE  : utils
// PURPOSE  : Prints banners, dividers, tables, and
//            formatted messages for all UI screens
// ============================================================

package utils;

import static utils.ConsoleColors.*;

public class ConsolePrinter {

    // ─────────────────────────────────────────
    // Divider Width
    // ─────────────────────────────────────────
    private static final int WIDTH = 65;

    // ─────────────────────────────────────────
    // Main System Banner
    // ─────────────────────────────────────────
    public static void printMainBanner() {
        System.out.println(BOLD_CYAN);
        printLine("=");
        System.out.println(center("MediQueue: Smart Hospital Management System"));
        System.out.println(center("Design Patterns: Abstract Factory | Facade | Observer"));
        printLine("=");
        System.out.println(RESET);
    }

    // ─────────────────────────────────────────
    // Section Header
    // ─────────────────────────────────────────
    public static void printSectionHeader(String title) {
        System.out.println();
        System.out.println(BOLD_YELLOW + printLine("-") + RESET);
        System.out.println(BOLD_YELLOW + center(title) + RESET);
        System.out.println(BOLD_YELLOW + printLine("-") + RESET);
    }

    // ─────────────────────────────────────────
    // Department Header
    // ─────────────────────────────────────────
    public static void printDepartmentHeader(String deptCode, String deptName) {
        String color = ConsoleColors.getDepartmentColor(deptCode);
        System.out.println();
        System.out.println(color + printLine("*") + RESET);
        System.out.println(color + center("[ " + deptName + " ]") + RESET);
        System.out.println(color + printLine("*") + RESET);
    }

    // ─────────────────────────────────────────
    // Success Message
    // ─────────────────────────────────────────
    public static void printSuccess(String message) {
        System.out.println(BOLD_GREEN + "  ✔  " + message + RESET);
    }

    // ─────────────────────────────────────────
    // Error Message
    // ─────────────────────────────────────────
    public static void printError(String message) {
        System.out.println(BOLD_RED + "  ✘  ERROR : " + message + RESET);
    }

    // ─────────────────────────────────────────
    // Warning Message
    // ─────────────────────────────────────────
    public static void printWarning(String message) {
        System.out.println(BOLD_YELLOW + "  ⚠  WARNING : " + message + RESET);
    }

    // ─────────────────────────────────────────
    // Info Message
    // ─────────────────────────────────────────
    public static void printInfo(String message) {
        System.out.println(BOLD_CYAN + "  ℹ  " + message + RESET);
    }

    // ─────────────────────────────────────────
    // Emergency Alert
    // ─────────────────────────────────────────
    public static void printEmergencyAlert(String message) {
        System.out.println(BG_RED + BOLD_WHITE + "  🚨 EMERGENCY : " + message + "  " + RESET);
    }

    // ─────────────────────────────────────────
    // Menu Option
    // ─────────────────────────────────────────
    public static void printMenuOption(int number, String label) {
        System.out.println(BOLD_WHITE + "   [" + number + "]  " + label + RESET);
    }

    // ─────────────────────────────────────────
    // Input Prompt
    // ─────────────────────────────────────────
    public static void printPrompt(String label) {
        System.out.print(BOLD_CYAN + "  ➤  " + label + " : " + RESET);
    }

    // ─────────────────────────────────────────
    // Divider Line
    // ─────────────────────────────────────────
    public static String printLine(String ch) {
        return ch.repeat(WIDTH);
    }

    // ─────────────────────────────────────────
    // Center Text
    // ─────────────────────────────────────────
    public static String center(String text) {
        int padding = (WIDTH - text.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + text;
    }

    // ─────────────────────────────────────────
    // Blank Line
    // ─────────────────────────────────────────
    public static void printBlankLine() {
        System.out.println();
    }

    // ─────────────────────────────────────────
    // Private Constructor — Utility Class
    // ─────────────────────────────────────────
    private ConsolePrinter() {}
}