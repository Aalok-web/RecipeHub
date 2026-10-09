
package com.recipehub.servlet;

import com.recipehub.dao.UserDAO;
import com.recipehub.dao.UserDAOImpl;
import com.recipehub.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles password changes for authenticated RecipeHub users.
 *
 * This servlet verifies the current password, validates the new
 * password and its confirmation, and updates the database.
 */
@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    // DAO object used to verify and update user passwords.
    private final UserDAO userDAO = new UserDAOImpl();

    /**
     * Processes the password-change form submitted from profile.jsp.
     *
     * @param request contains the submitted password fields and session.
     * @param response sends redirects back to the profile page.
     * @throws ServletException if a servlet-related error occurs.
     * @throws IOException if a redirect fails.
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve the existing session without creating a new one.
        HttpSession session = request.getSession(false);

        // Only logged-in users are allowed to change their passwords.
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Retrieve the currently logged-in user from the session.
        User user = (User) session.getAttribute("user");

        // Read the password fields submitted by the profile form.
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Reject the request if any required field is missing or blank.
        if (oldPassword == null || newPassword == null
                || confirmPassword == null
                || oldPassword.isBlank()
                || newPassword.isBlank()
                || confirmPassword.isBlank()) {

            response.sendRedirect("profile?password=invalid");
            return;
        }

        // Require the new password to contain at least eight characters.
        if (newPassword.length() < 8) {
            response.sendRedirect("profile?password=invalid");
            return;
        }

        // Ensure that the new password and confirmation are identical.
        if (!newPassword.equals(confirmPassword)) {
            response.sendRedirect("profile?password=mismatch");
            return;
        }

        // Prevent the user from choosing the same password again.
        if (oldPassword.equals(newPassword)) {
            response.sendRedirect("profile?password=invalid");
            return;
        }

        // Verify the old password and update it through the DAO.
        boolean updated = userDAO.changePassword(
                user.getId(),
                oldPassword,
                newPassword
        );

        // Redirect with a status that profile.jsp can display.
        if (updated) {

            // Update the password in the session's User object as well.
            user.setPassword(newPassword);
            session.setAttribute("user", user);

            response.sendRedirect("profile?password=success");

        } else {

            // A failed update usually means the old password was incorrect
            // or the database operation could not be completed.
            response.sendRedirect("profile?password=incorrect");
        }
    }
}