package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.QuestionBean;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.QuestionLogic;
import com.dubhad.qaproject.resource.Message;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.util.TagUtil;
import com.dubhad.qaproject.util.validator.QuestionValidator;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.logic.UserLogic;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Saves changes to question, made by admin
 * Required user status: available, role: admin
 * @see CommandEnum
 */
@Log4j2
public class SaveAdminQuestionChangesCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: title, tags, questionId
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        String title = request.getParameter(RequestParamAttr.TITLE).trim();
        String tags = request.getParameter(RequestParamAttr.TAGS).trim();
        boolean infoValid = true;
        try (UserLogic userLogic = new UserLogic();
             QuestionLogic questionLogic = new QuestionLogic()){
            Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
            long id = Long.parseLong(request.getParameter(RequestParamAttr.QUESTION_ID));
            UserBean user = userLogic.getUserFromContext();
            // UserBean user = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            boolean hasRight = userLogic.hasGeneralQuestionEditRights(user);
            if(hasRight){
                if(!QuestionValidator.validateTitle(title)){
                    infoValid = false;
                    String message = Message.QUESTION_TITLE_INVALID.get(locale);
                    request.setAttribute(RequestParamAttr.ERROR_MESSAGE_TITLE, message);
                }
                TagUtil.ParseResult parsedTags = TagUtil.parseQuestionsTags(tags, locale);
                if(parsedTags.getErrorMessage() != null){
                    infoValid = false;
                    request.setAttribute(RequestParamAttr.ERROR_MESSAGE_TAGS, parsedTags.getErrorMessage());
                }
                if(infoValid){
                    QuestionBean questionBean = new QuestionBean();
                    questionBean.setId(id);
                    questionBean.setTitle(title);
                    questionBean.setTags(parsedTags.getTags());
                    questionLogic.editQuestionAdmin(questionBean);
                    router.setPage(PathEnum.INTERMEDIATE);
                    request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_QUESTION.name());
                    request.setAttribute(RequestParamAttr.QUESTION_ID, questionBean.getId());
                }
                else {
                    QuestionBean questionBean = questionLogic.getQuestionFull(id, user);
                    request.setAttribute(RequestParamAttr.QUESTION, questionBean);
                    router.setPage(PathEnum.ADMIN_EDIT_QUESTION);
                }
            }
            else{
                router.setPage(PathEnum.ERROR_403);
                router.changeAction();
            }
        }
        catch (NumberFormatException | ProjectException e){
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
