package com.dubhad.qaproject.util.validator;

import java.util.regex.Pattern;

public class PasswordValidator implements Validator<String> {
    private static final Pattern pattern = Pattern.compile("[\\p{L}0-9_]{8,16}");
    @Override
    public boolean validate(String s) {
        return pattern.matcher(s).matches();
    }
}
