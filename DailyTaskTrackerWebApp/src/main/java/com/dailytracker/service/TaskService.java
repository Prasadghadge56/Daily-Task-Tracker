package com.dailytracker.service;

import com.dailytracker.dao.TaskDao;
import com.dailytracker.model.Task;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskService {
    private TaskDao taskDao;

    public TaskService() {
        this.taskDao = new TaskDao();
    }

    public void addTask(Task task) throws SQLException {
        taskDao.addTask(task);
    }

    public void markTaskComplete(int taskId, int userId) throws SQLException {
        taskDao.updateTaskStatus(taskId, "COMPLETE", userId);
    }

    public void markTaskIncomplete(int taskId, int userId) throws SQLException {
        taskDao.updateTaskStatus(taskId, "INCOMPLETE", userId);
    }

    public List<Task> getAllTasks(int userId) throws SQLException {
        return taskDao.getAllTasks(userId);
    }

    public List<Task> getTodayTasks(int userId) throws SQLException {
        return taskDao.getTasksByDate(LocalDate.now(), userId);
    }

    public Map<String, Double> getProductivityInsights(int userId) throws SQLException {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // Today's completion rate
        int completedToday = taskDao.getCompletedTasksCountForDate(today, userId);
        int totalToday = taskDao.getTotalTasksCountForDate(today, userId);
        double dailyCompletionRate = (totalToday > 0) ? ((double) completedToday / totalToday) * 100 : 0.0;

        // Weekly completion rate
        int completedThisWeek = taskDao.getCompletedTasksCountForWeek(startOfWeek, endOfWeek, userId);
        int totalThisWeek = taskDao.getTotalTasksCountForWeek(startOfWeek, endOfWeek, userId);
        double weeklyCompletionRate = (totalThisWeek > 0) ? ((double) completedThisWeek / totalThisWeek) * 100 : 0.0;

        // Longest streak - Fetch tasks for the specific user
        List<Task> allTasks = taskDao.getAllTasksForStreakCalculation(userId); // Use new method
        int longestStreak = calculateLongestStreak(allTasks);

        return Map.of(
            "dailyCompletionRate", dailyCompletionRate,
            "weeklyCompletionRate", weeklyCompletionRate,
            "longestStreak", (double) longestStreak
        );
    }

    // Existing calculateLongestStreak method remains the same, it processes the list it receives.
    private int calculateLongestStreak(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }

        // Sort tasks by date in ascending order
        Collections.sort(tasks, (t1, t2) -> t1.getTaskDate().compareTo(t2.getTaskDate()));

        // Group tasks by date
        Map<LocalDate, List<Task>> tasksByDate = tasks.stream()
                .collect(Collectors.groupingBy(Task::getTaskDate));

        int currentStreak = 0;
        int maxStreak = 0;
        LocalDate previousDate = null;

        List<LocalDate> sortedDates = tasksByDate.keySet().stream().sorted().collect(Collectors.toList());

        for (LocalDate date : sortedDates) {
            // Check if ALL tasks on this date for the current user are completed
            boolean allTasksCompletedOnDate = tasksByDate.get(date).stream()
                    .allMatch(t -> "COMPLETE".equals(t.getStatus()));

            if (allTasksCompletedOnDate) {
                if (previousDate == null || date.equals(previousDate.plusDays(1))) {
                    currentStreak++;
                } else {
                    currentStreak = 1; // Streak broken, start new
                }
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 0; // Streak broken
            }
            previousDate = date;
        }
        return maxStreak;
    }
}