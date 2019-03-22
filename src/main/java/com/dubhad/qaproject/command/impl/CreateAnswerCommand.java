package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.AnswerLogic;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.Message;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.util.validator.AnswerValidator;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Creates answer for specified question
 * Required user status: available, role: any
 * @see CommandEnum
 */
@Log4j2
public class CreateAnswerCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: text, questionId
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        try(AnswerLogic answerLogic = new AnswerLogic();
            UserLogic userLogic = new UserLogic()){
            String text = request.getParameter(RequestParamAttr.TEXT).trim();
            long questionId = Long.parseLong(request.getParameter(RequestParamAttr.QUESTION_ID));
            UserBean currentUser = userLogic.getUserFromContext();
            // UserBean currentUser = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
            if(AnswerValidator.validateText(text)) {
                answerLogic.createAnswer(currentUser.getId(), questionId, text);
            }
            else{
                String message = Message.ANSWER_TEXT_INVALID.get(locale);
                request.getSession().setAttribute(RequestParamAttr.ERROR_MESSAGE_ANSWER, message);
            }
            router.setPage(PathEnum.INTERMEDIATE);
            request.setAttribute(RequestParamAttr.QUESTION_ID, questionId);
            request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_QUESTION.name());
        }
        catch (NumberFormatException | ProjectException e) {
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
