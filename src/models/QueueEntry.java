// ============================================================
// FILE     : QueueEntry.java
// PACKAGE  : models
// PURPOSE  : Represents a single entry inside the live queue
//            Tracks position, priority, and estimated wait
// ============================================================

package models;

public class QueueEntry {

    // ─────────────────────────────────────────
    // Priority Constants
    // ─────────────────────────────────────────
    public static final int PRIORITY_EMERGENCY = 1;
    public static final int PRIORITY_VIP       = 2;
    public static final int PRIORITY_GENERAL   = 3;

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private Patient patient;
    private Token   token;
    private int     priority;
    private int     queuePosition;
    private int     estimatedWaitMinutes;
    private String  enteredQueueAt;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public QueueEntry(Patient patient,
                      Token token,
                      int priority,
                      int queuePosition,
                      int estimatedWaitMinutes,
                      String enteredQueueAt) {

        this.patient              = patient;
        this.token                = token;
        this.priority             = priority;
        this.queuePosition        = queuePosition;
        this.estimatedWaitMinutes = estimatedWaitMinutes;
        this.enteredQueueAt       = enteredQueueAt;
    }

    // ─────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────
    public Patient getPatient()              { return patient;              }
    public Token   getToken()                { return token;                }
    public int     getPriority()             { return priority;             }
    public int     getQueuePosition()        { return queuePosition;        }
    public int     getEstimatedWaitMinutes() { return estimatedWaitMinutes; }
    public String  getEnteredQueueAt()       { return enteredQueueAt;       }

    // ─────────────────────────────────────────
    // Setters
    // ─────────────────────────────────────────
    public void setQueuePosition(int position) {
        this.queuePosition = position;
    }

    public void setEstimatedWaitMinutes(int minutes) {
        this.estimatedWaitMinutes = minutes;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    // ─────────────────────────────────────────
    // Helper
    // ─────────────────────────────────────────
    public String getPriorityLabel() {
        switch (priority) {
            case PRIORITY_EMERGENCY: return "EMERGENCY";
            case PRIORITY_VIP:       return "VIP";
            default:                 return "GENERAL";
        }
    }

    // ─────────────────────────────────────────
    // Display
    // ─────────────────────────────────────────
    @Override
    public String toString() {
        return String.format(
            "QueueEntry [ Pos: %2d | Token#: %03d | Patient: %-15s | Priority: %-10s | Wait: %d mins ]",
            queuePosition,
            token.getTokenNumber(),
            patient.getName(),
            getPriorityLabel(),
            estimatedWaitMinutes
        );
    }
}