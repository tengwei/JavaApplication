package com.tw;

import org.springframework.web.WebApplicationInitializer;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


/**
 * Created by Administrator on 2017/2/15.
 */
public class AppInitializer implements WebApplicationInitializer  {
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext webApplicationContext=new AnnotationConfigWebApplicationContext();
        webApplicationContext.register(AppConfig.class);

        DispatcherServlet dispatcherServlet=new DispatcherServlet( webApplicationContext);
        ServletRegistration.Dynamic dynamic=servletContext.addServlet("dispatcherServlet",dispatcherServlet);
        dynamic.addMapping("/");
    }
}
