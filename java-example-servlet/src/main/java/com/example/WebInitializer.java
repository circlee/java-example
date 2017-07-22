package com.example;

import javax.servlet.Filter;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.servlet.http.HttpServlet;
import java.util.Set;
import java.util.logging.Logger;

@HandlesTypes({HttpServlet.class, Filter.class})
public class WebInitializer implements ServletContainerInitializer {

    private static Logger logger = Logger.getGlobal();

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        logger.info(String.format("[%s]", this));
    }

}
