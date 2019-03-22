package com.dubhad.qaproject.tag;

import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.entity.UserRole;
import lombok.extern.log4j.Log4j2;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Custom tag, displays its content only if current user in session has admin rights (Admin or SuperAdmin)
 */
@Log4j2
public class AdminRightsTag extends TagSupport {
    @Override
    public int doStartTag(){
        boolean evaluateBody = true;
        Object userObject = pageContext.getSession().getAttribute(RequestParamAttr.USER);
        if(userObject == null){
            evaluateBody = false;
        }
        if(userObject instanceof UserBean){
            UserBean userBean = (UserBean) userObject;
            if(userBean.getPrivilegeLevel() < UserRole.ADMIN.ordinal()){
                evaluateBody = false;
            }
        }
        else {
            evaluateBody = false;
        }
        if(evaluateBody){
            return EVAL_BODY_INCLUDE;
        }
        else {
            return SKIP_BODY;
        }
    }
}
