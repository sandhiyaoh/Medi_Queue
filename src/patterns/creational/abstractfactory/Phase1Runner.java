// ============================================================
// FILE     : Phase1Runner.java
// PACKAGE  : patterns.creational.abstractfactory
// PURPOSE  : Test runner for Phase 1 — Abstract Factory
//            Demonstrates all three department factories
//            with real Patient, Doctor, and Appointment data
// ============================================================

package patterns.creational.abstractfactory;

import models.Department;
import models.Doctor;
import models.Patient;
import patterns.creational.abstractfactory.client.HospitalDepartment;
import patterns.creational.abstractfactory.factories.EmergencyHospitalFactory;
import patterns.creational.abstractfactory.factories.GeneralHospitalFactory;
import patterns.creational.abstractfactory.factories.VIPHospitalFactory;
import patterns.creational.abstractfactory.interfaces.HospitalFactory;
import utils.ConsolePrinter;
import utils.DateTimeUtils;

public class Phase1Runner {

    public static void run() {

        ConsolePrinter.printMainBanner();

        // ─────────────────────────────────────
        // Sample Doctors
        // ─────────────────────────────────────
        

        Doctor generalDoctor = new Doctor(
            DateTimeUtils.generateDoctorId(1),
            "Ramesh Kumar",              // ← no Dr. here
            "General Physician",
            Department.GENERAL,
            true,
            "9876543001"
        );

        Doctor emergencyDoctor = new Doctor(
            DateTimeUtils.generateDoctorId(2),
            "Priya Menon",               // ← no Dr. here
            "Emergency Specialist",
            Department.EMERGENCY,
            true,
            "9876543002"
        );


        Doctor vipDoctor = new Doctor(
            DateTimeUtils.generateDoctorId(3),
            "Arjun Nair",                // ← no Dr. here
            "Senior Consultant",
            Department.VIP,
            true,
            "9876543003"
        );


        

        // ─────────────────────────────────────
        // Sample Patients
        // ─────────────────────────────────────
        Patient p1 = new Patient(
            DateTimeUtils.generatePatientId(1),
            "Thulasiraman", 21, "9000000001",
            "thulasi@email.com", Department.GENERAL, false,
            DateTimeUtils.getCurrentTimestamp()
        );

        Patient p2 = new Patient(
            DateTimeUtils.generatePatientId(2),
            "Suresh", 23, "9000000002",
            "suresh@email.com", Department.GENERAL, false,
            DateTimeUtils.getCurrentTimestamp()
        );

        Patient p3 = new Patient(
            DateTimeUtils.generatePatientId(3),
            "Aravind", 45, "9000000003",
            "aravind@email.com", Department.EMERGENCY, true,
            DateTimeUtils.getCurrentTimestamp()
        );

        Patient p4 = new Patient(
            DateTimeUtils.generatePatientId(4),
            "Meena", 38, "9000000004",
            "meena@email.com", Department.VIP, false,
            DateTimeUtils.getCurrentTimestamp()
        );

        // ═══════════════════════════════════════
        // GENERAL DEPARTMENT
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("PHASE 1 — GENERAL DEPARTMENT");

        HospitalFactory generalFactory = new GeneralHospitalFactory();
        generalFactory.displayFactoryInfo();

        HospitalDepartment generalDept  = new HospitalDepartment(generalFactory);

        // Admit patients
        generalDept.admitPatient(p1);
        generalDept.admitPatient(p2);

        // Book appointment
        generalDept.bookAppointment(p1, generalDoctor, "10:00 AM");

        // Display queue
        generalDept.displayQueueStatus();

        // Display schedule
        generalDept.displaySchedule();

        // Serve next
        generalDept.serveNextPatient();

        // Display queue after serving
        generalDept.displayQueueStatus();

        // ═══════════════════════════════════════
        // EMERGENCY DEPARTMENT
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("PHASE 1 — EMERGENCY DEPARTMENT");

        HospitalFactory emergencyFactory = new EmergencyHospitalFactory();
        emergencyFactory.displayFactoryInfo();

        HospitalDepartment emergencyDept = new HospitalDepartment(emergencyFactory);

        // Admit emergency patient
        emergencyDept.admitPatient(p3);

        // Book immediate appointment
        emergencyDept.bookAppointment(p3, emergencyDoctor, "IMMEDIATE");

        // Broadcast alert
        emergencyDept.broadcastAlert("Critical patient admitted to Emergency Ward!");

        // Display queue
        emergencyDept.displayQueueStatus();

        // Display schedule
        emergencyDept.displaySchedule();

        // Serve
        emergencyDept.serveNextPatient();

        // ═══════════════════════════════════════
        // VIP DEPARTMENT
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader("PHASE 1 — VIP DEPARTMENT");

        HospitalFactory vipFactory = new VIPHospitalFactory();
        vipFactory.displayFactoryInfo();

        HospitalDepartment vipDept = new HospitalDepartment(vipFactory);

        // Admit VIP patient
        vipDept.admitPatient(p4);

        // Book VIP appointment
        vipDept.bookAppointment(p4, vipDoctor, "02:00 PM");

        // Display queue
        vipDept.displayQueueStatus();

        // Display schedule
        vipDept.displaySchedule();

        // Cancel and reschedule demo
        String aptId = vipDept.getScheduler()
                              .getTodaysAppointments()
                              .get(0)
                              .getAppointmentId();

        vipDept.rescheduleAppointment(p4, aptId, "03:30 PM");

        // Display updated schedule
        vipDept.displaySchedule();

        // Serve
        vipDept.serveNextPatient();

        // ─────────────────────────────────────
        // Final Summary
        // ─────────────────────────────────────
        ConsolePrinter.printSectionHeader("PHASE 1 — ABSTRACT FACTORY COMPLETE");

        ConsolePrinter.printSuccess("General  Factory → GeneralQueue + GeneralScheduler + SMSNotification");
        ConsolePrinter.printSuccess("Emergency Factory → EmergencyQueue + EmergencyScheduler + EmergencyAlertNotification");
        ConsolePrinter.printSuccess("VIP      Factory → PremiumQueue + PriorityScheduler + PremiumNotification");

        ConsolePrinter.printBlankLine();
        ConsolePrinter.printInfo("All three department factories demonstrated successfully.");
        ConsolePrinter.printInfo("Client (HospitalDepartment) used only abstract interfaces — zero concrete coupling.");
        ConsolePrinter.printBlankLine();
    }
}