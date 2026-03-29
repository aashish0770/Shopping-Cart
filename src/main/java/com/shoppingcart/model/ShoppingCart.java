package com.shoppingcart.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCart {

    private final List<CartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        items.add(item);
    }
    public void removeItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Invalid item index: " + index);
        }
        items.remove(index);
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }


    public int getItemCount() {
        return items.size();
    }

    public double getTotalCost() {
        double total = 0.0;
        for (CartItem item : items) {
            total += item.getItemTotal();
        }
        return total;
    }
    public double getItemTotal(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Invalid item index: " + index);
        }
        return items.get(index).getItemTotal();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ShoppingCart{\n");
        for (int i = 0; i < items.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(items.get(i)).append("\n");
        }
        sb.append("  Total: ").append(String.format("%.2f", getTotalCost())).append("\n}");
        return sb.toString();
    }
}
