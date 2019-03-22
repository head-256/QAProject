package com.dubhad.qaproject.filter;

import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.resource.PathEnum;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter, that checks, whether specified command exists in enum and sends redirect for error page if command does not
 * exist
 */
@Log4j2
@WebFilter(
        filterName = "servletCommandFilter",
        urlPatterns = "/controller")
public class ServletCommandFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String command = request.getParameter(RequestParamAttr.COMMAND);
        log.debug("ServletCommandFilter on " + command);
        boolean isRequestCommitted = true;
        if(command != null) {
            try {
                CommandEnum.valueOf(command.toUpperCase());
                isRequestCommitted = false;
            } catch (IllegalArgumentException e) {
                log.error("Unable to find command", e);
                String page = PathEnum.ERROR_404.getPath();
                ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + page);
            }
        }
        else{
            String page = PathEnum.ERROR_404.getPath();
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + page);
        }
        if(!isRequestCommitted) {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
