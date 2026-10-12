package com.hcl.order;

/**
 * Order Model Class
 * Represents customer order details.
 */
public class Order {

    private final String orderId;
    private final String itemCode;
    private final int quantity;
    private final double unitPrice;

    public Order(String orderId, String itemCode, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalAmount() {
        return quantity * unitPrice;
    }

    @Override
    public String toString() {
        return String.format("Order[ID=%s, Item=%s, Qty=%d, Total=$%.2f]",
                orderId, itemCode, quantity, getTotalAmount());
    }
}
