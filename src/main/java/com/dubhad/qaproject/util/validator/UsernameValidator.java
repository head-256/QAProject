package com.dubhad.qaproject.util.validator;

import java.util.regex.Pattern;

public class UsernameValidator implements Validator<String> {
    private static final Pattern pattern = Pattern.compile("\\p{L}[\\p{L}0-9_]{5,19}");
    @Override
    public boolean validate(String s) {
        return pattern.matcher(s).matches();
    }
}
