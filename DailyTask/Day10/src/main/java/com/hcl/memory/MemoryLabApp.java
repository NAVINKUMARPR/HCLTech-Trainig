package com.hcl.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * MemoryLabApp - Master Demonstration Runner for Day 10
 *
 * Runs all memory lab experiments:
 * 1. StackOverflowError depth test
 * 2. OutOfMemoryError heap test
 * 3. String += vs StringBuilder benchmark
 * 4. Integer cache equality pitfall
 * 5. Garbage Collection logging churn simulation (-Xlog:gc)
 */
public class MemoryLabApp {

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("           DAY 10: JVM MEMORY LAB & BENCHMARK            ");
        System.out.println("=========================================================");

        // -------------------------------------------------------------
        // SECTION 1: StackOverflowError Depth Test
        // -------------------------------------------------------------
        System.out.println("\n>>> SECTION 1: StackOverflowError DEPTH TEST");
        System.out.println("Recursively invoking call frames until thread stack exhaustion:");
        StackDepthExperiment.runExperiment();

        // -------------------------------------------------------------
        // SECTION 2: OutOfMemoryError Heap Test
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 2: OutOfMemoryError HEAP TEST");
        System.out.println("---------------------------------------------------------");
        System.out.println("Allocating 5MB buffers until heap exhaustion:");
        HeapOOMExperiment.runExperiment();

        // -------------------------------------------------------------
        // SECTION 3: String += vs StringBuilder Timing
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 3: String += vs StringBuilder TIMING");
        System.out.println("---------------------------------------------------------");
        StringConcatenationBenchmark.runBenchmark();

        // -------------------------------------------------------------
        // SECTION 4: Integer Cache Equality Pitfall
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 4: Integer CACHE '==' PITFALL");
        System.out.println("---------------------------------------------------------");
        IntegerCachePitfall.runExperiment();

        // -------------------------------------------------------------
        // SECTION 5: GC Churn for -Xlog:gc Observation
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 5: GARBAGE COLLECTION SIMULATION (-Xlog:gc)");
        System.out.println("---------------------------------------------------------");
        simulateGarbageCollectionChurn();

        System.out.println("\n=========================================================");
        System.out.println("          DAY 10 MEMORY LAB EXECUTED SUCCESSFULLY        ");
        System.out.println("=========================================================");
    }

    /**
     * Allocates short-lived objects to trigger Young Generation GC events
     * observable when passing -Xlog:gc flag to the JVM.
     */
    private static void simulateGarbageCollectionChurn() {
        System.out.println("   Generating short-lived object allocations to trigger GC...");
        List<byte[]> churnList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            churnList.add(new byte[1024 * 1024]); // 1 MB chunk
            if (i % 5 == 0) {
                churnList.clear(); // Drop references to make objects eligible for GC
                System.gc();       // Suggest GC collection
            }
        }
        System.out.println("   [GC SIMULATION COMPLETE] GC cycles triggered.");
        System.out.println("   Run with 'java -Xlog:gc' to observe detailed GC logging output.");
    }
}
