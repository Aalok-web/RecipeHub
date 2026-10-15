package com.recipehub.listener;

import com.recipehub.util.Database;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Logger;

/**
 * Application Lifecycle Listener.
 * Executes on web application deployment and shutdown.
 * Performs database connectivity health check and initializes application-wide configuration.
 */
@WebListener
public class AppLifecycleListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppLifecycleListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("=================================================");
        LOGGER.info("   RecipeHub Web Application Initializing...     ");
        LOGGER.info("=================================================");

        // Test database connectivity
        boolean dbHealthy = Database.testConnection();
        if (dbHealthy) {
            LOGGER.info("✓ RecipeHub Database connectivity verified successfully.");
        } else {
            LOGGER.warning("⚠️ RecipeHub Database check failed. Please ensure MySQL is running and credentials are set.");
        }

        sce.getServletContext().setAttribute("appDbHealthy", dbHealthy);
        sce.getServletContext().setAttribute("appName", "RecipeHub");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("RecipeHub Web Application Shutting Down.");

        // Deregister JDBC drivers loaded by this webapp classloader to prevent Tomcat classloader leaks
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        java.util.Enumeration<java.sql.Driver> drivers = java.sql.DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            java.sql.Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                try {
                    java.sql.DriverManager.deregisterDriver(driver);
                    LOGGER.info("Deregistered JDBC driver: " + driver);
                } catch (java.sql.SQLException ex) {
                    LOGGER.warning("Could not deregister JDBC driver: " + driver);
                }
            }
        }

        // Cleanly stop MySQL abandoned connection cleanup thread
        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
            LOGGER.info("MySQL AbandonedConnectionCleanupThread stopped successfully.");
        } catch (Throwable ignored) {
            // Cleanup thread already terminated
        }
    }
}
