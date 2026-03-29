package com.shoppingcart.model;

import com.shoppingcart.util.LocaleManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link LocaleManager}.
 * Tests locale switching and string retrieval for all supported languages.
 */
@DisplayName("LocaleManager Tests")
class LocaleManagerTest {

    @AfterEach
    void resetLocale() {
        // Always reset to English after each test
        LocaleManager.setLocale(LocaleManager.ENGLISH);
    }

    // ── Default locale ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Default locale is English")
    void testDefaultLocaleIsEnglish() {
        LocaleManager.setLocale(LocaleManager.ENGLISH);
        assertEquals(LocaleManager.ENGLISH, LocaleManager.getCurrentLocale());
    }

    // ── setLocale / getCurrentLocale ──────────────────────────────────────────

    @Test
    @DisplayName("setLocale changes current locale to Finnish")
    void testSetFinnishLocale() {
        LocaleManager.setLocale(LocaleManager.FINNISH);
        assertEquals(LocaleManager.FINNISH, LocaleManager.getCurrentLocale());
    }

    @Test
    @DisplayName("setLocale changes current locale to Swedish")
    void testSetSwedishLocale() {
        LocaleManager.setLocale(LocaleManager.SWEDISH);
        assertEquals(LocaleManager.SWEDISH, LocaleManager.getCurrentLocale());
    }

    @Test
    @DisplayName("setLocale changes current locale to Japanese")
    void testSetJapaneseLocale() {
        LocaleManager.setLocale(LocaleManager.JAPANESE);
        assertEquals(LocaleManager.JAPANESE, LocaleManager.getCurrentLocale());
    }

    @Test
    @DisplayName("setLocale changes current locale to Arabic")
    void testSetArabicLocale() {
        LocaleManager.setLocale(LocaleManager.ARABIC);
        assertEquals(LocaleManager.ARABIC, LocaleManager.getCurrentLocale());
    }

    // ── getString ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("English: getString returns non-empty string for known key")
    void testEnglishGetString() {
        LocaleManager.setLocale(LocaleManager.ENGLISH);
        String val = LocaleManager.getString("label.totalCost");
        assertNotNull(val);
        assertFalse(val.isEmpty());
        assertFalse(val.startsWith("!"));
    }

    @Test
    @DisplayName("Finnish: getString returns non-empty string for known key")
    void testFinnishGetString() {
        LocaleManager.setLocale(LocaleManager.FINNISH);
        String val = LocaleManager.getString("label.totalCost");
        assertNotNull(val);
        assertFalse(val.isEmpty());
        assertFalse(val.startsWith("!"));
    }

    @Test
    @DisplayName("Swedish: getString returns non-empty string for known key")
    void testSwedishGetString() {
        LocaleManager.setLocale(LocaleManager.SWEDISH);
        String val = LocaleManager.getString("button.calculate");
        assertNotNull(val);
        assertFalse(val.isEmpty());
    }

    @Test
    @DisplayName("Japanese: getString returns non-empty string for known key")
    void testJapaneseGetString() {
        LocaleManager.setLocale(LocaleManager.JAPANESE);
        String val = LocaleManager.getString("label.numItems");
        assertNotNull(val);
        assertFalse(val.isEmpty());
    }

    @Test
    @DisplayName("Arabic: getString returns non-empty string for known key")
    void testArabicGetString() {
        LocaleManager.setLocale(LocaleManager.ARABIC);
        String val = LocaleManager.getString("label.numItems");
        assertNotNull(val);
        assertFalse(val.isEmpty());
    }

    @Test
    @DisplayName("getString: unknown key returns !key! placeholder")
    void testUnknownKeyReturnsPlaceholder() {
        LocaleManager.setLocale(LocaleManager.ENGLISH);
        String val = LocaleManager.getString("no.such.key.xyz");
        assertTrue(val.startsWith("!") && val.endsWith("!"));
    }

    // ── isRTL ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isRTL: false for English")
    void testIsRTLEnglish() {
        LocaleManager.setLocale(LocaleManager.ENGLISH);
        assertFalse(LocaleManager.isRTL());
    }

    @Test
    @DisplayName("isRTL: false for Finnish")
    void testIsRTLFinnish() {
        LocaleManager.setLocale(LocaleManager.FINNISH);
        assertFalse(LocaleManager.isRTL());
    }

    @Test
    @DisplayName("isRTL: true for Arabic")
    void testIsRTLArabic() {
        LocaleManager.setLocale(LocaleManager.ARABIC);
        assertTrue(LocaleManager.isRTL());
    }

    // ── getDisplayName ────────────────────────────────────────────────────────

    @Test
    @DisplayName("getDisplayName: English returns 'English'")
    void testDisplayNameEnglish() {
        assertEquals("English", LocaleManager.getDisplayName(LocaleManager.ENGLISH));
    }

    @Test
    @DisplayName("getDisplayName: Finnish returns Finnish label")
    void testDisplayNameFinnish() {
        assertTrue(LocaleManager.getDisplayName(LocaleManager.FINNISH).contains("Finnish"));
    }

    @Test
    @DisplayName("getDisplayName: Arabic returns Arabic label")
    void testDisplayNameArabic() {
        String name = LocaleManager.getDisplayName(LocaleManager.ARABIC);
        assertNotNull(name);
        assertFalse(name.isEmpty());
    }

    // ── getBundle ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getBundle returns non-null ResourceBundle")
    void testGetBundleNotNull() {
        assertNotNull(LocaleManager.getBundle());
    }
}
