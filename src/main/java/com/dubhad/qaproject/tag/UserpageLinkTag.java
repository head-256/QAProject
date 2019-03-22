package com.dubhad.qaproject.tag;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.logic.UserLogic;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Custom tag, displays link to OwnerUserpage, if passed user is the same, as exists in session, or to Userpage
 * otherwise
 */
@Log4j2
public class UserpageLinkTag extends TagSupport {
    /**
     * User to compare with one, that exists in session
     */
    private UserBean user;

    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public int doStartTag() throws JspException {
        log.error("userpage_link_tag");
        boolean ownerUserpage = true;
        String contextPath = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
        Object userObject = pageContext.getSession().getAttribute(RequestParamAttr.USER);
        if(userObject == null){
            ownerUserpage = false;
        }
        if(userObject instanceof UserBean){
            UserBean userBean = (UserBean) userObject;
            if(userBean.getId() != user.getId()){
                ownerUserpage = false;
            }
        }
        else {
            ownerUserpage = false;
        }
        JspWriter out = pageContext.getOut();
        if(ownerUserpage){
            try {
                out.write("<a class=\"question-author-username\" href=\"" + contextPath +
                        "/controller?command=show_owner_user_page&user_id=" + user.getId() + "\">");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }else {
            try {
                out.write("<a class=\"question-author-username\" href=\"" + contextPath +
                        "/controller?command=show_user_page&user_id=" + user.getId() + "\">");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.write("</a>");
        } catch (IOException e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }
}
