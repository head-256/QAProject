package com.dubhad.qaproject.servlet;

import com.dubhad.qaproject.command.AjaxCommand;
import com.dubhad.qaproject.command.AjaxCommandEnum;
import com.dubhad.qaproject.command.AjaxFactory;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet for all ajax requests. Defines required command, executes it and writes it's result as json object to
 * response object
 * @see AjaxFactory
 * @see AjaxCommandEnum
 */
@Log4j2
@WebServlet(urlPatterns = "/async")
public class AjaxController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AjaxFactory client = new AjaxFactory();
        RequestWrapper wrapper = new RequestWrapper(request);
        log.debug(request.getParameter("command"));
        AjaxCommand command = client.defineCommand(wrapper);
        Map<String, String> data = command.execute(wrapper);
        log.debug(data);
        response.setContentType("application/json");
        String json = new Gson().toJson(data);
        response.getWriter().print(json);
        response.getWriter().flush();
    }
}
