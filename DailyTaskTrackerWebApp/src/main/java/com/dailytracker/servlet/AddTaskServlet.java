package com.dailytracker.servlet;

import com.dailytracker.model.Task;
import com.dailytracker.model.User; // Import User model
import com.dailytracker.service.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Import HttpSession
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/addTask")
public class AddTaskServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TaskService taskService;

    public void init() {
        taskService = new TaskService();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // The filter should prevent this, but as a safeguard:
        if (currentUser == null) {
            response.sendRedirect("login"); // Redirect to login if user somehow isn't in session
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String taskDateStr = request.getParameter("taskDate");

        LocalDate taskDate = null;
        if (taskDateStr != null && !taskDateStr.isEmpty()) {
            taskDate = LocalDate.parse(taskDateStr);
        } else {
            taskDate = LocalDate.now();
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setTaskDate(taskDate);
        task.setStatus("INCOMPLETE");
        task.setUserId(currentUser.getId()); // Set the user ID for the task

        try {
            taskService.addTask(task);
            response.sendRedirect("viewTasks");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error adding task: " + e.getMessage());
            request.getRequestDispatcher("addTask.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // No change here, the filter handles authentication
        request.getRequestDispatcher("addTask.jsp").forward(request, response);
    }
}