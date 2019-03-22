package com.dubhad.qaproject.command;

import com.dubhad.qaproject.command.ajax.*;
import com.dubhad.qaproject.entity.UserRole;
import lombok.Getter;

/**
 * Enum, that specifies mapping between names of ajax commands and actual implementations. All ajax commands must be
 * registered there
 */
public enum AjaxCommandEnum {
    RATE_POSITIVE(new RatePositive(), UserRole.USER),
    RATE_NEGATIVE(new RateNegative(), UserRole.USER),
    MAKE_ADMIN(new MakeAdminCommand(), UserRole.ADMIN),
    UNMAKE_ADMIN(new UnmakeAdminCommand(), UserRole.ADMIN),
    BAN_USER(new BanUserCommand(), UserRole.ADMIN),
    UNBAN_USER(new UnbanUserCommand(), UserRole.ADMIN);

    @Getter
    private AjaxCommand ajaxCommand;
    @Getter
    private UserRole minRole;

    AjaxCommandEnum(AjaxCommand ajaxCommand) {
        this.ajaxCommand = ajaxCommand;
    }

    AjaxCommandEnum(AjaxCommand ajaxCommand, UserRole minUserRole) {
        this.ajaxCommand = ajaxCommand;
        this.minRole = minUserRole;
    }
}
