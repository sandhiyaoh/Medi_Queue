// ============================================================
// FILE     : GeneralQueue.java
// PACKAGE  : patterns.creational.abstractfactory.products.general
// PURPOSE  : General Department Queue Manager
//            FIFO strategy — First Come First Serve
// PATTERN  : Abstract Factory — Concrete Product
// ============================================================

package patterns.creational.abstractfactory.products.general;

import models.Patient;
import models.QueueEntry;
import models.Token;
import patterns.creational.abstractfactory.interfaces.QueueManager;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GeneralQueue implements QueueManager {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private static final String DEPT_CODE        = "GEN";
    private static final int    AVG_CONSULT_MINS = 10;

    private final Queue<QueueEntry> queue;
    private int tokenCounter;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public GeneralQueue() {
        this.queue        = new LinkedList<>();
        this.tokenCounter = 1;
    }

    // ─────────────────────────────────────────
    // Add Patient — FIFO Strategy
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

        int position     = queue.size() + 1;
        int waitTime     = DateTimeUtils.calculateWaitTime(position);

        QueueEntry entry = new QueueEntry(
            patient,
            token,
            QueueEntry.PRIORITY_GENERAL,
            position,
            waitTime,
            DateTimeUtils.getCurrentTimestamp()
        );

        queue.offer(entry);
        tokenCounter++;

        ConsolePrinter.printSuccess(
            ConsoleColors.BOLD_GREEN
            + "[GENERAL] Patient Added → "
            + patient.getName()
            + " | Token#: " + String.format("%03d", token.getTokenNumber())
            + " | Wait: " + waitTime + " mins"
            + ConsoleColors.RESET
        );
    }

    // ─────────────────────────────────────────
    // Serve Next — FIFO
    // ─────────────────────────────────────────
    @Override
    public Patient serveNextPatient() {
        if (isQueueEmpty()) {
            ConsolePrinter.printWarning("[GENERAL] Queue is Empty. No patients to serve.");
            return null;
        }

        QueueEntry entry = queue.poll();
        entry.getToken().setServed(true);
        recalculatePositions();

        ConsolePrinter.printSuccess(
            "[GENERAL] Serving Patient → "
            + entry.getPatient().getName()
            + " | Token#: "
            + String.format("%03d", entry.getToken().getTokenNumber())
        );

        return entry.getPatient();
    }

    // ─────────────────────────────────────────
    // Recalculate Queue Positions
    // ─────────────────────────────────────────
    private void recalculatePositions() {
        int pos = 1;
        for (QueueEntry e : queue) {
            e.setQueuePosition(pos);
            e.setEstimatedWaitMinutes(pos * AVG_CONSULT_MINS);
            pos++;
        }
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
        ConsolePrinter.printDepartmentHeader(DEPT_CODE, "GENERAL DEPARTMENT — Live Queue");

        if (isQueueEmpty()) {
            ConsolePrinter.printInfo("Queue is currently empty.");
            return;
        }

        System.out.println(ConsoleColors.BOLD_GREEN);
        System.out.printf("  %-5s %-10s %-20s %-12s %-12s%n",
            "Pos", "Token#", "Patient Name", "Priority", "Wait (mins)");
        System.out.println(ConsolePrinter.printLine("-"));

        for (QueueEntry entry : queue) {
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
    // Get Estimated Wait Time
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
        System.out.println(ConsoleColors.BOLD_GREEN
            + "  [GENERAL] Queue Manager → FIFO Strategy Active"
            + " | Patients in Queue : " + getQueueSize()
            + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Get Queue Entries
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
        ConsolePrinter.printWarning("[GENERAL] Queue has been cleared by Admin.");
    }

    // ─────────────────────────────────────────
    // Department Code
    // ─────────────────────────────────────────
    @Override
    public String getDepartmentCode() {
        return DEPT_CODE;
    }
}