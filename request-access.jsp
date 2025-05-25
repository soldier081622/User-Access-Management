<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Request Access - User Access Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .request-container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="#">User Access Management</a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container request-container">
        <h2 class="mb-4">Request Access</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>
        
        <div class="card mb-4">
            <div class="card-header">
                <h5 class="card-title mb-0">New Access Request</h5>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/employee/request-access" method="post">
                    <div class="mb-3">
                        <label for="softwareId" class="form-label">Software</label>
                        <select class="form-select" id="softwareId" name="softwareId" required>
                            <option value="">Select Software</option>
                            <c:forEach items="${softwareList}" var="software">
                                <option value="${software.id}">${software.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="mb-3">
                        <label for="accessType" class="form-label">Access Type</label>
                        <select class="form-select" id="accessType" name="accessType" required>
                            <option value="">Select Access Type</option>
                            <option value="Read">Read</option>
                            <option value="Write">Write</option>
                            <option value="Admin">Admin</option>
                        </select>
                    </div>
                    
                    <div class="mb-3">
                        <label for="reason" class="form-label">Reason for Request</label>
                        <textarea class="form-control" id="reason" name="reason" rows="3" required></textarea>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">Submit Request</button>
                </form>
            </div>
        </div>
        
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">My Requests</h5>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Software</th>
                                <th>Access Type</th>
                                <th>Reason</th>
                                <th>Status</th>
                                <th>Requested On</th>
                                <th>Updated On</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${requestList}" var="req">
                                <tr>
                                    <td>${req.softwareName}</td>
                                    <td>${req.accessType}</td>
                                    <td>${req.reason}</td>
                                    <td>
                                        <span class="badge bg-${req.status == 'Pending' ? 'warning' : req.status == 'Approved' ? 'success' : 'danger'}">
                                            ${req.status}
                                        </span>
                                    </td>
                                    <td><fmt:formatDate value="${req.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td><fmt:formatDate value="${req.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 