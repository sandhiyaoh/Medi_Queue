// ============================================================
// FILE     : EmergencyHospitalFactory.java
// PACKAGE  : patterns.creational.abstractfactory.factories
// PURPOSE  : Concrete Factory for Emergency Department
//            Creates EmergencyQueue, EmergencyScheduler,
//            EmergencyAlertNotification
// PATTERN  : Abstract Factory — Concrete Factory
// ============================================================

package patterns.creational.abstractfactory.factories;

import models.Department;
import patterns.creational.abstractfactory.interfaces.AppointmentScheduler;
import patterns.creational.abstractfactory.interfaces.HospitalFactory;
import patterns.creational.abstractfactory.interfaces.NotificationService;
import patterns.creational.abstractfactory.interfaces.QueueManager;
import patterns.creational.abstractfactory.products.emergency.EmergencyAlertNotification;
import patterns.creational.abstractfactory.products.emergency.EmergencyQueue;
import patterns.creational.abstractfactory.products.emergency.EmergencyScheduler;

public class EmergencyHospitalFactory implements HospitalFactory {

    // ─────────────────────────────────────────
    // Create Queue Manager
    // ─────────────────────────────────────────
    @Override
    public QueueManager createQueueManager() {
        return new EmergencyQueue();
    }

    // ─────────────────────────────────────────
    // Create Scheduler
    // ─────────────────────────────────────────
    @Override
    public AppointmentScheduler createAppointmentScheduler() {
        return new EmergencyScheduler();
    }

    // ─────────────────────────────────────────
    // Create Notification Service
    // ─────────────────────────────────────────
    @Override
    public NotificationService createNotificationService() {
        return new EmergencyAlertNotification();
    }

    // ─────────────────────────────────────────
    // Department
    // ─────────────────────────────────────────
    @Override
    public Department getDepartment() {
        return Department.EMERGENCY;
    }
}