// ============================================================
// FILE     : HospitalFactory.java
// PACKAGE  : patterns.creational.abstractfactory.interfaces
// PURPOSE  : Abstract Factory Interface — defines the contract
//            for creating families of department objects
// PATTERN  : Abstract Factory — Factory Interface
// ============================================================

package patterns.creational.abstractfactory.interfaces;

import models.Department;

public interface HospitalFactory {

    // ─────────────────────────────────────────
    // Factory Methods — creates a family of
    // related objects for a specific department
    // ─────────────────────────────────────────

    // Creates department-specific Queue Manager
    QueueManager createQueueManager();

    // Creates department-specific Appointment Scheduler
    AppointmentScheduler createAppointmentScheduler();

    // Creates department-specific Notification Service
    NotificationService createNotificationService();

    // Returns the department this factory handles
    Department getDepartment();

    // Display factory info
    default void displayFactoryInfo() {
        System.out.println("  Factory : " + this.getClass().getSimpleName()
                         + " | Department : " + getDepartment().getDisplayName());
    }
}