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
import java.util.Objects;

/**
 * Tries to sign up with specified params
 * Required user status: any, role: any
 * @see CommandEnum
 */
@Log4j2
public class SignUpCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: username, email, passwordOne, passwordTwo
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        try (UserLogic userLogic = new UserLogic()){
            String username = request.getParameter(RequestParamAttr.USERNAME);
            String email = request.getParameter(RequestParamAttr.EMAIL);
            String passwordOne = request.getParameter(RequestParamAttr.PASSWORD_ONE);
            String passwordTwo = request.getParameter(RequestParamAttr.PASSWORD_TWO);
            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

            boolean infoValid = true;
            if(!UserValidator.validateUsername(username)){
                infoValid = false;
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_USERNAME, Message.USERNAME_INVALID.get(locale));
            }
            else if(!userLogic.checkUsernameAvailable(username)){
                infoValid = false;
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_USERNAME, Message.SIGNUP_USERNAME_TAKEN.get(locale));
            }
            if(!UserValidator.validatePassword(passwordOne) || !UserValidator.validatePassword(passwordTwo)){
                infoValid = false;
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_PASSWORD, Message.PASSWORD_INVALID.get(locale));
            }
            else if(!Objects.equals(passwordOne, passwordTwo)){
                infoValid = false;
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_PASSWORD, Message.SIGNUP_PASSWORD_MISMATCH.get(locale));
            }
            if(!UserValidator.validateEmail(email)){
                infoValid = false;
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_EMAIL, Message.EMAIL_INVALID.get(locale));
            }
            else if(!userLogic.checkEmailAvailable(email)){
                infoValid = false;
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_EMAIL, Message.SIGNUP_EMAIL_TAKEN.get(locale));
            }
            if(infoValid) {
                UserBean userBean = userLogic.createUser(email, username, passwordOne);
                StringBuffer baseLink = request.getRequestURL();
                router.setPage(PathEnum.INTERMEDIATE);
                request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_QUESTIONS);
            }
            else{
                router.setPage(PathEnum.SIGNUP);
                request.setAttribute(RequestParamAttr.USERNAME, username);
                request.setAttribute(RequestParamAttr.EMAIL, email);
            }
        } catch (ProjectException e) {
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
