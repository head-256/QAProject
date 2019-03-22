package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.QuestionLogic;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.bean.UserBean;
import lombok.extern.log4j.Log4j2;

/**
 * Closes question
 * Required user status: available, role: admin
 * @see CommandEnum
 */
@Log4j2
public class CloseQuestionCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: question id
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        try(QuestionLogic questionLogic = new QuestionLogic();
            UserLogic userLogic = new UserLogic()){
            UserBean user = userLogic.getUserFromContext();
            // UserBean user = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            long questionId = Long.parseLong(request.getParameter(RequestParamAttr.QUESTION_ID));
            boolean hasCloseRights = questionLogic.hasCloseRights(questionId, user);
            if(hasCloseRights){
                questionLogic.closeQuestion(questionId);
                router.setPage(PathEnum.INTERMEDIATE);
                request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_QUESTION);
                request.setAttribute(RequestParamAttr.QUESTION_ID, questionId);
            }
            else{
                router.setPage(PathEnum.ERROR_403);
                router.changeAction();
            }
        } catch (ProjectException | NumberFormatException e) {
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
