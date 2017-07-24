package com.example;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private static Logger logger = Logger.getGlobal();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        String content = "{\"id\":\"1\",\"username\":\"conanli\",\"password\":\"123456\",\"age\":27,\"birth\":1500734600491}";
        out.write(content);
        out.close();
    }

}
