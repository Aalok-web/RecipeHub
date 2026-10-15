package com.recipehub.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doLogout(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doLogout(request, response);
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear remember-me cookie if present
        Cookie rememberCookie = new Cookie("recipehub_remembered_email", "");
        rememberCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        rememberCookie.setMaxAge(0);
        response.addCookie(rememberCookie);

        response.sendRedirect(request.getContextPath() + "/?loggedOut=true");
    }
}
