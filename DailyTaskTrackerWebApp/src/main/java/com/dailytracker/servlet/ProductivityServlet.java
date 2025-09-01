package com.dailytracker.servlet;

import com.dailytracker.model.User; // Import the User model
import com.dailytracker.service.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Import HttpSession
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/productivity")
public class ProductivityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TaskService taskService;

    public void init() {
        taskService = new TaskService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // IMPORTANT: Ensure the user is logged in.
        // The AuthenticationFilter should handle this, but it's a good safeguard.
        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        // Get the user ID
        int userId = currentUser.getId();

        try {
            // Pass the userId to the service method
            Map<String, Double> insights = taskService.getProductivityInsights(userId);
            request.setAttribute("dailyCompletionRate", insights.get("dailyCompletionRate"));
            request.setAttribute("weeklyCompletionRate", insights.get("weeklyCompletionRate"));
            request.setAttribute("longestStreak", insights.get("longestStreak").intValue()); // Cast to int for display

            request.getRequestDispatcher("productivity.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error retrieving productivity insights: " + e.getMessage());
            request.getRequestDispatcher("productivity.jsp").forward(request, response);
        }
    }
}