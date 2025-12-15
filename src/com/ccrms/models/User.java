package com.ccrms.models;

/**
 * Model class representing a User (e.g., Admin, Officer).
 * This class is a simple Plain Old Java Object (POJO) with fields
 * corresponding to the 'users' table columns.
 */
public class User {
    private int userId;
    private String username;
    private String passwordHash; // This will be set for creation/update, but not typically fetched.
    private String role;

    // Default constructor
    public User() {}

    // Constructor for fetching user data (excluding password)
    public User(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User [ID=" + userId + ", Username=" + username + ", Role=" + role + "]";
    }
}
