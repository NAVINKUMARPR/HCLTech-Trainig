package com.hcl.library;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * ListBenchmark Class
 *
 * Micro-benchmarks ArrayList vs LinkedList for:
 * 1. Head Insertion: add(0, element) -> Demonstrates O(n) array shift vs O(1) node linking.
 * 2. Positional Access: get(i) -> Demonstrates O(1) direct index access vs O(n) pointer traversal.
 */
public class ListBenchmark {

    public static void runBenchmark() {
        final int INSERT_COUNT = 50_000;
        final int GET_COUNT = 10_000;

        System.out.println("\n=========================================================");
        System.out.println("   BENCHMARK: ArrayList vs. LinkedList Performance       ");
        System.out.println("=========================================================");

        // -------------------------------------------------------------
        // 1. Benchmark: Head Insertion (add(0, item))
        // -------------------------------------------------------------
        System.out.printf("%n[TEST 1] Inserting %,d elements at Head (index 0):%n", INSERT_COUNT);

        // ArrayList Head Insertion
        List<Integer> arrayList = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < INSERT_COUNT; i++) {
            arrayList.add(0, i);
        }
        long arrayListInsertTime = System.nanoTime() - start;

        // LinkedList Head Insertion
        List<Integer> linkedList = new LinkedList<>();
        start = System.nanoTime();
        for (int i = 0; i < INSERT_COUNT; i++) {
            linkedList.add(0, i);
        }
        long linkedListInsertTime = System.nanoTime() - start;

        System.out.printf("   - ArrayList.add(0, e)  : %8.2f ms (O(n) - element shifting required)%n",
                arrayListInsertTime / 1_000_000.0);
        System.out.printf("   - LinkedList.add(0, e) : %8.2f ms (O(1) - constant-time node link)%n",
                linkedListInsertTime / 1_000_000.0);

        if (linkedListInsertTime < arrayListInsertTime) {
            System.out.printf("   >>> Winner for Head Inserts: LinkedList (%.1fx faster)%n",
                    (double) arrayListInsertTime / linkedListInsertTime);
        }

        // -------------------------------------------------------------
        // 2. Benchmark: Random Access (get(i))
        // -------------------------------------------------------------
        System.out.printf("%n[TEST 2] Random get(i) across %,d lookups from %,d elements:%n", GET_COUNT, INSERT_COUNT);

        Random random = new Random(42);
        int[] indices = new int[GET_COUNT];
        for (int i = 0; i < GET_COUNT; i++) {
            indices[i] = random.nextInt(INSERT_COUNT);
        }

        // ArrayList get(i)
        start = System.nanoTime();
        long sumArray = 0;
        for (int idx : indices) {
            sumArray += arrayList.get(idx);
        }
        long arrayListGetTime = System.nanoTime() - start;

        // LinkedList get(i)
        start = System.nanoTime();
        long sumLinked = 0;
        for (int idx : indices) {
            sumLinked += linkedList.get(idx);
        }
        long linkedListGetTime = System.nanoTime() - start;

        System.out.printf("   - ArrayList.get(i)     : %8.2f ms (O(1) - direct memory offset calculation)%n",
                arrayListGetTime / 1_000_000.0);
        System.out.printf("   - LinkedList.get(i)    : %8.2f ms (O(n) - sequential node pointer hopping)%n",
                linkedListGetTime / 1_000_000.0);

        if (arrayListGetTime < linkedListGetTime) {
            System.out.printf("   >>> Winner for Random Access: ArrayList (%.1fx faster)%n",
                    (double) linkedListGetTime / arrayListGetTime);
        }
    }
}
