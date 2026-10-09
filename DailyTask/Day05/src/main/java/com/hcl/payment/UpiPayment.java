package com.hcl.payment;

/**
 * UpiPayment Class
 * Represents instant UPI transfers (e.g., GPay, PhonePe, Paytm). Implements Refundable.
 */
public class UpiPayment extends Payment implements Refundable {

    private final String upiId;
    private double refundedAmount = 0.0;

    public UpiPayment(String transactionId, double amount, String upiId) {
        super(transactionId, amount);
        this.upiId = upiId;
    }

    @Override
    public void processPayment() {
        setStatus("COMPLETED");
        System.out.printf("   [UPI SUCCESS] Transferred $%.2f via UPI ID (%s). Txn ID: %s%n",
                getAmount(), upiId, getTransactionId());
    }

    @Override
    public boolean processRefund(double amount) {
        double maxRefundable = getAmount() - refundedAmount;
        if (amount <= 0 || amount > maxRefundable) {
            System.out.printf("   [REFUND FAILED] Invalid refund amount $%.2f. Max refundable: $%.2f%n",
                    amount, maxRefundable);
            return false;
        }

        refundedAmount += amount;
        setStatus(refundedAmount == getAmount() ? "REFUNDED" : "PARTIALLY_REFUNDED");
        System.out.printf("   [REFUND SUCCESS] Instant reversal of $%.2f credited to UPI ID (%s).%n",
                amount, upiId);
        return true;
    }

    @Override
    public String getRefundDetails() {
        return String.format("UPI Refund [VPA: %s, Refunded: $%.2f, Status: %s]",
                upiId, refundedAmount, getStatus());
    }

    public String getUpiId() {
        return upiId;
    }
}
