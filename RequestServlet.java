package com.accessmanagement.servlet;

import com.accessmanagement.model.Request;
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

@WebServlet("/employee/request-access/*")
public class RequestServlet extends HttpServlet {
    private static final String INSERT_REQUEST = "INSERT INTO requests (user_id, software_id, access_type, reason, status) VALUES (?, ?, ?, ?, 'Pending')";
    private static final String SELECT_ALL_SOFTWARE = "SELECT * FROM software ORDER BY name";
    private static final String SELECT_USER_REQUESTS = "SELECT r.*, u.username, s.name as software_name " +
            "FROM requests r " +
            "JOIN users u ON r.user_id = u.id " +
            "JOIN software s ON r.software_id = s.id " +
            "WHERE r.user_id = ? " +
            "ORDER BY r.created_at DESC";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !user.isEmployee()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            // Get available software
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SOFTWARE);
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
            }

            // Get user's requests
            try (PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_REQUESTS)) {
                pstmt.setInt(1, user.getId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    List<Request> requestList = new ArrayList<>();
                    while (rs.next()) {
                        Request req = new Request();
                        req.setId(rs.getInt("id"));
                        req.setUserId(rs.getInt("user_id"));
                        req.setSoftwareId(rs.getInt("software_id"));
                        req.setAccessType(rs.getString("access_type"));
                        req.setReason(rs.getString("reason"));
                        req.setStatus(rs.getString("status"));
                        req.setCreatedAt(rs.getTimestamp("created_at"));
                        req.setUpdatedAt(rs.getTimestamp("updated_at"));
                        req.setUsername(rs.getString("username"));
                        req.setSoftwareName(rs.getString("software_name"));
                        requestList.add(req);
                    }
                    request.setAttribute("requestList", requestList);
                }
            }

            request.getRequestDispatcher("/WEB-INF/views/employee/request-access.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "An error occurred while fetching data");
            request.getRequestDispatcher("/WEB-INF/views/employee/request-access.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !user.isEmployee()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String softwareId = request.getParameter("softwareId");
        String accessType = request.getParameter("accessType");
        String reason = request.getParameter("reason");
        
        if (softwareId == null || accessType == null || reason == null || 
            softwareId.trim().isEmpty() || accessType.trim().isEmpty() || reason.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("/WEB-INF/views/employee/request-access.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_REQUEST)) {
            
            pstmt.setInt(1, user.getId());
            pstmt.setInt(2, Integer.parseInt(softwareId));
            pstmt.setString(3, accessType);
            pstmt.setString(4, reason);
            
            pstmt.executeUpdate();
            
            response.sendRedirect(request.getContextPath() + "/employee/request-access");
        } catch (SQLException e) {
            request.setAttribute("error", "An error occurred while submitting the request");
            request.getRequestDispatcher("/WEB-INF/views/employee/request-access.jsp").forward(request, response);
        }
    }
} 