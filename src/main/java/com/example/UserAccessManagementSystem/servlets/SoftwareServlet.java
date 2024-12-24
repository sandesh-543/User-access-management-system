package com.example.UserAccessManagementSystem.servlets;

import com.example.UserAccessManagementSystem.models.Software;
import com.example.UserAccessManagementSystem.servlets.auth.PagePaths;
import com.example.UserAccessManagementSystem.servlets.auth.UserRoles;
import com.example.UserAccessManagementSystem.utils.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/createSoftware")
public class SoftwareServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("role");

        if (userRole == null || !userRole.equals(UserRoles.ADMIN)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to create software.");
            return;
        }

        String[] accessLevels = request.getParameterValues("accessLevel");
        if (accessLevels == null || accessLevels.length == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Access levels must be specified.");
            return;
        }

        String softwareName = request.getParameter("softwareName");
        String description = request.getParameter("description");

        if (softwareName == null || softwareName.isEmpty() || softwareName.length() > 255) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid software name.");
            return;
        }

        if (description != null && description.length() > 1000) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Description must not exceed 1000 characters.");
            return;
        }

        Software software = new Software(softwareName, description, getHighestPriorityAccess(accessLevels));

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection == null) {
                throw new RuntimeException("Failed to obtain a database connection.");
            }

            String sql = "INSERT INTO software (name, description, access_levels) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, software.getName());
                preparedStatement.setString(2, software.getDescription());
                preparedStatement.setString(3, software.getAccessLevels());
                preparedStatement.executeUpdate();
            }

            response.sendRedirect(PagePaths.REQUEST_SUCCESS);
        } catch (SQLException e) {
            log("Database operation failed: " + e.getMessage(), e);
            session.setAttribute("errorMessage", "Unable to create software. Something went wrong. Please try again later.");
            response.sendRedirect(PagePaths.ERROR_PAGE);
        }
    }
    private String getHighestPriorityAccess(String[] accessLevels) {
        String highestPriority = "Read";
        for (String accessLevel : accessLevels) {
            if(accessLevel.equals("Admin")){
                return accessLevel;
            } else if (accessLevel.equals("Write")) {
                highestPriority = "Write";
            }
        }
        return highestPriority;
    }
}
