package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.logic.UserLogic;

import java.io.File;

/**
 * Deletes avatar of current user
 * Required user status: any, role: user
 * @see CommandEnum
 */
public class DeleteAvatarCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required session attributes: user
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        // UserBean user = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
        try(UserLogic userLogic = new UserLogic()){
            UserBean user = userLogic.getUserFromContext();
            if(userLogic.isUserAvailable(user)){
                userLogic.deleteAvatar(user, request.getServletContext().getRealPath(File.separator));
                router.setPage(PathEnum.INTERMEDIATE);
                request.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_OWNER_USER_PAGE.name());
            }
            else{
                router.setPage(PathEnum.ERROR_403);
                router.changeAction();
            }
        } catch (ProjectException e) {
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
