// ============================================================
// FILE     : PatientRegistrationService.java
// PACKAGE  : patterns.structural.facade.subsystems
// PURPOSE  : Handles all patient registration operations
//            Stores and retrieves patient records
// PATTERN  : Facade — Subsystem Class
// ============================================================

package patterns.structural.facade.subsystems;

import models.Department;
import models.Patient;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientRegistrationService {

    // ─────────────────────────────────────────
    // Storage
    // ─────────────────────────────────────────
    private final Map<String, Patient> patientRegistry;
    private int patientCounter;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public PatientRegistrationService() {
        this.patientRegistry = new HashMap<>();
        this.patientCounter  = 1;
    }

    // ─────────────────────────────────────────
    // Register New Patient
    // ─────────────────────────────────────────
    public Patient registerPatient(String name,
                                   int age,
                                   String contact,
                                   String email,
                                   Department department,
                                   boolean isEmergency) {

        String patientId = DateTimeUtils.generatePatientId(patientCounter++);

        Patient patient = new Patient(
            patientId,
            name,
            age,
            contact,
            email,
            department,
            isEmergency,
            DateTimeUtils.getCurrentTimestamp()
        );

        patientRegistry.put(patientId, patient);

        System.out.println(ConsoleColors.BOLD_GREEN
            + "  [REGISTRATION] Patient Registered Successfully"
            + "\n  ID      : " + patientId
            + "\n  Name    : " + name
            + "\n  Age     : " + age
            + "\n  Dept    : " + department.getDisplayName()
            + "\n  Contact : " + contact
            + "\n  Email   : " + email
            + ConsoleColors.RESET
        );

        return patient;
    }

    // ─────────────────────────────────────────
    // Get Patient By ID
    // ─────────────────────────────────────────
    public Patient getPatientById(String patientId) {
        Patient patient = patientRegistry.get(patientId);

        if (patient == null) {
            ConsolePrinter.printError(
                "[REGISTRATION] Patient not found : " + patientId
            );
        }

        return patient;
    }

    // ─────────────────────────────────────────
    // Get All Patients
    // ─────────────────────────────────────────
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientRegistry.values());
    }

    // ─────────────────────────────────────────
    // Get Patients By Department
    // ─────────────────────────────────────────
    public List<Patient> getPatientsByDepartment(Department department) {
        List<Patient> result = new ArrayList<>();

        for (Patient patient : patientRegistry.values()) {
            if (patient.getDepartment() == department) {
                result.add(patient);
            }
        }

        return result;
    }

    // ─────────────────────────────────────────
    // Check Patient Exists
    // ─────────────────────────────────────────
    public boolean patientExists(String patientId) {
        return patientRegistry.containsKey(patientId);
    }

    // ─────────────────────────────────────────
    // Total Registered Count
    // ─────────────────────────────────────────
    public int getTotalRegisteredCount() {
        return patientRegistry.size();
    }

    // ─────────────────────────────────────────
    // Display All Patients
    // ─────────────────────────────────────────
    public void displayAllPatients() {
        ConsolePrinter.printSectionHeader("REGISTERED PATIENTS");

        if (patientRegistry.isEmpty()) {
            ConsolePrinter.printInfo("No patients registered yet.");
            return;
        }

        System.out.println(ConsoleColors.BOLD_CYAN);
        System.out.printf("  %-22s %-18s %-5s %-10s %-12s %-12s%n",
            "Patient ID", "Name", "Age", "Dept", "Contact", "Emergency");
        System.out.println(ConsolePrinter.printLine("-"));

        for (Patient p : patientRegistry.values()) {
            System.out.printf("  %-22s %-18s %-5d %-10s %-12s %-12s%n",
                p.getPatientId(),
                p.getName(),
                p.getAge(),
                p.getDepartment().getCode(),
                p.getContact(),
                p.isEmergency() ? "YES" : "NO"
            );
        }

        System.out.println(ConsoleColors.RESET);
        ConsolePrinter.printInfo("Total Registered : " + patientRegistry.size());
    }
}