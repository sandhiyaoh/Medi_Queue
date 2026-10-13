// ============================================================
// FILE     : QueueObserver.java
// PACKAGE  : patterns.behavioral.observer.interfaces
// PURPOSE  : Observer interface — every observer (Patient,
//            Doctor, Admin) must implement this interface
//            Gets called when queue state changes
// PATTERN  : Observer — Observer Interface
// ============================================================

package patterns.behavioral.observer.interfaces;

import models.QueueEntry;
import java.util.List;

public interface QueueObserver {

    // ─────────────────────────────────────────
    // Called when queue state changes
    // ─────────────────────────────────────────
    void onQueueUpdated(String departmentCode,
                        List<QueueEntry> currentQueue);

    // ─────────────────────────────────────────
    // Called when new patient is added
    // ─────────────────────────────────────────
    void onPatientAdded(String departmentCode,
                        QueueEntry newEntry);

    // ─────────────────────────────────────────
    // Called when patient is served / removed
    // ─────────────────────────────────────────
    void onPatientServed(String departmentCode,
                         QueueEntry servedEntry);

    // ─────────────────────────────────────────
    // Called when emergency patient is added
    // ─────────────────────────────────────────
    void onEmergencyAlert(String departmentCode,
                          QueueEntry emergencyEntry);

    // ─────────────────────────────────────────
    // Get observer name/type
    // ─────────────────────────────────────────
    String getObserverName();
}