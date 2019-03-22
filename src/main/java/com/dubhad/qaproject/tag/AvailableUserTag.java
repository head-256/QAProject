package com.dubhad.qaproject.tag;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.entity.UserStatus;
import com.dubhad.qaproject.logic.UserLogic;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Custom tag, displays its content only if current user has status Available
 */
@Log4j2
public class AvailableUserTag extends TagSupport {
    @Override
    public int doStartTag(){
        log.error("available_user_tag");
        boolean evaluateBody = false;
        Object userObject = pageContext.getSession().getAttribute(RequestParamAttr.USER);
        if(userObject != null && userObject instanceof UserBean && ((UserBean)userObject).getStatus() == UserStatus.AVAILABLE){
            evaluateBody = true;
        }
        if(evaluateBody){
            return EVAL_BODY_INCLUDE;
        }
        else {
            return SKIP_BODY;
        }
    }
}
