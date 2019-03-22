package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.Message;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.util.validator.UserValidator;
import lombok.extern.log4j.Log4j2;

import java.util.Locale;
import java.util.Optional;

/**
 * Tries to log in by specified parameters
 * Required user status: any, role: any
 * @see CommandEnum
 */
@Log4j2
public class LoginCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: username, password
     */
    @Override
    public Router execute(RequestWrapper request){
        log.error("SESSION ID: " + request.getSession().getId());
        Router router = new Router();
        try (UserLogic userLogic = new UserLogic()) {
            String username = request.getParameter(RequestParamAttr.USERNAME);
            String password = request.getParameter(RequestParamAttr.PASSWORD);
            log.debug(username);
            Locale locale = request.getLocale();
            router.setPage(PathEnum.LOGIN);
            boolean infoValid = true;
            if(username.isEmpty()){
                String message = Message.REQUIRED_FIELD.get(locale);
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_USERNAME, message);
                infoValid = false;
            }
            else if(!UserValidator.validateUsername(username)){
                String message = Message.USERNAME_INVALID.get(locale);
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_USERNAME, message);
                infoValid = false;
            }
            if(username.isEmpty()){
                String message = Message.REQUIRED_FIELD.get(locale);
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_PASSWORD, message);
                infoValid = false;
            }
            else if(!UserValidator.validatePassword(password)){
                String message = Message.PASSWORD_INVALID.get(locale);
                request.setAttribute(RequestParamAttr.ERROR_MESSAGE_PASSWORD, message);
                infoValid = false;
            }
            if(infoValid){
                Optional<UserBean> userBeanOptional = userLogic.validateLogin(username, password);
                if(userBeanOptional.isPresent()){
                    userLogic.saveUserToContext(userBeanOptional.get());
                    request.getSession().setAttribute(RequestParamAttr.USER, userBeanOptional.get());
                    router.setPage(PathEnum.INTERMEDIATE);
                    request.setAttribute(RequestParamAttr.COMMAND.toLowerCase(), CommandEnum.SHOW_QUESTIONS);
                }
                else {
                    String message = Message.LOGIN_FAILED.get(locale);
                    request.setAttribute(RequestParamAttr.ERROR_MESSAGE_LOGIN, message);
                    request.setAttribute(RequestParamAttr.USERNAME, username);
                }
            }else{
                request.setAttribute(RequestParamAttr.USERNAME, username);
                request.setAttribute(RequestParamAttr.PASSWORD, password);
            }
        } catch (ProjectException e) {
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
