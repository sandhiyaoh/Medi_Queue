// ============================================================
// FILE     : SMSNotification.java
// PACKAGE  : patterns.creational.abstractfactory.products.general
// PURPOSE  : SMS-based notification for General Department
// PATTERN  : Abstract Factory — Concrete Product
// ============================================================

package patterns.creational.abstractfactory.products.general;

import models.Doctor;
import models.Patient;
import models.Token;
import patterns.creational.abstractfactory.interfaces.NotificationService;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

public class SMSNotification implements NotificationService {

    // ─────────────────────────────────────────
    // Channel Label
    // ─────────────────────────────────────────
    private static final String CHANNEL = "SMS";

    // ─────────────────────────────────────────
    // Notify Patient
    // ─────────────────────────────────────────
    @Override
    public void notifyPatient(Patient patient, String message) {
        printSMS(
            "To: " + patient.getContact()
            + " | Patient: " + patient.getName()
            + " | Msg: " + message
        );
    }

    // ─────────────────────────────────────────
    // Notify Doctor
    // ─────────────────────────────────────────
    @Override
    public void notifyDoctor(Doctor doctor, String message) {
        printSMS(
            "To: " + doctor.getContactNumber()
            + " | Doctor: Dr." + doctor.getName()
            + " | Msg: " + message
        );
    }

    // ─────────────────────────────────────────
    // Token Generated
    // ─────────────────────────────────────────
    @Override
    public void notifyTokenGenerated(Patient patient, Token token) {
        printSMS(
            "To: " + patient.getContact()
            + " | Dear " + patient.getName()
            + ", Your Token#: " + String.format("%03d", token.getTokenNumber())
            + " has been generated. Issued at: " + token.getIssuedAt()
        );
    }

    // ─────────────────────────────────────────
    // Appointment Booked
    // ─────────────────────────────────────────
    @Override
    public void notifyAppointmentBooked(Patient patient, String appointmentTime) {
        printSMS(
            "To: " + patient.getContact()
            + " | Dear " + patient.getName()
            + ", Your appointment is confirmed at " + appointmentTime
        );
    }

    // ─────────────────────────────────────────
    // Appointment Cancelled
    // ─────────────────────────────────────────
    @Override
    public void notifyAppointmentCancelled(Patient patient, String appointmentId) {
        printSMS(
            "To: " + patient.getContact()
            + " | Dear " + patient.getName()
            + ", Your appointment [" + appointmentId + "] has been cancelled."
        );
    }

    // ─────────────────────────────────────────
    // Patient Turn
    // ─────────────────────────────────────────
    @Override
    public void notifyPatientTurn(Patient patient, int tokenNumber) {
        printSMS(
            "To: " + patient.getContact()
            + " | Dear " + patient.getName()
            + ", Token#: " + String.format("%03d", tokenNumber)
            + " — It's your turn! Please proceed to the General counter."
        );
    }

    // ─────────────────────────────────────────
    // Emergency Broadcast
    // ─────────────────────────────────────────
    @Override
    public void broadcastEmergencyAlert(String message) {
        ConsolePrinter.printEmergencyAlert("[GENERAL-SMS] Broadcast → " + message);
    }

    // ─────────────────────────────────────────
    // Channel
    // ─────────────────────────────────────────
    @Override
    public String getNotificationChannel() {
        return CHANNEL;
    }

    // ─────────────────────────────────────────
    // Internal Print Helper
    // ─────────────────────────────────────────
    private void printSMS(String content) {
        System.out.println(ConsoleColors.BOLD_GREEN
            + "  [SMS][GENERAL][" + DateTimeUtils.getCurrentTime() + "] "
            + content
            + ConsoleColors.RESET);
    }
}