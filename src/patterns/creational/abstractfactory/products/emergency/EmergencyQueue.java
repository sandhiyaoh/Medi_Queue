// ============================================================
// FILE     : EmergencyQueue.java
// PACKAGE  : patterns.creational.abstractfactory.products.emergency
// PURPOSE  : Emergency Department Queue Manager
//            Priority strategy — highest severity served first
// PATTERN  : Abstract Factory — Concrete Product
// ============================================================

package patterns.creational.abstractfactory.products.emergency;

import models.Patient;
import models.QueueEntry;
import models.Token;
import patterns.creational.abstractfactory.interfaces.QueueManager;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class EmergencyQueue implements QueueManager {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private static final String DEPT_CODE        = "EMG";
    private static final int    AVG_CONSULT_MINS = 5;

    // Priority Queue — lower priority number = served first
    private final PriorityQueue<QueueEntry> queue;
    private int tokenCounter;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public EmergencyQueue() {
        this.queue        = new PriorityQueue<>(
            Comparator.comparingInt(QueueEntry::getPriority)
        );
        this.tokenCounter = 1;
    }

    // ─────────────────────────────────────────
    // Add Patient — Priority Strategy
    // ─────────────────────────────────────────
    @Override
    public void addPatientToQueue(Patient patient) {

        Token token = new Token(
            DateTimeUtils.generateTokenId(DEPT_CODE, tokenCounter),
            patient.getPatientId(),
            patient.getName(),
            patient.getDepartment(),
            tokenCounter,
            DateTimeUtils.getCurrentTimestamp()
        );

        // Emergency always gets PRIORITY_EMERGENCY = 1
        int priority = QueueEntry.PRIORITY_EMERGENCY;
        int position = queue.size() + 1;
        int waitTime = DateTimeUtils.calculateEmergencyWaitTime(position);

        QueueEntry entry = new QueueEntry(
            patient,
            token,
            priority,
            position,
            waitTime,
            DateTimeUtils.getCurrentTimestamp()
        );

        queue.offer(entry);
        tokenCounter++;

        ConsolePrinter.printEmergencyAlert(
            "[EMERGENCY] Patient Added → "
            + patient.getName()
            + " | Token#: " + String.format("%03d", token.getTokenNumber())
            + " | Priority: EMERGENCY | Wait: " + waitTime + " mins"
        );
    }

    // ─────────────────────────────────────────
    // Serve Next — Highest Priority First
    // ─────────────────────────────────────────
    @Override
    public Patient serveNextPatient() {
        if (isQueueEmpty()) {
            ConsolePrinter.printWarning("[EMERGENCY] Queue is Empty.");
            return null;
        }

        QueueEntry entry = queue.poll();
        entry.getToken().setServed(true);

        ConsolePrinter.printEmergencyAlert(
            "[EMERGENCY] Serving PRIORITY Patient → "
            + entry.getPatient().getName()
            + " | Token#: " + String.format("%03d", entry.getToken().getTokenNumber())
        );

        return entry.getPatient();
    }

    // ─────────────────────────────────────────
    // Queue Size
    // ─────────────────────────────────────────
    @Override
    public int getQueueSize() {
        return queue.size();
    }

    // ─────────────────────────────────────────
    // Is Empty
    // ─────────────────────────────────────────
    @Override
    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    // ─────────────────────────────────────────
    // Display Queue
    // ─────────────────────────────────────────
    @Override
    public void displayQueue() {
        ConsolePrinter.printDepartmentHeader(DEPT_CODE, "EMERGENCY DEPARTMENT — Priority Queue");

        if (isQueueEmpty()) {
            ConsolePrinter.printInfo("Emergency queue is currently clear.");
            return;
        }

        List<QueueEntry> sorted = new ArrayList<>(queue);
        sorted.sort(Comparator.comparingInt(QueueEntry::getPriority));

        System.out.println(ConsoleColors.BOLD_RED);
        System.out.printf("  %-5s %-10s %-20s %-12s %-12s%n",
            "Pos", "Token#", "Patient Name", "Priority", "Wait (mins)");
        System.out.println(ConsolePrinter.printLine("-"));

        for (QueueEntry entry : sorted) {
            System.out.printf("  %-5d %-10s %-20s %-12s %-12d%n",
                entry.getQueuePosition(),
                String.format("%03d", entry.getToken().getTokenNumber()),
                entry.getPatient().getName(),
                entry.getPriorityLabel(),
                entry.getEstimatedWaitMinutes()
            );
        }
        System.out.println(ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Estimated Wait Time
    // ─────────────────────────────────────────
    @Override
    public int getEstimatedWaitTime(String patientId) {
        for (QueueEntry entry : queue) {
            if (entry.getPatient().getPatientId().equals(patientId)) {
                return entry.getEstimatedWaitMinutes();
            }
        }
        return -1;
    }

    // ─────────────────────────────────────────
    // Manage Queue
    // ─────────────────────────────────────────
    @Override
    public void manageQueue() {
        System.out.println(ConsoleColors.BOLD_RED
            + "  [EMERGENCY] Queue Manager → Priority Strategy Active"
            + " | Patients in Queue : " + getQueueSize()
            + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Queue Entries
    // ─────────────────────────────────────────
    @Override
    public List<QueueEntry> getQueueEntries() {
        return new ArrayList<>(queue);
    }

    // ─────────────────────────────────────────
    // Clear Queue
    // ─────────────────────────────────────────
    @Override
    public void clearQueue() {
        queue.clear();
        ConsolePrinter.printWarning("[EMERGENCY] Queue has been cleared by Admin.");
    }

    // ─────────────────────────────────────────
    // Department Code
    // ─────────────────────────────────────────
    @Override
    public String getDepartmentCode() {
        return DEPT_CODE;
    }
}