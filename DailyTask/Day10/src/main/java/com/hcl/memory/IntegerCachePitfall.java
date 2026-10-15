package com.hcl.memory;

/**
 * IntegerCachePitfall Class
 *
 * Demonstrates the JVM Integer Cache pitfall:
 * - Java caches Integer objects for values from -128 to 127 via Integer.valueOf() (used in autoboxing).
 * - Comparing with '==' tests reference equality (memory identity), NOT numeric equality.
 * - Values within [-128, 127] share the exact same cached object reference ('==' returns true).
 * - Values outside this range instantiate distinct heap objects ('==' returns false).
 * - Solution: Always use .equals() or primitive 'int' for value comparisons.
 */
public class IntegerCachePitfall {

    public static void runExperiment() {
        // Test 1: Inside cache boundary (100)
        Integer a = 100;
        Integer b = 100;
        System.out.printf("   [CASE 1] a = 100, b = 100 (Within [-128, 127])%n");
        System.out.printf("   - a == b               : %b (Points to identical cached instance!)%n", a == b);
        System.out.printf("   - a.equals(b)          : %b%n", a.equals(b));
        System.out.printf("   - identityHashCode(a)  : %d%n", System.identityHashCode(a));
        System.out.printf("   - identityHashCode(b)  : %d%n", System.identityHashCode(b));

        // Test 2: Upper boundary overflow (128)
        Integer c = 128;
        Integer d = 128;
        System.out.printf("%n   [CASE 2] c = 128, d = 128 (Outside cache range!)%n");
        System.out.printf("   - c == d               : %b (PITFALL! Separate heap objects)%n", c == d);
        System.out.printf("   - c.equals(d)          : %b (CORRECT: Compares numeric value)%n", c.equals(d));
        System.out.printf("   - identityHashCode(c)  : %d%n", System.identityHashCode(c));
        System.out.printf("   - identityHashCode(d)  : %d%n", System.identityHashCode(d));

        // Test 3: Lower boundary limits (-128 vs -129)
        Integer e = -128;
        Integer f = -128;
        Integer g = -129;
        Integer h = -129;
        System.out.printf("%n   [CASE 3] Lower Boundary Limits:%n");
        System.out.printf("   - -128 == -128         : %b (Cached lower bound)%n", e == f);
        System.out.printf("   - -129 == -129         : %b (Outside cache range)%n", g == h);
    }

    public static void main(String[] args) {
        System.out.println("=== Standalone Integer Cache Pitfall ===");
        runExperiment();
    }
}
