package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.resource.Configuration;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.logic.UserLogic;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * Show specified page of all users
 * Required user status: any, role: admin
 * @see CommandEnum
 */
@Log4j2
public class ShowUsersCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request params: page
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        try(UserLogic userLogic = new UserLogic()){
            long page = Long.parseLong(request.getParameter(RequestParamAttr.PAGE));
            long noOfRecords = userLogic.getCount();
            long noOfPages = (int) Math.ceil(noOfRecords * 1.0 / Configuration.USERS_PER_PAGE);
            List<UserBean> list;
            if(noOfPages > 1){
                list  = userLogic.getUsers((page-1)*Configuration.USERS_PER_PAGE, Configuration.USERS_PER_PAGE);
            }
            else{
                list  = userLogic.getUsers(0, Configuration.USERS_PER_PAGE);
                page = 1;
            }
            request.setAttribute(RequestParamAttr.USERS, list);
            request.setAttribute(RequestParamAttr.PAGES, noOfPages);
            request.setAttribute(RequestParamAttr.PAGE, page);
            router.setPage(PathEnum.USERS);
        } catch (ProjectException e) {
            log.error(e);
            router.setPage(PathEnum.ERROR_500);
            router.changeAction();
        }
        return router;
    }
}
