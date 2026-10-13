// ============================================================
// FILE     : PriorityScheduler.java
// PACKAGE  : patterns.creational.abstractfactory.products.vip
// PURPOSE  : VIP Department Appointment Scheduler
//            Priority scheduling with preferred time slots
// PATTERN  : Abstract Factory — Concrete Product
// ============================================================

package patterns.creational.abstractfactory.products.vip;

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

public class PriorityScheduler implements AppointmentScheduler {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private static final String DEPT_CODE = "VIP";

    private final Map<String, Appointment> appointments;
    private int appointmentCounter;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public PriorityScheduler() {
        this.appointments       = new HashMap<>();
        this.appointmentCounter = 1;
    }

    // ─────────────────────────────────────────
    // Schedule — VIP Preferred Slot
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

        System.out.println(ConsoleColors.BOLD_PURPLE
            + "  [VIP] ★ Priority Appointment → "
            + patient.getName()
            + " with Dr." + doctor.getName()
            + " at " + appointmentTime
            + " [VIP Preferred Slot]"
            + ConsoleColors.RESET);

        return appointment;
    }

    // ─────────────────────────────────────────
    // Cancel Appointment
    // ─────────────────────────────────────────
    @Override
    public boolean cancelAppointment(String appointmentId) {
        Appointment apt = appointments.get(appointmentId);

        if (apt == null) {
            ConsolePrinter.printError("[VIP] Appointment not found : " + appointmentId);
            return false;
        }

        apt.setStatus(Appointment.Status.CANCELLED);
        ConsolePrinter.printWarning("[VIP] VIP Appointment Cancelled → " + appointmentId);
        return true;
    }

    // ─────────────────────────────────────────
    // Reschedule Appointment
    // ─────────────────────────────────────────
    @Override
    public boolean rescheduleAppointment(String appointmentId, String newTime) {
        Appointment apt = appointments.get(appointmentId);

        if (apt == null) {
            ConsolePrinter.printError("[VIP] Appointment not found : " + appointmentId);
            return false;
        }

        apt.setAppointmentTime(newTime);
        apt.setStatus(Appointment.Status.RESCHEDULED);

        System.out.println(ConsoleColors.BOLD_PURPLE
            + "  [VIP] ★ Rescheduled → " + appointmentId
            + " | New Time : " + newTime
            + ConsoleColors.RESET);
        return true;
    }

    // ─────────────────────────────────────────
    // Doctor Availability
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
    // Get By ID
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
        ConsolePrinter.printDepartmentHeader(DEPT_CODE, "VIP DEPARTMENT — Priority Schedule");

        if (appointments.isEmpty()) {
            ConsolePrinter.printInfo("No VIP appointments scheduled.");
            return;
        }

        System.out.println(ConsoleColors.BOLD_PURPLE);
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