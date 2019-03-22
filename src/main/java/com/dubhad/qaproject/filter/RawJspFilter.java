package com.dubhad.qaproject.filter;

import com.dubhad.qaproject.resource.Configuration;
import com.dubhad.qaproject.resource.PathEnum;
import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebFilter(filterName="rawJspFilter",
        urlPatterns = {
                "/avatars/*",
                "/pages/*" })
public class RawJspFilter implements Filter {
    public void init(FilterConfig fConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if(!httpRequest.getRequestURI().startsWith(Configuration.APP_NAME + Configuration.ERROR_PAGES)) {
            log.debug(httpRequest.getRequestURI());
            httpResponse.sendRedirect(httpRequest.getContextPath() + PathEnum.ERROR_403.getPath());
        }
        else{
            chain.doFilter(request, response);
        }
    }

    public void destroy() {

    }
}