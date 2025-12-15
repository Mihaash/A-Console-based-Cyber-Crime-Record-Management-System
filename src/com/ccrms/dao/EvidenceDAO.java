package com.ccrms.dao;

import com.ccrms.models.Evidence;
import com.ccrms.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for the Evidence model.
 * Manages all database operations for evidence records.
 */
public class EvidenceDAO {

    /**
     * Adds a new piece of evidence to the database for a specific complaint.
     * @param evidence The Evidence object to add.
     * @return true if the evidence was added successfully, false otherwise.
     */
    public boolean addEvidence(Evidence evidence) {
        String sql = "INSERT INTO evidence (complaint_id, evidence_type, details) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, evidence.getComplaintId());
            pstmt.setString(2, evidence.getEvidenceType());
            pstmt.setString(3, evidence.getDetails());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error adding evidence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all evidence associated with a given complaint ID.
     * @param complaintId The ID of the complaint.
     * @return A List of Evidence objects related to the complaint.
     */
    public List<Evidence> getEvidenceForComplaint(int complaintId) {
        List<Evidence> evidenceList = new ArrayList<>();
        String sql = "SELECT * FROM evidence WHERE complaint_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, complaintId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    evidenceList.add(mapRowToEvidence(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error retrieving evidence: " + e.getMessage());
        }
        return evidenceList;
    }

    /**
     * Deletes a specific piece of evidence from the database.
     * @param evidenceId The ID of the evidence to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteEvidence(int evidenceId) {
        String sql = "DELETE FROM evidence WHERE evidence_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, evidenceId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error deleting evidence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Helper method to map a ResultSet row to an Evidence object.
     * @param rs The ResultSet to map.
     * @return A populated Evidence object.
     * @throws SQLException if a column is not found.
     */
    private Evidence mapRowToEvidence(ResultSet rs) throws SQLException {
        Evidence evidence = new Evidence();
        evidence.setEvidenceId(rs.getInt("evidence_id"));
        evidence.setComplaintId(rs.getInt("complaint_id"));
        evidence.setEvidenceType(rs.getString("evidence_type"));
        evidence.setDetails(rs.getString("details"));
        return evidence;
    }
}
