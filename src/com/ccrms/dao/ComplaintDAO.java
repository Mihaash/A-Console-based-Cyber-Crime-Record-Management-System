package com.ccrms.dao;

import com.ccrms.models.Complaint;
import com.ccrms.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Complaint model.
 * Handles all database operations for complaints.
 */
public class ComplaintDAO {

    /**
     * Adds a new complaint to the database.
     * @param complaint The Complaint object to be persisted.
     * @return true if the complaint was added successfully, false otherwise.
     */
    public boolean addComplaint(Complaint complaint) {
        String sql = "INSERT INTO complaints (victim_name, crime_type, complaint_date, description, status, assigned_officer_id, criminal_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, complaint.getVictimName());
            pstmt.setString(2, complaint.getCrimeType());
            pstmt.setDate(3, complaint.getComplaintDate());
            pstmt.setString(4, complaint.getDescription());
            pstmt.setString(5, complaint.getStatus());
            pstmt.setInt(6, complaint.getAssignedOfficerId());
            if (complaint.getCriminalId() != null) {
                pstmt.setInt(7, complaint.getCriminalId());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error adding complaint: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of all complaints from the database.
     * @return A List of Complaint objects.
     */
    public List<Complaint> getAllComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        String sql = "SELECT * FROM complaints ORDER BY complaint_date DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                complaints.add(mapRowToComplaint(rs));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error retrieving all complaints: " + e.getMessage());
        }
        return complaints;
    }

    /**
     * Updates the status of a specific complaint.
     * @param complaintId The ID of the complaint to update.
     * @param newStatus The new status to set.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateComplaintStatus(int complaintId, String newStatus) {
        String sql = "UPDATE complaints SET status = ? WHERE complaint_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, complaintId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error updating complaint status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a complaint and all associated evidence from the database.
     * @param complaintId The ID of the complaint to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteComplaint(int complaintId) {
        String deleteEvidenceSql = "DELETE FROM evidence WHERE complaint_id = ?";
        String deleteComplaintSql = "DELETE FROM complaints WHERE complaint_id = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First, delete associated evidence
            try (PreparedStatement pstmtEvidence = conn.prepareStatement(deleteEvidenceSql)) {
                pstmtEvidence.setInt(1, complaintId);
                pstmtEvidence.executeUpdate();
            }

            // Then, delete the complaint
            try (PreparedStatement pstmtComplaint = conn.prepareStatement(deleteComplaintSql)) {
                pstmtComplaint.setInt(1, complaintId);
                int affectedRows = pstmtComplaint.executeUpdate();
                
                conn.commit(); // Commit transaction
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error deleting complaint: " + e.getMessage());
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
     * Helper method to map a ResultSet row to a Complaint object.
     * This reduces code duplication.
     * @param rs The ResultSet from which to extract data.
     * @return A populated Complaint object.
     * @throws SQLException if a column is not found.
     */
    private Complaint mapRowToComplaint(ResultSet rs) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setComplaintId(rs.getInt("complaint_id"));
        complaint.setVictimName(rs.getString("victim_name"));
        complaint.setCrimeType(rs.getString("crime_type"));
        complaint.setComplaintDate(rs.getDate("complaint_date"));
        complaint.setDescription(rs.getString("description"));
        complaint.setStatus(rs.getString("status"));
        complaint.setAssignedOfficerId(rs.getInt("assigned_officer_id"));
        complaint.setCriminalId((Integer) rs.getObject("criminal_id")); // Handle possible NULL
        return complaint;
    }
}
