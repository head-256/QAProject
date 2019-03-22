package com.dubhad.qaproject.resource;

import lombok.Getter;
import java.util.Locale;

/**
 * Enum, that provides access to message constants by locale
 */
public enum Message {
    LOGIN_FAILED("messages.login.loginFailed"),

    SIGNUP_USERNAME_TAKEN("messages.signup.usernameTaken"),
    SIGNUP_EMAIL_TAKEN("messages.signup.emailTaken"),
    SIGNUP_PASSWORD_MISMATCH("messages.signup.passwordMismatch"),

    USERNAME_INVALID("messages.common.usernameInvalid"),
    PASSWORD_INVALID("messages.common.passwordInvalid"),
    EMAIL_INVALID("messages.common.emailInvalid"),
    REQUIRED_FIELD("messages.common.requiredField"),

    QUESTION_TEXT_INVALID("messages.question.textInvalid"),
    QUESTION_TITLE_INVALID("messages.question.titleInvalid"),
    QUESTION_TAGS_INVALID("messages.question.tagsInvalid"),
    QUESTION_TAG_REQUIRED("messages.question.tagRequired"),

    ANSWER_TEXT_INVALID("messages.answer.textInvalid"),
    AJAX_LOGIN_REQUIRED("messages.ajax.loginRequired"),
    AJAX_LOW_PRIVILEGES("messages.ajax.lowPrivileges"),
    AJAX_ANSWER_RATE_ERROR("messages.ajax.rateError"),

    CONFIRMATION_EXPIRED("message.confirmation.expired"),
    CONFIRMATION_ALREADY_CONFIRMED("message.confirmation.alreadyConfirmed"),
    CONFIRMATION_CONFIRMED("message.confirmation.confirmed");

    @Getter
    private String key;

    /**
     * Get localized message
     * @param locale desired locale
     * @return localized message
     */
    public String get(Locale locale){
        return MessageManager.getProperty(getKey(), locale);
    }

    Message(String key) {
        this.key = key;
    }
}
