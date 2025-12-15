package com.ccrms.dao;

import com.ccrms.models.Criminal;
import com.ccrms.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Criminal model.
 * Manages all database operations related to criminals.
 */
public class CriminalDAO {

    /**
     * Adds a new criminal to the database.
     * @param criminal The Criminal object to add.
     * @return The generated ID of the new criminal, or -1 if the operation fails.
     */
    public int addCriminal(Criminal criminal) {
        String sql = "INSERT INTO criminals (name, crime_history, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, criminal.getName());
            pstmt.setString(2, criminal.getCrimeHistory());
            pstmt.setString(3, criminal.getStatus());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the new criminal_id
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error adding criminal: " + e.getMessage());
        }
        return -1; // Return -1 to indicate failure
    }

    /**
     * Retrieves all criminals from the database.
     * @return A List of all Criminal objects.
     */
    public List<Criminal> getAllCriminals() {
        List<Criminal> criminals = new ArrayList<>();
        String sql = "SELECT * FROM criminals ORDER BY name";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                criminals.add(mapRowToCriminal(rs));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error retrieving all criminals: " + e.getMessage());
        }
        return criminals;
    }

    /**
     * Deletes a criminal from the database.
     * Before deletion, it updates associated complaints to set criminal_id to NULL.
     * @param criminalId The ID of the criminal to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteCriminal(int criminalId) {
        String updateComplaintsSql = "UPDATE complaints SET criminal_id = NULL WHERE criminal_id = ?";
        String deleteCriminalSql = "DELETE FROM criminals WHERE criminal_id = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First, update associated complaints
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateComplaintsSql)) {
                pstmtUpdate.setInt(1, criminalId);
                pstmtUpdate.executeUpdate();
            }

            // Then, delete the criminal
            try (PreparedStatement pstmtDelete = conn.prepareStatement(deleteCriminalSql)) {
                pstmtDelete.setInt(1, criminalId);
                int affectedRows = pstmtDelete.executeUpdate();
                
                conn.commit(); // Commit transaction
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error deleting criminal: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback on error
                } catch (SQLException ex) {
                    System.err.println("SQL Error on rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("SQL Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Helper method to map a ResultSet row to a Criminal object.
     * @param rs The ResultSet to map.
     * @return A populated Criminal object.
     * @throws SQLException if a column is not found.
     */
    private Criminal mapRowToCriminal(ResultSet rs) throws SQLException {
        Criminal criminal = new Criminal();
        criminal.setCriminalId(rs.getInt("criminal_id"));
        criminal.setName(rs.getString("name"));
        criminal.setCrimeHistory(rs.getString("crime_history"));
        criminal.setStatus(rs.getString("status"));
        return criminal;
    }
}
