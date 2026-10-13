// ============================================================
// FILE     : GeneralHospitalFactory.java
// PACKAGE  : patterns.creational.abstractfactory.factories
// PURPOSE  : Concrete Factory for General Department
//            Creates GeneralQueue, GeneralScheduler, SMSNotification
// PATTERN  : Abstract Factory — Concrete Factory
// ============================================================

package patterns.creational.abstractfactory.factories;

import models.Department;
import patterns.creational.abstractfactory.interfaces.AppointmentScheduler;
import patterns.creational.abstractfactory.interfaces.HospitalFactory;
import patterns.creational.abstractfactory.interfaces.NotificationService;
import patterns.creational.abstractfactory.interfaces.QueueManager;
import patterns.creational.abstractfactory.products.general.GeneralQueue;
import patterns.creational.abstractfactory.products.general.GeneralScheduler;
import patterns.creational.abstractfactory.products.general.SMSNotification;

public class GeneralHospitalFactory implements HospitalFactory {

    // ─────────────────────────────────────────
    // Create Queue Manager
    // ─────────────────────────────────────────
    @Override
    public QueueManager createQueueManager() {
        return new GeneralQueue();
    }

    // ─────────────────────────────────────────
    // Create Scheduler
    // ─────────────────────────────────────────
    @Override
    public AppointmentScheduler createAppointmentScheduler() {
        return new GeneralScheduler();
    }

    // ─────────────────────────────────────────
    // Create Notification Service
    // ─────────────────────────────────────────
    @Override
    public NotificationService createNotificationService() {
        return new SMSNotification();
    }

    // ─────────────────────────────────────────
    // Department
    // ─────────────────────────────────────────
    @Override
    public Department getDepartment() {
        return Department.GENERAL;
    }
}