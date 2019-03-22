package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.bean.QuestionBean;
import com.dubhad.qaproject.logic.QuestionLogic;
import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Shows owner user page
 * Required user status: any, role: user
 * @see CommandEnum
 */
@Log4j2
public class ShowOwnerUserPageCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        try(UserLogic userLogic = new UserLogic();
            QuestionLogic questionLogic = new QuestionLogic()){
            UserBean user = userLogic.getUserFromContext();
            // UserBean user = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            UserBean userpageUser = userLogic.getUser(user.getId());
            List<QuestionBean> questions = questionLogic.getUserQuestions(user);
            request.setAttribute(RequestParamAttr.USERPAGE_USER, userpageUser);
            request.setAttribute(RequestParamAttr.QUESTIONS, questions);
            router.setPage(PathEnum.OWNER_USERPAGE);
        } catch (ProjectException e) {
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
