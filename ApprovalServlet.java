package com.accessmanagement.servlet;

import com.accessmanagement.model.Request;
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

@WebServlet("/manager/requests/*")
public class ApprovalServlet extends HttpServlet {
    private static final String SELECT_PENDING_REQUESTS = "SELECT r.*, u.username, s.name as software_name " +
            "FROM requests r " +
            "JOIN users u ON r.user_id = u.id " +
            "JOIN software s ON r.software_id = s.id " +
            "WHERE r.status = 'Pending' " +
            "ORDER BY r.created_at DESC";
    private static final String UPDATE_REQUEST_STATUS = "UPDATE requests SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !user.isManager()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_PENDING_REQUESTS);
             ResultSet rs = pstmt.executeQuery()) {
            
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
            request.getRequestDispatcher("/WEB-INF/views/manager/requests.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "An error occurred while fetching requests");
            request.getRequestDispatcher("/WEB-INF/views/manager/requests.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !user.isManager()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String requestId = request.getParameter("requestId");
        String action = request.getParameter("action");
        
        if (requestId == null || action == null || 
            requestId.trim().isEmpty() || action.trim().isEmpty()) {
            request.setAttribute("error", "Invalid request");
            request.getRequestDispatcher("/WEB-INF/views/manager/requests.jsp").forward(request, response);
            return;
        }

        String status = "Approved".equals(action) ? "Approved" : "Rejected";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_REQUEST_STATUS)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, Integer.parseInt(requestId));
            
            pstmt.executeUpdate();
            
            response.sendRedirect(request.getContextPath() + "/manager/requests");
        } catch (SQLException e) {
            request.setAttribute("error", "An error occurred while updating the request");
            request.getRequestDispatcher("/WEB-INF/views/manager/requests.jsp").forward(request, response);
        }
    }
} 