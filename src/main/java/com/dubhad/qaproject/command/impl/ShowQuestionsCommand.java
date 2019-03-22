package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.QuestionBean;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.QuestionLogic;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.Configuration;
import com.dubhad.qaproject.resource.Message;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.util.TagUtil;
import com.dubhad.qaproject.util.validator.QuestionValidator;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.jstl.core.Config;
import java.util.List;
import java.util.Locale;

/**
 * Shows specified page of recently added or modified questions
 * Required user status: any, role: any
 * @see CommandEnum
 */
@Log4j2
public class ShowQuestionsCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: title, tags, page,
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        log.error("SESSION ID: " + request.getSession().getId());
        Router router = new Router();
        String title = request.getParameter(RequestParamAttr.TITLE);
        String tags = request.getParameter(RequestParamAttr.TAGS);
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        long page;
        try{
            page = Long.parseLong(request.getParameter(RequestParamAttr.PAGE));
        }
        catch (NumberFormatException e){
            page = 1;
        }
        try(QuestionLogic questionLogic = new QuestionLogic();
            UserLogic userLogic = new UserLogic()) {
            UserBean userBean = userLogic.getUserFromContext();
            // UserBean userBean = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            boolean infoValid = true;

            TagUtil.ParseResult parsedTags = TagUtil.parseSearchTags(tags, locale);
            if(tags != null && !tags.isEmpty() && parsedTags.getErrorMessage() != null){
                infoValid = false;
                request.getSession().setAttribute(RequestParamAttr.ERROR_MESSAGE_TAGS, parsedTags.getErrorMessage());
            }
            if(title != null && !title.isEmpty() && !QuestionValidator.validateSearchTitle(title)){
                infoValid = false;
                String message = Message.QUESTION_TITLE_INVALID.get(locale);
                request.getSession().setAttribute(RequestParamAttr.ERROR_MESSAGE_TITLE, message);
            }
            QuestionBean questionBean = new QuestionBean();
            if(infoValid) {
                questionBean.setTitle(title);
                questionBean.setTags(parsedTags.getTags());
                long noOfRecords = questionLogic.getCount(questionBean, userBean);
                long numOfPages = (int) Math.ceil(noOfRecords * 1.0 / Configuration.QUESTIONS_PER_PAGE);
                List<QuestionBean> list;
                if(numOfPages < page){
                    page = numOfPages;
                }
                if(numOfPages > 1){
                    long offset = (page-1)*Configuration.QUESTIONS_PER_PAGE;
                    list = questionLogic.getQuestionsPage(offset, Configuration.QUESTIONS_PER_PAGE, questionBean, userBean);
                }
                else{
                    list = questionLogic.getQuestionsPage(0, Configuration.QUESTIONS_PER_PAGE, questionBean, userBean);
                    page = 1;
                }
                request.setAttribute(RequestParamAttr.TITLE, title);
                request.setAttribute(RequestParamAttr.TAGS, tags);
                request.setAttribute(RequestParamAttr.QUESTIONS, list);
                request.setAttribute(RequestParamAttr.PAGES, numOfPages);
                request.setAttribute(RequestParamAttr.PAGE, page);
                router.setPage(PathEnum.QUESTIONS);
            }
            else{
                router.setPage(PathEnum.INTERMEDIATE);
                request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_QUESTIONS.name());
            }
        } catch (ProjectException e) {
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
