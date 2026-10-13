// ============================================================
// FILE     : QueueManager.java
// PACKAGE  : patterns.creational.abstractfactory.interfaces
// PURPOSE  : Abstract interface for all department
//            queue management strategies
// PATTERN  : Abstract Factory — Product Interface
// ============================================================

package patterns.creational.abstractfactory.interfaces;

import models.Patient;
import models.QueueEntry;
import java.util.List;

public interface QueueManager {

    // ─────────────────────────────────────────
    // Core Queue Operations
    // ─────────────────────────────────────────

    // Add a patient into the queue
    void addPatientToQueue(Patient patient);

    // Remove the next patient from queue (FIFO / Priority)
    Patient serveNextPatient();

    // Check current queue size
    int getQueueSize();

    // Check if queue is empty
    boolean isQueueEmpty();

    // Display all current queue entries
    void displayQueue();

    // Get estimated wait time for a specific patient
    int getEstimatedWaitTime(String patientId);

    // Manage queue logic (core management method)
    void manageQueue();

    // Get department code this manager handles
    String getDepartmentCode();

    // Get full queue list
    List<QueueEntry> getQueueEntries();

    // Clear queue (Admin operation)
    void clearQueue();
}