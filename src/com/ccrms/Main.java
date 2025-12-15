package com.ccrms;

import com.ccrms.dao.ComplaintDAO;
import com.ccrms.dao.CriminalDAO;
import com.ccrms.dao.EvidenceDAO;
import com.ccrms.dao.UserDAO;
import com.ccrms.models.Complaint;
import com.ccrms.models.Criminal;
import com.ccrms.models.Evidence;
import com.ccrms.models.User;
import com.ccrms.util.DatabaseConnector;
import com.ccrms.util.PasswordUtil;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for the Cyber Crime Record Management System.
 * This class serves as the entry point and console-based UI controller.
 */
public class Main {

    // DAOs for database interaction
    private static final UserDAO userDAO = new UserDAO();
    private static final ComplaintDAO complaintDAO = new ComplaintDAO();
    private static final CriminalDAO criminalDAO = new CriminalDAO();
    private static final EvidenceDAO evidenceDAO = new EvidenceDAO();

    // Scanner for user input
    private static final Scanner scanner = new Scanner(System.in);

    // Currently logged-in user
    private static User currentUser = null;

    public static void main(String[] args) {
        printHeader("Welcome to the Cyber Crime Record Management System");

        // Loop for login attempts
        int loginAttempts = 3;
        while (loginAttempts > 0 && currentUser == null) {
            if (attemptLogin()) {
                break;
            }
            loginAttempts--;
            if (loginAttempts > 0) {
                System.out.println("Login failed. You have " + loginAttempts + " attempts remaining.");
            } else {
                System.out.println("Maximum login attempts reached. The application will now exit.");
                return;
            }
        }

        // If login was successful, show the main menu
        if (currentUser != null) {
            showMainMenu();
        }

        // Cleanup resources before exiting
        scanner.close();
        DatabaseConnector.closeConnection();
        System.out.println("Application terminated.");
    }

    /**
     * Handles the user login process.
     * @return true if login is successful, false otherwise.
     */
    private static boolean attemptLogin() {
        System.out.println("\n--- System Login ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDAO.findByUsername(username);

        // Check if user exists and if the password matches the stored hash
        if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            currentUser = user;
            printHeader("Login Successful. Welcome, " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            return true;
        }
        else {
            System.out.println("Error: Invalid username or password.");
            return false;
        }
    }

    /**
     * Displays the main menu and handles user navigation.
     */
    private static void showMainMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Manage Complaints");
            System.out.println("2. Manage Criminals");
            System.out.println("3. Manage Evidence");
            System.out.println("4. Generate Reports");
            if (currentUser.getRole().equalsIgnoreCase("Admin")) {
                System.out.println("5. Delete Records");
            }
            System.out.println("0. Logout and Exit");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        manageComplaints();
                        break;
                    case 2:
                        manageCriminals();
                        break;
                    case 3:
                        manageEvidence();
                        break;
                    case 4:
                        generateReports();
                        break;
                    case 5:
                        if (currentUser.getRole().equalsIgnoreCase("Admin")) {
                            manageDeletion();
                        } else {
                            System.out.println("Invalid choice. Please enter a number from the menu.");
                        }
                        break;
                    case 0:
                        exit = true;
                        System.out.println("You have been logged out.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from the menu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void manageComplaints() {
        boolean back = false;
        while (!back) {
            printHeader("Complaint Management");
            System.out.println("1. Add New Complaint");
            System.out.println("2. View All Complaints");
            System.out.println("3. Update Complaint Status");
            System.out.println("9. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        addNewComplaint();
                        break;
                    case 2:
                        viewAllComplaints();
                        break;
                    case 3:
                        updateComplaintStatus();
                        break;
                    case 9:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void addNewComplaint() {
        printHeader("Add New Complaint");
        try {
            Complaint complaint = new Complaint();
            System.out.print("Enter Victim's Name: ");
            complaint.setVictimName(scanner.nextLine());
            System.out.print("Enter Crime Type (e.g., Phishing, Hacking): ");
            complaint.setCrimeType(scanner.nextLine());
            System.out.print("Enter Description: ");
            complaint.setDescription(scanner.nextLine());

            complaint.setComplaintDate(new Date(System.currentTimeMillis())); // Set current date
            complaint.setStatus("Open"); // Default status
            complaint.setAssignedOfficerId(currentUser.getUserId()); // Assign to current user

            if (complaintDAO.addComplaint(complaint)) {
                System.out.println("Complaint added successfully!");
            } else {
                System.out.println("Failed to add complaint.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void viewAllComplaints() {
        printHeader("All Complaints");
        List<Complaint> complaints = complaintDAO.getAllComplaints();
        if (complaints.isEmpty()) {
            System.out.println("No complaints found.");
        } else {
            for (Complaint complaint : complaints) {
                System.out.println("--------------------");
                System.out.println(complaint);
            }
        }
    }

    private static void updateComplaintStatus() {
        printHeader("Update Complaint Status");
        try {
            System.out.print("Enter Complaint ID to update: ");
            int complaintId = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter New Status (Open, Under Investigation, Closed): ");
            String newStatus = scanner.nextLine();

            if (complaintDAO.updateComplaintStatus(complaintId, newStatus)) {
                System.out.println("Status updated successfully!");
            } else {
                System.out.println("Failed to update status. Check if Complaint ID is correct.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Complaint ID. Please enter a number.");
        }
    }

    private static void manageCriminals() {
        boolean back = false;
        while (!back) {
            printHeader("Criminal Management");
            System.out.println("1. Add New Criminal");
            System.out.println("2. View All Criminals");
            System.out.println("9. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        addNewCriminal();
                        break;
                    case 2:
                        viewAllCriminals();
                        break;
                    case 9:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void addNewCriminal() {
        printHeader("Add New Criminal");
        try {
            Criminal criminal = new Criminal();
            System.out.print("Enter Criminal's Name: ");
            criminal.setName(scanner.nextLine());
            System.out.print("Enter Crime History: ");
            criminal.setCrimeHistory(scanner.nextLine());
            System.out.print("Enter Status (e.g., Under Investigation, Arrested): ");
            criminal.setStatus(scanner.nextLine());

            if (criminalDAO.addCriminal(criminal) != -1) {
                System.out.println("Criminal added successfully!");
            } else {
                System.out.println("Failed to add criminal.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void viewAllCriminals() {
        printHeader("All Criminals");
        List<Criminal> criminals = criminalDAO.getAllCriminals();
        if (criminals.isEmpty()) {
            System.out.println("No criminals found.");
        } else {
            for (Criminal criminal : criminals) {
                System.out.println("--------------------");
                System.out.println(criminal);
            }
        }
    }

    private static void manageEvidence() {
        boolean back = false;
        while (!back) {
            printHeader("Evidence Management");
            System.out.println("1. Add Evidence to a Complaint");
            System.out.println("2. View Evidence for a Complaint");
            System.out.println("9. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        addEvidence();
                        break;
                    case 2:
                        viewEvidenceForComplaint();
                        break;
                    case 9:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void addEvidence() {
        printHeader("Add New Evidence");
        try {
            Evidence evidence = new Evidence();
            System.out.print("Enter Complaint ID to link evidence: ");
            evidence.setComplaintId(Integer.parseInt(scanner.nextLine()));
            System.out.print("Enter Evidence Type (e.g., Email, IP Address): ");
            evidence.setEvidenceType(scanner.nextLine());
            System.out.print("Enter Evidence Details: ");
            evidence.setDetails(scanner.nextLine());

            if (evidenceDAO.addEvidence(evidence)) {
                System.out.println("Evidence added successfully!");
            } else {
                System.out.println("Failed to add evidence. Check if Complaint ID is correct.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Complaint ID. Please enter a number.");
        }
    }

    private static void viewEvidenceForComplaint() {
        printHeader("View Evidence for Complaint");
        try {
            System.out.print("Enter Complaint ID to view evidence: ");
            int complaintId = Integer.parseInt(scanner.nextLine());
            List<Evidence> evidenceList = evidenceDAO.getEvidenceForComplaint(complaintId);

            if (evidenceList.isEmpty()) {
                System.out.println("No evidence found for Complaint ID: " + complaintId);
            } else {
                System.out.println("Evidence for Complaint ID: " + complaintId);
                for (Evidence evidence : evidenceList) {
                    System.out.println("--------------------");
                    System.out.println(evidence);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Complaint ID. Please enter a number.");
        }
    }

    private static void generateReports() {
        printHeader("Generate Reports");
        System.out.println("This feature is a simplified demonstration.");
        
        List<Complaint> complaints = complaintDAO.getAllComplaints();
        long openCount = complaints.stream().filter(c -> "Open".equalsIgnoreCase(c.getStatus())).count();
        long investigatingCount = complaints.stream().filter(c -> "Under Investigation".equalsIgnoreCase(c.getStatus())).count();
        long closedCount = complaints.stream().filter(c -> "Closed".equalsIgnoreCase(c.getStatus())).count();

        System.out.println("\n--- Crime Status Report ---");
        System.out.println("Total Complaints: " + complaints.size());
        System.out.println("Open Cases: " + openCount);
        System.out.println("Cases Under Investigation: " + investigatingCount);
        System.out.println("Closed Cases: " + closedCount);
        System.out.println("\nPress Enter to return to the Main Menu.");
        scanner.nextLine(); // Wait for user to acknowledge
    }

    private static void manageDeletion() {
        boolean back = false;
        while(!back) {
            printHeader("Delete Records");
            System.out.println("1. Delete a Complaint");
            System.out.println("2. Delete a Criminal Record");
            System.out.println("3. Delete an Evidence Record");
            System.out.println("9. Back to Main Menu");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        deleteComplaint();
                        break;
                    case 2:
                        deleteCriminal();
                        break;
                    case 3:
                        deleteEvidence();
                        break;
                    case 9:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void deleteComplaint() {
        printHeader("Delete Complaint");
        try {
            System.out.print("Enter Complaint ID to delete: ");
            int complaintId = Integer.parseInt(scanner.nextLine());

            if (complaintDAO.deleteComplaint(complaintId)) {
                System.out.println("Complaint deleted successfully!");
            } else {
                System.out.println("Failed to delete complaint. Check if Complaint ID is correct.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Complaint ID. Please enter a number.");
        }
    }

    private static void deleteCriminal() {
        printHeader("Delete Criminal Record");
        try {
            System.out.print("Enter Criminal ID to delete: ");
            int criminalId = Integer.parseInt(scanner.nextLine());

            if (criminalDAO.deleteCriminal(criminalId)) {
                System.out.println("Criminal record deleted successfully!");
            } else {
                System.out.println("Failed to delete criminal record. Check if Criminal ID is correct.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Criminal ID. Please enter a number.");
        }
    }

    private static void deleteEvidence() {
        printHeader("Delete Evidence Record");
        try {
            System.out.print("Enter Evidence ID to delete: ");
            int evidenceId = Integer.parseInt(scanner.nextLine());

            if (evidenceDAO.deleteEvidence(evidenceId)) {
                System.out.println("Evidence record deleted successfully!");
            } else {
                System.out.println("Failed to delete evidence record. Check if Evidence ID is correct.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Evidence ID. Please enter a number.");
        }
    }

    /**
     * Utility method to print a formatted header.
     * @param title The title to display.
     */
    private static void printHeader(String title) {
        String border = "==========================================================";
        System.out.println("\n" + border);
        System.out.println("  " + title);
        System.out.println(border);
    }
}
