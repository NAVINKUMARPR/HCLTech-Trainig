package com.hcl.payment;

/**
 * CashPayment Class
 * Represents point-of-sale cash payments with change calculation.
 * Does NOT implement Refundable (over-the-counter cash payments are non-refundable electronically).
 */
public class CashPayment extends Payment {

    private final double cashTendered;

    public CashPayment(String transactionId, double amount, double cashTendered) {
        super(transactionId, amount);
        this.cashTendered = cashTendered;
    }

    @Override
    public void processPayment() {
        if (cashTendered < getAmount()) {
            setStatus("FAILED");
            System.out.printf("   [CASH FAILED] Insufficient cash tendered. Expected: $%.2f, Given: $%.2f%n",
                    getAmount(), cashTendered);
            return;
        }

        double change = cashTendered - getAmount();
        setStatus("COMPLETED");
        System.out.printf("   [CASH SUCCESS] Bill: $%.2f | Tendered: $%.2f | Change returned: $%.2f | Txn ID: %s%n",
                getAmount(), cashTendered, change, getTransactionId());
    }

    public double getCashTendered() {
        return cashTendered;
    }
}
