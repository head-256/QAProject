package com.dubhad.qaproject.util.validator;

import com.dubhad.qaproject.resource.Configuration;

/**
 * Class, that provides methods for answer validation
 */
public class AnswerValidator {
    /**
     * Validates passed answer text
     * @param text text to validate
     * @return true, if text is valid, false otherwise
     */
    public static boolean validateText(String text) {
        return text.length() >= Configuration.ANSWER_MIN_LENGTH &&
                text.length() <= Configuration.ANSWER_MAX_LENGTH;
    }
}
