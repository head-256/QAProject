package com.dubhad.qaproject.resource;

import lombok.extern.log4j.Log4j2;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class, that loads configuration constants from specified in  properties file. Throws runtime exception,
 * if property not found{@link Const}
 */
@Log4j2
class ConfigurationManager {
    private static final ResourceBundle RESOURCE_BUNDLE;

    static {
        try{
            RESOURCE_BUNDLE = ResourceBundle.getBundle(Const.CONFIGURATION_PATH);
        }
        catch (MissingResourceException e){
            log.fatal("Resource can't be found", e);
            throw new RuntimeException(e);
        }
    }

    private ConfigurationManager() { }

    /**
     * Gets values mapped with specified key in properties file
     * @param key key to get value
     * @return found value
     */
    public static String get(String key) {
        String result;
        try {
            result = RESOURCE_BUNDLE.getString(key);
        }
        catch (MissingResourceException e){
            log.error("Resource can't be found", e);
            throw new RuntimeException(e);
        }
        return result;
    }
}
