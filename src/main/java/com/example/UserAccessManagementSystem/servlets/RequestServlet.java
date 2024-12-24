package com.example.UserAccessManagementSystem.servlets;

import com.example.UserAccessManagementSystem.models.Request;
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

@WebServlet("/approveRequest")
public class RequestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userRole = (String) request.getSession().getAttribute("role");

        if (userRole == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to perform this action.");
            return;
        }

        Request requests = new Request((int) request.getSession().getAttribute("userId"), Integer.parseInt(request.getParameter("softwareId")), request.getParameter("accessType"), request.getParameter("reason"), "Pending");

        String accessType = request.getParameter("accessType");
        if (accessType == null || (!"READ".equals(accessType) && !"WRITE".equals(accessType))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid access type.");
            return;
        }

        String reason = request.getParameter("reason");
        if (reason == null || reason.trim().isEmpty() || reason.length() > 255) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Reason must not be empty and cannot exceed 255 characters.");
            return;
        }

        try(Connection connection = DatabaseConnection.getConnection()){
            if (connection == null) {
                throw new RuntimeException("Failed to obtain a database connection.");
            }

            String sql = "INSERT INTO requests (user_id, software_id, access_type, reason, status) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, requests.getUserId());
                preparedStatement.setInt(2, requests.getSoftwareId());
                preparedStatement.setString(3, requests.getAccessType());
                preparedStatement.setString(4, requests.getReason());
                preparedStatement.setString(5, requests.getStatus());
                preparedStatement.executeUpdate();
            }
            response.sendRedirect(PagePaths.REQUEST_SUCCESS);
        } catch (SQLException e) {
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage","Unable to add request. Something went wrong. Please try again later.");
            response.sendRedirect(PagePaths.ERROR_PAGE);
            throw new RuntimeException(e);
        }
    }
}
