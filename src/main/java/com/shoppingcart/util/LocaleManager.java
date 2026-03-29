package com.shoppingcart.util;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleManager {

    private static final String BUNDLE_BASE_NAME = "com/shoppingcart/i18n/MessagesBundle";

    private static Locale currentLocale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle(
            BUNDLE_BASE_NAME, currentLocale);

    // Supported locales
    public static final Locale ENGLISH  = new Locale("en", "US");
    public static final Locale FINNISH  = new Locale("fi", "FI");
    public static final Locale SWEDISH  = new Locale("sv", "SE");
    public static final Locale JAPANESE = new Locale("ja", "JP");
    public static final Locale ARABIC   = new Locale("ar", "AR");

    private LocaleManager() {}

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static String getString(String key) {
        try {
            String value = bundle.getString(key);
            // Ensure UTF-8 encoding for properties read as ISO-8859-1 by older JVMs
            return new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "!" + key + "!";
        }
    }


    public static ResourceBundle getBundle() {
        return bundle;
    }

    public static String getDisplayName(Locale locale) {
        if (locale.equals(FINNISH))  return "Suomi (Finnish)";
        if (locale.equals(SWEDISH))  return "Svenska (Swedish)";
        if (locale.equals(JAPANESE)) return "日本語 (Japanese)";
        if (locale.equals(ARABIC))   return "العربية (Arabic)";
        return "English";
    }

    public static boolean isRTL() {
        return currentLocale.equals(ARABIC);
    }
}
