// ============================================================
// FILE     : Doctor.java
// PACKAGE  : models
// PURPOSE  : Represents a doctor assigned to a department
//            Tracks availability and assigned department
// ============================================================

package models;

public class Doctor {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private String     doctorId;
    private String     name;
    private String     specialization;
    private Department department;
    private boolean    isAvailable;
    private String     contactNumber;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public Doctor(String doctorId,
                  String name,
                  String specialization,
                  Department department,
                  boolean isAvailable,
                  String contactNumber) {

        this.doctorId       = doctorId;
        this.name           = name;
        this.specialization = specialization;
        this.department     = department;
        this.isAvailable    = isAvailable;
        this.contactNumber  = contactNumber;
    }

    // ─────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────
    public String     getDoctorId()       { return doctorId;       }
    public String     getName()           { return name;           }
    public String     getSpecialization() { return specialization; }
    public Department getDepartment()     { return department;     }
    public boolean    isAvailable()       { return isAvailable;    }
    public String     getContactNumber()  { return contactNumber;  }

    // ─────────────────────────────────────────
    // Setters
    // ─────────────────────────────────────────
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    // ─────────────────────────────────────────
    // Display
    // ─────────────────────────────────────────
    @Override
    public String toString() {
        return String.format(
            "Doctor [ ID: %s | Name: %-15s | Dept: %-10s | Available: %s ]",
            doctorId,
            name,
            department.getCode(),
            isAvailable ? "YES" : "NO"
        );
    }
}