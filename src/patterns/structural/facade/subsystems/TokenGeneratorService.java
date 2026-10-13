// ============================================================
// FILE     : TokenGeneratorService.java
// PACKAGE  : patterns.structural.facade.subsystems
// PURPOSE  : Generates and manages digital tokens
//            per department — tracks issued tokens
// PATTERN  : Facade — Subsystem Class
// ============================================================

package patterns.structural.facade.subsystems;

import models.Department;
import models.Patient;
import models.Token;
import utils.ConsolePrinter;
import utils.ConsoleColors;
import utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenGeneratorService {

    // ─────────────────────────────────────────
    // Storage — per department token counter
    // ─────────────────────────────────────────
    private final Map<String, Token>   tokenRegistry;
    private final Map<String, Integer> deptTokenCounter;

    // ─────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────
    public TokenGeneratorService() {
        this.tokenRegistry    = new HashMap<>();
        this.deptTokenCounter = new HashMap<>();

        // Initialize counters for each department
        deptTokenCounter.put("GEN", 1);
        deptTokenCounter.put("EMG", 1);
        deptTokenCounter.put("VIP", 1);
    }

    // ─────────────────────────────────────────
    // Generate Token
    // ─────────────────────────────────────────
    public Token generateToken(Patient patient) {

        String deptCode = patient.getDepartment().getCode();

        int tokenNumber  = deptTokenCounter.getOrDefault(deptCode, 1);
        String tokenId   = DateTimeUtils.generateTokenId(deptCode, tokenNumber);

        Token token = new Token(
            tokenId,
            patient.getPatientId(),
            patient.getName(),
            patient.getDepartment(),
            tokenNumber,
            DateTimeUtils.getCurrentTimestamp()
        );

        // Store token
        tokenRegistry.put(tokenId, token);

        // Increment department counter
        deptTokenCounter.put(deptCode, tokenNumber + 1);

        System.out.println(ConsoleColors.BOLD_CYAN
            + "  [TOKEN] Digital Token Generated"
            + "\n  Token ID  : " + tokenId
            + "\n  Token#    : " + String.format("%03d", tokenNumber)
            + "\n  Patient   : " + patient.getName()
            + "\n  Dept      : " + patient.getDepartment().getDisplayName()
            + "\n  Issued At : " + token.getIssuedAt()
            + ConsoleColors.RESET
        );

        return token;
    }

    // ─────────────────────────────────────────
    // Get Token By ID
    // ─────────────────────────────────────────
    public Token getTokenById(String tokenId) {
        Token token = tokenRegistry.get(tokenId);

        if (token == null) {
            ConsolePrinter.printError("[TOKEN] Token not found : " + tokenId);
        }

        return token;
    }

    // ─────────────────────────────────────────
    // Get All Tokens By Department
    // ─────────────────────────────────────────
    public List<Token> getTokensByDepartment(Department department) {
        List<Token> result = new ArrayList<>();
        String deptCode    = department.getCode();

        for (Token token : tokenRegistry.values()) {
            if (token.getDepartment().getCode().equals(deptCode)) {
                result.add(token);
            }
        }

        return result;
    }

    // ─────────────────────────────────────────
    // Mark Token as Served
    // ─────────────────────────────────────────
    public void markTokenServed(String tokenId) {
        Token token = tokenRegistry.get(tokenId);

        if (token != null) {
            token.setServed(true);
            ConsolePrinter.printSuccess(
                "[TOKEN] Token#: "
                + String.format("%03d", token.getTokenNumber())
                + " marked as SERVED."
            );
        }
    }

    // ─────────────────────────────────────────
    // Display All Tokens
    // ─────────────────────────────────────────
    public void displayAllTokens() {
        ConsolePrinter.printSectionHeader("ISSUED TOKENS");

        if (tokenRegistry.isEmpty()) {
            ConsolePrinter.printInfo("No tokens issued yet.");
            return;
        }

        System.out.println(ConsoleColors.BOLD_CYAN);
        System.out.printf("  %-10s %-20s %-10s %-12s%n",
            "Token#", "Patient", "Dept", "Served");
        System.out.println(ConsolePrinter.printLine("-"));

        for (Token token : tokenRegistry.values()) {
            System.out.printf("  %-10s %-20s %-10s %-12s%n",
                String.format("%03d", token.getTokenNumber()),
                token.getPatientName(),
                token.getDepartment().getCode(),
                token.isServed() ? "YES" : "NO"
            );
        }

        System.out.println(ConsoleColors.RESET);
    }

    // ─────────────────────────────────────────
    // Total Tokens Issued
    // ─────────────────────────────────────────
    public int getTotalTokensIssued() {
        return tokenRegistry.size();
    }
}