package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.QuestionLogic;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.logic.UserLogic;
import lombok.extern.log4j.Log4j2;

/**
 * Deletes specified question
 * Required user status: available, role: admin
 * @see CommandEnum
 */
@Log4j2
public class DeleteQuestionCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: questionId
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        try(UserLogic userLogic = new UserLogic();
            QuestionLogic questionLogic = new QuestionLogic()){
            UserBean userBean = userLogic.getUserFromContext();
            // UserBean userBean = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            long questionId = Long.parseLong(request.getParameter(RequestParamAttr.QUESTION_ID));
            if(userLogic.hasDeleteRights(userBean)){
                questionLogic.deleteQuestion(questionId);
                router.setPage(PathEnum.INTERMEDIATE);
                request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_QUESTION.name());
                request.setAttribute(RequestParamAttr.QUESTION_ID, questionId);
            }
            else {
                router.setPage(PathEnum.ERROR_403);
                router.changeAction();
            }
        } catch (ProjectException e) {
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
