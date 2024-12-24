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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/approveRequest")
public class ApprovalServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("role");

        // Check if the user role is null or invalid
        if (userRole == null) {
            response.sendRedirect(PagePaths.LOGIN);
            return;
        }

        if (!UserRoles.MANAGER.equals(userRole) && !UserRoles.ADMIN.equals(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to perform this action.");
            return;
        }

        try {
            String action = request.getParameter("action");
            if ((!"approve".equals(action) && !"reject".equals(action))) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter.");
                return;
            }
            String status = "approve".equals(action) ? "Approved" : "Rejected";

            int requestId;
            try {
                requestId = Integer.parseInt(request.getParameter("id"));
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request ID.");
                return;
            }

            // Create a Request instance with the provided data
            Request requestObj = new Request(requestId, status);

            // Interact with the database using the Request instance
            try (Connection conn = DatabaseConnection.getConnection()) {
                if (conn == null) {
                    throw new RuntimeException("Failed to obtain a database connection.");
                }
                // Check if the request ID exists
                String checkSql = "SELECT COUNT(*) FROM requests WHERE id = ?";
                try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                    checkPs.setInt(1, requestObj.getId());
                    try (ResultSet rs = checkPs.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID does not exist.");
                            return;
                        }
                    }
                }

                // Update the request's status
                String updateSql = "UPDATE requests SET status = ? WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setString(1, requestObj.getStatus());
                    ps.setInt(2, requestObj.getId());
                    ps.executeUpdate();
                    response.sendRedirect(PagePaths.PENDING_REQUESTS);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while processing the request. Please try again later.");
            response.reset();
            response.sendRedirect(PagePaths.ERROR_PAGE);
        }
    }
}
