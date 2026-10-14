package com.hcl.generics;

import java.util.List;

/**
 * GenericUtils Utility Class
 *
 * Implements:
 * 1. Bounded generic method: <T extends Comparable<T>> T findMax(...)
 * 2. Upper-bounded wildcard method: ? extends Number (PECS: Producer Extends - reading)
 * 3. Lower-bounded wildcard method: ? super Integer (PECS: Consumer Super - writing)
 * 4. Unbounded wildcard method: List<?>
 */
public class GenericUtils {

    /**
     * Bounded Generic Method: Finds the maximum between three comparable elements.
     *
     * @param <T> Type that implements Comparable<T>
     * @param a first element
     * @param b second element
     * @param c third element
     * @return the maximum element
     */
    public static <T extends Comparable<T>> T findMax(T a, T b, T c) {
        T max = a;
        if (b.compareTo(max) > 0) {
            max = b;
        }
        if (c.compareTo(max) > 0) {
            max = c;
        }
        return max;
    }

    /**
     * Bounded Generic Method: Finds the maximum element in a list of comparables.
     */
    public static <T extends Comparable<T>> T findMax(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty.");
        }
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    /**
     * Upper-Bounded Wildcard Method: List<? extends Number>
     * Producer Extends: We only READ Numbers from this list to compute their sum.
     * Accepts List<Integer>, List<Double>, List<Float>, List<Long>, etc.
     */
    public static double sumOfNumbers(List<? extends Number> numbers) {
        double sum = 0.0;
        for (Number n : numbers) {
            sum += n.doubleValue(); // Safe read access
        }
        return sum;
    }

    /**
     * Lower-Bounded Wildcard Method: List<? super Integer>
     * Consumer Super: We WRITE/ADD Integers into this list.
     * Accepts List<Integer>, List<Number>, or List<Object>.
     */
    public static void addIntegers(List<? super Integer> list, Integer... values) {
        for (Integer val : values) {
            list.add(val); // Safe write access
        }
    }

    /**
     * Unbounded Wildcard Method: List<?>
     * Prints elements of unknown type.
     */
    public static void printList(String label, List<?> list) {
        System.out.printf("%s: %s%n", label, list);
    }
}
