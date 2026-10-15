package com.recipehub.util;

import org.junit.Test;
import java.sql.Connection;
import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void testDatabaseConnectionAttempt() {
        // If MySQL credentials are available in environment/system property, test real connection
        String user = System.getenv("RECIPEHUB_DB_USER");
        if (user == null) {
            user = System.getProperty("RECIPEHUB_DB_USER");
        }

        if (user != null && !user.isBlank()) {
            boolean connected = Database.testConnection();
            assertTrue("Database should connect when credentials are present", connected);
            try (Connection conn = Database.getConnection()) {
                assertNotNull(conn);
                assertFalse(conn.isClosed());
            } catch (Exception e) {
                fail("Connection should succeed: " + e.getMessage());
            }
        } else {
            // When credentials are null, Database.testConnection() gracefully returns false without crashing
            assertFalse(Database.testConnection());
        }
    }
}
