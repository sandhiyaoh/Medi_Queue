// ============================================================
// FILE     : VIPHospitalFactory.java
// PACKAGE  : patterns.creational.abstractfactory.factories
// PURPOSE  : Concrete Factory for VIP Department
//            Creates PremiumQueue, PriorityScheduler,
//            PremiumNotification
// PATTERN  : Abstract Factory — Concrete Factory
// ============================================================

package patterns.creational.abstractfactory.factories;

import models.Department;
import patterns.creational.abstractfactory.interfaces.AppointmentScheduler;
import patterns.creational.abstractfactory.interfaces.HospitalFactory;
import patterns.creational.abstractfactory.interfaces.NotificationService;
import patterns.creational.abstractfactory.interfaces.QueueManager;
import patterns.creational.abstractfactory.products.vip.PremiumNotification;
import patterns.creational.abstractfactory.products.vip.PremiumQueue;
import patterns.creational.abstractfactory.products.vip.PriorityScheduler;

public class VIPHospitalFactory implements HospitalFactory {

    // ─────────────────────────────────────────
    // Create Queue Manager
    // ─────────────────────────────────────────
    @Override
    public QueueManager createQueueManager() {
        return new PremiumQueue();
    }

    // ─────────────────────────────────────────
    // Create Scheduler
    // ─────────────────────────────────────────
    @Override
    public AppointmentScheduler createAppointmentScheduler() {
        return new PriorityScheduler();
    }

    // ─────────────────────────────────────────
    // Create Notification Service
    // ─────────────────────────────────────────
    @Override
    public NotificationService createNotificationService() {
        return new PremiumNotification();
    }

    // ─────────────────────────────────────────
    // Department
    // ─────────────────────────────────────────
    @Override
    public Department getDepartment() {
        return Department.VIP;
    }
}