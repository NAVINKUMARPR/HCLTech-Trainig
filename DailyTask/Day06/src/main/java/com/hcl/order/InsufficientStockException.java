package com.hcl.order;

/**
 * Custom Checked Exception: InsufficientStockException
 *
 * Extends java.lang.Exception (Checked).
 * Must be either declared in the method signature using 'throws'
 * or handled using a try-catch block.
 * Supports exception chaining via constructor with Throwable cause.
 */
public class InsufficientStockException extends Exception {

    private final String itemCode;
    private final int availableStock;
    private final int requestedStock;

    public InsufficientStockException(String message) {
        super(message);
        this.itemCode = "UNKNOWN";
        this.availableStock = 0;
        this.requestedStock = 0;
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause); // Exception chaining: links root cause
        this.itemCode = "UNKNOWN";
        this.availableStock = 0;
        this.requestedStock = 0;
    }

    public InsufficientStockException(String message, String itemCode, int availableStock, int requestedStock) {
        super(String.format("%s [Item: %s | Available: %d | Requested: %d]", 
                message, itemCode, availableStock, requestedStock));
        this.itemCode = itemCode;
        this.availableStock = availableStock;
        this.requestedStock = requestedStock;
    }

    public InsufficientStockException(String message, String itemCode, int availableStock, int requestedStock, Throwable cause) {
        super(String.format("%s [Item: %s | Available: %d | Requested: %d]", 
                message, itemCode, availableStock, requestedStock), cause);
        this.itemCode = itemCode;
        this.availableStock = availableStock;
        this.requestedStock = requestedStock;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public int getRequestedStock() {
        return requestedStock;
    }
}
