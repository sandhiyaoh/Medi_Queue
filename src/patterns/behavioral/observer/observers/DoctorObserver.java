// ============================================================
// FILE     : DoctorObserver.java
// PACKAGE  : patterns.behavioral.observer.observers
// PURPOSE  : Observer for a Doctor
//            Gets notified about next patient, queue size,
//            and emergency arrivals in their department
// PATTERN  : Observer — Concrete Observer
// ============================================================

package patterns.behavioral.observer.observers;

import models.Doctor;
import models.QueueEntry;
import patterns.behavioral.observer.interfaces.QueueObserver;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.List;

public class DoctorObserver implements QueueObserver {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final Doctor doctor;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public DoctorObserver(Doctor doctor) {
        this.doctor = doctor;
    }

    // ─────────────────────────────────────────
    // On Queue Updated
    // Doctor sees current queue size + next patient
    // ─────────────────────────────────────────
    @Override
    public void onQueueUpdated(String departmentCode,
            List<QueueEntry> currentQueue) {

        if (!departmentCode.equals(
                doctor.getDepartment().getCode())) {
            return;
        }

        System.out.println(ConsoleColors.BOLD_BLUE
                + "\n  [DOCTOR OBSERVER]["
                + DateTimeUtils.getCurrentTime() + "]"
                + "\n  Doctor     : Dr." + doctor.getName()
                + "\n  Department : "
                + doctor.getDepartment().getDisplayName()
                + "\n  Update     : Queue updated"
                + "\n  Queue Size : " + currentQueue.size()
                + ConsoleColors.RESET);

        // Show next patient info if queue not empty
        if (!currentQueue.isEmpty()) {
            QueueEntry next = currentQueue.get(0);
            System.out.println(ConsoleColors.BOLD_BLUE
                    + "  Next Patient : " + next.getPatient().getName()
                    + " | Token# : "
                    + String.format("%03d",
                            next.getToken().getTokenNumber())
                    + ConsoleColors.RESET);
        }
    }

    // ─────────────────────────────────────────
    // On Patient Added
    // ─────────────────────────────────────────
    @Override
    public void onPatientAdded(String departmentCode,
            QueueEntry newEntry) {

        if (!departmentCode.equals(
                doctor.getDepartment().getCode())) {
            return;
        }

        System.out.println(ConsoleColors.BOLD_BLUE
                + "\n  [DOCTOR OBSERVER]["
                + DateTimeUtils.getCurrentTime() + "]"
                + "\n  Doctor  : Dr." + doctor.getName()
                + "\n  Update  : New patient added to queue"
                + "\n  Patient : " + newEntry.getPatient().getName()
                + "\n  Token#  : "
                + String.format("%03d",
                        newEntry.getToken().getTokenNumber())
                + "\n  Position: #" + newEntry.getQueuePosition()
                + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // On Patient Served
    // Doctor sees who was just served
    // and who is next
    // ─────────────────────────────────────────
    @Override
    public void onPatientServed(String departmentCode,
            QueueEntry servedEntry) {

        if (!departmentCode.equals(
                doctor.getDepartment().getCode())) {
            return;
        }

        System.out.println(ConsoleColors.BOLD_BLUE
                + "\n  [DOCTOR OBSERVER]["
                + DateTimeUtils.getCurrentTime() + "]"
                + "\n  Doctor  : Dr." + doctor.getName()
                + "\n  Update  : Patient consultation complete"
                + "\n  Served  : " + servedEntry.getPatient().getName()
                + "\n  Token#  : "
                + String.format("%03d",
                        servedEntry.getToken().getTokenNumber())
                + "\n  Action  : Ready for next patient."
                + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // On Emergency Alert
    // Doctor gets immediate alert
    // ─────────────────────────────────────────
    @Override
    public void onEmergencyAlert(String departmentCode,
            QueueEntry emergencyEntry) {

        System.out.println(ConsoleColors.BOLD_RED
                + "\n  [DOCTOR OBSERVER — EMERGENCY]["
                + DateTimeUtils.getCurrentTime() + "]"
                + "\n  Doctor   : Dr." + doctor.getName()
                + "\n  ALERT    : Emergency patient incoming!"
                + "\n  Patient  : "
                + emergencyEntry.getPatient().getName()
                + "\n  Priority : EMERGENCY — Attend immediately!"
                + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Observer Name
    // ─────────────────────────────────────────
    @Override
    public String getObserverName() {
        return "DoctorObserver[Dr." + doctor.getName() + "]";
    }
}