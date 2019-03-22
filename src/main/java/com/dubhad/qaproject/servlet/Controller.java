package com.dubhad.qaproject.servlet;

import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.ActionFactory;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.pool.ConnectionPool;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.command.RequestParamAttr;
import com.dubhad.qaproject.command.Router;
import lombok.extern.log4j.Log4j2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Main controller of app, that takes almost all requests.
 */
@WebServlet("/controller")
@Log4j2
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class Controller extends HttpServlet {

    public static String SESSION_ID;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Method, called by both {@link Controller#doGet(HttpServletRequest, HttpServletResponse)}
     * and {@link Controller#doPost(HttpServletRequest, HttpServletResponse)} methods.
     * Defines a command by {@link ActionFactory}, executes it and
     * jumps on the next page, according to the result of command execution.
     * @see Router
     * @see CommandEnum
     * @param request http request, passed to doGet or doPost method
     * @param response http response, passed to doGet or doPost method
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.error("SESSION FROM processRequest: " + request.getSession().getId());
        SESSION_ID = request.getSession().getId();

        ActionFactory client = new ActionFactory();
        RequestWrapper wrapper = new RequestWrapper(request);
        ActionCommand command = client.defineCommand(wrapper);
        Router router = command.execute(wrapper);
        if (router != null) {
            if(router.isReturnBack()){
                try {
                    String referrer = request.getHeader(RequestParamAttr.REFERRER);
                    URI uri = new URI(referrer);
                    log.trace("Returning back. Referrer: " + referrer);
                    response.sendRedirect(uri.getRawSchemeSpecificPart());
                } catch (URISyntaxException e) {
                    log.error(e);
                    response.sendRedirect(request.getContextPath() + PathEnum.ERROR_500.getPath());
                }
            }
            else {
                switch (router.getAction()){
                    case FORWARD:
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(router.getPage().getPath());
                        dispatcher.forward(wrapper.getRequest(), response);
                        break;
                    case REDIRECT:
                        String page = router.getPage().getPath();
                        response.sendRedirect(request.getContextPath() + page);
                        break;
                    default:
                        response.sendRedirect(request.getContextPath() + PathEnum.ERROR_500.getPath());
                }
            }
        } else {
            String page = PathEnum.INDEX.getPath();
            response.sendRedirect(request.getContextPath() + page);
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().closePool();
    }
}
