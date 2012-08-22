package io.insideout.wordlift.web.api.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorld extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                                                                          IOException {
        resp.getWriter().write("Hello World");
    }

    /**
     * 
     */
    private static final long serialVersionUID = -5675155969088314241L;

}
