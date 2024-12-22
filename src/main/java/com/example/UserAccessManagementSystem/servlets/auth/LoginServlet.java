package com.example.UserAccessManagementSystem.servlets.auth;

import com.example.UserAccessManagementSystem.models.User;
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

@WebServlet("/login")
public class LoginServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        User user = new User(request.getParameter("username"), request.getParameter("password"));
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new ServletException("Username is required and cannot be blank.");
        }
        if (!user.getUsername().matches("[A-Za-z0-9_]{3,20}")) {
            throw new ServletException("Username must be alphanumeric with underscores and 3-20 characters long.");
        }
        try(Connection connection = DatabaseConnection.getConnection()){
            String sql = "SELECT id, role FROM users where username = ? AND password = ?";
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    user.setRole(rs.getString("role"));
                    int userId = rs.getInt("id");
                    HttpSession oldSession = request.getSession(false);
                    if (oldSession != null) {
                        oldSession.invalidate();
                    }
                    HttpSession newSession = request.getSession(true);
                    newSession.setAttribute("username", user.getUsername());
                    newSession.setAttribute("role", user.getRole());
                    newSession.setAttribute("id", userId);

                    switch(user.getRole()) {
                        case UserRoles.EMPLOYEE:
                            response.sendRedirect(PagePaths.REQUEST_ACCESS);
                            break;
                        case UserRoles.MANAGER:
                            response.sendRedirect(PagePaths.PENDING_REQUESTS);
                            break;
                        case UserRoles.ADMIN:
                            response.sendRedirect(PagePaths.CREATE_SOFTWARE);
                            break;
                        default:
                            response.sendRedirect(PagePaths.ERROR_PAGE);                    }
                }
                else{
                    HttpSession session = request.getSession();
                    session.setAttribute("errorMessage", "Invalid username or password.");
                    response.sendRedirect(PagePaths.ERROR_PAGE);                }
            }
        }
        catch (SQLException e) {
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage","Encountered an error while logging in. Please try again later.");
            response.sendRedirect("errorPage.jsp");
            throw new ServletException(e);
        }
    }
}