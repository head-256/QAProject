package com.dubhad.qaproject.filter;

import com.dubhad.qaproject.entity.UserRole;
import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.entity.UserStatus;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.PathEnum;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter, that checks, whether current user in session has privileges to perform desired action.
 * Checks role and status in db.
 * @see CommandEnum
 */
@Log4j2
@WebFilter(
        filterName="permissionFilter",
        urlPatterns = "/controller")
public class ServletPermissionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String commandName = httpRequest.getParameter(RequestParamAttr.COMMAND);
        CommandEnum command = CommandEnum.valueOf(commandName.toUpperCase());
        UserRole minRole = command.getMinRole();
        UserStatus status = command.getStatus();
        boolean responseCommitted = false;
        if(minRole != null){
            Object userObject = httpRequest.getSession().getAttribute(RequestParamAttr.USER);
            if(userObject == null){
                RequestDispatcher dispatcher = httpRequest.getServletContext().getRequestDispatcher(PathEnum.LOGIN.getPath());
                responseCommitted = true;
                dispatcher.forward(httpRequest, httpResponse);
            }
            else if(userObject instanceof UserBean){
                try (UserLogic userLogic = new UserLogic()) {
                    UserBean user = (UserBean) userObject;
                    if(userLogic.checkUserRole(user, minRole)) {
                        if(status != null && !userLogic.checkUserStatus(user, status)) {
                            log.trace("Status does not match");
                            responseCommitted = true;
                            httpRequest.setAttribute(RequestParamAttr.COMMAND, CommandEnum.SHOW_OWNER_USER_PAGE.name());
                            RequestDispatcher dispatcher = httpRequest.getServletContext().
                                    getRequestDispatcher(PathEnum.INTERMEDIATE.getPath());
                            dispatcher.forward(httpRequest, httpResponse);
                        }
                    }
                    else{
                        log.trace("Too low privileges level");
                        responseCommitted = true;
                        String page = PathEnum.ERROR_403.getPath();
                        httpResponse.sendRedirect(httpRequest.getContextPath() + page);
                    }
                } catch (ProjectException e) {
                    log.error(e);
                    responseCommitted = true;
                    String page = PathEnum.ERROR_500.getPath();
                    httpResponse.sendRedirect(httpRequest.getContextPath() + page);
                }
            }
        }
        if(!responseCommitted) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}