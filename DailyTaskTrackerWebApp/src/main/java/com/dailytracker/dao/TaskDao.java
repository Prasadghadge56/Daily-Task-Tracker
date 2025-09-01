package com.dailytracker.dao;

import com.dailytracker.model.Task;
import com.dailytracker.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    public void addTask(Task task) throws SQLException {
        // Modified SQL to include user_id
        String sql = "INSERT INTO tasks (title, description, task_date, status, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, Date.valueOf(task.getTaskDate()));
            pstmt.setString(4, task.getStatus());
            pstmt.setInt(5, task.getUserId()); // Set user_id
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    task.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateTaskStatus(int taskId, String status, int userId) throws SQLException {
        // Added userId to WHERE clause to prevent users from modifying other users' tasks
        String sql = "UPDATE tasks SET status = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, taskId);
            pstmt.setInt(3, userId); // Filter by user_id
            pstmt.executeUpdate();
        }
    }

    public List<Task> getAllTasks(int userId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        // Added userId to WHERE clause
        String sql = "SELECT id, title, description, task_date, status, user_id FROM tasks WHERE user_id = ? ORDER BY task_date DESC, id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        }
        return tasks;
    }

    public List<Task> getTasksByDate(LocalDate date, int userId) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        // Added userId to WHERE clause
        String sql = "SELECT id, title, description, task_date, status, user_id FROM tasks WHERE task_date = ? AND user_id = ? ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setInt(2, userId); // Filter by user_id
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        }
        return tasks;
    }

    public int getCompletedTasksCountForDate(LocalDate date, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tasks WHERE task_date = ? AND status = 'COMPLETE' AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getTotalTasksCountForDate(LocalDate date, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tasks WHERE task_date = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getCompletedTasksCountForWeek(LocalDate startDate, LocalDate endDate, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tasks WHERE task_date BETWEEN ? AND ? AND status = 'COMPLETE' AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            pstmt.setInt(3, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getTotalTasksCountForWeek(LocalDate startDate, LocalDate endDate, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tasks WHERE task_date BETWEEN ? AND ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            pstmt.setInt(3, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public List<Task> getAllTasksForStreakCalculation(int userId) throws SQLException {
        // Fetch all tasks for a specific user to calculate streak
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, title, description, task_date, status, user_id FROM tasks WHERE user_id = ? ORDER BY task_date ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        }
        return tasks;
    }

    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setTaskDate(rs.getDate("task_date").toLocalDate());
        task.setStatus(rs.getString("status"));
        task.setUserId(rs.getInt("user_id")); // Map user_id
        return task;
    }
}