package com.hcl.order;

/**
 * Custom Unchecked Exception: InvalidQuantityException
 *
 * Extends java.lang.RuntimeException (Unchecked).
 * Represents invalid client/caller input (e.g., negative or zero quantity).
 * Compiler does not force callers to declare 'throws' or enclose in try-catch.
 */
public class InvalidQuantityException extends RuntimeException {

    private final int quantity;

    public InvalidQuantityException(String message) {
        super(message);
        this.quantity = 0;
    }

    public InvalidQuantityException(String message, int quantity) {
        super(String.format("%s (Attempted quantity: %d)", message, quantity));
        this.quantity = quantity;
    }

    public InvalidQuantityException(String message, int quantity, Throwable cause) {
        super(String.format("%s (Attempted quantity: %d)", message, quantity), cause);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
