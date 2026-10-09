package com.hcl.payment;

/**
 * Refundable Interface
 * Represents payment methods that support processing refunds (e.g., Card, UPI).
 * Cash payments do not implement this interface.
 */
public interface Refundable {

    /**
     * Processes a refund of the specified amount back to original payment method.
     *
     * @param amount Amount to refund
     * @return true if refund succeeded, false otherwise
     */
    boolean processRefund(double amount);

    /**
     * Returns formatted details regarding the refund status.
     *
     * @return refund details string
     */
    String getRefundDetails();
}
