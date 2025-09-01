package com.dailytracker.model;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String description;
    private LocalDate taskDate;
    private String status; // "COMPLETE" or "INCOMPLETE"
    private String taskDateFormatted; // formatted date for JSP display
    private int userId;

    // ✅ Default constructor
    public Task() {
    }

    // ✅ All-args constructor (including userId)
    public Task(int id, String title, String description, LocalDate taskDate, String status, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskDate = taskDate;
        this.status = status;
        this.userId = userId;
    }

    // ✅ Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }
    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskDateFormatted() {
        return taskDateFormatted;
    }
    public void setTaskDateFormatted(String taskDateFormatted) {
        this.taskDateFormatted = taskDateFormatted;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskDate=" + taskDate +
                ", status='" + status + '\'' +
                ", taskDateFormatted='" + taskDateFormatted + '\'' +
                ", userId=" + userId +
                '}';
    }
}
