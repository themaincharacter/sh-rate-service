package com.spot.hero.example;


import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.spot.hero.example.util.HealthCheckServletContextListener;
import com.spot.hero.example.util.MetricsContextListener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ApplicationListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY,HealthCheckServletContextListener.HEALTH_CHECK_REGISTRY);
        servletContextEvent.getServletContext().setAttribute(MetricsServlet.METRICS_REGISTRY, MetricsContextListener.REGISTRY);
        new InMemoryCacheInstance();

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}