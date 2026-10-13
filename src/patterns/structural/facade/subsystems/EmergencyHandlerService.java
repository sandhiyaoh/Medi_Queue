// ============================================================
// FILE     : EmergencyHandlerService.java
// PACKAGE  : patterns.structural.facade.subsystems
// PURPOSE  : Handles all emergency patient operations
//            Priority insertion and system-wide alerts
// PATTERN  : Facade — Subsystem Class
// ============================================================

package patterns.structural.facade.subsystems;

import models.Department;
import models.Patient;
import patterns.creational.abstractfactory.client.HospitalDepartment;
import utils.ConsolePrinter;
import utils.ConsoleColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmergencyHandlerService {

    // ─────────────────────────────────────────
    // Department References
    // ─────────────────────────────────────────
    private final Map<String, HospitalDepartment> departments;

    // ─────────────────────────────────────────
    // Emergency Log
    // ─────────────────────────────────────────
    private final List<String> emergencyLog;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public EmergencyHandlerService(Map<String, HospitalDepartment> departments) {
        this.departments  = departments;
        this.emergencyLog = new ArrayList<>();
    }

    // ─────────────────────────────────────────
    // Handle Emergency Patient
    // ─────────────────────────────────────────
    public void handleEmergency(Patient patient) {

        ConsolePrinter.printEmergencyAlert(
            "EMERGENCY CASE INITIATED → Patient : " + patient.getName()
        );

        // Force patient to Emergency Department
        patient.setDepartment(Department.EMERGENCY);
        patient.setEmergency(true);

        // Add to emergency queue
        HospitalDepartment emergencyDept = departments.get("EMG");

        if (emergencyDept == null) {
            ConsolePrinter.printError(
                "[EMERGENCY] Emergency Department not initialized!"
            );
            return;
        }

        emergencyDept.admitPatient(patient);

        // Broadcast to all departments
        broadcastToAllDepartments(
            "EMERGENCY PATIENT ADMITTED → " + patient.getName()
            + " | Immediate attention required!"
        );

        // Log the emergency
        String logEntry = "[" + java.time.LocalDateTime.now() + "] "
                        + "Emergency Patient : " + patient.getName()
                        + " | Contact : " + patient.getContact();

        emergencyLog.add(logEntry);

        ConsolePrinter.printSuccess(
            "[EMERGENCY] Patient admitted and all departments notified."
        );
    }

    // ─────────────────────────────────────────
    // Broadcast Alert to All Departments
    // ─────────────────────────────────────────
    public void broadcastToAllDepartments(String message) {
        System.out.println(ConsoleColors.BOLD_RED
            + "\n  ╔══════════════════════════════════════════════════════╗"
            + "\n  ║         🚨  ALL DEPARTMENT BROADCAST  🚨             ║"
            + "\n  ║  " + String.format("%-52s", message) + "  ║"
            + "\n  ╚══════════════════════════════════════════════════════╝"
            + ConsoleColors.RESET
        );

        for (HospitalDepartment dept : departments.values()) {
            dept.broadcastAlert(message);
        }
    }

    // ─────────────────────────────────────────
    // Display Emergency Log
    // ─────────────────────────────────────────
    public void displayEmergencyLog() {
        ConsolePrinter.printSectionHeader("EMERGENCY LOG");

        if (emergencyLog.isEmpty()) {
            ConsolePrinter.printInfo("No emergency cases recorded.");
            return;
        }

        System.out.println(ConsoleColors.BOLD_RED);
        for (int i = 0; i < emergencyLog.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + emergencyLog.get(i));
        }
        System.out.println(ConsoleColors.RESET);

        ConsolePrinter.printInfo(
            "Total Emergency Cases : " + emergencyLog.size()
        );
    }

    // ─────────────────────────────────────────
    // Total Emergency Count
    // ─────────────────────────────────────────
    public int getTotalEmergencyCases() {
        return emergencyLog.size();
    }
}