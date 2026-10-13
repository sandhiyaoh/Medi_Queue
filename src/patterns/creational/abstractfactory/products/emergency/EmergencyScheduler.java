// ============================================================
// FILE     : EmergencyScheduler.java
// PACKAGE  : patterns.creational.abstractfactory.products.emergency
// PURPOSE  : Emergency Department Appointment Scheduler
//            Immediate scheduling — override any slot
// PATTERN  : Abstract Factory — Concrete Product
// ============================================================

package patterns.creational.abstractfactory.products.emergency;

import models.Appointment;
import models.Doctor;
import models.Patient;
import patterns.creational.abstractfactory.interfaces.AppointmentScheduler;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmergencyScheduler implements AppointmentScheduler {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private static final String DEPT_CODE = "EMG";

    private final Map<String, Appointment> appointments;
    private int appointmentCounter;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public EmergencyScheduler() {
        this.appointments       = new HashMap<>();
        this.appointmentCounter = 1;
    }

    // ─────────────────────────────────────────
    // Schedule — Immediate Override
    // Emergency ignores slot conflicts
    // ─────────────────────────────────────────
    @Override
    public Appointment schedule(Patient patient, Doctor doctor, String appointmentTime) {

        String appointmentId = DateTimeUtils.generateAppointmentId(appointmentCounter++);

        Appointment appointment = new Appointment(
            appointmentId,
            patient,
            doctor,
            patient.getDepartment(),
            appointmentTime,
            DateTimeUtils.getCurrentTimestamp()
        );

        appointments.put(appointmentId, appointment);

        ConsolePrinter.printEmergencyAlert(
            "[EMERGENCY] IMMEDIATE Appointment → "
            + patient.getName()
            + " with Dr." + doctor.getName()
            + " at " + appointmentTime
            + " [OVERRIDE — Emergency Priority]"
        );

        return appointment;
    }

    // ─────────────────────────────────────────
    // Cancel Appointment
    // ─────────────────────────────────────────
    @Override
    public boolean cancelAppointment(String appointmentId) {
        Appointment apt = appointments.get(appointmentId);

        if (apt == null) {
            ConsolePrinter.printError("[EMERGENCY] Appointment not found : " + appointmentId);
            return false;
        }

        apt.setStatus(Appointment.Status.CANCELLED);
        ConsolePrinter.printWarning("[EMERGENCY] Appointment Cancelled → " + appointmentId);
        return true;
    }

    // ─────────────────────────────────────────
    // Reschedule Appointment
    // ─────────────────────────────────────────
    @Override
    public boolean rescheduleAppointment(String appointmentId, String newTime) {
        Appointment apt = appointments.get(appointmentId);

        if (apt == null) {
            ConsolePrinter.printError("[EMERGENCY] Appointment not found : " + appointmentId);
            return false;
        }

        apt.setAppointmentTime(newTime);
        apt.setStatus(Appointment.Status.RESCHEDULED);

        ConsolePrinter.printEmergencyAlert(
            "[EMERGENCY] Rescheduled → " + appointmentId + " | New Time : " + newTime
        );
        return true;
    }

    // ─────────────────────────────────────────
    // Doctor Availability
    // Emergency always allows scheduling
    // ─────────────────────────────────────────
    @Override
    public boolean isDoctorAvailable(Doctor doctor, String time) {
        return doctor.isAvailable(); // Emergency overrides slot conflicts
    }

    // ─────────────────────────────────────────
    // Today's Appointments
    // ─────────────────────────────────────────
    @Override
    public List<Appointment> getTodaysAppointments() {
        List<Appointment> result = new ArrayList<>();
        String today = DateTimeUtils.getCurrentDate();

        for (Appointment apt : appointments.values()) {
            if (apt.getBookedAt().startsWith(today)) {
                result.add(apt);
            }
        }
        return result;
    }

    // ─────────────────────────────────────────
    // Get Appointment By ID
    // ─────────────────────────────────────────
    @Override
    public Appointment getAppointmentById(String appointmentId) {
        return appointments.get(appointmentId);
    }

    // ─────────────────────────────────────────
    // Display Schedule
    // ─────────────────────────────────────────
    @Override
    public void displaySchedule() {
        ConsolePrinter.printDepartmentHeader(DEPT_CODE, "EMERGENCY DEPARTMENT — Appointment Schedule");

        if (appointments.isEmpty()) {
            ConsolePrinter.printInfo("No emergency appointments scheduled.");
            return;
        }

        System.out.println(ConsoleColors.BOLD_RED);
        System.out.printf("  %-20s %-18s %-18s %-15s %-12s%n",
            "Apt ID", "Patient", "Doctor", "Time", "Status");
        System.out.println(ConsolePrinter.printLine("-"));

        for (Appointment apt : appointments.values()) {
            System.out.printf("  %-20s %-18s %-18s %-15s %-12s%n",
                apt.getAppointmentId(),
                apt.getPatient().getName(),
                apt.getDoctor().getName(),
                apt.getAppointmentTime(),
                apt.getStatus()
            );
        }
        System.out.println(ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Department Code
    // ─────────────────────────────────────────
    @Override
    public String getDepartmentCode() {
        return DEPT_CODE;
    }
}