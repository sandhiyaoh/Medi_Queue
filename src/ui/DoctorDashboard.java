// ============================================================
// FILE     : DoctorDashboard.java
// PACKAGE  : ui
// PURPOSE  : Doctor-facing UI — view appointments,
//            serve next patient, update availability
// ============================================================

package ui;

import models.Department;
import models.Doctor;
import models.Patient;
import patterns.structural.facade.MediQueueFacade;
import utils.ConsoleColors;

import java.util.List;
import java.util.Scanner;

public class DoctorDashboard {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final MediQueueFacade facade;
    private final Scanner         scanner;

    // Selected department
    private Department currentDept;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public DoctorDashboard(MediQueueFacade facade, Scanner scanner) {
        this.facade   = facade;
        this.scanner  = scanner;
    }

    // ─────────────────────────────────────────
    // Show Doctor Dashboard
    // ─────────────────────────────────────────
    public void show() {
        selectDepartment();

        boolean running = true;

        while (running) {
            printDashboardMenu();

            int choice = getIntInput();

            switch (choice) {
                case 1: viewTodaysAppointments();   break;
                case 2: viewCurrentQueue();         break;
                case 3: serveNextPatient();         break;
                case 4: viewAllDoctors();           break;
                case 5: running = false;            break;
                default: printInvalid();
            }
        }
    }

    // ─────────────────────────────────────────
    // Select Department on Login
    // ─────────────────────────────────────────
    private void selectDepartment() {
        printHeader("DOCTOR LOGIN — SELECT DEPARTMENT");

        System.out.println(ConsoleColors.BOLD_WHITE);
        System.out.println("   [1]  General Department");
        System.out.println("   [2]  Emergency Department");
        System.out.println("   [3]  VIP Department");
        System.out.println(ConsoleColors.RESET);

        System.out.print(ConsoleColors.BOLD_BLUE
            + "  >> Select Your Department : "
            + ConsoleColors.RESET);

        int choice = getIntInput();

        switch (choice) {
            case 1: currentDept = Department.GENERAL;   break;
            case 2: currentDept = Department.EMERGENCY; break;
            case 3: currentDept = Department.VIP;       break;
            default:
                currentDept = Department.GENERAL;
                printInvalid();
        }

        System.out.println(ConsoleColors.BOLD_BLUE
            + "\n  [OK] Logged in as : "
            + currentDept.getDisplayName() + " Doctor"
            + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Print Dashboard Menu
    // ─────────────────────────────────────────
    private void printDashboardMenu() {
        System.out.println(ConsoleColors.BOLD_BLUE);
        System.out.println("  -----------------------------------------------------------------");
        System.out.println("                       DOCTOR DASHBOARD                            ");
        System.out.println("                  Dept : "
            + currentDept.getDisplayName());
        System.out.println("  -----------------------------------------------------------------");
        System.out.println(ConsoleColors.RESET);

        System.out.println(ConsoleColors.BOLD_WHITE);
        System.out.println("   [1]  View Today's Appointments");
        System.out.println("   [2]  View Current Queue");
        System.out.println("   [3]  Serve Next Patient");
        System.out.println("   [4]  View All Doctors");
        System.out.println("   [5]  Back to Main Menu");
        System.out.println(ConsoleColors.RESET);

        System.out.print(ConsoleColors.BOLD_BLUE
            + "  >> Select Option : " + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // 1. View Today's Appointments
    // ─────────────────────────────────────────
    private void viewTodaysAppointments() {
        printHeader("TODAY'S APPOINTMENTS — "
            + currentDept.getDisplayName());

        facade.viewSchedule(currentDept);
    }

    // ─────────────────────────────────────────
    // 2. View Current Queue
    // ─────────────────────────────────────────
    private void viewCurrentQueue() {
        printHeader("CURRENT QUEUE — "
            + currentDept.getDisplayName());

        facade.viewDepartmentQueue(currentDept);
    }

    // ─────────────────────────────────────────
    // 3. Serve Next Patient
    // ─────────────────────────────────────────
    private void serveNextPatient() {
        printHeader("SERVE NEXT PATIENT — "
            + currentDept.getDisplayName());

        Patient served = facade.serveNextPatient(currentDept);

        if (served != null) {
            System.out.println(ConsoleColors.BOLD_BLUE);
            System.out.println("  +------------------------------------------+");
            System.out.println("  |          PATIENT BEING SERVED             |");
            System.out.println("  +------------------------------------------+");
            System.out.printf ("  |  Name       : %-27s|%n",
                served.getName());
            System.out.printf ("  |  Age        : %-27s|%n",
                served.getAge());
            System.out.printf ("  |  Contact    : %-27s|%n",
                served.getContact());
            System.out.printf ("  |  Department : %-27s|%n",
                served.getDepartment().getDisplayName());
            System.out.println("  +------------------------------------------+");
            System.out.println(ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.BOLD_YELLOW
                + "\n  [INFO] No patients in queue currently."
                + ConsoleColors.RESET);
        }
    }

    // ─────────────────────────────────────────
    // 4. View All Doctors
    // ─────────────────────────────────────────
    private void viewAllDoctors() {
        printHeader("ALL REGISTERED DOCTORS");
        facade.viewAllDoctors();
    }

    // ─────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────
    private void printHeader(String title) {
        System.out.println(ConsoleColors.BOLD_BLUE);
        System.out.println("  -----------------------------------------------------------------");
        System.out.println("  " + title);
        System.out.println("  -----------------------------------------------------------------");
        System.out.println(ConsoleColors.RESET);
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