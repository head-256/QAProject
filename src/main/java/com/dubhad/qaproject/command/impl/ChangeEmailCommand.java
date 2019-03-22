package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.resource.Message;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.util.validator.UserValidator;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.logic.UserLogic;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Changes email of current user and sends confirmation email
 * Required user status: not banned, role: any
 * @see CommandEnum
 */
@Log4j2
public class ChangeEmailCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: email;
     * Required session attributes: user;
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        String email = request.getParameter(RequestParamAttr.EMAIL);
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        if(UserValidator.validateEmail(email)) {
            try (UserLogic userLogic = new UserLogic()) {
                UserBean user = userLogic.getUserFromContext();
                // UserBean user = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
                if(userLogic.checkEmailAvailable(email)){
                    user.setEmail(email);
                    userLogic.changeEmail(user, locale, request.getRequestURL().toString());
                    router.setPage(PathEnum.INTERMEDIATE);
                    request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_OWNER_USER_PAGE.name());
                }
                else{
                    request.getSession().setAttribute(RequestParamAttr.ERROR_MESSAGE_EMAIL,
                            Message.SIGNUP_EMAIL_TAKEN.get(locale));
                    request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_OWNER_USER_PAGE.name());
                    router.setPage(PathEnum.INTERMEDIATE);
                }
            } catch (ProjectException e) {
                log.error(e);
                router.setPage(PathEnum.ERROR_500);
                router.changeAction();
            }
        }
        else{
            request.getSession().setAttribute(RequestParamAttr.ERROR_MESSAGE_EMAIL, Message.EMAIL_INVALID.get(locale));
            request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_OWNER_USER_PAGE.name());
            router.setPage(PathEnum.INTERMEDIATE);
        }
        return router;
    }
}
