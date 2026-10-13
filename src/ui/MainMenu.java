// ============================================================
// FILE     : MainMenu.java
// PACKAGE  : ui
// PURPOSE  : Main entry menu — routes to Patient Portal,
//            Doctor Dashboard, Admin Panel
//            Talks ONLY to MediQueueFacade + LiveQueueSubject
// ============================================================

package ui;

import models.Department;
import models.Doctor;
import patterns.behavioral.observer.observers.AdminObserver;
import patterns.behavioral.observer.observers.DoctorObserver;
import patterns.behavioral.observer.subject.LiveQueueSubject;
import patterns.structural.facade.MediQueueFacade;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.Scanner;

public class MainMenu {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final MediQueueFacade  facade;
    private final LiveQueueSubject queueSubject;
    private final Scanner          scanner;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public MainMenu() {
        this.scanner      = new Scanner(System.in);
        this.facade       = new MediQueueFacade();
        this.queueSubject = new LiveQueueSubject(facade);

        initializeDefaultDoctors();
        initializeDefaultObservers();
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

        facade.registerDoctor(d1);
        facade.registerDoctor(d2);
        facade.registerDoctor(d3);

        // Register Doctor Observers
        queueSubject.registerObserver(new DoctorObserver(d1));
        queueSubject.registerObserver(new DoctorObserver(d2));
        queueSubject.registerObserver(new DoctorObserver(d3));
    }

    // ─────────────────────────────────────────
    // Initialize Default Observers
    // ─────────────────────────────────────────
    private void initializeDefaultObservers() {
        // Admin always watching all departments
        queueSubject.registerObserver(
            new AdminObserver("Admin-MediQueue")
        );
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
                    new PatientPortal(facade, queueSubject, scanner).show();
                    break;
                case 2:
                    new DoctorDashboard(facade, scanner).show();
                    break;
                case 3:
                    new AdminPanel(facade, queueSubject, scanner).show();
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
        System.out.println("         Design Patterns : Abstract Factory | Facade | Observer   ");
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