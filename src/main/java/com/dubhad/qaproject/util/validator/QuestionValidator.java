package com.dubhad.qaproject.util.validator;

import com.dubhad.qaproject.resource.Configuration;

/**
 * Class, that provides validation methods for question fields
 */
public class QuestionValidator {
    /**
     * Validates passed text
     * @param text text to validateUsername
     * @return true, if text is valid, false otherwise
     */
    public static boolean validateText(String text) {
        return text.length() <= Configuration.QUESTION_MAX_TEXT_LENGTH;
    }

    /**
     * Validates passed title
     * @param title title to validateUsername
     * @return true, if title is valid, false otherwise
     */
    public static boolean validateTitle(String title) {
        return title.length() >= Configuration.QUESTION_MIN_TITLE_LENGTH &&
                title.length() <= Configuration.QUESTION_MAX_TITLE_LENGTH;
    }

    /**
     * Validates passed title for search purposes
     * @param title title to validateUsername
     * @return true, if title is valid for search, false otherwise
     */
    public static boolean validateSearchTitle(String title) {
        return title.length() <= Configuration.QUESTION_MAX_TITLE_LENGTH;
    }
}
