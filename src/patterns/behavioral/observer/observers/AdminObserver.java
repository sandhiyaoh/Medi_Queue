// ============================================================
// FILE     : AdminObserver.java
// PACKAGE  : patterns.behavioral.observer.observers
// PURPOSE  : Observer for Hospital Admin
//            Monitors ALL departments simultaneously
//            Gets notified about every queue change
//            across General, Emergency, and VIP
// PATTERN  : Observer — Concrete Observer
// ============================================================

package patterns.behavioral.observer.observers;

import models.QueueEntry;
import patterns.behavioral.observer.interfaces.QueueObserver;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminObserver implements QueueObserver {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final String              adminName;

    // Tracks queue sizes per department
    private final Map<String, Integer> departmentQueueSizes;

    // Alert threshold
    private static final int QUEUE_ALERT_THRESHOLD = 3;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public AdminObserver(String adminName) {
        this.adminName            = adminName;
        this.departmentQueueSizes = new HashMap<>();

        departmentQueueSizes.put("GEN", 0);
        departmentQueueSizes.put("EMG", 0);
        departmentQueueSizes.put("VIP", 0);
    }

    // ─────────────────────────────────────────
    // On Queue Updated
    // Admin sees full queue stats for ALL depts
    // ─────────────────────────────────────────
    @Override
    public void onQueueUpdated(String departmentCode,
                               List<QueueEntry> currentQueue) {

        // Update tracked size
        departmentQueueSizes.put(departmentCode, currentQueue.size());

        System.out.println(ConsoleColors.BOLD_PURPLE
            + "\n  [ADMIN OBSERVER]["
            + DateTimeUtils.getCurrentTime() + "]"
            + "\n  Admin      : " + adminName
            + "\n  Department : " + departmentCode
            + "\n  Queue Size : " + currentQueue.size()
            + ConsoleColors.RESET
        );

        // Threshold warning
        if (currentQueue.size() >= QUEUE_ALERT_THRESHOLD) {
            ConsolePrinter.printWarning(
                "[ADMIN] HIGH QUEUE ALERT — "
                + departmentCode
                + " has " + currentQueue.size()
                + " patients waiting!"
            );
        }

        // Print overall stats
        printOverallStats();
    }

    // ─────────────────────────────────────────
    // On Patient Added
    // ─────────────────────────────────────────
    @Override
    public void onPatientAdded(String departmentCode,
                               QueueEntry newEntry) {

        int currentSize = departmentQueueSizes
            .getOrDefault(departmentCode, 0) + 1;
        departmentQueueSizes.put(departmentCode, currentSize);

        System.out.println(ConsoleColors.BOLD_PURPLE
            + "\n  [ADMIN OBSERVER]["
            + DateTimeUtils.getCurrentTime() + "]"
            + "\n  Admin      : " + adminName
            + "\n  Update     : New patient admitted"
            + "\n  Department : " + departmentCode
            + "\n  Patient    : "
            + newEntry.getPatient().getName()
            + "\n  Token#     : "
            + String.format("%03d",
                newEntry.getToken().getTokenNumber())
            + "\n  Dept Total : " + currentSize + " in queue"
            + ConsoleColors.RESET
        );
    }

    // ─────────────────────────────────────────
    // On Patient Served
    // ─────────────────────────────────────────
    @Override
    public void onPatientServed(String departmentCode,
                                QueueEntry servedEntry) {

        int currentSize = Math.max(
            0,
            departmentQueueSizes.getOrDefault(departmentCode, 0) - 1
        );
        departmentQueueSizes.put(departmentCode, currentSize);

        System.out.println(ConsoleColors.BOLD_PURPLE
            + "\n  [ADMIN OBSERVER]["
            + DateTimeUtils.getCurrentTime() + "]"
            + "\n  Admin      : " + adminName
            + "\n  Update     : Patient served"
            + "\n  Department : " + departmentCode
            + "\n  Patient    : "
            + servedEntry.getPatient().getName()
            + "\n  Token#     : "
            + String.format("%03d",
                servedEntry.getToken().getTokenNumber())
            + "\n  Remaining  : " + currentSize + " in queue"
            + ConsoleColors.RESET
        );
    }

    // ─────────────────────────────────────────
    // On Emergency Alert
    // Admin gets highest priority notification
    // ─────────────────────────────────────────
    @Override
    public void onEmergencyAlert(String departmentCode,
                                 QueueEntry emergencyEntry) {

        System.out.println(ConsoleColors.BG_RED
            + ConsoleColors.BOLD_WHITE
            + "\n  [ADMIN OBSERVER — CRITICAL]["
            + DateTimeUtils.getCurrentTime() + "]"
            + "\n  Admin    : " + adminName
            + "\n  CRITICAL : Emergency case in "
            + departmentCode
            + "\n  Patient  : "
            + emergencyEntry.getPatient().getName()
            + "\n  Action   : All staff alerted!"
            + ConsoleColors.RESET
        );
    }

    // ─────────────────────────────────────────
    // Print Overall Stats — All Departments
    // ─────────────────────────────────────────
    private void printOverallStats() {
        System.out.println(ConsoleColors.BOLD_PURPLE
            + "  ┌─────────────────────────────────┐"
            + "\n  │   ADMIN — OVERALL QUEUE STATS   │"
            + "\n  ├─────────────────────────────────┤"
            + "\n  │  GEN  : "
            + String.format("%-5d", departmentQueueSizes.getOrDefault("GEN", 0))
            + " patients waiting      │"
            + "\n  │  EMG  : "
            + String.format("%-5d", departmentQueueSizes.getOrDefault("EMG", 0))
            + " patients waiting      │"
            + "\n  │  VIP  : "
            + String.format("%-5d", departmentQueueSizes.getOrDefault("VIP", 0))
            + " patients waiting      │"
            + "\n  └─────────────────────────────────┘"
            + ConsoleColors.RESET
        );
    }

    // ─────────────────────────────────────────
    // Observer Name
    // ─────────────────────────────────────────
    @Override
    public String getObserverName() {
        return "AdminObserver[" + adminName + "]";
    }
}