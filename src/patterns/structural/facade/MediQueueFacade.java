// ============================================================
// FILE     : MediQueueFacade.java
// PACKAGE  : patterns.structural.facade
// PURPOSE  : FACADE — Single unified entry point for entire
//            MediQueue system. Hides all subsystem complexity.
//            UI layer talks ONLY to this class.
// PATTERN  : Facade — Main Facade Class
// ============================================================

package patterns.structural.facade;

import models.Appointment;
import models.Department;
import models.Doctor;
import models.Patient;
import models.Token;
import patterns.creational.abstractfactory.client.HospitalDepartment;
import patterns.creational.abstractfactory.factories.EmergencyHospitalFactory;
import patterns.creational.abstractfactory.factories.GeneralHospitalFactory;
import patterns.creational.abstractfactory.factories.VIPHospitalFactory;
import patterns.structural.facade.subsystems.AppointmentService;
import patterns.structural.facade.subsystems.EmergencyHandlerService;
import patterns.structural.facade.subsystems.PatientRegistrationService;
import patterns.structural.facade.subsystems.TokenGeneratorService;
import patterns.structural.facade.subsystems.WaitTimeCalculatorService;
import utils.ConsolePrinter;
import utils.ConsoleColors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediQueueFacade {

    // ─────────────────────────────────────────
    // Department Map — from Phase 1 Factories
    // ─────────────────────────────────────────
    private final Map<String, HospitalDepartment> departments;

    // ─────────────────────────────────────────
    // Subsystem References
    // ─────────────────────────────────────────
    private final PatientRegistrationService registrationService;
    private final TokenGeneratorService      tokenService;
    private final AppointmentService         appointmentService;
    private final EmergencyHandlerService    emergencyService;
    private final WaitTimeCalculatorService  waitTimeService;

    // ─────────────────────────────────────────
    // Constructor — Initializes ALL subsystems
    //               and ALL department factories
    // ─────────────────────────────────────────
    public MediQueueFacade() {

        // ── Step 1: Build Department Map using Abstract Factories ──
        this.departments = new HashMap<>();
        departments.put("GEN", new HospitalDepartment(new GeneralHospitalFactory()));
        departments.put("EMG", new HospitalDepartment(new EmergencyHospitalFactory()));
        departments.put("VIP", new HospitalDepartment(new VIPHospitalFactory()));

        // ── Step 2: Initialize all Subsystems ──
        this.registrationService = new PatientRegistrationService();
        this.tokenService        = new TokenGeneratorService();
        this.appointmentService  = new AppointmentService(departments);
        this.emergencyService    = new EmergencyHandlerService(departments);
        this.waitTimeService     = new WaitTimeCalculatorService(departments);

        System.out.println(ConsoleColors.BOLD_CYAN
            + "\n  [FACADE] MediQueueFacade Initialized"
            + "\n  Subsystems Ready:"
            + "\n    ✔ PatientRegistrationService"
            + "\n    ✔ TokenGeneratorService"
            + "\n    ✔ AppointmentService"
            + "\n    ✔ EmergencyHandlerService"
            + "\n    ✔ WaitTimeCalculatorService"
            + "\n  Departments Ready:"
            + "\n    ✔ General Department"
            + "\n    ✔ Emergency Department"
            + "\n    ✔ VIP Department"
            + ConsoleColors.RESET
        );
    }

    // ═════════════════════════════════════════
    //   PATIENT OPERATIONS
    // ═════════════════════════════════════════

    // ─────────────────────────────────────────
    // Register Patient
    // ─────────────────────────────────────────
    public Patient registerPatient(String name,
                                   int age,
                                   String contact,
                                   String email,
                                   Department department,
                                   boolean isEmergency) {

        ConsolePrinter.printSectionHeader("REGISTER PATIENT");
        return registrationService.registerPatient(
            name, age, contact, email, department, isEmergency
        );
    }

    // ─────────────────────────────────────────
    // Get Patient By ID
    // ─────────────────────────────────────────
    public Patient getPatientById(String patientId) {
        return registrationService.getPatientById(patientId);
    }

    // ─────────────────────────────────────────
    // View All Patients
    // ─────────────────────────────────────────
    public void viewAllPatients() {
        registrationService.displayAllPatients();
    }

    // ═════════════════════════════════════════
    //   DOCTOR OPERATIONS
    // ═════════════════════════════════════════

    // ─────────────────────────────────────────
    // Register Doctor
    // ─────────────────────────────────────────
    public void registerDoctor(Doctor doctor) {
        ConsolePrinter.printSectionHeader("REGISTER DOCTOR");
        appointmentService.registerDoctor(doctor);
    }

    // ─────────────────────────────────────────
    // View All Doctors
    // ─────────────────────────────────────────
    public void viewAllDoctors() {
        appointmentService.displayAllDoctors();
    }

    // ─────────────────────────────────────────
    // Get Available Doctors
    // ─────────────────────────────────────────
    public List<Doctor> getAvailableDoctors(Department department) {
        return appointmentService.getAvailableDoctors(department);
    }

    // ═════════════════════════════════════════
    //   TOKEN OPERATIONS
    // ═════════════════════════════════════════

    // ─────────────────────────────────────────
    // Generate Token + Admit to Queue
    // ─────────────────────────────────────────
    public Token generateTokenAndAdmit(Patient patient) {
        ConsolePrinter.printSectionHeader("GENERATE TOKEN & ADMIT TO QUEUE");

        // Step 1 — Generate Token
        Token token = tokenService.generateToken(patient);

        // Step 2 — Admit Patient to Department Queue
        HospitalDepartment dept = departments.get(patient.getDepartment().getCode());
        if (dept != null) {
            dept.admitPatient(patient);
        }

        return token;
    }

    // ─────────────────────────────────────────
    // View All Tokens
    // ─────────────────────────────────────────
    public void viewAllTokens() {
        tokenService.displayAllTokens();
    }

    // ═════════════════════════════════════════
    //   APPOINTMENT OPERATIONS
    // ═════════════════════════════════════════

    // ─────────────────────────────────────────
    // Book Appointment
    // ─────────────────────────────────────────
    public Appointment bookAppointment(Patient patient,
                                       Doctor doctor,
                                       String time) {
        ConsolePrinter.printSectionHeader("BOOK APPOINTMENT");
        return appointmentService.bookAppointment(patient, doctor, time);
    }

    // ─────────────────────────────────────────
    // Cancel Appointment
    // ─────────────────────────────────────────
    public void cancelAppointment(Patient patient,
                                  String appointmentId,
                                  Department department) {
        ConsolePrinter.printSectionHeader("CANCEL APPOINTMENT");
        appointmentService.cancelAppointment(patient, appointmentId, department);
    }

    // ─────────────────────────────────────────
    // Reschedule Appointment
    // ─────────────────────────────────────────
    public void rescheduleAppointment(Patient patient,
                                      String appointmentId,
                                      String newTime,
                                      Department department) {
        ConsolePrinter.printSectionHeader("RESCHEDULE APPOINTMENT");
        appointmentService.rescheduleAppointment(
            patient, appointmentId, newTime, department
        );
    }

    // ─────────────────────────────────────────
    // View Schedule By Department
    // ─────────────────────────────────────────
    public void viewSchedule(Department department) {
        appointmentService.displayDepartmentSchedule(department);
    }

    // ═════════════════════════════════════════
    //   QUEUE OPERATIONS
    // ═════════════════════════════════════════

    // ─────────────────────────────────────────
    // View Queue Status — All Departments
    // ─────────────────────────────────────────
    public void viewAllQueueStatus() {
        ConsolePrinter.printSectionHeader("ALL DEPARTMENT QUEUE STATUS");
        waitTimeService.displayAllDepartmentStatus();
    }

    // ─────────────────────────────────────────
    // View Queue — Specific Department
    // ─────────────────────────────────────────
    public void viewDepartmentQueue(Department department) {
        waitTimeService.displayDepartmentQueue(department);
    }

    // ─────────────────────────────────────────
    // Get Estimated Wait Time
    // ─────────────────────────────────────────
    public int getEstimatedWaitTime(Patient patient) {
        ConsolePrinter.printSectionHeader("ESTIMATED WAIT TIME");
        return waitTimeService.getWaitTime(patient);
    }

    // ─────────────────────────────────────────
    // Serve Next Patient
    // ─────────────────────────────────────────
    public Patient serveNextPatient(Department department) {
        ConsolePrinter.printSectionHeader("SERVE NEXT PATIENT — "
            + department.getDisplayName());

        HospitalDepartment dept = departments.get(department.getCode());
        if (dept != null) {
            return dept.serveNextPatient();
        }

        ConsolePrinter.printError(
            "[QUEUE] Department not found : " + department.getCode()
        );
        return null;
    }

    // ═════════════════════════════════════════
    //   EMERGENCY OPERATIONS
    // ═════════════════════════════════════════

    // ─────────────────────────────────────────
    // Handle Emergency
    // ─────────────────────────────────────────
    public void handleEmergency(Patient patient) {
        ConsolePrinter.printSectionHeader("EMERGENCY HANDLER");
        emergencyService.handleEmergency(patient);
    }

    // ─────────────────────────────────────────
    // Broadcast Emergency Alert
    // ─────────────────────────────────────────
    public void broadcastAlert(String message) {
        emergencyService.broadcastToAllDepartments(message);
    }

    // ─────────────────────────────────────────
    // View Emergency Log
    // ─────────────────────────────────────────
    public void viewEmergencyLog() {
        emergencyService.displayEmergencyLog();
    }

    // ═════════════════════════════════════════
    //   SYSTEM SUMMARY
    // ═════════════════════════════════════════

    // ─────────────────────────────────────────
    // Display Full System Summary
    // ─────────────────────────────────────────
    public void viewSystemSummary() {
        ConsolePrinter.printSectionHeader("MEDIQUEUE SYSTEM SUMMARY");

        System.out.println(ConsoleColors.BOLD_CYAN);
        System.out.println(ConsolePrinter.printLine("="));
        System.out.println(ConsolePrinter.center("SYSTEM STATISTICS"));
        System.out.println(ConsolePrinter.printLine("="));

        System.out.printf("  %-35s : %d%n",
            "Total Registered Patients",
            registrationService.getTotalRegisteredCount());

        System.out.printf("  %-35s : %d%n",
            "Total Tokens Issued",
            tokenService.getTotalTokensIssued());

        System.out.printf("  %-35s : %d%n",
            "Total Emergency Cases",
            emergencyService.getTotalEmergencyCases());

        System.out.printf("  %-35s : %d%n",
            "General Queue Size",
            departments.get("GEN").getQueueManager().getQueueSize());

        System.out.printf("  %-35s : %d%n",
            "Emergency Queue Size",
            departments.get("EMG").getQueueManager().getQueueSize());

        System.out.printf("  %-35s : %d%n",
            "VIP Queue Size",
            departments.get("VIP").getQueueManager().getQueueSize());

        System.out.println(ConsolePrinter.printLine("="));
        System.out.println(ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Expose Department Map (for Phase 3)
    // ─────────────────────────────────────────
    public Map<String, HospitalDepartment> getDepartments() {
        return departments;
    }
}