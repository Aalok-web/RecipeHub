
package com.recipehub.dao;

import com.recipehub.model.User;

public interface UserDAO {

    // Register a new user.
    boolean register(User user);

    // Login an existing user.
    User login(String email, String password);

    // Verify the old password and update it with the new password.
    boolean changePassword(int userId, String oldPassword, String newPassword);
}