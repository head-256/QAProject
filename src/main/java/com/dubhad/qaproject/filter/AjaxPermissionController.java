package com.dubhad.qaproject.filter;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.entity.UserRole;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.AjaxCommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.resource.Message;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Filter for ajax requests. Checks, whether current user has permissions to perform specified action.
 * If permission is not granted, special value is returned at json object
 * For permissions details
 * @see AjaxCommandEnum
 */
@Log4j2
@WebFilter(urlPatterns = {"/async"})
public class AjaxPermissionController implements Filter {
    private static final String CONTENT_TYPE_AJAX_JSON = "application/json";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String command = request.getParameter(RequestParamAttr.COMMAND);
        UserRole minRole = AjaxCommandEnum.valueOf(command.toUpperCase()).getMinRole();
        Locale locale = (Locale) Config.get(((HttpServletRequest)request).getSession(), Config.FMT_LOCALE);
        Map<String, String> dataMap = new HashMap<>();
        if(minRole != null){
            Object userObject = ((HttpServletRequest)request).getSession().getAttribute(RequestParamAttr.USER);
            if(userObject == null){
                dataMap.put(RequestParamAttr.ERROR_MESSAGE, Message.AJAX_LOGIN_REQUIRED.get(locale));
            }
            else{
                if(userObject instanceof UserBean){
                    UserBean user = (UserBean) userObject;
                    if(user.getPrivilegeLevel() < minRole.ordinal()){
                        dataMap.put(RequestParamAttr.ERROR_MESSAGE, Message.AJAX_LOW_PRIVILEGES.get(locale));
                    }
                }
            }
        }
        if(dataMap.isEmpty()) {
            chain.doFilter(request, response);
        }
        else{
            response.setContentType(CONTENT_TYPE_AJAX_JSON);
            String json = new Gson().toJson(dataMap);
            response.getWriter().print(json);
            response.getWriter().flush();
        }
    }

    @Override
    public void destroy() {

    }
}