package com.hcl.payment;

/**
 * CardPayment Class
 * Represents Credit/Debit card payments. Implements Refundable.
 */
public class CardPayment extends Payment implements Refundable {

    private final String cardNumber;
    private final String cardType; // "Credit" or "Debit"
    private double refundedAmount = 0.0;

    public CardPayment(String transactionId, double amount, String cardNumber, String cardType) {
        super(transactionId, amount);
        this.cardNumber = cardNumber;
        this.cardType = cardType;
    }

    @Override
    public void processPayment() {
        setStatus("COMPLETED");
        System.out.printf("   [CARD SUCCESS] Charged $%.2f to %s Card (%s). Txn ID: %s%n",
                getAmount(), cardType, maskCardNumber(cardNumber), getTransactionId());
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
        System.out.printf("   [REFUND SUCCESS] Refunded $%.2f to %s Card (%s).%n",
                amount, cardType, maskCardNumber(cardNumber));
        return true;
    }

    @Override
    public String getRefundDetails() {
        return String.format("Card Refund [Card: %s, Refunded: $%.2f, Status: %s]",
                maskCardNumber(cardNumber), refundedAmount, getStatus());
    }

    private String maskCardNumber(String number) {
        if (number == null || number.length() < 4) {
            return "****";
        }
        return "****-****-****-" + number.substring(number.length() - 4);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardType() {
        return cardType;
    }
}
