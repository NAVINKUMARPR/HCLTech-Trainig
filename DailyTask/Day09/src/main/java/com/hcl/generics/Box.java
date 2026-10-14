package com.hcl.generics;

/**
 * Bounded Generic Box Class
 * Bounded by <T extends Number>: Restricts the type parameter to subclasses of java.lang.Number
 * (e.g., Integer, Double, Float, Long).
 */
public class Box<T extends Number> {

    private T item;

    public Box(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    /**
     * Converts the contained item to a double value using the Number base class method.
     */
    public double doubleValue() {
        return item != null ? item.doubleValue() : 0.0;
    }

    /**
     * Compares this box's numerical value with another box of any Number subtype.
     */
    public boolean isGreaterThan(Box<? extends Number> other) {
        if (other == null) return true;
        return this.doubleValue() > other.doubleValue();
    }

    @Override
    public String toString() {
        return String.format("Box[%s (%s)]", item, item != null ? item.getClass().getSimpleName() : "null");
    }
}
