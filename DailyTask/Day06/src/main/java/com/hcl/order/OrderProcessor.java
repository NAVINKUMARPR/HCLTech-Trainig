package com.hcl.order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * OrderProcessor Class
 *
 * Core business service demonstrating:
 * 1. Throwing unchecked InvalidQuantityException for invalid arguments.
 * 2. Throwing checked InsufficientStockException with chained causes (WarehouseSyncException).
 * 3. Multi-catch handling: catch (InsufficientStockException | InvalidQuantityException e)
 * 4. Guaranteed audit execution inside the finally block.
 */
public class OrderProcessor {

    private final Map<String, Integer> inventory = new HashMap<>();

    public OrderProcessor() {
        // Initialize sample warehouse inventory
        inventory.put("LAPTOP-01", 10);
        inventory.put("PHONE-02", 5);
        inventory.put("TABLET-03", 2);
    }

    /**
     * Core business method to process an order.
     *
     * @param order the order to validate and process
     * @throws InsufficientStockException checked exception when stock is unavailable or sync fails
     * @throws InvalidQuantityException   unchecked exception when order quantity <= 0
     */
    public void processOrder(Order order) throws InsufficientStockException {
        // 1. Validation: Throw unchecked InvalidQuantityException if quantity <= 0
        if (order.getQuantity() <= 0) {
            throw new InvalidQuantityException("Order quantity must be strictly positive", order.getQuantity());
        }

        String item = order.getItemCode();

        // 2. Simulated Chained Cause: Warehouse DB failure on SKU "SYNC-ERR"
        if ("SYNC-ERR".equalsIgnoreCase(item)) {
            WarehouseSyncException rootCause = new WarehouseSyncException(
                    "Connection timed out while querying inventory cluster", "NODE-NORTH-04");
            
            // Exception Chaining: Wrapping root cause inside InsufficientStockException
            throw new InsufficientStockException(
                    "Stock verification failed due to remote warehouse failure",
                    item, 0, order.getQuantity(), rootCause);
        }

        // 3. Stock Check: Throw checked InsufficientStockException if unavailable
        int currentStock = inventory.getOrDefault(item, 0);
        if (order.getQuantity() > currentStock) {
            throw new InsufficientStockException(
                    "Requested quantity exceeds available inventory",
                    item, currentStock, order.getQuantity());
        }

        // 4. Deduct Stock & Complete Order
        inventory.put(item, currentStock - order.getQuantity());
        System.out.printf("   [SUCCESS] Order %s processed! Reserved %d units of %s. Remaining: %d%n",
                order.getOrderId(), order.getQuantity(), item, inventory.get(item));
    }

    /**
     * Executes order processing wrapped in Multi-Catch and Finally Audit.
     *
     * @param order the order to process
     */
    public void processWithAudit(Order order) {
        boolean isSuccess = false;
        System.out.printf("%n======================================================%n");
        System.out.printf("Processing %s%n", order);
        System.out.printf("======================================================%n");

        try {
            processOrder(order);
            isSuccess = true;
        } catch (InsufficientStockException | InvalidQuantityException ex) {
            // MULTI-CATCH BLOCK: Handles both checked and unchecked domain exceptions
            System.err.printf("   [MULTI-CATCH] Caught %s: %s%n", 
                    ex.getClass().getSimpleName(), ex.getMessage());

            // Check for chained cause
            if (ex.getCause() != null) {
                System.err.printf("   [ROOT CAUSE DETECTED] %s: %s%n", 
                        ex.getCause().getClass().getSimpleName(), ex.getCause().getMessage());
            }
        } finally {
            // FINALLY AUDIT BLOCK: Guaranteed to execute in all scenarios
            String status = isSuccess ? "SUCCESS" : "REJECTED_AUDITED";
            System.out.printf("   [FINALLY AUDIT LOG] Order: %s | Status: %s | Timestamp: %s | Audit Trail Saved.%n",
                    order.getOrderId(), status, LocalDateTime.now());
        }
    }

    public int getStock(String itemCode) {
        return inventory.getOrDefault(itemCode, 0);
    }
}
