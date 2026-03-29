package com.shoppingcart.model;

public class CartItem {

    private String name;
    private double price;
    private int quantity;

    public CartItem(String name, double price, int quantity) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public CartItem(double price, int quantity) {
        this("Item", price, quantity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    public double getItemTotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return String.format("CartItem{name='%s', price=%.2f, quantity=%d, total=%.2f}",
                name, price, quantity, getItemTotal());
    }
}
