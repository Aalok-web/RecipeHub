
package com.recipehub.dao;

import com.recipehub.model.User;
import com.recipehub.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * UserDAOImpl implements the UserDAO interface.
 * It handles all database operations related to users,
 * including registration, login, and password changes.
 */
public class UserDAOImpl implements UserDAO {

    /**
     * Registers a new user in the database.
     *
     * @param user User object containing name, email,
     *             password, and role.
     * @return true if registration is successful, otherwise false.
     */
    @Override
    public boolean register(User user) {

        // SQL query to insert a new user into the users table.
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        // Establish a database connection and prepare the SQL statement.
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Set user details in the query parameters.
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            // Execute the INSERT query and check whether a row was added.
            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            // Print the error details if registration fails.
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Authenticates a user using their email and password.
     *
     * @param email User's registered email address.
     * @param password User's password.
     * @return User object if credentials are valid, otherwise null.
     */
    @Override
    public User login(String email, String password) {

        // Find a user whose email and password match the supplied values.
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Safely bind the login credentials to the SQL query.
            ps.setString(1, email);
            ps.setString(2, password);

            // Execute the SELECT query and read the matching user.
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    // Create a User object using the database record.
                    User user = new User();

                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));

                    // Return the authenticated user's details.
                    return user;
                }
            }

        } catch (Exception e) {

            // Log database or authentication-related errors.
            e.printStackTrace();
        }

        // Return null when no matching user is found or an error occurs.
        return null;
    }

    /**
     * Changes a user's password after verifying the old password.
     *
     * @param userId ID of the currently logged-in user.
     * @param oldPassword User's current password.
     * @param newPassword New password to be saved.
     * @return true if the password is updated successfully,
     *         otherwise false.
     */
    @Override
    public boolean changePassword(int userId,
                                  String oldPassword,
                                  String newPassword) {

        // Query to verify the user's ID and current password.
        String verifySql =
                "SELECT id FROM users WHERE id = ? AND password = ?";

        // Query to update the password for the verified user.
        String updateSql =
                "UPDATE users SET password = ? WHERE id = ?";

        // Open a database connection for verification and updating.
        try (Connection con = DBConnection.getConnection()) {

            // Step 1: Verify that the old password is correct.
            try (PreparedStatement verifyPs =
                         con.prepareStatement(verifySql)) {

                verifyPs.setInt(1, userId);
                verifyPs.setString(2, oldPassword);

                // If no record matches, the old password is incorrect.
                try (ResultSet rs = verifyPs.executeQuery()) {
                    if (!rs.next()) {
                        return false;
                    }
                }
            }

            // Step 2: Update the password after successful verification.
            try (PreparedStatement updatePs =
                         con.prepareStatement(updateSql)) {

                updatePs.setString(1, newPassword);
                updatePs.setInt(2, userId);

                // Return true if the database updates a row successfully.
                return updatePs.executeUpdate() > 0;
            }

        } catch (Exception e) {

            // Print the error if verification or updating fails.
            e.printStackTrace();
            return false;
        }
    }
}