// ============================================================
// FILE     : EmergencyAlertNotification.java
// PACKAGE  : patterns.creational.abstractfactory.products.emergency
// PURPOSE  : Emergency Alert Notification — SMS + Alert Push
//            Notifies both patient and doctor simultaneously
// PATTERN  : Abstract Factory — Concrete Product
// ============================================================

package patterns.creational.abstractfactory.products.emergency;

import models.Doctor;
import models.Patient;
import models.Token;
import patterns.creational.abstractfactory.interfaces.NotificationService;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

public class EmergencyAlertNotification implements NotificationService {

    // ─────────────────────────────────────────
    // Channel Label
    // ─────────────────────────────────────────
    private static final String CHANNEL = "SMS + EMERGENCY ALERT";

    // ─────────────────────────────────────────
    // Notify Patient
    // ─────────────────────────────────────────
    @Override
    public void notifyPatient(Patient patient, String message) {
        printAlert(
            "🚨 PATIENT ALERT → " + patient.getName()
            + " [" + patient.getContact() + "] : " + message
        );
    }

    // ─────────────────────────────────────────
    // Notify Doctor
    // ─────────────────────────────────────────
    @Override
    public void notifyDoctor(Doctor doctor, String message) {
        printAlert(
            "🚨 DOCTOR ALERT → Dr." + doctor.getName()
            + " [" + doctor.getContactNumber() + "] : " + message
        );
    }

    // ─────────────────────────────────────────
    // Token Generated
    // ─────────────────────────────────────────
    @Override
    public void notifyTokenGenerated(Patient patient, Token token) {
        printAlert(
            "🚨 EMERGENCY TOKEN → " + patient.getName()
            + " | Token#: " + String.format("%03d", token.getTokenNumber())
            + " | Issued: " + token.getIssuedAt()
            + " | IMMEDIATE PRIORITY"
        );
    }

    // ─────────────────────────────────────────
    // Appointment Booked
    // ─────────────────────────────────────────
    @Override
    public void notifyAppointmentBooked(Patient patient, String appointmentTime) {
        printAlert(
            "🚨 EMERGENCY APPOINTMENT → " + patient.getName()
            + " | Immediate slot at: " + appointmentTime
        );
    }

    // ─────────────────────────────────────────
    // Appointment Cancelled
    // ─────────────────────────────────────────
    @Override
    public void notifyAppointmentCancelled(Patient patient, String appointmentId) {
        printAlert(
            "⚠  EMERGENCY CANCELLATION → " + patient.getName()
            + " | Apt ID: " + appointmentId
        );
    }

    // ─────────────────────────────────────────
    // Patient Turn
    // ─────────────────────────────────────────
    @Override
    public void notifyPatientTurn(Patient patient, int tokenNumber) {
        printAlert(
            "🚨 YOUR TURN NOW → " + patient.getName()
            + " | Token#: " + String.format("%03d", tokenNumber)
            + " | PROCEED TO EMERGENCY ROOM IMMEDIATELY"
        );
    }

    // ─────────────────────────────────────────
    // Emergency Broadcast
    // ─────────────────────────────────────────
    @Override
    public void broadcastEmergencyAlert(String message) {
        System.out.println(ConsoleColors.BG_RED + ConsoleColors.BOLD_WHITE);
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║           🚨  SYSTEM-WIDE EMERGENCY ALERT  🚨        ║");
        System.out.println("  ║  " + String.format("%-52s", message) + "  ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
        System.out.println(ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Channel
    // ─────────────────────────────────────────
    @Override
    public String getNotificationChannel() {
        return CHANNEL;
    }

    // ─────────────────────────────────────────
    // Internal Alert Printer
    // ─────────────────────────────────────────
    private void printAlert(String content) {
        System.out.println(ConsoleColors.BOLD_RED
            + "  [ALERT][EMERGENCY][" + DateTimeUtils.getCurrentTime() + "] "
            + content
            + ConsoleColors.RESET);
    }
}