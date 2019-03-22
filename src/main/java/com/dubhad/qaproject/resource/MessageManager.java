package com.dubhad.qaproject.resource;

import java.util.*;

/**
 * Class, that provides methods for localized messages. Has HashMap from locale to ResourceBundle to prevent
 * creating bundles for each request
 */
public class MessageManager {
    private final static Map<Locale, ResourceBundle> RESOURCE_BUNDLE_MAP = new HashMap<>();

    /**
     * Get specified message with specified locale
     * @param key message key
     * @param locale desired message locale
     * @return localized message
     */
    public static String getProperty(String key, Locale locale) {
        if(!RESOURCE_BUNDLE_MAP.keySet().contains(locale)){
            RESOURCE_BUNDLE_MAP.put(locale, ResourceBundle.getBundle(Const.MESSAGES_PATH, locale));
        }
        return RESOURCE_BUNDLE_MAP.get(locale).getString(key);
    }
}
