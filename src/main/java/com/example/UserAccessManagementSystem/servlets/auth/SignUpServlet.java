package com.example.UserAccessManagementSystem.servlets.auth;

import com.example.UserAccessManagementSystem.models.User;
import com.example.UserAccessManagementSystem.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/signup")
public class SignUpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = UserRoles.EMPLOYEE; // Default role assignment

        // Input Validation
        if (username == null || !username.matches("[A-Za-z0-9_]{3,20}")) {
            throw new ServletException("Invalid username. Must be 3–20 alphanumeric characters with underscores allowed.");
        }
        if (password == null || password.length() < 8) {
            throw new ServletException("Password must be at least 8 characters long.");
        }

        // Hashing the Password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, hashedPassword);
                ps.setString(3, role); // Safely assign default role
                ps.executeUpdate();
            }

            // Reset session upon successful signup
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            request.getSession(true).setAttribute("username", username);
            response.sendRedirect(PagePaths.LOGIN); // Redirect to login
        } catch (SQLException e) {
            System.err.println("Database exception occurred: " + e.getMessage());
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Unable to sign up. Please try again later.");
            response.sendRedirect(PagePaths.ERROR_PAGE);
        }
    }
}
