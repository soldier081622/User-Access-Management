<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pending Requests - User Access Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .requests-container {
            max-width: 1000px;
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

    <div class="container requests-container">
        <h2 class="mb-4">Pending Access Requests</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>
        
        <div class="card">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Employee</th>
                                <th>Software</th>
                                <th>Access Type</th>
                                <th>Reason</th>
                                <th>Requested On</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${requestList}" var="req">
                                <tr>
                                    <td>${req.username}</td>
                                    <td>${req.softwareName}</td>
                                    <td>${req.accessType}</td>
                                    <td>${req.reason}</td>
                                    <td><fmt:formatDate value="${req.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/manager/requests" method="post" class="d-inline">
                                            <input type="hidden" name="requestId" value="${req.id}">
                                            <input type="hidden" name="action" value="Approved">
                                            <button type="submit" class="btn btn-success btn-sm">Approve</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/manager/requests" method="post" class="d-inline">
                                            <input type="hidden" name="requestId" value="${req.id}">
                                            <input type="hidden" name="action" value="Rejected">
                                            <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty requestList}">
                                <tr>
                                    <td colspan="6" class="text-center">No pending requests</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 