package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.QuestionBean;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.QuestionLogic;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.Message;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.util.TagUtil;
import com.dubhad.qaproject.util.validator.QuestionValidator;
import com.dubhad.qaproject.bean.UserBean;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Creates question
 * Required user status: available, role: any
 * @see CommandEnum
 */
@Log4j2
public class CreateQuestionCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: title, tags, text
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        String title = request.getParameter(RequestParamAttr.TITLE).trim();
        String tags = request.getParameter(RequestParamAttr.TAGS).trim();
        String text = request.getParameter(RequestParamAttr.TEXT).trim();
        UserLogic userLogic = new UserLogic();
        UserBean currentUser = null;
        try {
            currentUser = userLogic.getUserFromContext();
        } catch (ProjectException e) {
            e.printStackTrace();
        }
        // UserBean currentUser = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        router.setPage(PathEnum.CREATE_QUESTION);
        boolean infoValid = true;
        if(!QuestionValidator.validateText(text)){
            infoValid = false;
            request.setAttribute(RequestParamAttr.ERROR_MESSAGE_TEXT, Message.QUESTION_TEXT_INVALID);
        }
        if(!QuestionValidator.validateTitle(title)){
            infoValid = false;
            request.setAttribute(RequestParamAttr.ERROR_MESSAGE_TITLE, Message.QUESTION_TITLE_INVALID);
        }
        TagUtil.ParseResult parsedTags = TagUtil.parseQuestionsTags(tags, locale);
        if(parsedTags.getErrorMessage() != null){
            infoValid = false;
            request.setAttribute(RequestParamAttr.ERROR_MESSAGE_TAGS, parsedTags.getErrorMessage());
        }
        if(infoValid){
            try {
                QuestionLogic questionLogic = new QuestionLogic();
                QuestionBean questionBean = new QuestionBean();
                questionBean.setTitle(title);
                if(!text.isEmpty()){
                    questionBean.setText(text);
                }
                else{
                    questionBean.setText(title);
                }
                questionBean.setTags(parsedTags.getTags());
                questionLogic.createQuestion(questionBean, currentUser);
                router.setPage(PathEnum.INTERMEDIATE);
                request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_QUESTION.name());
                request.setAttribute(RequestParamAttr.QUESTION_ID, questionBean.getId());
            }
            catch (ProjectException e){
                log.error(e);
                router.setPage(PathEnum.ERROR_500);
                router.changeAction();
            }
        }
        else{
            router.setPage(PathEnum.CREATE_QUESTION);
            request.setAttribute(RequestParamAttr.TAGS, tags);
            request.setAttribute(RequestParamAttr.TITLE, title);
            request.setAttribute(RequestParamAttr.TEXT, text);
        }
        return router;
    }
}
