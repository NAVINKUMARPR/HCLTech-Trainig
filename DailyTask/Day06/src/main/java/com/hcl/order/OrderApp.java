package com.hcl.order;

/**
 * OrderApp - Main Demonstration Runner for Day 6
 *
 * Demonstrates:
 * 1. Successful order completion with finally audit log.
 * 2. Unchecked InvalidQuantityException handled via multi-catch.
 * 3. Checked InsufficientStockException with Chained Cause handled via multi-catch.
 * 4. Generates the two clean stack traces for GitHub Copilot Chat analysis.
 */
public class OrderApp {

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("     DAY 6: ORDER PROCESSOR EXCEPTION HANDLING DEMO      ");
        System.out.println("=========================================================");

        OrderProcessor processor = new OrderProcessor();

        // -----------------------------------------------------------------
        // DEMO 1: Successful Order Processing
        // -----------------------------------------------------------------
        Order validOrder = new Order("ORD-1001", "LAPTOP-01", 2, 899.99);
        processor.processWithAudit(validOrder);

        // -----------------------------------------------------------------
        // DEMO 2: Invalid Quantity (Unchecked InvalidQuantityException)
        // -----------------------------------------------------------------
        Order invalidQtyOrder = new Order("ORD-1002", "PHONE-02", -3, 499.50);
        processor.processWithAudit(invalidQtyOrder);

        // -----------------------------------------------------------------
        // DEMO 3: Insufficient Stock (Checked InsufficientStockException)
        // -----------------------------------------------------------------
        Order overStockOrder = new Order("ORD-1003", "TABLET-03", 50, 299.00);
        processor.processWithAudit(overStockOrder);

        // -----------------------------------------------------------------
        // DEMO 4: Chained Exception (WarehouseSyncException -> InsufficientStockException)
        // -----------------------------------------------------------------
        Order chainedErrorOrder = new Order("ORD-1004", "SYNC-ERR", 1, 150.00);
        processor.processWithAudit(chainedErrorOrder);

        // -----------------------------------------------------------------
        // DEMO 5: Printing Stack Traces for Copilot Chat Analysis
        // -----------------------------------------------------------------
        System.out.println("\n\n=========================================================");
        System.out.println("   STACK TRACES FOR COPILOT CHAT PROMPTING & ANALYSIS   ");
        System.out.println("=========================================================");

        System.out.println("\n--- [TRACE 1] InvalidQuantityException Stack Trace ---");
        try {
            processor.processOrder(new Order("ORD-ERR-01", "LAPTOP-01", -5, 800.0));
        } catch (InvalidQuantityException | InsufficientStockException ex) {
            ex.printStackTrace(System.out);
        }

        System.out.println("\n--- [TRACE 2] InsufficientStockException (Chained Cause) Stack Trace ---");
        try {
            processor.processOrder(new Order("ORD-ERR-02", "SYNC-ERR", 3, 250.0));
        } catch (InvalidQuantityException | InsufficientStockException ex) {
            ex.printStackTrace(System.out);
        }

        System.out.println("\n=========================================================");
        System.out.println("           DAY 6 DEMONSTRATION EXECUTED SUCCESSFULLY     ");
        System.out.println("=========================================================");
    }
}
