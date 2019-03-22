package com.dubhad.qaproject.filter;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import javax.servlet.Filter;import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * Filter, that sets encoding of the request. Is required for appropriate work with unicode characters
 */
@Log4j2
@WebFilter(filterName="encodingFilter",
        urlPatterns = {"/*" },
        initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8", description = "Encoding Param") })
public class EncodingFilter implements Filter {
    private String code;
    public void init(FilterConfig fConfig) throws ServletException {
        code = fConfig.getInitParameter("encoding");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String requestCharacterEncoding = request.getCharacterEncoding();
        if (code != null && !code.equalsIgnoreCase(requestCharacterEncoding)) {
            request.setCharacterEncoding(code);
        }
        String responseCharacterEncoding = response.getCharacterEncoding();
        if (code != null && !code.equalsIgnoreCase(responseCharacterEncoding)) {
            response.setCharacterEncoding(code);
        }
        log.debug("Encoding - " + request.getCharacterEncoding() + ", " + response.getCharacterEncoding());
        chain.doFilter(request, response);
    }

    public void destroy() {
        code = null;
    }
}