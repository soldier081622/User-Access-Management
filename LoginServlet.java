package com.accessmanagement.servlet;

import com.accessmanagement.model.User;
import com.accessmanagement.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final String SELECT_USER = "SELECT * FROM users WHERE username = ? AND password = ?";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USER)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In a real application, this should be hashed
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setRole(rs.getString("role"));
                    
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    
                    // Redirect based on role
                    switch (user.getRole()) {
                        case "Admin":
                            response.sendRedirect(request.getContextPath() + "/admin/software");
                            break;
                        case "Manager":
                            response.sendRedirect(request.getContextPath() + "/manager/requests");
                            break;
                        case "Employee":
                            response.sendRedirect(request.getContextPath() + "/employee/request-access");
                            break;
                        default:
                            response.sendRedirect(request.getContextPath() + "/login");
                    }
                } else {
                    request.setAttribute("error", "Invalid username or password");
                    request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
                }
            }
        } catch (SQLException e) {
            request.setAttribute("error", "An error occurred during login");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
} 