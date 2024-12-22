package com.example.UserAccessManagementSystem.servlets.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class SignOutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Preventing session creation if it doesn't exist
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // Invalidate the session
            }
            // Redirecting to login page
            response.sendRedirect(PagePaths.INDEX);
        } catch (Exception e) {
            throw new ServletException("Logout failed", e);
        }
    }
}