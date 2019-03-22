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

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Uploads avatar to server
 * Required user status: available, role: any
 * @see CommandEnum
 */
@Log4j2
public class UploadAvatarCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: basePath (real path of app)
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router page = new Router();
        try(UserLogic userLogic = new UserLogic();
            QuestionLogic questionLogic = new QuestionLogic()){
            UserBean user = userLogic.getUserFromContext();
            // UserBean user = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            String basePath = request.getServletContext().getRealPath(File.separator);
            String extension = userLogic.uploadAvatar(user.getId(), basePath, request.getParts());
            user.setAvatarPath(extension);
            UserBean userBean = userLogic.getUser(user.getId());
            List<QuestionBean> questions = questionLogic.getUserQuestions(user.getId(), user);
            request.setAttribute(RequestParamAttr.USERPAGE_USER, userBean);
            request.setAttribute(RequestParamAttr.QUESTIONS, questions);
            page.setPage(PathEnum.OWNER_USERPAGE);
        } catch (ProjectException | ServletException | IOException e) {
            log.error(e);
            page.setPage(PathEnum.ERROR_500);
            page.changeAction();
        }
        return page;
    }
}
