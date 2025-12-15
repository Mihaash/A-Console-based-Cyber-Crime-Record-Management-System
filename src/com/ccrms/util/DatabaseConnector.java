package com.ccrms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 * It provides a static method to get a database connection.
 *
 * NOTE: In a production environment, connection details should be externalized
 * to a configuration file (e.g., .properties) or environment variables
 * instead of being hardcoded.
 */
public class DatabaseConnector {

    // --- PLACEHOLDER DATABASE CREDENTIALS ---
    // Replace with your actual database configuration.
    // It's recommended to create a dedicated database and user for this application.
    // Example MySQL setup commands:
    // CREATE DATABASE cyber_crime_db;
    // CREATE USER 'ccrms_user'@'localhost' IDENTIFIED BY 'ccrms_password';
    // GRANT ALL PRIVILEGES ON cyber_crime_db.* TO 'ccrms_user'@'localhost';
    // FLUSH PRIVILEGES;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cyber_crime_db";
    private static final String DB_USER = "ccrms_user";
    private static final String DB_PASSWORD = "ccrms_password";

    private static Connection connection = null;

    /**
     * Returns a singleton connection to the database.
     * If the connection is null or closed, it attempts to create a new one.
     *
     * @return A valid database Connection object.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // The new driver class is com.mysql.cj.jdbc.Driver
                // However, it's automatically registered via the SPI mechanism,
                // so Class.forName() is not required for modern JDBC drivers.
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (SQLException e) {
                System.err.println("FATAL: Database connection failed.");
                System.err.println("Please check if the database is running and if the credentials in DatabaseConnector.java are correct.");
                throw e; // Propagate the exception to the caller
            }
        }
        return connection;
    }

    /**
     * Closes the singleton database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error while closing the database connection: " + e.getMessage());
            }
        }
    }
}
