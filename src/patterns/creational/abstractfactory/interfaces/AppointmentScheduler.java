// ============================================================
// FILE     : AppointmentScheduler.java
// PACKAGE  : patterns.creational.abstractfactory.interfaces
// PURPOSE  : Abstract interface for all department
//            appointment scheduling strategies
// PATTERN  : Abstract Factory — Product Interface
// ============================================================

package patterns.creational.abstractfactory.interfaces;

import models.Appointment;
import models.Doctor;
import models.Patient;
import java.util.List;

public interface AppointmentScheduler {

    // ─────────────────────────────────────────
    // Core Scheduling Operations
    // ─────────────────────────────────────────

    // Schedule a new appointment
    Appointment schedule(Patient patient, Doctor doctor, String appointmentTime);

    // Cancel an existing appointment by ID
    boolean cancelAppointment(String appointmentId);

    // Reschedule an existing appointment
    boolean rescheduleAppointment(String appointmentId, String newTime);

    // Check if a doctor is available at given time
    boolean isDoctorAvailable(Doctor doctor, String time);

    // Get all appointments for today
    List<Appointment> getTodaysAppointments();

    // Get appointment by ID
    Appointment getAppointmentById(String appointmentId);

    // Display all scheduled appointments
    void displaySchedule();

    // Get department code
    String getDepartmentCode();
}