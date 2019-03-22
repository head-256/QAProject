package com.dubhad.qaproject.util.validator;

import java.util.regex.Pattern;

/**
 * Class, that provides methods for validating User fields
 */
public class UserValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])" +
                    "?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("\\p{L}[\\p{L}0-9_]{5,19}");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[\\p{L}0-9_]{8,16}$");

    /**
     * Validate passed email
     * @param email email to validateText
     * @return true, if email is valid, false otherwise
     */
    public static boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate passed username
     * @param username username to validateText
     * @return true, if username is valid, false otherwise
     */
    public static boolean validateUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Validate passed password
     * @param password password to validateText
     * @return true, if password is valid, false otherwise
     */
    public static boolean validatePassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
