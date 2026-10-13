import patterns.behavioral.observer.Phase3Runner;

public class Main {

    public static void main(String[] args) {

        // PHASE 1 — Abstract Factory (standalone test)
        // Phase1Runner.run(); ← runs inside Phase 2

        // PHASE 2 — Facade Pattern (standalone test)
        // Phase2Runner.run(); ← runs inside Phase 3

        // PHASE 3 — Observer Pattern
        // Includes Phase 1 + Phase 2 internally
        Phase3Runner.run();
    }
}