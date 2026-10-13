// ============================================================
// FILE     : PatientPortal.java
// PACKAGE  : ui
// PURPOSE  : Patient-facing UI — register, book, view queue,
//            check wait time, cancel/reschedule
// ============================================================

package ui;

import models.Appointment;
import models.Department;
import models.Doctor;
import models.Patient;
import models.Token;
import patterns.behavioral.observer.observers.PatientObserver;
import patterns.behavioral.observer.subject.LiveQueueSubject;
import patterns.structural.facade.MediQueueFacade;
import utils.ConsoleColors;

import java.util.List;
import java.util.Scanner;

public class PatientPortal {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final MediQueueFacade  facade;
    private final LiveQueueSubject queueSubject;
    private final Scanner          scanner;

    // Track current logged-in patient
    private Patient currentPatient;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public PatientPortal(MediQueueFacade facade,
                         LiveQueueSubject queueSubject,
                         Scanner scanner) {
        this.facade       = facade;
        this.queueSubject = queueSubject;
        this.scanner      = scanner;
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

        // Register via Facade
        currentPatient = facade.registerPatient(
            name, age, contact, email, dept, false
        );

        // Register Patient Observer
        queueSubject.registerObserver(
            new PatientObserver(currentPatient)
        );

        printSuccess("Registration successful! Patient ID: "
            + currentPatient.getPatientId());
    }

    // ─────────────────────────────────────────
    // 2. Book Appointment
    // ─────────────────────────────────────────
    private void bookAppointment() {
        if (!checkPatientLoggedIn()) return;

        printHeader("BOOK APPOINTMENT");

        // Show available doctors
        List<Doctor> doctors = facade.getAvailableDoctors(
            currentPatient.getDepartment()
        );

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

        Appointment apt = facade.bookAppointment(
            currentPatient, selectedDoc, time
        );

        if (apt != null) {
            printSuccess("Appointment booked! ID: "
                + apt.getAppointmentId());
        }
    }

    // ─────────────────────────────────────────
    // 3. Generate Token & Join Queue
    // ─────────────────────────────────────────
    private void generateTokenAndAdmit() {
        if (!checkPatientLoggedIn()) return;

        printHeader("GENERATE TOKEN & JOIN QUEUE");

        Token token = facade.generateTokenAndAdmit(currentPatient);

        if (token != null) {
            System.out.println(ConsoleColors.BOLD_GREEN);
            System.out.println("  +------------------------------------------+");
            System.out.println("  |           YOUR DIGITAL TOKEN              |");
            System.out.println("  +------------------------------------------+");
            System.out.printf ("  |  Token Number : %-25s|%n",
                String.format("%03d", token.getTokenNumber()));
            System.out.printf ("  |  Patient      : %-25s|%n",
                currentPatient.getName());
            System.out.printf ("  |  Department   : %-25s|%n",
                currentPatient.getDepartment().getCode());
            System.out.printf ("  |  Issued At    : %-25s|%n",
                token.getIssuedAt());
            System.out.println("  +------------------------------------------+");
            System.out.println(ConsoleColors.RESET);
        }
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

        Department dept = getDepartmentFromChoice(choice);

        if (dept == null) {
            // Show all departments
            facade.viewAllQueueStatus();
        } else {
            facade.viewDepartmentQueue(dept);
        }
    }

    // ─────────────────────────────────────────
    // 5. Check Wait Time
    // ─────────────────────────────────────────
    private void checkWaitTime() {
        if (!checkPatientLoggedIn()) return;

        printHeader("ESTIMATED WAIT TIME");

        int waitTime = facade.getEstimatedWaitTime(currentPatient);

        if (waitTime == -1) {
            printError("You are not currently in any queue.");
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

        facade.cancelAppointment(
            currentPatient,
            aptId,
            currentPatient.getDepartment()
        );
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

        facade.rescheduleAppointment(
            currentPatient,
            aptId,
            newTime,
            currentPatient.getDepartment()
        );
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

        // Show appointment schedule
        facade.viewSchedule(currentPatient.getDepartment());
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