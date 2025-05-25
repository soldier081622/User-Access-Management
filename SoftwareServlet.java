package com.accessmanagement.servlet;

import com.accessmanagement.model.Software;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/software/*")
public class SoftwareServlet extends HttpServlet {
    private static final String INSERT_SOFTWARE = "INSERT INTO software (name, description, access_levels) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_SOFTWARE = "SELECT * FROM software ORDER BY name";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SOFTWARE);
             ResultSet rs = pstmt.executeQuery()) {
            
            List<Software> softwareList = new ArrayList<>();
            while (rs.next()) {
                Software software = new Software();
                software.setId(rs.getInt("id"));
                software.setName(rs.getString("name"));
                software.setDescription(rs.getString("description"));
                software.setAccessLevels(rs.getString("access_levels"));
                softwareList.add(software);
            }
            
            request.setAttribute("softwareList", softwareList);
            request.getRequestDispatcher("/WEB-INF/views/admin/software.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "An error occurred while fetching software list");
            request.getRequestDispatcher("/WEB-INF/views/admin/software.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !user.isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String[] accessLevels = request.getParameterValues("accessLevels");
        
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Software name is required");
            request.getRequestDispatcher("/WEB-INF/views/admin/software.jsp").forward(request, response);
            return;
        }

        String accessLevelsStr = accessLevels != null ? String.join(",", accessLevels) : "";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SOFTWARE)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, accessLevelsStr);
            
            pstmt.executeUpdate();
            
            response.sendRedirect(request.getContextPath() + "/admin/software");
        } catch (SQLException e) {
            request.setAttribute("error", "An error occurred while creating software");
            request.getRequestDispatcher("/WEB-INF/views/admin/software.jsp").forward(request, response);
        }
    }
} 