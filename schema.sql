-- Drop tables if they exist to ensure a clean setup
DROP TABLE IF EXISTS evidence;
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS criminals;
DROP TABLE IF EXISTS users;

-- User table for authentication and authorization
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- Store hashed passwords, not plaintext
    role VARCHAR(20) NOT NULL CHECK (role IN ('Admin', 'Officer'))
);

-- Criminals table
CREATE TABLE criminals (
    criminal_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    crime_history TEXT,
    status VARCHAR(50) DEFAULT 'Under Investigation'
);

-- Complaints table
CREATE TABLE complaints (
    complaint_id INT AUTO_INCREMENT PRIMARY KEY,
    victim_name VARCHAR(100) NOT NULL,
    crime_type VARCHAR(50) NOT NULL,
    complaint_date DATE NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL CHECK (status IN ('Open', 'Under Investigation', 'Closed')),
    assigned_officer_id INT,
    criminal_id INT,
    FOREIGN KEY (assigned_officer_id) REFERENCES users(user_id),
    FOREIGN KEY (criminal_id) REFERENCES criminals(criminal_id)
);

-- Evidence table linked to complaints
CREATE TABLE evidence (
    evidence_id INT AUTO_INCREMENT PRIMARY KEY,
    complaint_id INT NOT NULL,
    evidence_type VARCHAR(50) NOT NULL, -- e.g., 'Email ID', 'IP Address', 'Phone Number', 'Digital Note'
    details TEXT NOT NULL,
    FOREIGN KEY (complaint_id) REFERENCES complaints(complaint_id)
);

-- Insert some sample data for testing
-- Note: Passwords should be properly hashed in a real application.
-- For this example, we'll use a placeholder hash. 'password123' might be the plaintext.
INSERT INTO users (username, password_hash, role) VALUES
('admin', 'hashed_admin_password', 'Admin'),
('officer_jane', 'hashed_officer_password', 'Officer');

INSERT INTO criminals (name, crime_history, status) VALUES
('John Doe', 'Previous phishing attempt in 2022.', 'Arrested');

INSERT INTO complaints (victim_name, crime_type, complaint_date, description, status, assigned_officer_id, criminal_id) VALUES
('Alice Smith', 'Phishing', '2025-07-15', 'Received a suspicious email asking for bank details.', 'Closed', 2, 1),
('Bob Johnson', 'Hacking', '2025-07-16', 'Unauthorized access to personal computer.', 'Under Investigation', 2, NULL);

INSERT INTO evidence (complaint_id, evidence_type, details) VALUES
(1, 'Email ID', 'scammer@fakebank.com'),
(2, 'IP Address', '192.168.1.101');
