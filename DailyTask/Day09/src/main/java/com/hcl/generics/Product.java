package com.hcl.generics;

/**
 * Product Model Class
 * Represents an inventory item parsed from products.csv.
 * Implements Comparable to allow finding the most expensive product via generic max().
 */
public class Product implements Comparable<Product> {

    private final int id;
    private final String name;
    private final String category;
    private final double price;
    private final int stock;

    public Product(int id, String name, String category, double price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public int compareTo(Product other) {
        // Compares products by price
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return String.format("Product[#%-3d | %-32s | %-12s | $%,7.2f | Stock: %3d]",
                id, name, category, price, stock);
    }
}
