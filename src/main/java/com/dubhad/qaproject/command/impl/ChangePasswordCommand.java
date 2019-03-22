package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.Message;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.util.validator.UserValidator;
import com.dubhad.qaproject.bean.UserBean;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Changes password of current user
 * Required user status: not banned, role: any
 * @see CommandEnum
 */
@Log4j2
public class ChangePasswordCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: passwordOne, passwordTwo
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        String passwordOne = request.getParameter(RequestParamAttr.PASSWORD_ONE);
        String passwordTwo = request.getParameter(RequestParamAttr.PASSWORD_TWO);
        if(passwordOne != null && passwordOne.equals(passwordTwo)){
            if(UserValidator.validatePassword(passwordOne)) {
                try (UserLogic userLogic = new UserLogic()) {
                    UserBean currentUser = userLogic.getUserFromContext();
                    // UserBean currentUser = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
                    if (userLogic.isUserAvailable(currentUser.getId())) {
                        userLogic.changePassword(currentUser, passwordOne);
                        router.setPage(PathEnum.INTERMEDIATE);
                        request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_OWNER_USER_PAGE.name());
                    } else {
                        router.setPage(PathEnum.ERROR_403);
                        router.changeAction();
                    }
                } catch (ProjectException e) {
                    log.error(e);
                    router.setPage(PathEnum.ERROR_500);
                    router.changeAction();
                }
            }
            else{
                request.getSession().setAttribute(RequestParamAttr.ERROR_MESSAGE_PASSWORD,
                        Message.PASSWORD_INVALID.get(locale));
                request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_OWNER_USER_PAGE.name());
                router.setPage(PathEnum.INTERMEDIATE);
            }
        }
        else{
            request.getSession().setAttribute(RequestParamAttr.ERROR_MESSAGE_PASSWORD,
                    Message.SIGNUP_PASSWORD_MISMATCH.get(locale));
            request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_OWNER_USER_PAGE.name());
            router.setPage(PathEnum.INTERMEDIATE);
        }
        return router;
    }
}
