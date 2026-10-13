// ============================================================
// FILE     : NotificationService.java
// PACKAGE  : patterns.creational.abstractfactory.interfaces
// PURPOSE  : Abstract interface for all department
//            patient & doctor notification strategies
// PATTERN  : Abstract Factory — Product Interface
// ============================================================

package patterns.creational.abstractfactory.interfaces;

import models.Doctor;
import models.Patient;
import models.Token;

public interface NotificationService {

    // ─────────────────────────────────────────
    // Core Notification Operations
    // ─────────────────────────────────────────

    // Notify patient with a custom message
    void notifyPatient(Patient patient, String message);

    // Notify doctor with a custom message
    void notifyDoctor(Doctor doctor, String message);

    // Notify patient when token is generated
    void notifyTokenGenerated(Patient patient, Token token);

    // Notify patient about appointment confirmation
    void notifyAppointmentBooked(Patient patient, String appointmentTime);

    // Notify patient about cancellation
    void notifyAppointmentCancelled(Patient patient, String appointmentId);

    // Notify patient when it is their turn
    void notifyPatientTurn(Patient patient, int tokenNumber);

    // Notify all in case of emergency
    void broadcastEmergencyAlert(String message);

    // Get notification channel type
    String getNotificationChannel();
}