package com.ccrms.models;

/**
 * Model class representing a Criminal.
 * This POJO maps to the 'criminals' table in the database.
 */
public class Criminal {
    private int criminalId;
    private String name;
    private String crimeHistory;
    private String status;

    // Default constructor
    public Criminal() {}

    // Constructor for creating a complete Criminal object
    public Criminal(int criminalId, String name, String crimeHistory, String status) {
        this.criminalId = criminalId;
        this.name = name;
        this.crimeHistory = crimeHistory;
        this.status = status;
    }

    // Getters and Setters
    public int getCriminalId() {
        return criminalId;
    }

    public void setCriminalId(int criminalId) {
        this.criminalId = criminalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCrimeHistory() {
        return crimeHistory;
    }

    public void setCrimeHistory(String crimeHistory) {
        this.crimeHistory = crimeHistory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Criminal [ID=" + criminalId + ", Name=" + name + ", Status=" + status + "]\n  History: " + crimeHistory;
    }
}
