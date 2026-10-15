package com.hcl.memory;

import java.util.ArrayList;
import java.util.List;

/**
 * HeapOOMExperiment Class
 *
 * Demonstrates OutOfMemoryError: Java heap space by accumulating byte buffers.
 * Used to test bounded heap size flag:
 *   - java -Xmx64m com.hcl.memory.HeapOOMExperiment
 */
public class HeapOOMExperiment {

    public static void runExperiment() {
        Runtime runtime = Runtime.getRuntime();
        long initialMax = runtime.maxMemory() / (1024 * 1024);
        System.out.printf("   [JVM INFO] Configured Max Heap (-Xmx): %,d MB%n", initialMax);

        List<byte[]> memoryHog = new ArrayList<>();
        int chunkCount = 0;
        final int CHUNK_SIZE_MB = 5;
        final int BYTES_PER_MB = 1024 * 1024;

        try {
            while (true) {
                // Allocate 5 MB byte array and hold its reference to prevent GC collection
                memoryHog.add(new byte[CHUNK_SIZE_MB * BYTES_PER_MB]);
                chunkCount++;
            }
        } catch (OutOfMemoryError oom) {
            long totalAllocated = (long) chunkCount * CHUNK_SIZE_MB;
            System.out.printf("   [RESULT] OutOfMemoryError: Java heap space triggered!%n");
            System.out.printf("   - Total chunks successfully allocated: %,d (%,d MB)%n", chunkCount, totalAllocated);
            System.out.printf("   - Heap limit reached under configured -Xmx%n");
        } finally {
            // Free references to allow normal continuation of subsequent lab steps
            memoryHog.clear();
            System.gc();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Standalone Heap OOM Experiment ===");
        runExperiment();
    }
}
