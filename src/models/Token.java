// ============================================================
// FILE     : Token.java
// PACKAGE  : models
// PURPOSE  : Represents a digital token issued to a patient
//            after appointment booking
// ============================================================

package models;

public class Token {

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private String     tokenId;
    private String     patientId;
    private String     patientName;
    private Department department;
    private int        tokenNumber;
    private String     issuedAt;
    private boolean    isServed;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public Token(String tokenId,
                 String patientId,
                 String patientName,
                 Department department,
                 int tokenNumber,
                 String issuedAt) {

        this.tokenId     = tokenId;
        this.patientId   = patientId;
        this.patientName = patientName;
        this.department  = department;
        this.tokenNumber = tokenNumber;
        this.issuedAt    = issuedAt;
        this.isServed    = false;
    }

    // ─────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────
    public String     getTokenId()     { return tokenId;     }
    public String     getPatientId()   { return patientId;   }
    public String     getPatientName() { return patientName; }
    public Department getDepartment()  { return department;  }
    public int        getTokenNumber() { return tokenNumber; }
    public String     getIssuedAt()    { return issuedAt;    }
    public boolean    isServed()       { return isServed;    }

    // ─────────────────────────────────────────
    // Setters
    // ─────────────────────────────────────────
    public void setServed(boolean served) {
        this.isServed = served;
    }

    // ─────────────────────────────────────────
    // Display
    // ─────────────────────────────────────────
    @Override
    public String toString() {
        return String.format(
            "Token [ ID: %s | Token#: %03d | Patient: %-15s | Dept: %-10s | Issued: %s | Served: %s ]",
            tokenId,
            tokenNumber,
            patientName,
            department.getCode(),
            issuedAt,
            isServed ? "YES" : "NO"
        );
    }
}