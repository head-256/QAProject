package com.dubhad.qaproject.command;

import com.dubhad.qaproject.command.impl.*;
import com.dubhad.qaproject.entity.UserRole;
import com.dubhad.qaproject.servlet.Controller;
import com.dubhad.qaproject.entity.UserStatus;
import lombok.Getter;

/**
 * Enum, that specifies mapping between names of commands and actual implementations. All commands must be
 * registered there
 * @see Controller
 */
@Getter
public enum CommandEnum {
    SHOW_QUESTION(new ShowQuestionCommand()),
    SHOW_QUESTIONS(new ShowQuestionsCommand()),
    SHOW_OWNER_USER_PAGE(new ShowOwnerUserPageCommand(), UserRole.USER),
    SHOW_USER_PAGE(new ShowUserPageCommand()),
    SHOW_USERS(new ShowUsersCommand(), UserRole.ADMIN, UserStatus.AVAILABLE),
    SHOW_LOGIN(new ShowLoginCommand()),

    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    SIGNUP(new SignUpCommand()),
    SHOW_SIGNUP(new ShowSignupCommand()),
    CHANGE_PASSWORD(new ChangePasswordCommand(), UserRole.USER),
    CHANGE_EMAIL(new ChangeEmailCommand(), UserRole.USER),

    SHOW_QUESTION_CREATION(new ShowQuestionCreationFromCommand(), UserRole.USER, UserStatus.AVAILABLE),
    CREATE_QUESTION(new CreateQuestionCommand(), UserRole.USER, UserStatus.AVAILABLE),
    SHOW_EDIT_QUESTION(new ShowEditQuestionFormCommand(), UserRole.USER, UserStatus.AVAILABLE),
    SAVE_QUESTION_CHANGES(new SaveQuestionChangesCommand(), UserRole.USER, UserStatus.AVAILABLE),
    SAVE_ADMIN_QUESTION_CHANGES(new SaveAdminQuestionChangesCommand(), UserRole.ADMIN, UserStatus.AVAILABLE),
    CLOSE_QUESTION(new CloseQuestionCommand(), UserRole.USER, UserStatus.AVAILABLE),
    DELETE_QUESTION(new DeleteQuestionCommand(), UserRole.ADMIN, UserStatus.AVAILABLE),

    CREATE_ANSWER(new CreateAnswerCommand(), UserRole.USER, UserStatus.AVAILABLE),
    EDIT_ANSWER(new EditAnswerCommand(), UserRole.USER, UserStatus.AVAILABLE),
    DELETE_ANSWER(new DeleteAnswerCommand(), UserRole.ADMIN, UserStatus.AVAILABLE),

    CHANGE_LOCALE(new ChangeLocaleCommand()),
    UPLOAD_AVATAR(new UploadAvatarCommand(), UserRole.USER, UserStatus.AVAILABLE),
    DELETE_AVATAR(new DeleteAvatarCommand(), UserRole.USER);

    private ActionCommand actionCommand;
    private UserRole minRole;
    private UserStatus status;

    CommandEnum(ActionCommand actionCommand, UserRole minRole, UserStatus status) {
        this.actionCommand = actionCommand;
        this.minRole = minRole;
        this.status = status;
    }

    CommandEnum(ActionCommand actionCommand, UserRole minRole) {
        this.actionCommand = actionCommand;
        this.minRole = minRole;
    }

    CommandEnum(ActionCommand actionCommand) {
        this.actionCommand = actionCommand;
    }
}
