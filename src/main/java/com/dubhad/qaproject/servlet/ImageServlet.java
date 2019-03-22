package com.dubhad.qaproject.servlet;

import com.dubhad.qaproject.resource.Configuration;
import com.dubhad.qaproject.command.RequestParamAttr;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Servlet for image download action. Writes each bit of an image while it doesn't equal -1
 */
@WebServlet("/image")
@Log4j2
public class ImageServlet extends HttpServlet {
    private static final String IMAGE_CONTENT_TYPE = "image/png";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter(RequestParamAttr.IMAGE);
        if(fileName == null || fileName.isEmpty()){
            fileName = Configuration.AVATARS_FOLDER + File.separator + Configuration.DEFAULT_AVATAR;
        }
        try (FileInputStream fis = new FileInputStream(new File(getServletContext().getRealPath(File.separator) + fileName));
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            response.setContentType(IMAGE_CONTENT_TYPE);
            BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
            for (int data; (data = bis.read()) > -1; ) {
                output.write(data);
            }
            output.flush();
        } catch (IOException e) {
            log.error(e);
        }
    }
}

