package com.example;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    private static Logger logger = Logger.getGlobal();

    @Override
    public void init() throws ServletException {
        logger.info(String.format("[%s]", this));
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info(String.format("[%s]", this));
        PrintWriter out = response.getWriter();
        out.write("<h1>Hello World</h1>");
        out.close();
    }

    @Override
    public void destroy() {
        logger.info(String.format("[%s]", this));
    }

}
