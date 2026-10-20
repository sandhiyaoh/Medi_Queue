// ============================================================
// FILE     : AdminPanel.java
// PACKAGE  : ui
// PURPOSE  : Admin-facing UI — full system control
//            View all data, handle emergencies,
//            serve queues, view system summary
//            Uses Abstract Factory (HospitalDepartment) only
// TEAM     : Sandhiya O H (23MIS0114)
//            Deepak Kumar S (23MIS0191)
// ============================================================

package ui;

import models.Department;
import models.Patient;
import patterns.creational.abstractfactory.client.HospitalDepartment;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.Scanner;

public class AdminPanel {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final HospitalDepartment generalDept;
    private final HospitalDepartment emergencyDept;
    private final HospitalDepartment vipDept;
    private final Scanner            scanner;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public AdminPanel(HospitalDepartment generalDept,
                      HospitalDepartment emergencyDept,
                      HospitalDepartment vipDept,
                      Scanner scanner) {
        this.generalDept   = generalDept;
        this.emergencyDept = emergencyDept;
        this.vipDept       = vipDept;
        this.scanner       = scanner;
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
                case 8:  viewEmergencyPatients();   break;
                case 9:  viewSystemSummary();       break;
                case 10: running = false;           break;
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
        System.out.println();
        System.out.println("  -- EMERGENCY --");
        System.out.println("   [5]   Handle Emergency Patient");
        System.out.println("   [6]   Broadcast System Alert");
        System.out.println("   [8]   View Emergency Patients");
        System.out.println();
        System.out.println("  -- SYSTEM --");
        System.out.println("   [9]   View System Summary");
        System.out.println("   [10]  Back to Main Menu");
        System.out.println(ConsoleColors.RESET);

        System.out.print(ConsoleColors.BOLD_PURPLE
            + "  >> Select Option : " + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // 1. View All Patients
    // ─────────────────────────────────────────
    private void viewAllPatients() {
        printHeader("ALL REGISTERED PATIENTS");
        generalDept.displayAllPatients();
        emergencyDept.displayAllPatients();
        vipDept.displayAllPatients();
    }

    // ─────────────────────────────────────────
    // 2. View All Doctors
    // ─────────────────────────────────────────
    private void viewAllDoctors() {
        printHeader("ALL REGISTERED DOCTORS");
        generalDept.displayAllDoctors();
        emergencyDept.displayAllDoctors();
        vipDept.displayAllDoctors();
    }

    // ─────────────────────────────────────────
    // 3. View All Queues
    // ─────────────────────────────────────────
    private void viewAllQueues() {
        printHeader("ALL DEPARTMENT QUEUES");

        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  -- General Department Queue --"
            + ConsoleColors.RESET);
        generalDept.displayQueueStatus();

        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  -- Emergency Department Queue --"
            + ConsoleColors.RESET);
        emergencyDept.displayQueueStatus();

        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  -- VIP Department Queue --"
            + ConsoleColors.RESET);
        vipDept.displayQueueStatus();
    }

    // ─────────────────────────────────────────
    // 4. View All Schedules
    // ─────────────────────────────────────────
    private void viewAllSchedules() {
        printHeader("ALL APPOINTMENT SCHEDULES");

        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  -- General Department --" + ConsoleColors.RESET);
        generalDept.displaySchedule();

        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  -- Emergency Department --" + ConsoleColors.RESET);
        emergencyDept.displaySchedule();

        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  -- VIP Department --" + ConsoleColors.RESET);
        vipDept.displaySchedule();
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

        // Create emergency patient
        Patient emergencyPatient = new Patient(
            DateTimeUtils.generatePatientId((int)(Math.random() * 9000) + 1000),
            name, age, contact, email,
            Department.EMERGENCY, true,
            DateTimeUtils.getCurrentTimestamp()
        );

        // Register and admit to emergency queue
        emergencyDept.registerPatient(emergencyPatient);
        emergencyDept.admitPatient(emergencyPatient);

        // Broadcast emergency alert to all departments
        String alertMsg = "EMERGENCY: " + name + " admitted to Emergency Department!";
        generalDept.broadcastAlert(alertMsg);
        emergencyDept.broadcastAlert(alertMsg);
        vipDept.broadcastAlert(alertMsg);

        System.out.println(ConsoleColors.BOLD_RED
            + "\n  [!!!] Emergency patient admitted and alert broadcast to all departments!"
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

        generalDept.broadcastAlert(message);
        emergencyDept.broadcastAlert(message);
        vipDept.broadcastAlert(message);

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

        HospitalDepartment dept;

        switch (choice) {
            case 1: dept = generalDept;   break;
            case 2: dept = emergencyDept; break;
            case 3: dept = vipDept;       break;
            default:
                printInvalid();
                return;
        }

        Patient served = dept.serveNextPatient();

        if (served == null) {
            System.out.println(ConsoleColors.BOLD_YELLOW
                + "\n  [INFO] No patients in "
                + dept.getDepartmentName() + " queue."
                + ConsoleColors.RESET);
        } else {
            System.out.println(ConsoleColors.BOLD_GREEN
                + "\n  [OK] Served patient: " + served.getName()
                + ConsoleColors.RESET);
        }
    }

    // ─────────────────────────────────────────
    // 8. View Emergency Patients
    // ─────────────────────────────────────────
    private void viewEmergencyPatients() {
        printHeader("EMERGENCY PATIENTS");
        emergencyDept.displayAllPatients();
    }

    // ─────────────────────────────────────────
    // 9. System Summary
    // ─────────────────────────────────────────
    private void viewSystemSummary() {
        printHeader("SYSTEM SUMMARY");

        System.out.println(ConsoleColors.BOLD_WHITE);
        System.out.println("  +--------------------------------------------------+");
        System.out.println("  |              MEDIQUEUE SYSTEM SUMMARY             |");
        System.out.println("  +--------------------------------------------------+");
        System.out.printf ("  |  Design Pattern   : %-29s|%n", "Abstract Factory");
        System.out.printf ("  |  Departments      : %-29s|%n", "General | Emergency | VIP");
        System.out.printf ("  |  General Patients : %-29s|%n", generalDept.getAllPatients().size());
        System.out.printf ("  |  Emergency Pts    : %-29s|%n", emergencyDept.getAllPatients().size());
        System.out.printf ("  |  VIP Patients     : %-29s|%n", vipDept.getAllPatients().size());
        System.out.printf ("  |  General Doctors  : %-29s|%n", generalDept.getAllDoctors().size());
        System.out.printf ("  |  Emergency Doctors: %-29s|%n", emergencyDept.getAllDoctors().size());
        System.out.printf ("  |  VIP Doctors      : %-29s|%n", vipDept.getAllDoctors().size());
        System.out.println("  +--------------------------------------------------+");
        System.out.println(ConsoleColors.RESET);
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