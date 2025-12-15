package com.ccrms.util;

/**
 * A utility class for handling password hashing and verification.
 *
 * IMPORTANT: This implementation is a simple placeholder for demonstration
 * purposes and is NOT SECURE. In a production system, you must use a strong,
 * standardized hashing algorithm like BCrypt or Argon2.
 *
 * Example with BCrypt library:
 * // To hash a password:
 * String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
 *
 * // To check a password:
 * boolean matches = BCrypt.checkpw(candidate, hashed);
 */
public class PasswordUtil {

    /**
     * "Hashes" a password by prefixing it. THIS IS NOT SECURE.
     * The sample data in schema.sql uses this format. For example, for the
     * password 'admin_password', the stored hash is 'hashed_admin_password'.
     *
     * @param password The plain-text password.
     * @return A non-secure, placeholder "hash".
     */
    public static String hashPassword(String password) {
        // This is a placeholder for a real hashing function.
        return "hashed_" + password;
    }

    /**
     * Checks if a plain-text password matches the insecure "hashed" version.
     *
     * @param plainPassword The password entered by the user.
     * @param storedHash The "hash" retrieved from the database.
     * @return true if the password is correct, false otherwise.
     */
    public static boolean checkPassword(String plainPassword, String storedHash) {
        // This is a placeholder for a real hash-checking function.
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        // Re-create the "hash" from the plain password and compare.
        return storedHash.equals(hashPassword(plainPassword));
    }
}
