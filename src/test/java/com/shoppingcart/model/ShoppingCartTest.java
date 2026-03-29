package com.shoppingcart.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ShoppingCart}.
 * Tests total calculation, item management, and edge cases.
 */
@DisplayName("ShoppingCart Tests")
class ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    // ── Empty cart ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("New cart has 0 items")
    void testEmptyCartItemCount() {
        assertEquals(0, cart.getItemCount());
    }

    @Test
    @DisplayName("New cart total is 0.00")
    void testEmptyCartTotal() {
        assertEquals(0.00, cart.getTotalCost(), 0.001);
    }

    // ── addItem ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("addItem: item count increments")
    void testAddItemCount() {
        cart.addItem(new CartItem(1.00, 1));
        assertEquals(1, cart.getItemCount());
        cart.addItem(new CartItem(2.00, 2));
        assertEquals(2, cart.getItemCount());
    }

    @Test
    @DisplayName("addItem: null throws IllegalArgumentException")
    void testAddNullItemThrows() {
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(null));
    }

    // ── getTotalCost ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Single item: total = price × qty")
    void testSingleItemTotal() {
        cart.addItem(new CartItem(3.00, 5));
        assertEquals(15.00, cart.getTotalCost(), 0.001);
    }

    @Test
    @DisplayName("Multiple items: totals are summed correctly")
    void testMultipleItemsTotal() {
        cart.addItem(new CartItem(2.50, 4));   // 10.00
        cart.addItem(new CartItem(1.00, 3));   //  3.00
        cart.addItem(new CartItem(5.99, 2));   // 11.98
        assertEquals(24.98, cart.getTotalCost(), 0.001);
    }

    @Test
    @DisplayName("All items with qty=0 → total is 0")
    void testAllZeroQuantityTotal() {
        cart.addItem(new CartItem(9.99, 0));
        cart.addItem(new CartItem(1.50, 0));
        assertEquals(0.00, cart.getTotalCost(), 0.001);
    }

    @Test
    @DisplayName("All items with price=0 → total is 0")
    void testAllZeroPriceTotal() {
        cart.addItem(new CartItem(0.00, 5));
        cart.addItem(new CartItem(0.00, 10));
        assertEquals(0.00, cart.getTotalCost(), 0.001);
    }

    @Test
    @DisplayName("Large quantity: total computed correctly")
    void testLargeQuantity() {
        cart.addItem(new CartItem(0.01, 10000));
        assertEquals(100.00, cart.getTotalCost(), 0.01);
    }

    @Test
    @DisplayName("Many items: total is cumulative sum")
    void testManyItemsTotal() {
        for (int i = 1; i <= 10; i++) {
            cart.addItem(new CartItem(i, 1)); // 1+2+…+10 = 55
        }
        assertEquals(55.00, cart.getTotalCost(), 0.001);
    }

    @Test
    @DisplayName("Fractional prices: total precision is maintained")
    void testFractionalPricesTotal() {
        cart.addItem(new CartItem(0.10, 1));
        cart.addItem(new CartItem(0.20, 1));
        cart.addItem(new CartItem(0.30, 1));
        assertEquals(0.60, cart.getTotalCost(), 0.001);
    }

    // ── removeItem ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("removeItem: removes correct item and adjusts count")
    void testRemoveItem() {
        cart.addItem(new CartItem(1.00, 1));
        cart.addItem(new CartItem(2.00, 1));
        cart.removeItem(0);
        assertEquals(1, cart.getItemCount());
        assertEquals(2.00, cart.getTotalCost(), 0.001);
    }

    @Test
    @DisplayName("removeItem: invalid index throws IndexOutOfBoundsException")
    void testRemoveInvalidIndexThrows() {
        cart.addItem(new CartItem(1.00, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> cart.removeItem(5));
    }

    @Test
    @DisplayName("removeItem: negative index throws IndexOutOfBoundsException")
    void testRemoveNegativeIndexThrows() {
        cart.addItem(new CartItem(1.00, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> cart.removeItem(-1));
    }

    // ── clear ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("clear: empties the cart")
    void testClear() {
        cart.addItem(new CartItem(5.00, 2));
        cart.addItem(new CartItem(3.00, 1));
        cart.clear();
        assertEquals(0, cart.getItemCount());
        assertEquals(0.00, cart.getTotalCost(), 0.001);
    }

    // ── getItems ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getItems returns unmodifiable list")
    void testGetItemsIsUnmodifiable() {
        cart.addItem(new CartItem(1.00, 1));
        List<CartItem> items = cart.getItems();
        assertThrows(UnsupportedOperationException.class,
                () -> items.add(new CartItem(2.00, 1)));
    }

    @Test
    @DisplayName("getItems reflects correct item references")
    void testGetItemsContent() {
        CartItem a = new CartItem("A", 1.00, 2);
        CartItem b = new CartItem("B", 3.00, 1);
        cart.addItem(a);
        cart.addItem(b);
        List<CartItem> items = cart.getItems();
        assertEquals(2, items.size());
        assertEquals("A", items.get(0).getName());
        assertEquals("B", items.get(1).getName());
    }

    // ── getItemTotal(index) ───────────────────────────────────────────────────

    @Test
    @DisplayName("getItemTotal(index) returns correct per-item total")
    void testGetItemTotalByIndex() {
        cart.addItem(new CartItem(4.00, 3));  // 12.00
        cart.addItem(new CartItem(2.00, 5));  // 10.00
        assertEquals(12.00, cart.getItemTotal(0), 0.001);
        assertEquals(10.00, cart.getItemTotal(1), 0.001);
    }

    @Test
    @DisplayName("getItemTotal(index): invalid index throws")
    void testGetItemTotalInvalidIndexThrows() {
        cart.addItem(new CartItem(1.00, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> cart.getItemTotal(99));
    }

    // ── toString ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("toString mentions Total")
    void testToStringContainsTotal() {
        cart.addItem(new CartItem(2.00, 3));
        assertTrue(cart.toString().contains("Total"));
    }
}
