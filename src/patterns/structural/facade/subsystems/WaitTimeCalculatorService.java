// ============================================================
// FILE     : WaitTimeCalculatorService.java
// PACKAGE  : patterns.structural.facade.subsystems
// PURPOSE  : Calculates and displays estimated wait times
//            for patients across all departments
// PATTERN  : Facade — Subsystem Class
// ============================================================

package patterns.structural.facade.subsystems;

import models.Department;
import models.Patient;
import patterns.creational.abstractfactory.client.HospitalDepartment;
import utils.ConsolePrinter;
import utils.ConsoleColors;

import java.util.Map;

public class WaitTimeCalculatorService {

    // ─────────────────────────────────────────
    // Department References
    // ─────────────────────────────────────────
    private final Map<String, HospitalDepartment> departments;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public WaitTimeCalculatorService(Map<String, HospitalDepartment> departments) {
        this.departments = departments;
    }

    // ─────────────────────────────────────────
    // Get Wait Time For Patient
    // ─────────────────────────────────────────
    public int getWaitTime(Patient patient) {

        String deptCode = patient.getDepartment().getCode();
        HospitalDepartment dept = departments.get(deptCode);

        if (dept == null) {
            ConsolePrinter.printError(
                "[WAIT TIME] Department not found : " + deptCode
            );
            return -1;
        }

        int waitTime = dept.getWaitTime(patient.getPatientId());

        if (waitTime == -1) {
            ConsolePrinter.printWarning(
                "[WAIT TIME] Patient not found in queue : " + patient.getName()
            );
        } else {
            System.out.println(ConsoleColors.BOLD_CYAN
                + "  [WAIT TIME] Estimated Wait"
                + "\n  Patient : " + patient.getName()
                + "\n  Dept    : " + patient.getDepartment().getDisplayName()
                + "\n  Wait    : " + waitTime + " minutes"
                + ConsoleColors.RESET
            );
        }

        return waitTime;
    }

    // ─────────────────────────────────────────
    // Display All Department Queue Sizes
    // ─────────────────────────────────────────
    public void displayAllDepartmentStatus() {
        ConsolePrinter.printSectionHeader("ALL DEPARTMENT QUEUE STATUS");

        System.out.println(ConsoleColors.BOLD_CYAN);
        System.out.printf("  %-25s %-15s %-20s%n",
            "Department", "Queue Size", "Avg Wait (mins)");
        System.out.println(ConsolePrinter.printLine("-"));

        // General
        HospitalDepartment gen = departments.get("GEN");
        if (gen != null) {
            int genSize    = gen.getQueueManager().getQueueSize();
            int genAvgWait = genSize * 10;
            System.out.printf("  %-25s %-15d %-20d%n",
                Department.GENERAL.getDisplayName(), genSize, genAvgWait);
        }

        // Emergency
        HospitalDepartment emg = departments.get("EMG");
        if (emg != null) {
            int emgSize    = emg.getQueueManager().getQueueSize();
            int emgAvgWait = emgSize * 5;
            System.out.printf("  %-25s %-15d %-20d%n",
                Department.EMERGENCY.getDisplayName(), emgSize, emgAvgWait);
        }

        // VIP
        HospitalDepartment vip = departments.get("VIP");
        if (vip != null) {
            int vipSize    = vip.getQueueManager().getQueueSize();
            int vipAvgWait = vipSize * 7;
            System.out.printf("  %-25s %-15d %-20d%n",
                Department.VIP.getDisplayName(), vipSize, vipAvgWait);
        }

        System.out.println(ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Display Queue for One Department
    // ─────────────────────────────────────────
    public void displayDepartmentQueue(Department department) {
        HospitalDepartment dept = departments.get(department.getCode());

        if (dept != null) {
            dept.displayQueueStatus();
        } else {
            ConsolePrinter.printError(
                "[WAIT TIME] Department not found : " + department.getCode()
            );
        }
    }
}