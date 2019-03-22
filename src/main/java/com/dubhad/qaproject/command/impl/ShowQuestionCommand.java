package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.QuestionBean;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.QuestionLogic;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import lombok.extern.log4j.Log4j2;

/**
 * Shows full question info with answers
 * Required user status: any, role: any
 * @see CommandEnum
 */
@Log4j2
public class ShowQuestionCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: questionId
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        try (QuestionLogic questionLogic = new QuestionLogic();
             UserLogic userLogic = new UserLogic()){
            long questionId = Long.parseLong(request.getParameter(RequestParamAttr.QUESTION_ID));
            UserBean currentUser = userLogic.getUserFromContext();
            // UserBean currentUser = (UserBean)request.getSession().getAttribute(RequestParamAttr.USER);
            if(questionLogic.questionViewAvailable(questionId, currentUser)) {
                QuestionBean question = questionLogic.getQuestionFull(questionId, currentUser);
                request.setAttribute(RequestParamAttr.QUESTION, question);
                if(currentUser != null) {
                    UserBean userBean = userLogic.getUser(currentUser.getId());
                    request.setAttribute(RequestParamAttr.CURRENT_USER, userBean);
                }
                router.setPage(PathEnum.QUESTION);
            }
            else{
                router.setPage(PathEnum.ERROR_403);
                router.changeAction();
            }
        } catch (ProjectException e) {
            log.error("Exception in dao layer", e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        } catch (NumberFormatException e){
            log.error("Question id should be long", e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
