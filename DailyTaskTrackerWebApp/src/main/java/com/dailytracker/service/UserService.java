package com.dailytracker.service;

import com.dailytracker.dao.UserDao;
import com.dailytracker.model.User;

import java.sql.SQLException;

public class UserService {
    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public boolean registerUser(String username, String password, String email) throws SQLException {
        // Basic validation
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            return false;
        }

        // Check if username or email already exists
        if (userDao.getUserByUsername(username) != null) {
            return false; // Username already taken
        }
        if (userDao.getUserByEmail(email) != null) {
            return false; // Email already registered
        }

        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setPassword(password); // In a real app, hash the password here (e.g., using BCrypt)
        newUser.setEmail(email.trim());

        userDao.registerUser(newUser);
        return true;
    }

    public User loginUser(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }

        User user = userDao.getUserByUsername(username.trim());
        if (user != null) {
            // In a real app, compare hashed passwords: BCrypt.checkpw(password, user.getPassword())
            if (user.getPassword().equals(password)) { // **THIS IS INSECURE FOR PRODUCTION!**
                return user;
            }
        }
        return null; // Login failed
    }
}