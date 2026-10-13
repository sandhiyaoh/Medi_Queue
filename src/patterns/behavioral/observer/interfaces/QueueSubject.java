// ============================================================
// FILE     : QueueSubject.java
// PACKAGE  : patterns.behavioral.observer.interfaces
// PURPOSE  : Subject interface — LiveQueueSubject implements
//            this. Manages registration and notification
//            of all observers
// PATTERN  : Observer — Subject Interface
// ============================================================

package patterns.behavioral.observer.interfaces;

import models.QueueEntry;

public interface QueueSubject {

    // ─────────────────────────────────────────
    // Register an observer
    // ─────────────────────────────────────────
    void registerObserver(QueueObserver observer);

    // ─────────────────────────────────────────
    // Remove an observer
    // ─────────────────────────────────────────
    void removeObserver(QueueObserver observer);

    // ─────────────────────────────────────────
    // Notify all observers — queue updated
    // ─────────────────────────────────────────
    void notifyQueueUpdated(String departmentCode);

    // ─────────────────────────────────────────
    // Notify all observers — patient added
    // ─────────────────────────────────────────
    void notifyPatientAdded(String departmentCode,
                            QueueEntry newEntry);

    // ─────────────────────────────────────────
    // Notify all observers — patient served
    // ─────────────────────────────────────────
    void notifyPatientServed(String departmentCode,
                             QueueEntry servedEntry);

    // ─────────────────────────────────────────
    // Notify all observers — emergency
    // ─────────────────────────────────────────
    void notifyEmergencyAlert(String departmentCode,
                              QueueEntry emergencyEntry);
}