package com.hcl.order;

/**
 * Low-level Exception: WarehouseSyncException
 *
 * Represents an infrastructure or external database sync failure.
 * Used to demonstrate Exception Chaining (acting as the 'cause' of InsufficientStockException).
 */
public class WarehouseSyncException extends RuntimeException {

    private final String warehouseNode;

    public WarehouseSyncException(String message, String warehouseNode) {
        super(String.format("%s [Warehouse Node: %s]", message, warehouseNode));
        this.warehouseNode = warehouseNode;
    }

    public WarehouseSyncException(String message, String warehouseNode, Throwable cause) {
        super(String.format("%s [Warehouse Node: %s]", message, warehouseNode), cause);
        this.warehouseNode = warehouseNode;
    }

    public String getWarehouseNode() {
        return warehouseNode;
    }
}
