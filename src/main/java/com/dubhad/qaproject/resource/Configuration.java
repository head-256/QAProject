package com.dubhad.qaproject.resource;

import lombok.extern.log4j.Log4j2;

import java.util.MissingResourceException;

/**
 * Class, that defines runtime-loaded configuration constants for app.
 * @see ConfigurationManager
 */
@Log4j2
public class Configuration {
    public static final String DEFAULT_LOCALE_LANGUAGE;
    public static final String DEFAULT_LOCALE_COUNTRY;
    public static final String AVATARS_FOLDER;
    public static final String APP_NAME;
    public static final String ERROR_PAGES;
    public static final int SALT_LENGTH;
    public static final int CONFIRMATION_CODE_LENGTH;
    public static final int CONFIRMATION_EXPIRATION;
    public static final int QUESTION_MAX_TEXT_LENGTH;
    public static final int QUESTION_MAX_TITLE_LENGTH;
    public static final int QUESTION_MIN_TITLE_LENGTH;
    public static final int ANSWER_MAX_LENGTH;
    public static final int ANSWER_MIN_LENGTH;
    public static final int QUESTIONS_PER_PAGE;
    public static final int USERS_PER_PAGE;
    public static final String DEFAULT_AVATAR;

    static {
        try {
            DEFAULT_LOCALE_LANGUAGE = ConfigurationManager.get("config.locale.language");
            APP_NAME = ConfigurationManager.get("config.main.appName");
            ERROR_PAGES = ConfigurationManager.get("config.main.errorPages");
            DEFAULT_AVATAR = ConfigurationManager.get("config.image.defaultAvatar");
            DEFAULT_LOCALE_COUNTRY = ConfigurationManager.get("config.locale.country");
            AVATARS_FOLDER = ConfigurationManager.get("config.uploads.avatarsFolder");
            SALT_LENGTH = Integer.parseInt(ConfigurationManager.get("config.security.saltLength"));
            CONFIRMATION_CODE_LENGTH = Integer.parseInt(ConfigurationManager.get("config.security.confirmationLength"));
            CONFIRMATION_EXPIRATION = Integer.parseInt(ConfigurationManager.get("config.security.confirmationExpiration"));
            QUESTION_MAX_TITLE_LENGTH = Integer.parseInt(ConfigurationManager.get("config.question.maxTitleLength"));
            QUESTION_MIN_TITLE_LENGTH = Integer.parseInt(ConfigurationManager.get("config.question.minTitleLength"));
            QUESTION_MAX_TEXT_LENGTH = Integer.parseInt(ConfigurationManager.get("config.question.maxTextLength"));
            ANSWER_MAX_LENGTH = Integer.parseInt(ConfigurationManager.get("config.answer.maxLength"));
            ANSWER_MIN_LENGTH = Integer.parseInt(ConfigurationManager.get("config.answer.minLength"));
            USERS_PER_PAGE = Integer.parseInt(ConfigurationManager.get("config.paginator.usersPerPage"));
            QUESTIONS_PER_PAGE = Integer.parseInt(ConfigurationManager.get("config.paginator.questionsPerPage"));
        }
        catch (MissingResourceException e){
            log.fatal(e);
            throw new RuntimeException(e);
        }
    }
}
