// ============================================================
// FILE     : GeneralScheduler.java
// PACKAGE  : patterns.creational.abstractfactory.products.general
// PURPOSE  : General Department Appointment Scheduler
//            Standard scheduling — first-available slot
// PATTERN  : Abstract Factory — Concrete Product
// ============================================================

package patterns.creational.abstractfactory.products.general;

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

public class GeneralScheduler implements AppointmentScheduler {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private static final String DEPT_CODE = "GEN";

    private final Map<String, Appointment> appointments;
    private int appointmentCounter;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public GeneralScheduler() {
        this.appointments       = new HashMap<>();
        this.appointmentCounter = 1;
    }

    // ─────────────────────────────────────────
    // Schedule Appointment
    // ─────────────────────────────────────────
    @Override
    public Appointment schedule(Patient patient, Doctor doctor, String appointmentTime) {

        if (!isDoctorAvailable(doctor, appointmentTime)) {
            ConsolePrinter.printError(
                "[GENERAL] Doctor " + doctor.getName() + " is not available at " + appointmentTime
            );
            return null;
        }

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

        ConsolePrinter.printSuccess(
            ConsoleColors.BOLD_GREEN
            + "[GENERAL] Appointment Scheduled → "
            + patient.getName()
            + " with Dr. " + doctor.getName()
            + " at " + appointmentTime
            + ConsoleColors.RESET
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
            ConsolePrinter.printError("[GENERAL] Appointment ID not found : " + appointmentId);
            return false;
        }

        apt.setStatus(Appointment.Status.CANCELLED);
        ConsolePrinter.printWarning(
            "[GENERAL] Appointment Cancelled → ID : " + appointmentId
        );
        return true;
    }

    // ─────────────────────────────────────────
    // Reschedule Appointment
    // ─────────────────────────────────────────
    @Override
    public boolean rescheduleAppointment(String appointmentId, String newTime) {
        Appointment apt = appointments.get(appointmentId);

        if (apt == null) {
            ConsolePrinter.printError("[GENERAL] Appointment ID not found : " + appointmentId);
            return false;
        }

        apt.setAppointmentTime(newTime);
        apt.setStatus(Appointment.Status.RESCHEDULED);

        ConsolePrinter.printSuccess(
            "[GENERAL] Appointment Rescheduled → ID : "
            + appointmentId + " | New Time : " + newTime
        );
        return true;
    }

    // ─────────────────────────────────────────
    // Doctor Availability Check
    // ─────────────────────────────────────────
    @Override
    public boolean isDoctorAvailable(Doctor doctor, String time) {
        if (!doctor.isAvailable()) return false;

        for (Appointment apt : appointments.values()) {
            if (apt.getDoctor().getDoctorId().equals(doctor.getDoctorId())
                    && apt.getAppointmentTime().equals(time)
                    && apt.getStatus() == Appointment.Status.BOOKED) {
                return false;
            }
        }
        return true;
    }

    // ─────────────────────────────────────────
    // Get Today's Appointments
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
        ConsolePrinter.printDepartmentHeader(DEPT_CODE, "GENERAL DEPARTMENT — Appointment Schedule");

        if (appointments.isEmpty()) {
            ConsolePrinter.printInfo("No appointments scheduled.");
            return;
        }

        System.out.println(ConsoleColors.BOLD_GREEN);
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