// ============================================================
// FILE     : Phase2Runner.java
// PACKAGE  : patterns.structural.facade
// PURPOSE  : Test runner for Phase 2 — Facade Pattern
//            Shows how UI talks ONLY to MediQueueFacade
//            and never directly to subsystems
// ============================================================

package patterns.structural.facade;

import models.Department;
import models.Doctor;
import models.Patient;
import models.Appointment;
import models.Token;
import utils.ConsolePrinter;
import utils.DateTimeUtils;

public class Phase2Runner {

    public static void run() {

        ConsolePrinter.printMainBanner();
        ConsolePrinter.printSectionHeader("PHASE 2 — FACADE PATTERN");

        // ─────────────────────────────────────
        // Step 1 — Create Facade (single entry)
        // ALL complexity hidden inside facade
        // ─────────────────────────────────────
        MediQueueFacade facade = new MediQueueFacade();

        // ═══════════════════════════════════════
        // SCENARIO 1 — Register Doctors
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("SCENARIO 1 — DOCTOR REGISTRATION");

        Doctor generalDoc = new Doctor(
            DateTimeUtils.generateDoctorId(1),
            "Ramesh Kumar", "General Physician",
            Department.GENERAL, true, "9876543001"
        );

        Doctor emergencyDoc = new Doctor(
            DateTimeUtils.generateDoctorId(2),
            "Priya Menon", "Emergency Specialist",
            Department.EMERGENCY, true, "9876543002"
        );

        Doctor vipDoc = new Doctor(
            DateTimeUtils.generateDoctorId(3),
            "Arjun Nair", "Senior Consultant",
            Department.VIP, true, "9876543003"
        );

        facade.registerDoctor(generalDoc);
        facade.registerDoctor(emergencyDoc);
        facade.registerDoctor(vipDoc);

        facade.viewAllDoctors();

        // ═══════════════════════════════════════
        // SCENARIO 2 — Register Patients
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("SCENARIO 2 — PATIENT REGISTRATION");

        Patient p1 = facade.registerPatient(
            "Thulasiraman", 21, "9000000001",
            "thulasi@email.com", Department.GENERAL, false
        );

        Patient p2 = facade.registerPatient(
            "Suresh", 23, "9000000002",
            "suresh@email.com", Department.GENERAL, false
        );

        Patient p3 = facade.registerPatient(
            "Meena", 38, "9000000003",
            "meena@email.com", Department.VIP, false
        );

        Patient p4 = facade.registerPatient(
            "Aravind", 45, "9000000004",
            "aravind@email.com", Department.EMERGENCY, true
        );

        facade.viewAllPatients();

        // ═══════════════════════════════════════
        // SCENARIO 3 — Generate Tokens & Admit
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("SCENARIO 3 — TOKEN GENERATION & QUEUE ADMISSION");

        Token t1 = facade.generateTokenAndAdmit(p1);
        Token t2 = facade.generateTokenAndAdmit(p2);
        Token t3 = facade.generateTokenAndAdmit(p3);

        facade.viewAllTokens();

        // ═══════════════════════════════════════
        // SCENARIO 4 — Book Appointments
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("SCENARIO 4 — APPOINTMENT BOOKING");

        Appointment apt1 = facade.bookAppointment(p1, generalDoc, "10:00 AM");
        Appointment apt2 = facade.bookAppointment(p2, generalDoc, "11:00 AM");
        Appointment apt3 = facade.bookAppointment(p3, vipDoc,     "02:00 PM");

        facade.viewSchedule(Department.GENERAL);
        facade.viewSchedule(Department.VIP);

        // ═══════════════════════════════════════
        // SCENARIO 5 — Cancel & Reschedule
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("SCENARIO 5 — CANCEL & RESCHEDULE");

        // Cancel p2 appointment
        if (apt2 != null) {
            facade.cancelAppointment(p2, apt2.getAppointmentId(), Department.GENERAL);
        }

        // Reschedule p3 appointment
        if (apt3 != null) {
            facade.rescheduleAppointment(
                p3, apt3.getAppointmentId(), "03:30 PM", Department.VIP
            );
        }

        facade.viewSchedule(Department.GENERAL);
        facade.viewSchedule(Department.VIP);

        // ═══════════════════════════════════════
        // SCENARIO 6 — Queue Monitoring
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("SCENARIO 6 — QUEUE MONITORING");

        facade.viewAllQueueStatus();
        facade.viewDepartmentQueue(Department.GENERAL);
        facade.viewDepartmentQueue(Department.VIP);

        // ═══════════════════════════════════════
        // SCENARIO 7 — Wait Time
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("SCENARIO 7 — ESTIMATED WAIT TIME");

        facade.getEstimatedWaitTime(p1);
        facade.getEstimatedWaitTime(p2);
        facade.getEstimatedWaitTime(p3);

        // ═══════════════════════════════════════
        // SCENARIO 8 — Emergency Handling
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("SCENARIO 8 — EMERGENCY HANDLING");

        facade.handleEmergency(p4);
        facade.viewDepartmentQueue(Department.EMERGENCY);
        facade.viewEmergencyLog();

        // ═══════════════════════════════════════
        // SCENARIO 9 — Serve Patients
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("SCENARIO 9 — SERVING PATIENTS");

        facade.serveNextPatient(Department.GENERAL);
        facade.serveNextPatient(Department.EMERGENCY);
        facade.serveNextPatient(Department.VIP);

        facade.viewAllQueueStatus();

        // ═══════════════════════════════════════
        // SCENARIO 10 — System Summary
        // ═══════════════════════════════════════
        facade.viewSystemSummary();

        // ─────────────────────────────────────
        // Facade Complete
        // ─────────────────────────────────────
        ConsolePrinter.printSectionHeader("PHASE 2 — FACADE PATTERN COMPLETE");

        ConsolePrinter.printSuccess("MediQueueFacade acted as single entry point");
        ConsolePrinter.printSuccess("UI never touched subsystems directly");
        ConsolePrinter.printSuccess("All 5 subsystems hidden behind Facade");
        ConsolePrinter.printSuccess("Abstract Factory (Phase 1) reused inside Facade");
        ConsolePrinter.printInfo("Phase 2 — Facade Pattern Verified Successfully.");
        ConsolePrinter.printBlankLine();
    }
}