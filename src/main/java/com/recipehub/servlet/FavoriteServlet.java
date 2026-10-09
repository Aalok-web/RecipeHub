package com.recipehub.servlet;

import com.recipehub.dao.FavoriteDAO;
import com.recipehub.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/favorite")
public class FavoriteServlet extends HttpServlet {

    private final FavoriteDAO favoriteDAO = new FavoriteDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the recipe ID submitted from the Favorite button.
        int recipeId = Integer.parseInt(request.getParameter("recipeId"));

        // Get the currently logged-in user from the session.
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Redirect the user to login if no active session exists.
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        // Save the selected recipe as a favorite for the logged-in user.
        boolean success = favoriteDAO.addFavorite(user.getId(), recipeId);

        if (success) {
            // Open the Favorites page after successful insertion.
            response.sendRedirect("favorite");
        } else {
            response.getWriter().println("Failed to add recipe to favorites.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        request.setAttribute("favorites", favoriteDAO.getFavorites(user.getId()));

        request.getRequestDispatcher("favorites.jsp").forward(request, response);
    }
}