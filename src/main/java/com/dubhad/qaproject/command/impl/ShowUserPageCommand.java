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

import java.util.List;

/**
 * Shows user page
 * Required user status: any, role: any
 * @see CommandEnum
 */
@Log4j2
public class ShowUserPageCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: userId
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        try(UserLogic userLogic = new UserLogic();
            QuestionLogic questionLogic = new QuestionLogic()){
            long id = Long.parseLong(request.getParameter(RequestParamAttr.USER_ID));
            UserBean currentUser = userLogic.getUserFromContext();
            // UserBean currentUser = (UserBean)request.getSession().getAttribute(RequestParamAttr.USER);
            UserBean userBean = userLogic.getUser(id);
            List<QuestionBean> questions = questionLogic.getUserQuestions(id, currentUser);
            request.setAttribute(RequestParamAttr.USERPAGE_USER, userBean);
            request.setAttribute(RequestParamAttr.QUESTIONS, questions);
            router.setPage(PathEnum.USERPAGE);
        }
        catch (ProjectException | NumberFormatException e){
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
        }
        return router;
    }
}
