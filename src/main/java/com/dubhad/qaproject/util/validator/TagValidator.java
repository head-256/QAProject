package com.dubhad.qaproject.util.validator;

import java.util.regex.Pattern;

/**
 * Class, that provides methods for tags validation
 */
public class TagValidator {
    private static final Pattern TAG_PATTERN = Pattern.compile("[\\p{L}0-9_][\\p{L}0-9\\-\\s_]{1,14}[\\p{L}0-9_]");

    /**
     * Validates passed tag
     * @param tag tag to validateText
     * @return true, if tag is valid, false otherwise
     */
    public static boolean validateText(String tag) {
        return TAG_PATTERN.matcher(tag).matches();
    }
}
