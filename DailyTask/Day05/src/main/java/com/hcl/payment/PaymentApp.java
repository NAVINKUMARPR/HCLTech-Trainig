package com.hcl.payment;

import java.util.ArrayList;
import java.util.List;

/**
 * PaymentApp - Demonstration Runner for Day 5
 *
 * Demonstrates:
 * 1. Payment Hierarchy (Abstract base class & subclasses)
 * 2. Overloaded pay() methods (amount only, amount with currency, amount with discount)
 * 3. Polymorphism (handling diverse payment modes via List<Payment>)
 * 4. Refundable interface implementation & pattern matching with instanceof
 */
public class PaymentApp {

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("       DAY 5: PAYMENT HIERARCHY & REFUND DEMO           ");
        System.out.println("=========================================================");

        // 1. Instantiate Payment objects (Subclasses assigned to Payment references)
        Payment cardPayment = new CardPayment("TXN-101", 120.00, "4111222233334444", "Credit");
        Payment upiPayment = new UpiPayment("TXN-102", 45.00, "alex@oksbi");
        Payment cashPayment = new CashPayment("TXN-103", 30.00, 50.00);

        // ==========================================
        // SECTION 1: Overloaded pay() Demonstration
        // ==========================================
        System.out.println("\n>>> SECTION 1: OVERLOADED pay() METHOD DEMO");

        // Overload 1: pay(amount)
        System.out.println("--- Test 1: Standard pay(amount) ---");
        cardPayment.pay(120.00);

        // Overload 2: pay(amount, currency)
        System.out.println("\n--- Test 2: Multi-currency pay(amount, currency) ---");
        upiPayment.pay(45.00, "USD");

        // Overload 3: pay(amount, discountPercent)
        System.out.println("\n--- Test 3: Promotional pay(amount, discountPercent) ---");
        Payment discountedCard = new CardPayment("TXN-104", 200.00, "5500111122228888", "Debit");
        discountedCard.pay(200.00, 15.0); // 15% discount applied

        // Standard Cash processing
        System.out.println("\n--- Test 4: Cash pay with change calculation ---");
        cashPayment.pay(30.00);

        // ==========================================
        // SECTION 2: Polymorphism & Refundable Interface
        // ==========================================
        System.out.println("\n---------------------------------------------------------");
        System.out.println(">>> SECTION 2: REFUNDABLE INTERFACE & POLYMORPHISM");
        System.out.println("---------------------------------------------------------");

        List<Payment> transactions = new ArrayList<>();
        transactions.add(cardPayment);
        transactions.add(upiPayment);
        transactions.add(cashPayment);
        transactions.add(discountedCard);

        for (Payment payment : transactions) {
            System.out.printf("%nEvaluating %s [ID: %s, Current Amount: $%.2f, Status: %s]:%n",
                    payment.getClass().getSimpleName(),
                    payment.getTransactionId(),
                    payment.getAmount(),
                    payment.getStatus());

            // Check if payment mode implements Refundable
            if (payment instanceof Refundable refundable) {
                System.out.println("   [STATUS] Payment method supports electronic refund.");
                
                // Process full refund for demonstration
                double refundAmount = payment.getAmount();
                refundable.processRefund(refundAmount);
                System.out.println("   [DETAILS] " + refundable.getRefundDetails());
            } else {
                System.out.println("   [STATUS] Non-refundable: Cash transactions have no electronic refund channel.");
            }
        }

        System.out.println("\n=========================================================");
        System.out.println("          DAY 5 DEMONSTRATION EXECUTED SUCCESSFULLY      ");
        System.out.println("=========================================================");
    }
}
