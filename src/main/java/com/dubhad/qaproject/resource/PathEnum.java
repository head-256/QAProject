package com.dubhad.qaproject.resource;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

/**
 * Enum, that provides access to all paths to pages of application
 */
@Getter
@Log4j2
public enum PathEnum {
    QUESTIONS("path.page.questions"),
    QUESTION("path.page.question"),
    LOGIN("path.page.login"),
    SIGNUP("path.page.signup"),
    INDEX("path.page.index"),
    OWNER_USERPAGE("path.page.ownerUserpage"),
    USERPAGE("path.page.userpage"),
    CREATE_QUESTION("path.page.createQuestion"),
    INTERMEDIATE("path.intermediate"),
    EDIT_QUESTION("path.page.editQuestion"),
    USERS("path.page.users"),
    ADMIN_EDIT_QUESTION("path.page.adminEditQuestion"),

    ERROR_403("path.error.403"),
    ERROR_404("path.error.404"),
    ERROR_500("path.error.500");

    private String name;
    private String path;

    PathEnum(String name) {
        this.name = name;
        this.path = ConfigurationManager.get(name);
    }
}
