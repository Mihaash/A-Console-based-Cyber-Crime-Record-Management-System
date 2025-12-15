package com.ccrms.models;

/**
 * Model class representing a piece of Evidence.
 * This POJO maps to the 'evidence' table.
 */
public class Evidence {
    private int evidenceId;
    private int complaintId;
    private String evidenceType;
    private String details;

    // Default constructor
    public Evidence() {}

    // Constructor for creating a complete Evidence object
    public Evidence(int evidenceId, int complaintId, String evidenceType, String details) {
        this.evidenceId = evidenceId;
        this.complaintId = complaintId;
        this.evidenceType = evidenceType;
        this.details = details;
    }

    // Getters and Setters
    public int getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(int evidenceId) {
        this.evidenceId = evidenceId;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return String.format("Evidence [ID=%d, ComplaintID=%d, Type=%s, Details=%s]",
            evidenceId, complaintId, evidenceType, details);
    }
}
