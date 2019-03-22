package com.dubhad.qaproject.tag;

import com.dubhad.qaproject.bean.UserBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Custom tag, that displays either avatar of passed user, if it exists, default avatar otherwise
 */
public class AvatarTag extends TagSupport{
    /**
     * User to fetch avatar
     */
    private UserBean user;

    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public int doStartTag() throws JspException {
        boolean userAvatar = true;
        String contextPath = ((HttpServletRequest)pageContext.getRequest()).getContextPath();
        if(user == null){
            userAvatar = false;
        }
        else {
            if(user.getAvatarPath() == null){
                userAvatar = false;
            }
        }
        JspWriter out = pageContext.getOut();
        if(userAvatar){
            try {
                out.write("<img class=\"author-img rounded-circle\" src=\"" + contextPath +
                        "/image?image=avatars/" + user.getUsername() + user.getAvatarPath() + "\">");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }else {
            try {
                out.write("<img class=\"author-img rounded-circle\" src=\"" + contextPath + "/image\">");
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
        return EVAL_BODY_INCLUDE;
    }
}
