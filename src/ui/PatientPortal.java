// ============================================================
// FILE     : PatientPortal.java
// PACKAGE  : ui
// PURPOSE  : Patient-facing UI — register, book, view queue,
//            check wait time, cancel/reschedule
//            Uses Abstract Factory (HospitalDepartment) only
// TEAM     : Sandhiya O H (23MIS0114)
//            Deepak Kumar S (23MIS0191)
// ============================================================

package ui;

import models.Appointment;
import models.Department;
import models.Doctor;
import models.Patient;
import models.Token;
import patterns.creational.abstractfactory.client.HospitalDepartment;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.List;
import java.util.Scanner;

public class PatientPortal {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final HospitalDepartment generalDept;
    private final HospitalDepartment emergencyDept;
    private final HospitalDepartment vipDept;
    private final Scanner            scanner;

    // Track current logged-in patient + their department
    private Patient          currentPatient;
    private HospitalDepartment currentDept;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public PatientPortal(HospitalDepartment generalDept,
                         HospitalDepartment emergencyDept,
                         HospitalDepartment vipDept,
                         Scanner scanner) {
        this.generalDept   = generalDept;
        this.emergencyDept = emergencyDept;
        this.vipDept       = vipDept;
        this.scanner       = scanner;
    }

    // ─────────────────────────────────────────
    // Show Patient Portal
    // ─────────────────────────────────────────
    public void show() {
        boolean running = true;

        while (running) {
            printPortalMenu();

            int choice = getIntInput();

            switch (choice) {
                case 1: registerPatient();          break;
                case 2: bookAppointment();          break;
                case 3: generateTokenAndAdmit();    break;
                case 4: viewQueueStatus();          break;
                case 5: checkWaitTime();            break;
                case 6: cancelAppointment();        break;
                case 7: rescheduleAppointment();    break;
                case 8: viewMyDetails();            break;
                case 9: running = false;            break;
                default: printInvalid();
            }
        }
    }

    // ─────────────────────────────────────────
    // Print Portal Menu
    // ─────────────────────────────────────────
    private void printPortalMenu() {
        System.out.println(ConsoleColors.BOLD_GREEN);
        System.out.println("  -----------------------------------------------------------------");
        System.out.println("                       PATIENT PORTAL                              ");

        if (currentPatient != null) {
            System.out.println("                  Welcome, "
                + currentPatient.getName()
                + " ["
                + currentPatient.getDepartment().getCode()
                + "]"
            );
        }

        System.out.println("  -----------------------------------------------------------------");
        System.out.println(ConsoleColors.RESET);

        System.out.println(ConsoleColors.BOLD_WHITE);
        System.out.println("   [1]  Register as New Patient");
        System.out.println("   [2]  Book Appointment");
        System.out.println("   [3]  Generate Token & Join Queue");
        System.out.println("   [4]  View Live Queue Status");
        System.out.println("   [5]  Check My Wait Time");
        System.out.println("   [6]  Cancel Appointment");
        System.out.println("   [7]  Reschedule Appointment");
        System.out.println("   [8]  View My Details");
        System.out.println("   [9]  Back to Main Menu");
        System.out.println(ConsoleColors.RESET);

        System.out.print(ConsoleColors.BOLD_GREEN
            + "  >> Select Option : " + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // 1. Register Patient
    // ─────────────────────────────────────────
    private void registerPatient() {
        printHeader("PATIENT REGISTRATION");

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Full Name    : " + ConsoleColors.RESET);
        String name = scanner.nextLine().trim();

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Age          : " + ConsoleColors.RESET);
        int age = getIntInput();

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Contact No   : " + ConsoleColors.RESET);
        String contact = scanner.nextLine().trim();

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Email        : " + ConsoleColors.RESET);
        String email = scanner.nextLine().trim();

        printDeptOptions();

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Department   : " + ConsoleColors.RESET);
        int deptChoice = getIntInput();

        Department dept = getDepartmentFromChoice(deptChoice);

        if (dept == null) {
            printInvalid();
            return;
        }

        currentDept = getDeptByDepartment(dept);

        // Create and register patient
        currentPatient = new Patient(
            DateTimeUtils.generatePatientId((int)(Math.random() * 9000) + 1000),
            name, age, contact, email, dept,
            dept == Department.EMERGENCY,
            DateTimeUtils.getCurrentTimestamp()
        );

        currentDept.registerPatient(currentPatient);

        printSuccess("Registration successful! Patient ID: "
            + currentPatient.getPatientId());
    }

    // ─────────────────────────────────────────
    // 2. Book Appointment
    // ─────────────────────────────────────────
    private void bookAppointment() {
        if (!checkPatientLoggedIn()) return;

        printHeader("BOOK APPOINTMENT");

        List<Doctor> doctors = currentDept.getAvailableDoctors();

        if (doctors.isEmpty()) {
            printError("No doctors available in "
                + currentPatient.getDepartment().getDisplayName());
            return;
        }

        System.out.println(ConsoleColors.BOLD_CYAN
            + "\n  Available Doctors :"
            + ConsoleColors.RESET);

        for (int i = 0; i < doctors.size(); i++) {
            System.out.println(ConsoleColors.BOLD_WHITE
                + "   [" + (i + 1) + "]  "
                + doctors.get(i).getName()
                + " | " + doctors.get(i).getSpecialization()
                + ConsoleColors.RESET);
        }

        System.out.print(ConsoleColors.BOLD_CYAN
            + "\n  >> Select Doctor : " + ConsoleColors.RESET);
        int docChoice = getIntInput();

        if (docChoice < 1 || docChoice > doctors.size()) {
            printInvalid();
            return;
        }

        Doctor selectedDoc = doctors.get(docChoice - 1);

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Preferred Time (e.g. 10:00 AM) : "
            + ConsoleColors.RESET);
        String time = scanner.nextLine().trim();

        Appointment apt = currentDept.bookAppointment(currentPatient, selectedDoc, time);

        if (apt != null) {
            printSuccess("Appointment booked! ID: " + apt.getAppointmentId());
        }
    }

    // ─────────────────────────────────────────
    // 3. Generate Token & Join Queue
    // ─────────────────────────────────────────
    private void generateTokenAndAdmit() {
        if (!checkPatientLoggedIn()) return;

        printHeader("GENERATE TOKEN & JOIN QUEUE");

        currentDept.admitPatient(currentPatient);

        System.out.println(ConsoleColors.BOLD_GREEN);
        System.out.println("  +------------------------------------------+");
        System.out.println("  |           QUEUE ADMISSION                 |");
        System.out.printf ("  |  Patient      : %-25s|%n", currentPatient.getName());
        System.out.printf ("  |  Department   : %-25s|%n",
            currentPatient.getDepartment().getDisplayName());
        System.out.printf ("  |  Admitted At  : %-25s|%n",
            DateTimeUtils.getCurrentTimestamp());
        System.out.println("  +------------------------------------------+");
        System.out.println(ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // 4. View Queue Status
    // ─────────────────────────────────────────
    private void viewQueueStatus() {
        printHeader("LIVE QUEUE STATUS");

        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  Select Department to View:"
            + ConsoleColors.RESET);

        printDeptOptions();

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Department : " + ConsoleColors.RESET);
        int choice = getIntInput();

        switch (choice) {
            case 1: generalDept.displayQueueStatus();   break;
            case 2: emergencyDept.displayQueueStatus(); break;
            case 3: vipDept.displayQueueStatus();       break;
            default:
                // Show all
                generalDept.displayQueueStatus();
                emergencyDept.displayQueueStatus();
                vipDept.displayQueueStatus();
        }
    }

    // ─────────────────────────────────────────
    // 5. Check Wait Time
    // ─────────────────────────────────────────
    private void checkWaitTime() {
        if (!checkPatientLoggedIn()) return;

        printHeader("ESTIMATED WAIT TIME");

        int waitTime = currentDept.getWaitTime(currentPatient.getPatientId());

        if (waitTime == -1) {
            printError("You are not currently in any queue.");
        } else {
            System.out.println(ConsoleColors.BOLD_GREEN
                + "\n  Estimated Wait Time : " + waitTime + " minutes"
                + ConsoleColors.RESET);
        }
    }

    // ─────────────────────────────────────────
    // 6. Cancel Appointment
    // ─────────────────────────────────────────
    private void cancelAppointment() {
        if (!checkPatientLoggedIn()) return;

        printHeader("CANCEL APPOINTMENT");

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Appointment ID : " + ConsoleColors.RESET);
        String aptId = scanner.nextLine().trim();

        currentDept.cancelAppointment(currentPatient, aptId);
    }

    // ─────────────────────────────────────────
    // 7. Reschedule Appointment
    // ─────────────────────────────────────────
    private void rescheduleAppointment() {
        if (!checkPatientLoggedIn()) return;

        printHeader("RESCHEDULE APPOINTMENT");

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Appointment ID  : " + ConsoleColors.RESET);
        String aptId = scanner.nextLine().trim();

        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> New Time        : " + ConsoleColors.RESET);
        String newTime = scanner.nextLine().trim();

        currentDept.rescheduleAppointment(currentPatient, aptId, newTime);
    }

    // ─────────────────────────────────────────
    // 8. View My Details
    // ─────────────────────────────────────────
    private void viewMyDetails() {
        if (!checkPatientLoggedIn()) return;

        printHeader("MY DETAILS");

        System.out.println(ConsoleColors.BOLD_GREEN);
        System.out.println("  +------------------------------------------+");
        System.out.println("  |             PATIENT DETAILS               |");
        System.out.println("  +------------------------------------------+");
        System.out.printf ("  |  Patient ID   : %-25s|%n",
            currentPatient.getPatientId());
        System.out.printf ("  |  Name         : %-25s|%n",
            currentPatient.getName());
        System.out.printf ("  |  Age          : %-25s|%n",
            currentPatient.getAge());
        System.out.printf ("  |  Department   : %-25s|%n",
            currentPatient.getDepartment().getDisplayName());
        System.out.printf ("  |  Contact      : %-25s|%n",
            currentPatient.getContact());
        System.out.printf ("  |  Email        : %-25s|%n",
            currentPatient.getEmail());
        System.out.printf ("  |  Emergency    : %-25s|%n",
            currentPatient.isEmergency() ? "YES" : "NO");
        System.out.println("  +------------------------------------------+");
        System.out.println(ConsoleColors.RESET);

        // Show appointment schedule for patient's department
        currentDept.displaySchedule();
    }

    // ─────────────────────────────────────────
    // Helper — Get HospitalDepartment by Department
    // ─────────────────────────────────────────
    private HospitalDepartment getDeptByDepartment(Department dept) {
        switch (dept) {
            case EMERGENCY: return emergencyDept;
            case VIP:       return vipDept;
            default:        return generalDept;
        }
    }

    // ─────────────────────────────────────────
    // Helper — Department Options
    // ─────────────────────────────────────────
    private void printDeptOptions() {
        System.out.println(ConsoleColors.BOLD_WHITE);
        System.out.println("   [1]  General Department");
        System.out.println("   [2]  Emergency Department");
        System.out.println("   [3]  VIP Department");
        System.out.println(ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Helper — Get Department from choice
    // ─────────────────────────────────────────
    private Department getDepartmentFromChoice(int choice) {
        switch (choice) {
            case 1: return Department.GENERAL;
            case 2: return Department.EMERGENCY;
            case 3: return Department.VIP;
            default: return null;
        }
    }

    // ─────────────────────────────────────────
    // Helper — Check Patient Registered
    // ─────────────────────────────────────────
    private boolean checkPatientLoggedIn() {
        if (currentPatient == null) {
            printError(
                "No patient registered yet."
                + " Please register first (Option 1)."
            );
            return false;
        }
        return true;
    }

    // ─────────────────────────────────────────
    // Helpers — UI Formatting
    // ─────────────────────────────────────────
    private void printHeader(String title) {
        System.out.println(ConsoleColors.BOLD_GREEN);
        System.out.println("  -----------------------------------------------------------------");
        System.out.println("  " + title);
        System.out.println("  -----------------------------------------------------------------");
        System.out.println(ConsoleColors.RESET);
    }

    private void printSuccess(String msg) {
        System.out.println(ConsoleColors.BOLD_GREEN
            + "\n  [OK] " + msg + ConsoleColors.RESET);
    }

    private void printError(String msg) {
        System.out.println(ConsoleColors.BOLD_RED
            + "\n  [ERR] " + msg + ConsoleColors.RESET);
    }

    private void printInvalid() {
        System.out.println(ConsoleColors.BOLD_RED
            + "\n  [ERR] Invalid choice. Try again."
            + ConsoleColors.RESET);
    }

    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}