// ============================================================
// FILE     : Patient.java
// PACKAGE  : models
// PURPOSE  : Represents a registered patient in the system
//            Holds personal info, department, and token ref
// ============================================================

package models;

public class Patient {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private String     patientId;
    private String     name;
    private int        age;
    private String     contact;
    private String     email;
    private Department department;
    private boolean    isEmergency;
    private String     registeredAt;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public Patient(String patientId,
                   String name,
                   int age,
                   String contact,
                   String email,
                   Department department,
                   boolean isEmergency,
                   String registeredAt) {

        this.patientId    = patientId;
        this.name         = name;
        this.age          = age;
        this.contact      = contact;
        this.email        = email;
        this.department   = department;
        this.isEmergency  = isEmergency;
        this.registeredAt = registeredAt;
    }

    // ─────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────
    public String     getPatientId()    { return patientId;    }
    public String     getName()         { return name;         }
    public int        getAge()          { return age;          }
    public String     getContact()      { return contact;      }
    public String     getEmail()        { return email;        }
    public Department getDepartment()   { return department;   }
    public boolean    isEmergency()     { return isEmergency;  }
    public String     getRegisteredAt() { return registeredAt; }

    // ─────────────────────────────────────────
    // Setters
    // ─────────────────────────────────────────
    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setEmergency(boolean emergency) {
        this.isEmergency = emergency;
    }

    // ─────────────────────────────────────────
    // Display
    // ─────────────────────────────────────────
    @Override
    public String toString() {
        return String.format(
            "Patient [ ID: %s | Name: %-15s | Age: %2d | Dept: %-10s | Emergency: %s ]",
            patientId,
            name,
            age,
            department.getCode(),
            isEmergency ? "YES" : "NO"
        );
    }
}