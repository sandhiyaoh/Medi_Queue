// ============================================================
// FILE     : PatientObserver.java
// PACKAGE  : patterns.behavioral.observer.observers
// PURPOSE  : Observer for a specific Patient
//            Gets notified when their queue position changes
//            or when it is their turn
// PATTERN  : Observer — Concrete Observer
// ============================================================

package patterns.behavioral.observer.observers;

import models.Patient;
import models.QueueEntry;
import patterns.behavioral.observer.interfaces.QueueObserver;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.List;

public class PatientObserver implements QueueObserver {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final Patient patient;
    private       int     lastKnownPosition;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public PatientObserver(Patient patient) {
        this.patient           = patient;
        this.lastKnownPosition = -1;
    }

    // ─────────────────────────────────────────
    // On Queue Updated
    // Only reacts if this patient is in queue
    // ─────────────────────────────────────────
    @Override
    public void onQueueUpdated(String departmentCode,
                               List<QueueEntry> currentQueue) {

        // Only care about own department
        if (!departmentCode.equals(patient.getDepartment().getCode())) {
            return;
        }

        for (QueueEntry entry : currentQueue) {
            if (entry.getPatient().getPatientId()
                     .equals(patient.getPatientId())) {

                int currentPos = entry.getQueuePosition();

                // Notify only if position changed
                if (currentPos != lastKnownPosition) {
                    lastKnownPosition = currentPos;

                    System.out.println(ConsoleColors.BOLD_GREEN
                        + "\n  [PATIENT OBSERVER]["
                        + DateTimeUtils.getCurrentTime() + "]"
                        + "\n  Patient   : " + patient.getName()
                        + "\n  Update    : Queue position changed"
                        + "\n  Position  : #" + currentPos
                        + "\n  Wait Time : " + entry.getEstimatedWaitMinutes()
                              + " minutes"
                        + ConsoleColors.RESET
                    );
                }
                return;
            }
        }
    }

    // ─────────────────────────────────────────
    // On Patient Added
    // ─────────────────────────────────────────
    @Override
    public void onPatientAdded(String departmentCode,
                               QueueEntry newEntry) {

        if (!departmentCode.equals(patient.getDepartment().getCode())) {
            return;
        }

        // Only notify if THIS patient was added
        if (newEntry.getPatient().getPatientId()
                    .equals(patient.getPatientId())) {

            System.out.println(ConsoleColors.BOLD_GREEN
                + "\n  [PATIENT OBSERVER]["
                + DateTimeUtils.getCurrentTime() + "]"
                + "\n  Patient  : " + patient.getName()
                + "\n  Update   : You have been added to the queue!"
                + "\n  Token#   : "
                + String.format("%03d", newEntry.getToken().getTokenNumber())
                + "\n  Position : #" + newEntry.getQueuePosition()
                + "\n  Wait     : " + newEntry.getEstimatedWaitMinutes()
                + " minutes"
                + ConsoleColors.RESET
            );

            lastKnownPosition = newEntry.getQueuePosition();
        }
    }

    // ─────────────────────────────────────────
    // On Patient Served
    // ─────────────────────────────────────────
    @Override
    public void onPatientServed(String departmentCode,
                                QueueEntry servedEntry) {

        if (!departmentCode.equals(patient.getDepartment().getCode())) {
            return;
        }

        // Notify if THIS patient was served
        if (servedEntry.getPatient().getPatientId()
                       .equals(patient.getPatientId())) {

            System.out.println(ConsoleColors.BOLD_GREEN
                + "\n  [PATIENT OBSERVER]["
                + DateTimeUtils.getCurrentTime() + "]"
                + "\n  Patient : " + patient.getName()
                + "\n  Update  : It is your turn!"
                + "\n  Token#  : "
                + String.format("%03d", servedEntry.getToken().getTokenNumber())
                + "\n  Action  : Please proceed to the consultation room."
                + ConsoleColors.RESET
            );

            lastKnownPosition = -1;
        }
    }

    // ─────────────────────────────────────────
    // On Emergency Alert
    // ─────────────────────────────────────────
    @Override
    public void onEmergencyAlert(String departmentCode,
                                 QueueEntry emergencyEntry) {

        if (!departmentCode.equals(patient.getDepartment().getCode())) {
            return;
        }

        System.out.println(ConsoleColors.BOLD_YELLOW
            + "\n  [PATIENT OBSERVER]["
            + DateTimeUtils.getCurrentTime() + "]"
            + "\n  Patient : " + patient.getName()
            + "\n  Alert   : Emergency patient added."
            + "  Your wait time may increase slightly."
            + ConsoleColors.RESET
        );
    }

    // ─────────────────────────────────────────
    // Observer Name
    // ─────────────────────────────────────────
    @Override
    public String getObserverName() {
        return "PatientObserver[" + patient.getName() + "]";
    }
}