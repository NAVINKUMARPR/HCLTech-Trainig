package com.hcl.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GenericApp - Main Demonstration Runner for Day 9
 *
 * Demonstrates:
 * 1. Generic Pair<K, V> class.
 * 2. Bounded Box<T extends Number> class.
 * 3. Bounded Generic Method: <T extends Comparable<T>> max().
 * 4. Wildcard methods: ? extends Number (Producer) and ? super Integer (Consumer).
 * 5. Reading products.csv using try-with-resources.
 * 6. Custom AutoCloseable demonstrating reverse close order (LIFO).
 */
public class GenericApp {

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("          DAY 9: GENERIC TOOLKIT & RESOURCES DEMO        ");
        System.out.println("=========================================================");

        // -------------------------------------------------------------
        // SECTION 1: Generic Pair<K, V>
        // -------------------------------------------------------------
        System.out.println("\n>>> SECTION 1: GENERIC Pair<K, V>");
        Pair<String, Integer> studentAge = Pair.of("Alice", 21);
        Pair<Integer, String> httpStatus = Pair.of(200, "OK");
        Pair<String, Double> productPrice = Pair.of("Laptop", 1299.99);

        System.out.println("Created Pairs:");
        System.out.println("   * " + studentAge + " | Key: " + studentAge.getKey() + ", Value: " + studentAge.getValue());
        System.out.println("   * " + httpStatus);
        System.out.println("   * " + productPrice);

        // -------------------------------------------------------------
        // SECTION 2: Bounded Box<T extends Number>
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 2: BOUNDED Box<T extends Number>");
        System.out.println("---------------------------------------------------------");
        Box<Integer> intBox = new Box<>(100);
        Box<Double> doubleBox = new Box<>(99.45);
        Box<Long> longBox = new Box<>(500L);

        System.out.println("   * " + intBox + " -> doubleValue: " + intBox.doubleValue());
        System.out.println("   * " + doubleBox + " -> doubleValue: " + doubleBox.doubleValue());
        System.out.println("   * " + longBox + " -> doubleValue: " + longBox.doubleValue());

        System.out.printf("   Is intBox (%s) > doubleBox (%s)? %b%n",
                intBox.getItem(), doubleBox.getItem(), intBox.isGreaterThan(doubleBox));

        // Note: Box<String> strBox = new Box<>("Hello"); // COMPILE ERROR: String does not extend Number!
        System.out.println("   [TYPE SAFETY]: Box<String> is rejected at compile-time because String does not extend Number.");

        // -------------------------------------------------------------
        // SECTION 3: Bounded Generic Method <T extends Comparable<T>> max()
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 3: GENERIC METHOD <T extends Comparable<T>> max()");
        System.out.println("---------------------------------------------------------");

        Integer maxInt = GenericUtils.findMax(45, 120, 89);
        System.out.println("   * max(45, 120, 89)                => " + maxInt);

        String maxString = GenericUtils.findMax("Banana", "Strawberry", "Apple");
        System.out.println("   * max(Banana, Strawberry, Apple)  => " + maxString);

        Double maxDouble = GenericUtils.findMax(Arrays.asList(12.5, 99.8, 34.2, 88.0, 5.1));
        System.out.println("   * max(List of Doubles)            => " + maxDouble);

        // -------------------------------------------------------------
        // SECTION 4: Wildcards (? extends Number vs ? super Integer)
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 4: WILDCARDS & PECS PRINCIPLE");
        System.out.println("---------------------------------------------------------");

        // 1. Upper-bounded wildcard (? extends Number - Producer Extends)
        List<Integer> integers = Arrays.asList(10, 20, 30);
        List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);
        System.out.println("1. Upper-Bounded (? extends Number) - Reading values:");
        System.out.printf("   - Sum of integers: %.2f%n", GenericUtils.sumOfNumbers(integers));
        System.out.printf("   - Sum of doubles : %.2f%n", GenericUtils.sumOfNumbers(doubles));

        // 2. Lower-bounded wildcard (? super Integer - Consumer Super)
        System.out.println("\n2. Lower-Bounded (? super Integer) - Writing values:");
        List<Number> numberList = new ArrayList<>();
        GenericUtils.addIntegers(numberList, 100, 200, 300);
        GenericUtils.printList("   - numberList after addIntegers", numberList);

        List<Object> objectList = new ArrayList<>();
        GenericUtils.addIntegers(objectList, 500, 600);
        GenericUtils.printList("   - objectList after addIntegers", objectList);

        // -------------------------------------------------------------
        // SECTION 5: Reading products.csv with try-with-resources
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 5: READ products.csv (try-with-resources)");
        System.out.println("---------------------------------------------------------");

        List<Product> products = ProductReader.readProducts("products.csv");
        System.out.println("Parsed " + products.size() + " products from products.csv:\n");
        products.forEach(p -> System.out.println("   * " + p));

        // Find most expensive product using generic max()
        if (!products.isEmpty()) {
            Product mostExpensive = GenericUtils.findMax(products);
            System.out.println("\n   >>> Most Expensive Product (via generic max): " + mostExpensive);
        }

        // -------------------------------------------------------------
        // SECTION 6: Custom AutoCloseable & Close Order (LIFO)
        // -------------------------------------------------------------
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 6: CUSTOM AutoCloseable & CLOSE ORDER (LIFO)");
        System.out.println("---------------------------------------------------------");
        System.out.println("Declaring 3 resources inside try-with-resources:\n");

        try (CustomResource r1 = new CustomResource("Resource-1 (DatabaseConnection)");
             CustomResource r2 = new CustomResource("Resource-2 (CloudStorageSession)");
             CustomResource r3 = new CustomResource("Resource-3 (AuditLogStream)")) {

            System.out.println("\n   [INSIDE TRY BLOCK]: Working with active resources...");
            r1.doWork();
            r2.doWork();
            r3.doWork();
            System.out.println("   [INSIDE TRY BLOCK]: Work finished. Exiting try block now...\n");

        } // Resources automatically closed here in reverse order: r3 -> r2 -> r1

        System.out.println("\n(Notice: Resources were closed in REVERSE order of declaration: LIFO order!)");

        System.out.println("\n=========================================================");
        System.out.println("          DAY 9 DEMONSTRATION EXECUTED SUCCESSFULLY      ");
        System.out.println("=========================================================");
    }
}
