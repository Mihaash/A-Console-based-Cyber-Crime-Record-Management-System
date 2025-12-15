package com.ccrms.dao;

import com.ccrms.models.User;
import com.ccrms.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for the User model.
 * Handles all database operations related to Users.
 */
public class UserDAO {

    /**
     * Finds a user by their username. This is a key part of the login process.
     * It retrieves the user's details, including the stored password hash.
     *
     * @param username The username to search for.
     * @return A User object if a user with the given username is found, otherwise null.
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;

        // Using try-with-resources to ensure the connection is closed automatically
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            // In a more robust application, this would be handled by a logging framework
            // and might throw a custom DataAccessException.
        }
        return user;
    }
}
