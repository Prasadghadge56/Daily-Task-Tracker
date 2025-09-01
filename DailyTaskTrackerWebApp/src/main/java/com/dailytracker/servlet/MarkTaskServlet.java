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

@WebServlet("/markTask")
public class MarkTaskServlet extends HttpServlet {
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

        int taskId = Integer.parseInt(request.getParameter("id"));
        String action = request.getParameter("action"); // "complete" or "incomplete"

        try {
            if ("complete".equals(action)) {
                taskService.markTaskComplete(taskId, userId); // Pass userId
            } else if ("incomplete".equals(action)) {
                taskService.markTaskIncomplete(taskId, userId); // Pass userId
            }
            response.sendRedirect("viewTasks"); // Redirect back to task list
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error marking task: " + e.getMessage());
            request.getRequestDispatcher("viewTasks.jsp").forward(request, response); // Stay on current page with error
        }
    }
}