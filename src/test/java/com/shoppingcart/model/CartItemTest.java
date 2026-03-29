package com.shoppingcart.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CartItem}.
 * Tests price × quantity calculation and validation logic.
 */
@DisplayName("CartItem Tests")
class CartItemTest {

    // ── Constructor & basic getters ──────────────────────────────────────────

    @Test
    @DisplayName("Constructor sets fields correctly")
    void testConstructorSetsFields() {
        CartItem item = new CartItem("Apple", 1.50, 4);
        assertEquals("Apple", item.getName());
        assertEquals(1.50, item.getPrice(), 0.001);
        assertEquals(4, item.getQuantity());
    }

    @Test
    @DisplayName("Two-arg constructor defaults name to 'Item'")
    void testTwoArgConstructor() {
        CartItem item = new CartItem(2.00, 3);
        assertEquals("Item", item.getName());
        assertEquals(2.00, item.getPrice(), 0.001);
        assertEquals(3, item.getQuantity());
    }

    // ── getItemTotal ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("getItemTotal: price=2.50, qty=4 → 10.00")
    void testItemTotalBasic() {
        CartItem item = new CartItem(2.50, 4);
        assertEquals(10.00, item.getItemTotal(), 0.001);
    }

    @Test
    @DisplayName("getItemTotal: price=0, qty=5 → 0.00")
    void testItemTotalZeroPrice() {
        CartItem item = new CartItem(0.0, 5);
        assertEquals(0.00, item.getItemTotal(), 0.001);
    }

    @Test
    @DisplayName("getItemTotal: qty=0 → 0.00")
    void testItemTotalZeroQuantity() {
        CartItem item = new CartItem(9.99, 0);
        assertEquals(0.00, item.getItemTotal(), 0.001);
    }

    @Test
    @DisplayName("getItemTotal: price=0, qty=0 → 0.00")
    void testItemTotalBothZero() {
        CartItem item = new CartItem(0.0, 0);
        assertEquals(0.00, item.getItemTotal(), 0.001);
    }

    @Test
    @DisplayName("getItemTotal: large numbers")
    void testItemTotalLargeValues() {
        CartItem item = new CartItem(999.99, 1000);
        assertEquals(999990.00, item.getItemTotal(), 0.01);
    }

    @Test
    @DisplayName("getItemTotal: fractional cents precision")
    void testItemTotalPrecision() {
        CartItem item = new CartItem(0.10, 3);
        assertEquals(0.30, item.getItemTotal(), 0.001);
    }

    // ── Parameterized price × quantity tests ─────────────────────────────────

    @ParameterizedTest(name = "price={0}, qty={1} → total={2}")
    @CsvSource({
        "1.00,  1,   1.00",
        "5.00,  3,  15.00",
        "2.50,  4,  10.00",
        "0.99, 10,   9.90",
        "7.77,  7,  54.39",
        "100.0, 2, 200.00",
        "3.33,  3,   9.99"
    })
    @DisplayName("Parameterized: price × quantity = expected total")
    void testItemTotalParameterized(double price, int qty, double expected) {
        CartItem item = new CartItem(price, qty);
        assertEquals(expected, item.getItemTotal(), 0.005);
    }

    // ── Setters ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("setPrice updates the price")
    void testSetPrice() {
        CartItem item = new CartItem(1.00, 1);
        item.setPrice(5.50);
        assertEquals(5.50, item.getPrice(), 0.001);
        assertEquals(5.50, item.getItemTotal(), 0.001);
    }

    @Test
    @DisplayName("setQuantity updates the quantity")
    void testSetQuantity() {
        CartItem item = new CartItem(2.00, 1);
        item.setQuantity(6);
        assertEquals(6, item.getQuantity());
        assertEquals(12.00, item.getItemTotal(), 0.001);
    }

    @Test
    @DisplayName("setName updates the name")
    void testSetName() {
        CartItem item = new CartItem(1.00, 1);
        item.setName("Banana");
        assertEquals("Banana", item.getName());
    }

    // ── Validation ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Constructor: negative price throws IllegalArgumentException")
    void testNegativePriceThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(-1.0, 1));
    }

    @Test
    @DisplayName("Constructor: negative quantity throws IllegalArgumentException")
    void testNegativeQuantityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(1.0, -1));
    }

    @Test
    @DisplayName("setPrice: negative value throws IllegalArgumentException")
    void testSetNegativePriceThrows() {
        CartItem item = new CartItem(1.0, 1);
        assertThrows(IllegalArgumentException.class, () -> item.setPrice(-5.0));
    }

    @Test
    @DisplayName("setQuantity: negative value throws IllegalArgumentException")
    void testSetNegativeQuantityThrows() {
        CartItem item = new CartItem(1.0, 1);
        assertThrows(IllegalArgumentException.class, () -> item.setQuantity(-3));
    }

    // ── toString ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("toString contains key fields")
    void testToString() {
        CartItem item = new CartItem("Book", 12.99, 2);
        String s = item.toString();
        assertTrue(s.contains("Book"));
        assertTrue(s.contains("12.99"));
        assertTrue(s.contains("2"));
    }
}
