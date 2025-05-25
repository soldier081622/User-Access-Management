<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Software Management - User Access Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .software-container {
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

    <div class="container software-container">
        <h2 class="mb-4">Software Management</h2>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger" role="alert">
                ${error}
            </div>
        </c:if>
        
        <div class="card mb-4">
            <div class="card-header">
                <h5 class="card-title mb-0">Add New Software</h5>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/software" method="post">
                    <div class="mb-3">
                        <label for="name" class="form-label">Software Name</label>
                        <input type="text" class="form-control" id="name" name="name" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea class="form-control" id="description" name="description" rows="3"></textarea>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label">Access Levels</label>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="accessLevels" value="Read" id="read">
                            <label class="form-check-label" for="read">Read</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="accessLevels" value="Write" id="write">
                            <label class="form-check-label" for="write">Write</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="accessLevels" value="Admin" id="admin">
                            <label class="form-check-label" for="admin">Admin</label>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">Add Software</button>
                </form>
            </div>
        </div>
        
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">Software List</h5>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Description</th>
                                <th>Access Levels</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${softwareList}" var="software">
                                <tr>
                                    <td>${software.name}</td>
                                    <td>${software.description}</td>
                                    <td>${software.accessLevels}</td>
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