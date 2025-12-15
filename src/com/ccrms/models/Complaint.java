package com.ccrms.models;

import java.sql.Date;

/**
 * Model class representing a Complaint.
 * This POJO maps to the 'complaints' table.
 */
public class Complaint {
    private int complaintId;
    private String victimName;
    private String crimeType;
    private Date complaintDate;
    private String description;
    private String status;
    private int assignedOfficerId;
    private Integer criminalId; // Use Integer to allow for null values

    // Default constructor
    public Complaint() {}

    // Getters and Setters
    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public void setCrimeType(String crimeType) {
        this.crimeType = crimeType;
    }

    public Date getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(Date complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAssignedOfficerId() {
        return assignedOfficerId;
    }

    public void setAssignedOfficerId(int assignedOfficerId) {
        this.assignedOfficerId = assignedOfficerId;
    }

    public Integer getCriminalId() {
        return criminalId;
    }

    public void setCriminalId(Integer criminalId) {
        this.criminalId = criminalId;
    }

    @Override
    public String toString() {
        return String.format(
            "Complaint [ID=%d, Type=%s, Victim=%s, Date=%s, Status=%s]\n  Description: %s\n  OfficerID: %d, CriminalID: %s",
            complaintId,
            crimeType,
            victimName,
            complaintDate,
            status,
            description,
            assignedOfficerId,
            (criminalId == null || criminalId == 0) ? "N/A" : criminalId.toString()
        );
    }
}
