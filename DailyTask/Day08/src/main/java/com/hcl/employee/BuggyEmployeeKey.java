package com.hcl.employee;

import java.util.Objects;

/**
 * BuggyEmployeeKey Class
 *
 * Demonstrates the classic "Missing hashCode()" bug in Java:
 * - Overrides equals() correctly based on id.
 * - Omits overriding hashCode(), inheriting default System.identityHashCode().
 *
 * Result: Two distinct instances with the same id will have DIFFERENT hashCodes,
 * landing in different HashMap buckets, making lookup via map.get() return null!
 */
public class BuggyEmployeeKey {

    private final int id;

    public BuggyEmployeeKey(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuggyEmployeeKey that = (BuggyEmployeeKey) o;
        return this.id == that.id;
    }

    // [INTENTIONAL BUG]: hashCode() is NOT overridden here!
    // Inherits Object.hashCode() which computes hash based on memory address.

    @Override
    public String toString() {
        return "BuggyKey(id=" + id + ", hash=" + System.identityHashCode(this) + ")";
    }
}
