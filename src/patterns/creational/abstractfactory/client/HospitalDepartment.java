// ============================================================
// FILE     : HospitalDepartment.java
// PACKAGE  : patterns.creational.abstractfactory.client
// PURPOSE  : Client class — uses HospitalFactory to get
//            department objects without knowing concrete types
//            Demonstrates full Abstract Factory usage
// PATTERN  : Abstract Factory — Client
// ============================================================

package patterns.creational.abstractfactory.client;

import models.Appointment;
import models.Doctor;
import models.Patient;
import patterns.creational.abstractfactory.interfaces.AppointmentScheduler;
import patterns.creational.abstractfactory.interfaces.HospitalFactory;
import patterns.creational.abstractfactory.interfaces.NotificationService;
import patterns.creational.abstractfactory.interfaces.QueueManager;
import utils.ConsolePrinter;

public class HospitalDepartment {

    // ─────────────────────────────────────────
    // Fields — all abstract types, no concrete
    // ─────────────────────────────────────────
    private final QueueManager          queueManager;
    private final AppointmentScheduler  scheduler;
    private final NotificationService   notificationService;
    private final String                departmentName;

    // ─────────────────────────────────────────
    // Constructor — Factory injects all objects
    // ─────────────────────────────────────────
    public HospitalDepartment(HospitalFactory factory) {
        this.queueManager       = factory.createQueueManager();
        this.scheduler          = factory.createAppointmentScheduler();
        this.notificationService = factory.createNotificationService();
        this.departmentName     = factory.getDepartment().getDisplayName();

        ConsolePrinter.printInfo(
            "Department Initialized → " + departmentName
            + " | Channel : " + notificationService.getNotificationChannel()
        );
    }

    // ─────────────────────────────────────────
    // Add Patient to Queue + Notify
    // ─────────────────────────────────────────
    public void admitPatient(Patient patient) {
        queueManager.addPatientToQueue(patient);
        notificationService.notifyPatient(patient,
            "You have been added to the " + departmentName + " queue."
        );
    }

    // ─────────────────────────────────────────
    // Book Appointment + Notify
    // ─────────────────────────────────────────
    public Appointment bookAppointment(Patient patient,
                                       Doctor doctor,
                                       String time) {
        Appointment apt = scheduler.schedule(patient, doctor, time);

        if (apt != null) {
            notificationService.notifyAppointmentBooked(patient, time);
            notificationService.notifyDoctor(doctor,
                "New appointment with " + patient.getName() + " at " + time
            );
        }
        return apt;
    }

    // ─────────────────────────────────────────
    // Cancel Appointment + Notify
    // ─────────────────────────────────────────
    public void cancelAppointment(Patient patient, String appointmentId) {
        boolean cancelled = scheduler.cancelAppointment(appointmentId);
        if (cancelled) {
            notificationService.notifyAppointmentCancelled(patient, appointmentId);
        }
    }

    // ─────────────────────────────────────────
    // Reschedule Appointment + Notify
    // ─────────────────────────────────────────
    public void rescheduleAppointment(Patient patient,
                                      String appointmentId,
                                      String newTime) {
        boolean rescheduled = scheduler.rescheduleAppointment(appointmentId, newTime);
        if (rescheduled) {
            notificationService.notifyAppointmentBooked(patient, newTime);
        }
    }

    // ─────────────────────────────────────────
    // Serve Next Patient + Notify
    // ─────────────────────────────────────────
// ✅ AFTER — serve and pass actual QueueEntry with token number
public Patient serveNextPatient() {

    if (queueManager.isQueueEmpty()) {
        ConsolePrinter.printWarning("Queue is empty. No patients to serve.");
        return null;
    }

    // ─────────────────────────────────────────
    // Get token number BEFORE serving
    // ─────────────────────────────────────────
    int tokenNumber = 0;

    if (!queueManager.getQueueEntries().isEmpty()) {
        tokenNumber = queueManager.getQueueEntries()
                                  .get(0)
                                  .getToken()
                                  .getTokenNumber();   // ← get actual token number
    }

    Patient patient = queueManager.serveNextPatient();

    if (patient != null) {
        notificationService.notifyPatientTurn(patient, tokenNumber);  // ← pass real token
    }

    return patient;
}

    // ─────────────────────────────────────────
    // Display Queue Status
    // ─────────────────────────────────────────
    public void displayQueueStatus() {
        queueManager.displayQueue();
        queueManager.manageQueue();
    }

    // ─────────────────────────────────────────
    // Display Schedule
    // ─────────────────────────────────────────
    public void displaySchedule() {
        scheduler.displaySchedule();
    }

    // ─────────────────────────────────────────
    // Get Wait Time
    // ─────────────────────────────────────────
    public int getWaitTime(String patientId) {
        return queueManager.getEstimatedWaitTime(patientId);
    }

    // ─────────────────────────────────────────
    // Broadcast Emergency
    // ─────────────────────────────────────────
    public void broadcastAlert(String message) {
        notificationService.broadcastEmergencyAlert(message);
    }

    // ─────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────
    public QueueManager         getQueueManager()        { return queueManager;        }
    public AppointmentScheduler getScheduler()           { return scheduler;           }
    public NotificationService  getNotificationService() { return notificationService; }
    public String               getDepartmentName()      { return departmentName;      }
}