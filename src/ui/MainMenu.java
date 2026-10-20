// ============================================================
// FILE     : MainMenu.java
// PACKAGE  : ui
// PURPOSE  : Main entry menu — routes to Patient Portal,
//            Doctor Dashboard, Admin Panel
//            Uses Abstract Factory (HospitalDepartment) only
// TEAM     : Sandhiya O H (23MIS0114)
//            Deepak Kumar S (23MIS0191)
// ============================================================

package ui;

import models.Department;
import models.Doctor;
import patterns.creational.abstractfactory.client.HospitalDepartment;
import patterns.creational.abstractfactory.factories.EmergencyHospitalFactory;
import patterns.creational.abstractfactory.factories.GeneralHospitalFactory;
import patterns.creational.abstractfactory.factories.VIPHospitalFactory;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.Scanner;

public class MainMenu {

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
    public MainMenu() {
        this.scanner       = new Scanner(System.in);

        // Create department instances using Abstract Factory
        this.generalDept   = new HospitalDepartment(new GeneralHospitalFactory());
        this.emergencyDept = new HospitalDepartment(new EmergencyHospitalFactory());
        this.vipDept       = new HospitalDepartment(new VIPHospitalFactory());

        initializeDefaultDoctors();
    }

    // ─────────────────────────────────────────
    // Initialize Default Doctors
    // ─────────────────────────────────────────
    private void initializeDefaultDoctors() {
        Doctor d1 = new Doctor(
            DateTimeUtils.generateDoctorId(1),
            "Ramesh Kumar", "General Physician",
            Department.GENERAL, true, "9876543001"
        );
        Doctor d2 = new Doctor(
            DateTimeUtils.generateDoctorId(2),
            "Priya Menon", "Emergency Specialist",
            Department.EMERGENCY, true, "9876543002"
        );
        Doctor d3 = new Doctor(
            DateTimeUtils.generateDoctorId(3),
            "Arjun Nair", "Senior Consultant",
            Department.VIP, true, "9876543003"
        );

        generalDept.registerDoctor(d1);
        emergencyDept.registerDoctor(d2);
        vipDept.registerDoctor(d3);
    }

    // ─────────────────────────────────────────
    // Get department by choice
    // ─────────────────────────────────────────
    public HospitalDepartment getDept(int choice) {
        switch (choice) {
            case 1: return generalDept;
            case 2: return emergencyDept;
            case 3: return vipDept;
            default: return null;
        }
    }

    // ─────────────────────────────────────────
    // Start — Main Loop
    // ─────────────────────────────────────────
    public void start() {
        boolean running = true;

        while (running) {
            printMainMenu();

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    new PatientPortal(generalDept, emergencyDept, vipDept, scanner).show();
                    break;
                case 2:
                    new DoctorDashboard(generalDept, emergencyDept, vipDept, scanner).show();
                    break;
                case 3:
                    new AdminPanel(generalDept, emergencyDept, vipDept, scanner).show();
                    break;
                case 4:
                    printExit();
                    running = false;
                    break;
                default:
                    printInvalidChoice();
            }
        }

        scanner.close();
    }

    // ─────────────────────────────────────────
    // Print Main Menu
    // ─────────────────────────────────────────
    private void printMainMenu() {
        System.out.println(ConsoleColors.BOLD_CYAN);
        System.out.println("  =================================================================");
        System.out.println("         MediQueue : Smart Hospital Management System             ");
        System.out.println("         Design Pattern  : Abstract Factory                       ");
        System.out.println("         Team            : Sandhiya O H | Deepak Kumar S          ");
        System.out.println("  =================================================================");
        System.out.println(ConsoleColors.RESET);

        System.out.println(ConsoleColors.BOLD_WHITE);
        System.out.println("  -----------------------------------------------------------------");
        System.out.println("                        MAIN MENU                                  ");
        System.out.println("  -----------------------------------------------------------------");
        System.out.println(ConsoleColors.RESET);

        System.out.println(ConsoleColors.BOLD_GREEN
            + "   [1]  Patient Portal"     + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD_BLUE
            + "   [2]  Doctor Dashboard"   + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD_PURPLE
            + "   [3]  Admin Panel"        + ConsoleColors.RESET);
        System.out.println(ConsoleColors.BOLD_RED
            + "   [4]  Exit"               + ConsoleColors.RESET);

        System.out.println();
        System.out.print(ConsoleColors.BOLD_CYAN
            + "  >> Select Option : "      + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Print Exit
    // ─────────────────────────────────────────
    private void printExit() {
        System.out.println(ConsoleColors.BOLD_CYAN);
        System.out.println("  =================================================================");
        System.out.println("         Thank you for using MediQueue System                     ");
        System.out.println("         Stay Healthy! Goodbye.                                   ");
        System.out.println("  =================================================================");
        System.out.println(ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Invalid Choice
    // ─────────────────────────────────────────
    private void printInvalidChoice() {
        System.out.println(ConsoleColors.BOLD_RED
            + "\n  [ERR] Invalid choice. Please try again."
            + ConsoleColors.RESET
        );
    }

    // ─────────────────────────────────────────
    // Safe Int Input
    // ─────────────────────────────────────────
    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}