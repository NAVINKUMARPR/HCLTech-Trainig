package com.hcl.memory;

/**
 * StringConcatenationBenchmark Class
 *
 * Compares performance of immutable String concatenation (+=)
 * versus mutable java.lang.StringBuilder in a loop.
 *
 * Algorithmic complexity:
 * - String +=      : O(n^2) time due to repeated array reallocation and buffer copying.
 * - StringBuilder  : O(n) amortized time using dynamic exponential backing array expansion.
 */
public class StringConcatenationBenchmark {

    public static void runBenchmark() {
        final int ITERATIONS = 25_000;
        final String TOKEN = "A";

        System.out.printf("   [CONFIG] Iterations: %,d appends%n", ITERATIONS);

        // 1. Benchmark: String +=
        long start = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < ITERATIONS; i++) {
            str += TOKEN;
        }
        long stringTimeMs = System.currentTimeMillis() - start;

        // 2. Benchmark: StringBuilder.append()
        start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ITERATIONS; i++) {
            sb.append(TOKEN);
        }
        long sbTimeMs = System.currentTimeMillis() - start;

        System.out.printf("   - String +=        : %,6d ms (O(n^2) - copies full string on each append)%n", stringTimeMs);
        System.out.printf("   - StringBuilder    : %,6d ms (O(n)   - amortized in-place array resizing)%n", sbTimeMs);

        if (sbTimeMs == 0) sbTimeMs = 1; // Prevent division by zero
        double speedup = (double) stringTimeMs / sbTimeMs;
        System.out.printf("   >>> StringBuilder is approximately %,.1fx faster!%n", speedup);
    }

    public static void main(String[] args) {
        System.out.println("=== Standalone String Concatenation Benchmark ===");
        runBenchmark();
    }
}
