package com.example;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.logging.Logger;

@WebFilter("/*")
public class GlobalFilter implements Filter {

    private static Logger logger = Logger.getGlobal();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info(String.format("[%s]", this));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info(String.format("[%s]", this));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info(String.format("[%s]", this));
    }

}
