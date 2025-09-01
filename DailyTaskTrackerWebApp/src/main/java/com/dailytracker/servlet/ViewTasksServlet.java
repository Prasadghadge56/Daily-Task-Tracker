package com.dailytracker.servlet;

import com.dailytracker.model.Task;
import com.dailytracker.model.User;
import com.dailytracker.service.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.time.format.DateTimeFormatter;

@WebServlet("/viewTasks")
public class ViewTasksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TaskService taskService;

    public void init() {
        taskService = new TaskService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.sendRedirect("login");
            return;
        }

        int userId = currentUser.getId();

        String filter = request.getParameter("filter");

        try {
            List<Task> tasks;
            if ("today".equals(filter)) {
                tasks = taskService.getTodayTasks(userId);
                request.setAttribute("pageTitle", "Today's Tasks");
            } else {
                tasks = taskService.getAllTasks(userId);
                request.setAttribute("pageTitle", "All Tasks");
            }

            // ✅ New addition: Pre-format LocalDate to String
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Task task : tasks) {
                if (task.getTaskDate() != null) {
                    String formattedDate = task.getTaskDate().format(formatter);
                    task.setTaskDateFormatted(formattedDate);
                } else {
                    task.setTaskDateFormatted("");
                }
            }

            request.setAttribute("tasks", tasks);
            request.getRequestDispatcher("viewTasks.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error retrieving tasks: " + e.getMessage());
            request.getRequestDispatcher("viewTasks.jsp").forward(request, response);
        }
    }
}
