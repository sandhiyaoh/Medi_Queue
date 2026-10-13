// ============================================================
// FILE     : PremiumNotification.java
// PACKAGE  : patterns.creational.abstractfactory.products.vip
// PURPOSE  : VIP Premium Notification — Email + SMS + Concierge
//            Delivers personalized premium communications
// PATTERN  : Abstract Factory — Concrete Product
// ============================================================

package patterns.creational.abstractfactory.products.vip;

import models.Doctor;
import models.Patient;
import models.Token;
import patterns.creational.abstractfactory.interfaces.NotificationService;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

public class PremiumNotification implements NotificationService {

    // ─────────────────────────────────────────
    // Channel Label
    // ─────────────────────────────────────────
    private static final String CHANNEL = "EMAIL + SMS + CONCIERGE";

    // ─────────────────────────────────────────
    // Notify Patient
    // ─────────────────────────────────────────
    @Override
    public void notifyPatient(Patient patient, String message) {
        printPremium(
            "★ VIP PATIENT → " + patient.getName()
            + " | Email: " + patient.getEmail()
            + " | SMS: " + patient.getContact()
            + " | Msg: " + message
        );
    }

    // ─────────────────────────────────────────
    // Notify Doctor
    // ─────────────────────────────────────────
    @Override
    public void notifyDoctor(Doctor doctor, String message) {
        printPremium(
            "★ VIP DOCTOR → Dr." + doctor.getName()
            + " [" + doctor.getContactNumber() + "] : " + message
        );
    }

    // ─────────────────────────────────────────
    // Token Generated
    // ─────────────────────────────────────────
    @Override
    public void notifyTokenGenerated(Patient patient, Token token) {
        printPremium(
            "★ PREMIUM TOKEN → Dear " + patient.getName()
            + ", Your exclusive Token#: " + String.format("%03d", token.getTokenNumber())
            + " | Issued: " + token.getIssuedAt()
            + " | VIP Lounge access granted."
        );
    }

    // ─────────────────────────────────────────
    // Appointment Booked
    // ─────────────────────────────────────────
    @Override
    public void notifyAppointmentBooked(Patient patient, String appointmentTime) {
        printPremium(
            "★ PREMIUM APPOINTMENT → Dear " + patient.getName()
            + ", Your preferred appointment is confirmed at " + appointmentTime
            + ". A concierge will escort you."
        );
    }

    // ─────────────────────────────────────────
    // Appointment Cancelled
    // ─────────────────────────────────────────
    @Override
    public void notifyAppointmentCancelled(Patient patient, String appointmentId) {
        printPremium(
            "★ VIP CANCELLATION → Dear " + patient.getName()
            + ", Apt [" + appointmentId + "] cancelled."
            + " Please contact your VIP coordinator to reschedule."
        );
    }

    // ─────────────────────────────────────────
    // Patient Turn
    // ─────────────────────────────────────────
    @Override
    public void notifyPatientTurn(Patient patient, int tokenNumber) {
        printPremium(
            "★ VIP TURN → Dear " + patient.getName()
            + " | Token#: " + String.format("%03d", tokenNumber)
            + " — Your consultant is ready. The concierge will escort you now."
        );
    }

    // ─────────────────────────────────────────
    // Emergency Broadcast
    // ─────────────────────────────────────────
    @Override
    public void broadcastEmergencyAlert(String message) {
        ConsolePrinter.printEmergencyAlert("[VIP-PREMIUM] Broadcast → " + message);
    }

    // ─────────────────────────────────────────
    // Channel
    // ─────────────────────────────────────────
    @Override
    public String getNotificationChannel() {
        return CHANNEL;
    }

    // ─────────────────────────────────────────
    // Internal Premium Printer
    // ─────────────────────────────────────────
    private void printPremium(String content) {
        System.out.println(ConsoleColors.BOLD_PURPLE
            + "  [PREMIUM][VIP][" + DateTimeUtils.getCurrentTime() + "] "
            + content
            + ConsoleColors.RESET);
    }
}