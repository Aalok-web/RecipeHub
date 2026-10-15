package com.recipehub.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JDBC Connection Factory for RecipeHub.
 *
 * Provides connection management with:
 * 1. Optional JNDI DataSource lookup (java:comp/env/jdbc/RecipeHubDB) for connection pooling in container environments.
 * 2. Automatic fallback to direct DriverManager connections using environment variables or system properties.
 * 3. Connection health testing and descriptive configuration diagnostics.
 */
public final class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private static final String JNDI_NAME = "java:comp/env/jdbc/RecipeHubDB";
    private static volatile DataSource jndiDataSource = null;
    private static volatile boolean jndiChecked = false;

    private Database() {}

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "MySQL JDBC Driver not found in classpath. Relying on ServiceLoader SPI.", e);
        }
    }

    /**
     * Obtains a live JDBC Connection.
     * Callers must close the Connection using try-with-resources.
     *
     * @return a valid Connection instance
     * @throws SQLException if database credentials are missing or the connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        // 1. Attempt JNDI DataSource if configured in Tomcat context.xml
        if (!jndiChecked) {
            synchronized (Database.class) {
                if (!jndiChecked) {
                    try {
                        Context ctx = new InitialContext();
                        jndiDataSource = (DataSource) ctx.lookup(JNDI_NAME);
                        LOGGER.info("Successfully bound to container JNDI DataSource: " + JNDI_NAME);
                    } catch (NamingException ignored) {
                        // JNDI not configured; fall back to DriverManager
                    } finally {
                        jndiChecked = true;
                    }
                }
            }
        }

        if (jndiDataSource != null) {
            try {
                return jndiDataSource.getConnection();
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "JNDI DataSource failed; falling back to direct DriverManager connection.", ex);
            }
        }

        // 2. Fallback to direct JDBC URL and credentials
        String url = System.getenv("RECIPEHUB_DB_URL");
        if (url == null || url.isBlank()) {
            url = System.getProperty("RECIPEHUB_DB_URL", "jdbc:mysql://localhost:3306/recipehub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8");
        }

        String user = System.getenv("RECIPEHUB_DB_USER");
        if (user == null || user.isBlank()) {
            user = System.getProperty("RECIPEHUB_DB_USER");
        }

        String password = System.getenv("RECIPEHUB_DB_PASSWORD");
        if (password == null) {
            password = System.getProperty("RECIPEHUB_DB_PASSWORD");
        }

        if (user == null || user.isBlank() || password == null) {
            throw new SQLException(
                    "Database credentials are not configured. " +
                    "Set RECIPEHUB_DB_USER and RECIPEHUB_DB_PASSWORD via environment variables or JVM system properties (-DRECIPEHUB_DB_USER=... -DRECIPEHUB_DB_PASSWORD=...)."
            );
        }

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Diagnostic helper to verify whether the database is accessible and responsive.
     *
     * @return true if a connection can be opened and a lightweight query succeeds; false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT 1")) {
            return stmt.execute();
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Database connection check failed: " + e.getMessage());
            return false;
        }
    }
}
