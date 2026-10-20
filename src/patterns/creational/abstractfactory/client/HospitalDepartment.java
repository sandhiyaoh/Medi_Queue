// ============================================================
// FILE     : HospitalDepartment.java
// PACKAGE  : patterns.creational.abstractfactory.client
// PURPOSE  : Client class — uses HospitalFactory to get
//            department objects without knowing concrete types
//            Demonstrates full Abstract Factory usage
// PATTERN  : Abstract Factory — Client
// TEAM     : Sandhiya O H (23MIS0114)
//            Deepak Kumar S (23MIS0191)
// ============================================================

package patterns.creational.abstractfactory.client;

import models.Appointment;
import models.Doctor;
import models.Department;
import models.Patient;
import patterns.creational.abstractfactory.interfaces.AppointmentScheduler;
import patterns.creational.abstractfactory.interfaces.HospitalFactory;
import patterns.creational.abstractfactory.interfaces.NotificationService;
import patterns.creational.abstractfactory.interfaces.QueueManager;
import utils.ConsolePrinter;
import utils.ConsoleColors;

import java.util.ArrayList;
import java.util.List;

public class HospitalDepartment {

    // ─────────────────────────────────────────
    // Fields — all abstract types, no concrete
    // ─────────────────────────────────────────
    private final QueueManager          queueManager;
    private final AppointmentScheduler  scheduler;
    private final NotificationService   notificationService;
    private final String                departmentName;
    private final Department            department;

    // Doctor and patient registries
    private final List<Doctor>          doctors   = new ArrayList<>();
    private final List<Patient>         patients  = new ArrayList<>();

    // ─────────────────────────────────────────
    // Constructor — Factory injects all objects
    // ─────────────────────────────────────────
    public HospitalDepartment(HospitalFactory factory) {
        this.queueManager        = factory.createQueueManager();
        this.scheduler           = factory.createAppointmentScheduler();
        this.notificationService = factory.createNotificationService();
        this.departmentName      = factory.getDepartment().getDisplayName();
        this.department          = factory.getDepartment();

        ConsolePrinter.printInfo(
            "Department Initialized → " + departmentName
            + " | Channel : " + notificationService.getNotificationChannel()
        );
    }

    // ─────────────────────────────────────────
    // Register Doctor
    // ─────────────────────────────────────────
    public void registerDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    // ─────────────────────────────────────────
    // Get Available Doctors
    // ─────────────────────────────────────────
    public List<Doctor> getAvailableDoctors() {
        List<Doctor> available = new ArrayList<>();
        for (Doctor d : doctors) {
            if (d.isAvailable()) available.add(d);
        }
        return available;
    }

    // ─────────────────────────────────────────
    // Get All Doctors
    // ─────────────────────────────────────────
    public List<Doctor> getAllDoctors() {
        return doctors;
    }

    // ─────────────────────────────────────────
    // Register Patient (track registered patients)
    // ─────────────────────────────────────────
    public void registerPatient(Patient patient) {
        patients.add(patient);
    }

    // ─────────────────────────────────────────
    // Get All Registered Patients
    // ─────────────────────────────────────────
    public List<Patient> getAllPatients() {
        return patients;
    }

    // ─────────────────────────────────────────
    // Get Department
    // ─────────────────────────────────────────
    public Department getDepartment() {
        return department;
    }

    // ─────────────────────────────────────────
    // Display All Doctors
    // ─────────────────────────────────────────
    public void displayAllDoctors() {
        if (doctors.isEmpty()) {
            ConsolePrinter.printWarning("No doctors registered in " + departmentName);
            return;
        }
        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  Doctors in " + departmentName + " :" + ConsoleColors.RESET);
        for (Doctor d : doctors) {
            System.out.println(ConsoleColors.BOLD_WHITE
                + "   Dr. " + d.getName()
                + " | " + d.getSpecialization()
                + " | " + (d.isAvailable() ? "Available" : "Unavailable")
                + ConsoleColors.RESET);
        }
    }

    // ─────────────────────────────────────────
    // Display All Patients
    // ─────────────────────────────────────────
    public void displayAllPatients() {
        if (patients.isEmpty()) {
            ConsolePrinter.printWarning("No patients registered in " + departmentName);
            return;
        }
        System.out.println(ConsoleColors.BOLD_WHITE
            + "\n  Patients in " + departmentName + " :" + ConsoleColors.RESET);
        for (Patient p : patients) {
            System.out.println(ConsoleColors.BOLD_WHITE
                + "   [" + p.getPatientId() + "] "
                + p.getName() + " | Age: " + p.getAge()
                + " | " + (p.isEmergency() ? "EMERGENCY" : "Regular")
                + ConsoleColors.RESET);
        }
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