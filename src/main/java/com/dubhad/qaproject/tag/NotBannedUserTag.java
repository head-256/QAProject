package com.dubhad.qaproject.tag;

import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.entity.UserStatus;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.tagext.TagSupport;

@Log4j2
public class NotBannedUserTag extends TagSupport {
    @Override
    public int doStartTag(){
        boolean evaluateBody = false;
        Object userObject = pageContext.getSession().getAttribute(RequestParamAttr.USER);
        if(userObject != null && userObject instanceof UserBean && ((UserBean)userObject).getStatus() != UserStatus.BANNED){
            log.debug("User object status: " + ((UserBean) userObject).getStatus());
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
