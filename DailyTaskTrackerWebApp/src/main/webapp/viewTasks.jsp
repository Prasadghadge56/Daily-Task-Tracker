<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.dailytracker.model.User" %> <%-- Added this import as it was in header.jspf --%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${pageTitle != null ? pageTitle : 'All Tasks'} - Daily Task Tracker</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">

        <%-- START OF EMBEDDED HEADER.JSPF CONTENT --%>
        <div class="header">
            <h1>Daily Task Tracker</h1>
            <p>Organize your day, boost your productivity.</p>
        </div>

        <c:set var="currentUser" value="${sessionScope.currentUser}" />

        <c:if test="${currentUser != null}">
            <div class="welcome-message">
                Welcome, <strong>${currentUser.username}</strong>!
                <a href="logout">Logout</a>
            </div>
        </c:if>

        <div class="navbar">
            <c:if test="${currentUser != null}">
                <a href="addTask">Add New Task</a>
                <a href="viewTasks">All Tasks</a>
                <a href="viewTasks?filter=today">Today's Tasks</a>
                <a href="productivity">Productivity Insights</a>
            </c:if>
            <c:if test="${currentUser == null}">
                <a href="login">Login</a>
                <a href="register">Sign Up</a>
            </c:if>
        </div>
        <%-- END OF EMBEDDED HEADER.JSPF CONTENT --%>


        <h2>${pageTitle != null ? pageTitle : 'All Tasks'}</h2>

        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>

        <c:choose>
            <c:when test="${empty tasks}">
                <p style="text-align: center;">No tasks to display.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Description</th>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="task" items="${tasks}">
                            <tr>
                                <td>${task.id}</td>
                                <td class="${task.status == 'COMPLETE' ? 'task-complete' : 'task-incomplete'}">${task.title}</td>
                                <td>${task.description}</td>
<td>${task.taskDateFormatted}</td>

                                <td class="task-status ${task.status == 'COMPLETE' ? 'task-complete' : 'task-incomplete'}">${task.status}</td>
                                <td class="actions">
                                    <c:if test="${task.status == 'INCOMPLETE'}">
                                        <a href="markTask?id=${task.id}&action=complete">Mark Complete</a>
                                    </c:if>
                                    <c:if test="${task.status == 'COMPLETE'}">
                                        <a href="markTask?id=${task.id}&action=incomplete">Mark Incomplete</a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>