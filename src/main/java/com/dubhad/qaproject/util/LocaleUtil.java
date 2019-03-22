package com.dubhad.qaproject.util;

import com.dubhad.qaproject.resource.Configuration;

import java.util.Locale;

/**
 * Class, that provides utility methods for locale parsing
 */
public class LocaleUtil {
    private static final String LOCALE_DELIMITER = "_";

    /**
     * Get default locale of the application
     * @return default locale
     */
    public static Locale getLocale(){
        return getLocale(null);
    }

    /**
     * Get locale, parsed from input
     * @param localeString String, that represents locale. Expected format: language-country
     * @return locale, parsed from input, if valid, default otherwise
     */
    public static Locale getLocale(String localeString){
        Locale locale;
        if(localeString != null){
            String[] localeParts = localeString.split(LOCALE_DELIMITER);
            if(localeParts.length == 2){
                locale = new Locale(localeParts[0], localeParts[1]);
            }
            else{
                locale = new Locale(Configuration.DEFAULT_LOCALE_LANGUAGE, Configuration.DEFAULT_LOCALE_COUNTRY);
            }
        }
        else{
            locale = new Locale(Configuration.DEFAULT_LOCALE_LANGUAGE, Configuration.DEFAULT_LOCALE_COUNTRY);
        }
        return locale;
    }
}
