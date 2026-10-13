// ============================================================
// FILE     : AppointmentService.java
// PACKAGE  : patterns.structural.facade.subsystems
// PURPOSE  : Manages all appointments across departments
//            Works with HospitalDepartment from Phase 1
// PATTERN  : Facade — Subsystem Class
// ============================================================

package patterns.structural.facade.subsystems;

import models.Appointment;
import models.Department;
import models.Doctor;
import models.Patient;
import patterns.creational.abstractfactory.client.HospitalDepartment;
import utils.ConsolePrinter;
import utils.ConsoleColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentService {

    // ─────────────────────────────────────────
    // Department References from Phase 1
    // ─────────────────────────────────────────
    private final Map<String, HospitalDepartment> departments;

    // ─────────────────────────────────────────
    // Doctor Storage
    // ─────────────────────────────────────────
    private final Map<String, Doctor> doctorRegistry;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public AppointmentService(Map<String, HospitalDepartment> departments) {
        this.departments    = departments;
        this.doctorRegistry = new HashMap<>();
    }

    // ─────────────────────────────────────────
    // Register Doctor
    // ─────────────────────────────────────────
    public void registerDoctor(Doctor doctor) {
        doctorRegistry.put(doctor.getDoctorId(), doctor);
        ConsolePrinter.printSuccess(
            "[APPOINTMENT] Doctor Registered → "
            + doctor.getName()
            + " | Dept : " + doctor.getDepartment().getDisplayName()
        );
    }

    // ─────────────────────────────────────────
    // Book Appointment
    // ─────────────────────────────────────────
    public Appointment bookAppointment(Patient patient,
                                       Doctor doctor,
                                       String time) {

        String deptCode = patient.getDepartment().getCode();
        HospitalDepartment dept = departments.get(deptCode);

        if (dept == null) {
            ConsolePrinter.printError(
                "[APPOINTMENT] Department not found : " + deptCode
            );
            return null;
        }

        System.out.println(ConsoleColors.BOLD_CYAN
            + "\n  [APPOINTMENT] Booking Request"
            + "\n  Patient : " + patient.getName()
            + "\n  Doctor  : " + doctor.getName()
            + "\n  Dept    : " + patient.getDepartment().getDisplayName()
            + "\n  Time    : " + time
            + ConsoleColors.RESET
        );

        return dept.bookAppointment(patient, doctor, time);
    }

    // ─────────────────────────────────────────
    // Cancel Appointment
    // ─────────────────────────────────────────
    public void cancelAppointment(Patient patient,
                                  String appointmentId,
                                  Department department) {

        HospitalDepartment dept = departments.get(department.getCode());

        if (dept == null) {
            ConsolePrinter.printError(
                "[APPOINTMENT] Department not found : " + department.getCode()
            );
            return;
        }

        System.out.println(ConsoleColors.BOLD_YELLOW
            + "\n  [APPOINTMENT] Cancel Request"
            + "\n  Appointment ID : " + appointmentId
            + "\n  Patient        : " + patient.getName()
            + ConsoleColors.RESET
        );

        dept.cancelAppointment(patient, appointmentId);
    }

    // ─────────────────────────────────────────
    // Reschedule Appointment
    // ─────────────────────────────────────────
    public void rescheduleAppointment(Patient patient,
                                      String appointmentId,
                                      String newTime,
                                      Department department) {

        HospitalDepartment dept = departments.get(department.getCode());

        if (dept == null) {
            ConsolePrinter.printError(
                "[APPOINTMENT] Department not found : " + department.getCode()
            );
            return;
        }

        System.out.println(ConsoleColors.BOLD_CYAN
            + "\n  [APPOINTMENT] Reschedule Request"
            + "\n  Appointment ID : " + appointmentId
            + "\n  New Time       : " + newTime
            + ConsoleColors.RESET
        );

        dept.rescheduleAppointment(patient, appointmentId, newTime);
    }

    // ─────────────────────────────────────────
    // Get Available Doctors By Department
    // ─────────────────────────────────────────
    public List<Doctor> getAvailableDoctors(Department department) {
        List<Doctor> result = new ArrayList<>();

        for (Doctor doctor : doctorRegistry.values()) {
            if (doctor.getDepartment() == department && doctor.isAvailable()) {
                result.add(doctor);
            }
        }

        return result;
    }

    // ─────────────────────────────────────────
    // Get Doctor By ID
    // ─────────────────────────────────────────
    public Doctor getDoctorById(String doctorId) {
        return doctorRegistry.get(doctorId);
    }

    // ─────────────────────────────────────────
    // Display Schedule by Department
    // ─────────────────────────────────────────
    public void displayDepartmentSchedule(Department department) {
        HospitalDepartment dept = departments.get(department.getCode());

        if (dept != null) {
            dept.displaySchedule();
        } else {
            ConsolePrinter.printError(
                "[APPOINTMENT] Department not found : " + department.getCode()
            );
        }
    }

    // ─────────────────────────────────────────
    // Display All Doctors
    // ─────────────────────────────────────────
    public void displayAllDoctors() {
        ConsolePrinter.printSectionHeader("REGISTERED DOCTORS");

        if (doctorRegistry.isEmpty()) {
            ConsolePrinter.printInfo("No doctors registered yet.");
            return;
        }

        System.out.println(ConsoleColors.BOLD_CYAN);
        System.out.printf("  %-22s %-18s %-20s %-10s %-10s%n",
            "Doctor ID", "Name", "Specialization", "Dept", "Available");
        System.out.println(ConsolePrinter.printLine("-"));

        for (Doctor doc : doctorRegistry.values()) {
            System.out.printf("  %-22s %-18s %-20s %-10s %-10s%n",
                doc.getDoctorId(),
                doc.getName(),
                doc.getSpecialization(),
                doc.getDepartment().getCode(),
                doc.isAvailable() ? "YES" : "NO"
            );
        }

        System.out.println(ConsoleColors.RESET);
    }
}