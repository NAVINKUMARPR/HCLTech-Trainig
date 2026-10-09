package com.hcl.payment;

/**
 * Abstract Payment Class
 * Base class for all payment modes. Encapsulates common transaction data,
 * defines the abstract processPayment() method, and provides overloaded pay() methods.
 */
public abstract class Payment {

    private final String transactionId;
    private double amount;
    private String status;

    public Payment(String transactionId, double amount) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.status = "PENDING";
    }

    /**
     * Abstract method that each payment subtype must implement
     * with its specific processing rules.
     */
    public abstract void processPayment();

    // ==========================================
    // Overloaded pay() Methods
    // ==========================================

    /**
     * Overloaded pay() - Version 1: Standard amount.
     *
     * @param amount Transaction amount
     */
    public void pay(double amount) {
        this.amount = amount;
        System.out.printf("%n[INITIATE] Processing payment of $%.2f...%n", amount);
        processPayment();
    }

    /**
     * Overloaded pay() - Version 2: Amount with specified currency.
     *
     * @param amount   Transaction amount
     * @param currency Currency code (e.g., "USD", "INR", "EUR")
     */
    public void pay(double amount, String currency) {
        this.amount = amount;
        System.out.printf("%n[INITIATE] Processing payment of %.2f %s...%n", amount, currency);
        processPayment();
    }

    /**
     * Overloaded pay() - Version 3: Amount with promotional discount percentage.
     *
     * @param amount          Original amount
     * @param discountPercent Discount percentage (e.g., 10.0 for 10% off)
     */
    public void pay(double amount, double discountPercent) {
        double discount = (amount * discountPercent) / 100.0;
        double finalAmount = amount - discount;
        this.amount = finalAmount;
        System.out.printf("%n[INITIATE] Processing payment with %.1f%% discount: Original $%.2f -> Final $%.2f...%n",
                discountPercent, amount, finalAmount);
        processPayment();
    }

    // ==========================================
    // Getters and Setters
    // ==========================================

    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
