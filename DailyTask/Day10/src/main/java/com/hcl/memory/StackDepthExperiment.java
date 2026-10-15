package com.hcl.memory;

/**
 * StackDepthExperiment Class
 *
 * Demonstrates StackOverflowError by performing infinite recursion.
 * Records the recursion call frame depth reached before thread stack exhaustion.
 * Used to test different thread stack size flags:
 *   - java -Xss256k com.hcl.memory.StackDepthExperiment
 *   - java -Xss512k com.hcl.memory.StackDepthExperiment
 *   - java -Xss1m   com.hcl.memory.StackDepthExperiment
 */
public class StackDepthExperiment {

    private static int depth = 0;

    public static void runExperiment() {
        depth = 0;
        try {
            recursiveCall();
        } catch (StackOverflowError error) {
            System.out.printf("   [RESULT] StackOverflowError triggered at depth: %,d frames%n", depth);
        }
    }

    private static void recursiveCall() {
        depth++;
        recursiveCall();
    }

    public static void main(String[] args) {
        System.out.println("=== Standalone StackDepth Experiment ===");
        runExperiment();
    }
}
