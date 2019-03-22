package com.dubhad.qaproject.pool;

import lombok.extern.log4j.Log4j2;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class for fetching settings for db from properties file
 */
@Log4j2
class DbSettingsManager {
    private static final String DEFAULT_BUNDLE_NAME = "dbSettings";

    static final String DRIVER_NAME;
    static final String URL;
    static final String USER;
    static final String PASSWORD;

    static {
        Locale locale = Locale.ENGLISH;
        ResourceBundle myResources;
        try {
            myResources = ResourceBundle.getBundle(DEFAULT_BUNDLE_NAME, locale);
            DRIVER_NAME = myResources.getString("driverName");
            URL = myResources.getString("url");
            USER = myResources.getString("user");
            PASSWORD = myResources.getString("password");
        }
        catch (MissingResourceException e){
            log.fatal("Resource can't be found", e);
            throw new RuntimeException(e);
        }
        catch (NumberFormatException e){
            log.fatal("Resource value format mismatch", e);
            throw new RuntimeException(e);
        }
    }
}
