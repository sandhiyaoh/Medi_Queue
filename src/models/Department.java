// ============================================================
// FILE     : Department.java
// PACKAGE  : models
// PURPOSE  : Enum representing all hospital departments
//            Used across Factory, Observer, and Facade layers
// ============================================================

package models;

public enum Department {

    GENERAL("General Department", "GEN"),
    EMERGENCY("Emergency Department", "EMG"),
    VIP("VIP Department", "VIP");

    // ─────────────────────────────────────────
    // Fields
    // ─────────────────────────────────────────
    private final String displayName;
    private final String code;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    Department(String displayName, String code) {
        this.displayName = displayName;
        this.code        = code;
    }

    // ─────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────
    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    // ─────────────────────────────────────────
    // Display
    // ─────────────────────────────────────────
    @Override
    public String toString() {
        return "[" + code + "] " + displayName;
    }
}