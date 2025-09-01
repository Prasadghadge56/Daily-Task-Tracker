<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.dailytracker.model.User" %> <%-- Added this import as it was in header.jspf --%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Productivity Insights - Daily Task Tracker</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">

        <%-- START OF EMBEDDED HEADER CONTENT --%>
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
        <%-- END OF EMBEDDED HEADER CONTENT --%>


        <h2>Productivity Insights</h2>

        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>
        <c:if test="${empty errorMessage}">
            <div class="insight-item">
                <strong>Today's Completion Rate:</strong>
                <fmt:formatNumber value="${dailyCompletionRate}" type="number" maxFractionDigits="2"/>%
            </div>
            <div class="insight-item">
                <strong>Weekly Completion Rate:</strong>
                <fmt:formatNumber value="${weeklyCompletionRate}" type="number" maxFractionDigits="2"/>%
            </div>
            <div class="insight-item">
                <strong>Longest Completion Streak:</strong>
                ${longestStreak} days
            </div>
        </c:if>
    </div>
</body>
</html>