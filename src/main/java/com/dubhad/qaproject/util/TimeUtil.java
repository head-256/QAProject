package com.dubhad.qaproject.util;

import com.dubhad.qaproject.resource.Configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Class, that provides utility methods for time tasks
 */
public class TimeUtil {
    /**
     * Get expiration time for newly created confirmation object
     * @return time with specified in configuration offset from now
     */
    public static Instant getConfirmationExpiration(){
        return Instant.now().plus(Configuration.CONFIRMATION_EXPIRATION, ChronoUnit.MINUTES);
    }
}
