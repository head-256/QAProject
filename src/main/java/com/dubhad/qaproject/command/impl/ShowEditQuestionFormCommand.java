package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.QuestionBean;
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
 * Shows appropriate edit question form
 * Required user status: available, role: any
 * @see CommandEnum
 */
@Log4j2
public class ShowEditQuestionFormCommand implements ActionCommand {
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
            long id = Long.parseLong(request.getParameter(RequestParamAttr.QUESTION_ID));
            UserBean user = userLogic.getUserFromContext();
            // UserBean user = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            boolean hasRight = questionLogic.hasEditRights(id, user);
            if(hasRight){
                QuestionBean questionBean = questionLogic.getQuestionFull(id, user);
                request.setAttribute(RequestParamAttr.QUESTION, questionBean);
                router.setPage(PathEnum.EDIT_QUESTION);
            }
            else{
                if(userLogic.hasGeneralQuestionEditRights(user)){
                    QuestionBean questionBean = questionLogic.getQuestionFull(id, user);
                    request.setAttribute(RequestParamAttr.QUESTION, questionBean);
                    router.setPage(PathEnum.ADMIN_EDIT_QUESTION);
                }
                else {
                    router.setPage(PathEnum.ERROR_500);
                    router.changeAction();
                }
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
