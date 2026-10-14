package com.recipehub.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connectivity for the RecipeHub application.
 *
 * <p>Security & Best Practices:
 * <ul>
 *   <li>Avoids hardcoded passwords in version control.</li>
 *   <li>Loads configuration hierarchically:
 *     <ol>
 *       <li>Classpath resource: <code>db.properties</code></li>
 *       <li>Environment variables (12-factor standard): <code>DB_URL</code>, <code>DB_USER</code>, <code>DB_PASSWORD</code></li>
 *       <li>JVM system properties: <code>-Ddb.password=...</code></li>
 *       <li>Fallback defaults for local development.</li>
 *     </ol>
 *   </li>
 * </ul>
 */
public class DBConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/recipe_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    private static String url = DEFAULT_URL;
    private static String user = DEFAULT_USER;
    private static String password = DEFAULT_PASSWORD;

    static {
        loadConfiguration();
    }

    /**
     * Initializes database credentials from configuration files or environment variables.
     */
    private static void loadConfiguration() {
        // 1. Try reading from db.properties if provided on the classpath
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);

                String propUrl = props.getProperty("db.url");
                if (propUrl != null && !propUrl.trim().isEmpty()) {
                    url = propUrl.trim();
                }

                String propUser = props.getProperty("db.user");
                if (propUser != null && !propUser.trim().isEmpty()) {
                    user = propUser.trim();
                }

                String propPassword = props.getProperty("db.password");
                if (propPassword != null) {
                    password = propPassword.trim();
                }
            }
        } catch (Exception e) {
            // Configuration file is optional; continue to environment/default fallbacks
        }

        // 2. Allow environment variables to override properties (standard for Docker / CI / Production)
        String envUrl = System.getenv("DB_URL");
        if (envUrl != null && !envUrl.trim().isEmpty()) {
            url = envUrl.trim();
        }

        String envUser = System.getenv("DB_USER");
        if (envUser != null && !envUser.trim().isEmpty()) {
            user = envUser.trim();
        }

        String envPassword = System.getenv("DB_PASSWORD");
        if (envPassword != null) {
            password = envPassword.trim();
        }

        // 3. Allow JVM System properties (-Ddb.password=...)
        String sysUrl = System.getProperty("db.url");
        if (sysUrl != null && !sysUrl.trim().isEmpty()) {
            url = sysUrl.trim();
        }

        String sysUser = System.getProperty("db.user");
        if (sysUser != null && !sysUser.trim().isEmpty()) {
            user = sysUser.trim();
        }

        String sysPassword = System.getProperty("db.password");
        if (sysPassword != null) {
            password = sysPassword.trim();
        }
    }

    /**
     * Retrieves an active database Connection.
     *
     * @return Connection to MySQL database
     * @throws SQLException if a database access error occurs or driver is missing
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found in classpath", e);
        }

        return DriverManager.getConnection(url, user, password);
    }
}
