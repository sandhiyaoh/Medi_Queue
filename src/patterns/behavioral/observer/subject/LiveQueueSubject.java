// ============================================================
// FILE     : LiveQueueSubject.java
// PACKAGE  : patterns.behavioral.observer.subject
// PURPOSE  : Concrete Subject (Observable)
//            Manages all registered observers and
//            fires notifications on every queue event
//            Integrates with MediQueueFacade (Phase 2)
// PATTERN  : Observer — Concrete Subject
// ============================================================

package patterns.behavioral.observer.subject;

import models.Department;
import models.Patient;
import models.QueueEntry;
import patterns.behavioral.observer.interfaces.QueueObserver;
import patterns.behavioral.observer.interfaces.QueueSubject;
import patterns.creational.abstractfactory.client.HospitalDepartment;
import patterns.structural.facade.MediQueueFacade;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LiveQueueSubject implements QueueSubject {

    // ─────────────────────────────────────────
    // Registered Observers
    // ─────────────────────────────────────────
    private final List<QueueObserver> observers;

    // ─────────────────────────────────────────
    // Facade Reference — to access departments
    // ─────────────────────────────────────────
    private final MediQueueFacade facade;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public LiveQueueSubject(MediQueueFacade facade) {
        this.observers = new ArrayList<>();
        this.facade    = facade;

        System.out.println(ConsoleColors.BOLD_CYAN
            + "\n  [OBSERVER] LiveQueueSubject Initialized"
            + "\n  Ready to register observers and"
            + "\n  broadcast queue events in real-time."
            + ConsoleColors.RESET
        );
    }

    // ─────────────────────────────────────────
    // Register Observer
    // ─────────────────────────────────────────
    @Override
    public void registerObserver(QueueObserver observer) {
        observers.add(observer);
        ConsolePrinter.printSuccess(
            "[OBSERVER] Registered → " + observer.getObserverName()
        );
    }

    // ─────────────────────────────────────────
    // Remove Observer
    // ─────────────────────────────────────────
    @Override
    public void removeObserver(QueueObserver observer) {
        observers.remove(observer);
        ConsolePrinter.printWarning(
            "[OBSERVER] Removed → " + observer.getObserverName()
        );
    }

    // ─────────────────────────────────────────
    // Notify All — Queue Updated
    // ─────────────────────────────────────────
    @Override
    public void notifyQueueUpdated(String departmentCode) {

        List<QueueEntry> currentQueue = getQueueEntries(departmentCode);

        System.out.println(ConsoleColors.BOLD_CYAN
            + "\n  [SUBJECT] Notifying all observers"
            + " → Queue Updated [" + departmentCode + "]"
            + " | Observers : " + observers.size()
            + ConsoleColors.RESET
        );

        for (QueueObserver observer : observers) {
            observer.onQueueUpdated(departmentCode, currentQueue);
        }
    }

    // ─────────────────────────────────────────
    // Notify All — Patient Added
    // ─────────────────────────────────────────
    @Override
    public void notifyPatientAdded(String departmentCode,
                                   QueueEntry newEntry) {

        System.out.println(ConsoleColors.BOLD_CYAN
            + "\n  [SUBJECT] Notifying all observers"
            + " → Patient Added [" + departmentCode + "]"
            + " | Patient : " + newEntry.getPatient().getName()
            + ConsoleColors.RESET
        );

        for (QueueObserver observer : observers) {
            observer.onPatientAdded(departmentCode, newEntry);
        }
    }

    // ─────────────────────────────────────────
    // Notify All — Patient Served
    // ─────────────────────────────────────────
    @Override
    public void notifyPatientServed(String departmentCode,
                                    QueueEntry servedEntry) {

        System.out.println(ConsoleColors.BOLD_CYAN
            + "\n  [SUBJECT] Notifying all observers"
            + " → Patient Served [" + departmentCode + "]"
            + " | Patient : " + servedEntry.getPatient().getName()
            + ConsoleColors.RESET
        );

        for (QueueObserver observer : observers) {
            observer.onPatientServed(departmentCode, servedEntry);
        }
    }

    // ─────────────────────────────────────────
    // Notify All — Emergency Alert
    // ─────────────────────────────────────────
    @Override
    public void notifyEmergencyAlert(String departmentCode,
                                     QueueEntry emergencyEntry) {

        ConsolePrinter.printEmergencyAlert(
            "[SUBJECT] EMERGENCY BROADCAST → All "
            + observers.size() + " observers notified!"
        );

        for (QueueObserver observer : observers) {
            observer.onEmergencyAlert(departmentCode, emergencyEntry);
        }
    }

    // ─────────────────────────────────────────
    // Admit Patient with Observer Notification
    // ─────────────────────────────────────────
    public void admitPatientWithNotification(Patient patient) {

        String deptCode = patient.getDepartment().getCode();

        // Admit through facade
        Map<String, HospitalDepartment> departments =
            facade.getDepartments();

        HospitalDepartment dept = departments.get(deptCode);

        if (dept == null) {
            ConsolePrinter.printError(
                "[OBSERVER] Department not found: " + deptCode
            );
            return;
        }

        // Add to queue
        dept.getQueueManager().addPatientToQueue(patient);

        // Get the just-added entry (last in queue entries)
        List<QueueEntry> entries =
            dept.getQueueManager().getQueueEntries();

        if (!entries.isEmpty()) {
            QueueEntry newEntry = entries.get(entries.size() - 1);

            // Fire observer notifications
            if (patient.isEmergency()) {
                notifyEmergencyAlert(deptCode, newEntry);
            } else {
                notifyPatientAdded(deptCode, newEntry);
            }

            // Always notify queue updated
            notifyQueueUpdated(deptCode);
        }
    }

    // ─────────────────────────────────────────
    // Serve Next Patient with Observer Notification
    // ─────────────────────────────────────────
    public Patient serveNextWithNotification(Department department) {

        String deptCode = department.getCode();
        Map<String, HospitalDepartment> departments =
            facade.getDepartments();

        HospitalDepartment dept = departments.get(deptCode);

        if (dept == null) {
            ConsolePrinter.printError(
                "[OBSERVER] Department not found: " + deptCode
            );
            return null;
        }

        if (dept.getQueueManager().isQueueEmpty()) {
            ConsolePrinter.printWarning(
                "[OBSERVER] Queue is empty — " + deptCode
            );
            return null;
        }

        // Capture entry BEFORE serving
        QueueEntry servedEntry = dept.getQueueManager()
                                     .getQueueEntries()
                                     .get(0);

        // Serve patient
        Patient served = dept.getQueueManager().serveNextPatient();

        if (served != null) {
            // Notify all observers — patient served
            notifyPatientServed(deptCode, servedEntry);

            // Notify queue updated
            notifyQueueUpdated(deptCode);
        }

        return served;
    }

    // ─────────────────────────────────────────
    // Trigger Emergency with Observer Notification
    // ─────────────────────────────────────────
    public void triggerEmergencyWithNotification(Patient patient) {

        // Handle via facade
        facade.handleEmergency(patient);

        // Get emergency queue entry
        Map<String, HospitalDepartment> departments =
            facade.getDepartments();

        HospitalDepartment emgDept = departments.get("EMG");

        if (emgDept != null
                && !emgDept.getQueueManager().isQueueEmpty()) {

            List<QueueEntry> entries =
                emgDept.getQueueManager().getQueueEntries();

            QueueEntry emergencyEntry = entries.get(entries.size() - 1);

            // Fire emergency notification to ALL observers
            notifyEmergencyAlert("EMG", emergencyEntry);

            // Also notify General and VIP (cross-department alert)
            notifyEmergencyAlert("GEN", emergencyEntry);
            notifyEmergencyAlert("VIP", emergencyEntry);
        }
    }

    // ─────────────────────────────────────────
    // Display All Registered Observers
    // ─────────────────────────────────────────
    public void displayObservers() {
        ConsolePrinter.printSectionHeader("REGISTERED OBSERVERS");

        System.out.println(ConsoleColors.BOLD_CYAN);
        System.out.printf("  %-5s %-45s%n", "No.", "Observer");
        System.out.println(ConsolePrinter.printLine("-"));

        for (int i = 0; i < observers.size(); i++) {
            System.out.printf("  %-5d %-45s%n",
                (i + 1),
                observers.get(i).getObserverName()
            );
        }
        System.out.println(ConsoleColors.RESET);
        ConsolePrinter.printInfo(
            "Total Observers : " + observers.size()
        );
    }

    // ─────────────────────────────────────────
    // Get Total Observer Count
    // ─────────────────────────────────────────
    public int getObserverCount() {
        return observers.size();
    }

    // ─────────────────────────────────────────
    // Helper — Get Queue Entries From Department
    // ─────────────────────────────────────────
    private List<QueueEntry> getQueueEntries(String deptCode) {
        Map<String, HospitalDepartment> departments =
            facade.getDepartments();

        HospitalDepartment dept = departments.get(deptCode);

        if (dept != null) {
            return dept.getQueueManager().getQueueEntries();
        }

        return new ArrayList<>();
    }
}