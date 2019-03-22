package com.dubhad.qaproject.command.ajax;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.command.AjaxCommand;
import com.dubhad.qaproject.command.AjaxCommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.logic.UserLogic;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.bean.UserBean;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * Unbans specified user
 * Required user status: available, role: admin
 * @see AjaxCommandEnum
 */
@Log4j2
public class UnbanUserCommand implements AjaxCommand {
    /**
     * {@inheritDoc}
     * Required request params: userId
     * Required session attributes: user
     */
    @Override
    public Map<String, String> execute(RequestWrapper request) {
        Map<String, String> dataMap = new HashMap<>();
        try(UserLogic userLogic = new UserLogic()) {
            long userId = Long.parseLong(request.getParameter(RequestParamAttr.USER_ID));
            UserBean currentUser = userLogic.getUserFromContext();
            // UserBean currentUser = (UserBean) request.getSession().getAttribute(RequestParamAttr.USER);
            userLogic.unbanUser(currentUser, userId);
            dataMap.put(RequestParamAttr.SUCCESS, RequestParamAttr.SUCCESS_MARKER);
        } catch (ProjectException | NumberFormatException e) {
            log.error(e);
            dataMap.put(RequestParamAttr.SUCCESS, RequestParamAttr.FAILURE_MARKER);
        }
        return dataMap;
    }
}