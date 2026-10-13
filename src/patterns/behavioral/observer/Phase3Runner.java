// ============================================================
// FILE     : Phase3Runner.java
// PACKAGE  : patterns.behavioral.observer
// PURPOSE  : Test runner for Phase 3 — Observer Pattern
//            Shows real-time queue notifications firing
//            to Patient, Doctor, and Admin observers
//            Uses MediQueueFacade (Phase 2) which uses
//            Abstract Factories (Phase 1) internally
// ============================================================

package patterns.behavioral.observer;

import models.Department;
import models.Doctor;
import models.Patient;
import patterns.behavioral.observer.observers.AdminObserver;
import patterns.behavioral.observer.observers.DoctorObserver;
import patterns.behavioral.observer.observers.PatientObserver;
import patterns.behavioral.observer.subject.LiveQueueSubject;
import patterns.structural.facade.MediQueueFacade;
import utils.ConsolePrinter;
import utils.DateTimeUtils;

public class Phase3Runner {

    public static void run() {

        ConsolePrinter.printMainBanner();
        ConsolePrinter.printSectionHeader(
            "PHASE 3 — OBSERVER PATTERN"
        );

        // ═══════════════════════════════════════
        // SETUP — Initialize Facade + Subject
        // ═══════════════════════════════════════

        // Phase 2 Facade — hides Phase 1 factories
        MediQueueFacade facade = new MediQueueFacade();

        // Phase 3 Subject — the Observable
        LiveQueueSubject queueSubject = new LiveQueueSubject(facade);

        // ═══════════════════════════════════════
        // SCENARIO 1 — Register Doctors
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader(
            "SCENARIO 1 — SETUP DOCTORS"
        );

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

        // ═══════════════════════════════════════
        // SCENARIO 2 — Register Patients
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader(
            "SCENARIO 2 — REGISTER PATIENTS"
        );

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

        Patient p5 = facade.registerPatient(
            "Kavya", 30, "9000000005",
            "kavya@email.com", Department.GENERAL, false
        );

        // ═══════════════════════════════════════
        // SCENARIO 3 — Register Observers
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader(
            "SCENARIO 3 — REGISTER OBSERVERS"
        );

        // Patient Observers — each patient watches own queue
        PatientObserver patientObs1 = new PatientObserver(p1);
        PatientObserver patientObs2 = new PatientObserver(p2);
        PatientObserver patientObs3 = new PatientObserver(p3);
        PatientObserver patientObs5 = new PatientObserver(p5);

        // Doctor Observers — each doctor watches own dept
        DoctorObserver doctorObs1 = new DoctorObserver(generalDoc);
        DoctorObserver doctorObs2 = new DoctorObserver(emergencyDoc);
        DoctorObserver doctorObs3 = new DoctorObserver(vipDoc);

        // Admin Observer — watches ALL departments
        AdminObserver adminObs = new AdminObserver("Admin-Senthil");

        // Register all observers to subject
        queueSubject.registerObserver(patientObs1);
        queueSubject.registerObserver(patientObs2);
        queueSubject.registerObserver(patientObs3);
        queueSubject.registerObserver(patientObs5);
        queueSubject.registerObserver(doctorObs1);
        queueSubject.registerObserver(doctorObs2);
        queueSubject.registerObserver(doctorObs3);
        queueSubject.registerObserver(adminObs);

        // Display all registered observers
        queueSubject.displayObservers();

        // ═══════════════════════════════════════
        // SCENARIO 4 — Admit Patients
        //              Observer fires on each add
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader(
            "SCENARIO 4 — ADMIT PATIENTS (Observer Fires)"
        );

        ConsolePrinter.printInfo(
            "Adding Thulasiraman to General Queue..."
        );
        queueSubject.admitPatientWithNotification(p1);

        ConsolePrinter.printBlankLine();
        ConsolePrinter.printInfo(
            "Adding Suresh to General Queue..."
        );
        queueSubject.admitPatientWithNotification(p2);

        ConsolePrinter.printBlankLine();
        ConsolePrinter.printInfo(
            "Adding Meena to VIP Queue..."
        );
        queueSubject.admitPatientWithNotification(p3);

        ConsolePrinter.printBlankLine();
        ConsolePrinter.printInfo(
            "Adding Kavya to General Queue..."
        );
        queueSubject.admitPatientWithNotification(p5);

        // ═══════════════════════════════════════
        // SCENARIO 5 — Live Queue Status Check
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader(
            "SCENARIO 5 — LIVE QUEUE STATUS"
        );

        facade.viewAllQueueStatus();
        facade.viewDepartmentQueue(Department.GENERAL);
        facade.viewDepartmentQueue(Department.VIP);

        // ═══════════════════════════════════════
        // SCENARIO 6 — Serve Patient
        //              Observer fires on serve
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader(
            "SCENARIO 6 — SERVE PATIENT (Observer Fires)"
        );

        ConsolePrinter.printInfo(
            "Serving next patient in General Department..."
        );
        queueSubject.serveNextWithNotification(Department.GENERAL);

        ConsolePrinter.printBlankLine();
        ConsolePrinter.printInfo(
            "Serving next patient in VIP Department..."
        );
        queueSubject.serveNextWithNotification(Department.VIP);

        // ═══════════════════════════════════════
        // SCENARIO 7 — Emergency Case
        //              All observers notified
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader(
            "SCENARIO 7 — EMERGENCY CASE (All Observers Alert)"
        );

        ConsolePrinter.printInfo(
            "Emergency patient Aravind incoming..."
        );
        queueSubject.triggerEmergencyWithNotification(p4);

        // ═══════════════════════════════════════
        // SCENARIO 8 — Remove Observer + Notify
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader(
            "SCENARIO 8 — REMOVE OBSERVER"
        );

        // Suresh leaves — remove his observer
        queueSubject.removeObserver(patientObs2);
        queueSubject.displayObservers();

        // Serve next — Suresh observer should NOT fire now
        ConsolePrinter.printInfo(
            "Serving General queue (Suresh observer removed)..."
        );
        queueSubject.serveNextWithNotification(Department.GENERAL);

        // ═══════════════════════════════════════
        // SCENARIO 9 — Final System Summary
        // ═══════════════════════════════════════
        ConsolePrinter.printSectionHeader(
            "SCENARIO 9 — FINAL SYSTEM SUMMARY"
        );

        facade.viewAllQueueStatus();
        facade.viewSystemSummary();

        // ─────────────────────────────────────
        // Phase 3 Complete
        // ─────────────────────────────────────
        ConsolePrinter.printSectionHeader(
            "PHASE 3 — OBSERVER PATTERN COMPLETE"
        );

        ConsolePrinter.printSuccess(
            "LiveQueueSubject managed all observer notifications"
        );
        ConsolePrinter.printSuccess(
            "PatientObserver → notified when own queue changes"
        );
        ConsolePrinter.printSuccess(
            "DoctorObserver  → notified about next patient"
        );
        ConsolePrinter.printSuccess(
            "AdminObserver   → monitors ALL departments"
        );
        ConsolePrinter.printSuccess(
            "Observer removed → stops receiving notifications"
        );
        ConsolePrinter.printSuccess(
            "Phase 1 (Factory) + Phase 2 (Facade) + Phase 3 (Observer) working together"
        );
        ConsolePrinter.printBlankLine();
        ConsolePrinter.printInfo(
            "All 3 Design Patterns Integrated Successfully!"
        );
        ConsolePrinter.printBlankLine();
    }
}