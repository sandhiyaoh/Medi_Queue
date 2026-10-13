// ============================================================
// FILE     : AdminPanel.java
// PACKAGE  : ui
// PURPOSE  : Admin-facing UI — full system control
//            View all data, handle emergencies,
//            serve queues, view system summary
// ============================================================

package ui;

import models.Department;
import models.Patient;
import patterns.behavioral.observer.subject.LiveQueueSubject;
import patterns.structural.facade.MediQueueFacade;
import utils.ConsoleColors;

import java.util.Scanner;

public class AdminPanel {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final MediQueueFacade  facade;
    private final LiveQueueSubject queueSubject;
    private final Scanner          scanner;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public AdminPanel(MediQueueFacade facade,
                      LiveQueueSubject queueSubject,
                      Scanner scanner) {
        this.facade       = facade;
        this.queueSubject = queueSubject;
        this.scanner      = scanner;
    }

    // ─────────────────────────────────────────
    // Show Admin Panel
    // ─────────────────────────────────────────
    public void show() {
        boolean running = true;

        while (running) {
            printAdminMenu();

            int choice = getIntInput();

            switch (choice) {
                case 1:  viewAllPatients();         break;
                case 2:  viewAllDoctors();          break;
                case 3:  viewAllQueues();           break;
                case 4:  viewAllSchedules();        break;
                case 5:  handleEmergency();         break;
                case 6:  broadcastAlert();          break;
                case 7:  servePatient();            break;
                case 8:  viewEmergencyLog();        break;
                case 9:  viewAllTokens();           break;
                case 10: viewObservers();           break;
                case 11: viewSystemSummary();       break;
                case 12: running = false;           break;
                default: printInvalid();
            }
        }
    }

    // ─────────────────────────────────────────
    // Print Admin Menu
    // ─────────────────────────────────────────
    private void printAdminMenu() {
        System.out.println(ConsoleColors.BOLD_PURPLE);
        System.out.println("  =================================================================");
        System.out.println("                        ADMIN PANEL                                ");
        System.out.println("                   Full System Control                             ");
        System.out.println("  =================================================================");
        System.out.println(ConsoleColors.RESET);

        System.out.println(ConsoleColors.BOLD_WHITE);
        System.out.println("  -- PATIENT & DOCTOR --");
        System.out.println("   [1]   View All Registered Patients");
        System.out.println("   [2]   View All Registered Doctors");
        System.out.println();
        System.out.println("  -- QUEUE & SCHEDULE --");
        System.out.println("   [3]   View All Department Queues");
        System.out.println("   [4]   View All Appointment Schedules");
        System.out.println("   [7]   Serve Next Patient (Any Dept)");
        System.out.println("   [9]   View All Issued Tokens");
        System.out.println();
        System.out.println("  -- EMERGENCY --");
        System.out.println("   [5]   Handle Emergency Patient");
        System.out.println("   [6]   Broadcast System Alert");
        System.out.println("   [8]   View Emergency Log");
        System.out.println();
        System.out.println("  -- OBSERVER & SYSTEM --");
        System.out.println("   [10]  View All Observers");
        System.out.println("   [11]  View System Summary");
        System.out.println("   [12]  Back to Main Menu");
        System.out.println(ConsoleColors.RESET);

        System.out.print(ConsoleColors.BOLD_PURPLE
            + "  >> Select Option : " + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // 1. View All Patients
    // ─────────────────────────────────────────
    private void viewAllPatients() {
        printHeader("ALL REGISTERED PATIENTS");
        facade.viewAllPatients();
    }

    // ─────────────────────────────────────────
    // 2. View All Doctors
    // ─────────────────────────────────────────
    private void viewAllDoctors() {
        printHeader("ALL REGISTERED DOCTORS");
        facade.viewAllDoctors();
    }

    // ─────────────────────────────────────────
    // 3. View All Queues
    // ─────────────────────────────────────────
    private void viewAllQueues() {
        printHeader("ALL DEPARTMENT QUEUES");

        facade.viewAllQueueStatus();

        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  -- Detailed Queue View --"
            + ConsoleColors.RESET);

        facade.viewDepartmentQueue(Department.GENERAL);
        facade.viewDepartmentQueue(Department.EMERGENCY);
        facade.viewDepartmentQueue(Department.VIP);
    }

    // ─────────────────────────────────────────
    // 4. View All Schedules
    // ─────────────────────────────────────────
    private void viewAllSchedules() {
        printHeader("ALL APPOINTMENT SCHEDULES");

        facade.viewSchedule(Department.GENERAL);
        facade.viewSchedule(Department.EMERGENCY);
        facade.viewSchedule(Department.VIP);
    }

    // ─────────────────────────────────────────
    // 5. Handle Emergency
    // ─────────────────────────────────────────
    private void handleEmergency() {
        printHeader("EMERGENCY PATIENT HANDLER");

        System.out.print(ConsoleColors.BOLD_RED
            + "  >> Patient Name    : " + ConsoleColors.RESET);
        String name = scanner.nextLine().trim();

        System.out.print(ConsoleColors.BOLD_RED
            + "  >> Age             : " + ConsoleColors.RESET);
        int age = getIntInput();

        System.out.print(ConsoleColors.BOLD_RED
            + "  >> Contact No      : " + ConsoleColors.RESET);
        String contact = scanner.nextLine().trim();

        System.out.print(ConsoleColors.BOLD_RED
            + "  >> Email           : " + ConsoleColors.RESET);
        String email = scanner.nextLine().trim();

        // Register and immediately handle as emergency
        Patient emergencyPatient = facade.registerPatient(
            name, age, contact, email,
            Department.EMERGENCY, true
        );

        // Trigger emergency with observer notifications
        queueSubject.triggerEmergencyWithNotification(emergencyPatient);

        System.out.println(ConsoleColors.BOLD_RED
            + "\n  [!!!] Emergency patient admitted and all"
            + " observers notified!"
            + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // 6. Broadcast Alert
    // ─────────────────────────────────────────
    private void broadcastAlert() {
        printHeader("BROADCAST SYSTEM ALERT");

        System.out.print(ConsoleColors.BOLD_RED
            + "  >> Alert Message : " + ConsoleColors.RESET);
        String message = scanner.nextLine().trim();

        facade.broadcastAlert(message);

        System.out.println(ConsoleColors.BOLD_RED
            + "\n  [OK] Alert broadcast to all departments."
            + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // 7. Serve Patient
    // ─────────────────────────────────────────
    private void servePatient() {
        printHeader("SERVE NEXT PATIENT");

        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  Select Department :"
            + ConsoleColors.RESET);
        System.out.println("   [1]  General");
        System.out.println("   [2]  Emergency");
        System.out.println("   [3]  VIP");

        System.out.print(ConsoleColors.BOLD_PURPLE
            + "\n  >> Department : " + ConsoleColors.RESET);
        int choice = getIntInput();

        Department dept;

        switch (choice) {
            case 1: dept = Department.GENERAL;   break;
            case 2: dept = Department.EMERGENCY; break;
            case 3: dept = Department.VIP;       break;
            default:
                printInvalid();
                return;
        }

        // Serve with observer notification
        Patient served = queueSubject.serveNextWithNotification(dept);

        if (served == null) {
            System.out.println(ConsoleColors.BOLD_YELLOW
                + "\n  [INFO] No patients in "
                + dept.getDisplayName() + " queue."
                + ConsoleColors.RESET);
        }
    }

    // ─────────────────────────────────────────
    // 8. View Emergency Log
    // ─────────────────────────────────────────
    private void viewEmergencyLog() {
        printHeader("EMERGENCY LOG");
        facade.viewEmergencyLog();
    }

    // ─────────────────────────────────────────
    // 9. View All Tokens
    // ─────────────────────────────────────────
    private void viewAllTokens() {
        printHeader("ALL ISSUED TOKENS");
        facade.viewAllTokens();
    }

    // ─────────────────────────────────────────
    // 10. View All Observers
    // ─────────────────────────────────────────
    private void viewObservers() {
        printHeader("REGISTERED OBSERVERS");
        queueSubject.displayObservers();
    }

    // ─────────────────────────────────────────
    // 11. System Summary
    // ─────────────────────────────────────────
    private void viewSystemSummary() {
        printHeader("SYSTEM SUMMARY");
        facade.viewSystemSummary();
    }

    // ─────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────
    private void printHeader(String title) {
        System.out.println(ConsoleColors.BOLD_PURPLE);
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