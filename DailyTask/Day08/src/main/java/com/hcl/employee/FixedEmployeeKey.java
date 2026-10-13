package com.hcl.employee;

import java.util.Objects;

/**
 * FixedEmployeeKey Class
 *
 * Demonstrates the fix to the hashCode contract:
 * - Overrides equals() based on id.
 * - Overrides hashCode() based on id using Objects.hash(id) or Integer.hashCode(id).
 *
 * Result: Two distinct instances with the same id produce identical hashCodes,
 * landing in the same HashMap bucket and allowing successful map.get() retrieval.
 */
public class FixedEmployeeKey {

    private final int id;

    public FixedEmployeeKey(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FixedEmployeeKey that = (FixedEmployeeKey) o;
        return this.id == that.id;
    }

    // [FIX]: Consistent hashCode() implementation adhering to the equals/hashCode contract
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FixedKey(id=" + id + ", hash=" + hashCode() + ")";
    }
}
