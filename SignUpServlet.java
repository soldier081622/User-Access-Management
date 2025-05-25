package com.accessmanagement.servlet;

import com.accessmanagement.model.User;
import com.accessmanagement.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    private static final String INSERT_USER = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_USER)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In a real application, this should be hashed
            pstmt.setString(3, "Employee"); // Default role
            
            pstmt.executeUpdate();
            
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (SQLException e) {
            if (e.getMessage().contains("unique constraint")) {
                request.setAttribute("error", "Username already exists");
            } else {
                request.setAttribute("error", "An error occurred during registration");
            }
            request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(request, response);
    }
} 