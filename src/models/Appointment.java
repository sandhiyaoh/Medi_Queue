// ============================================================
// FILE     : Appointment.java
// PACKAGE  : models
// PURPOSE  : Represents a booked appointment between
//            a patient and a doctor in a department
// ============================================================

package models;

public class Appointment {

    // ─────────────────────────────────────────
    // Appointment Status Enum (inner)
    // ─────────────────────────────────────────
    public enum Status {
        BOOKED,
        CANCELLED,
        RESCHEDULED,
        COMPLETED
    }

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private String     appointmentId;
    private Patient    patient;
    private Doctor     doctor;
    private Department department;
    private String     appointmentTime;
    private Status     status;
    private String     bookedAt;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public Appointment(String appointmentId,
                       Patient patient,
                       Doctor doctor,
                       Department department,
                       String appointmentTime,
                       String bookedAt) {

        this.appointmentId   = appointmentId;
        this.patient         = patient;
        this.doctor          = doctor;
        this.department      = department;
        this.appointmentTime = appointmentTime;
        this.bookedAt        = bookedAt;
        this.status          = Status.BOOKED;
    }

    // ─────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────
    public String     getAppointmentId()   { return appointmentId;   }
    public Patient    getPatient()         { return patient;         }
    public Doctor     getDoctor()          { return doctor;          }
    public Department getDepartment()      { return department;      }
    public String     getAppointmentTime() { return appointmentTime; }
    public Status     getStatus()          { return status;          }
    public String     getBookedAt()        { return bookedAt;        }

    // ─────────────────────────────────────────
    // Setters
    // ─────────────────────────────────────────
    public void setStatus(Status status) {
        this.status = status;
    }

    public void setAppointmentTime(String newTime) {
        this.appointmentTime = newTime;
    }

    // ─────────────────────────────────────────
    // Display
    // ─────────────────────────────────────────
    @Override
    public String toString() {
        return String.format(
            "Appointment [ ID: %s | Patient: %-12s | Doctor: %-12s | Dept: %-10s | Time: %s | Status: %s ]",
            appointmentId,
            patient.getName(),
            doctor.getName(),
            department.getCode(),
            appointmentTime,
            status
        );
    }
}