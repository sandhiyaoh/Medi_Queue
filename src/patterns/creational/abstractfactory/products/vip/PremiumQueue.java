// ============================================================
// FILE     : PremiumQueue.java
// PACKAGE  : patterns.creational.abstractfactory.products.vip
// PURPOSE  : VIP Department Queue Manager
//            Priority strategy — VIP patients served first
//            with premium comfort standards
// PATTERN  : Abstract Factory — Concrete Product
// ============================================================

package patterns.creational.abstractfactory.products.vip;

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

public class PremiumQueue implements QueueManager {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private static final String DEPT_CODE        = "VIP";
    private static final int    AVG_CONSULT_MINS = 7;

    private final PriorityQueue<QueueEntry> queue;
    private int tokenCounter;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public PremiumQueue() {
        this.queue        = new PriorityQueue<>(
            Comparator.comparingInt(QueueEntry::getPriority)
        );
        this.tokenCounter = 1;
    }

    // ─────────────────────────────────────────
    // Add Patient — VIP Priority
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

        int priority = QueueEntry.PRIORITY_VIP;
        int position = queue.size() + 1;
        int waitTime = DateTimeUtils.calculateVIPWaitTime(position);

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

        System.out.println(ConsoleColors.BOLD_PURPLE
            + "  [VIP] ★ Premium Patient Added → "
            + patient.getName()
            + " | Token#: " + String.format("%03d", token.getTokenNumber())
            + " | VIP Wait: " + waitTime + " mins"
            + ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Serve Next — VIP Priority
    // ─────────────────────────────────────────
    @Override
    public Patient serveNextPatient() {
        if (isQueueEmpty()) {
            ConsolePrinter.printWarning("[VIP] Premium Queue is Empty.");
            return null;
        }

        QueueEntry entry = queue.poll();
        entry.getToken().setServed(true);

        System.out.println(ConsoleColors.BOLD_PURPLE
            + "  [VIP] ★ Serving VIP Patient → "
            + entry.getPatient().getName()
            + " | Token#: " + String.format("%03d", entry.getToken().getTokenNumber())
            + ConsoleColors.RESET);

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
        ConsolePrinter.printDepartmentHeader(DEPT_CODE, "VIP DEPARTMENT — Premium Queue");

        if (isQueueEmpty()) {
            ConsolePrinter.printInfo("VIP Queue is currently empty.");
            return;
        }

        List<QueueEntry> sorted = new ArrayList<>(queue);
        sorted.sort(Comparator.comparingInt(QueueEntry::getPriority));

        System.out.println(ConsoleColors.BOLD_PURPLE);
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
        System.out.println(ConsoleColors.BOLD_PURPLE
            + "  [VIP] ★ Queue Manager → Premium Priority Strategy Active"
            + " | VIP Patients : " + getQueueSize()
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
        ConsolePrinter.printWarning("[VIP] Premium Queue has been cleared.");
    }

    // ─────────────────────────────────────────
    // Department Code
    // ─────────────────────────────────────────
    @Override
    public String getDepartmentCode() {
        return DEPT_CODE;
    }
}