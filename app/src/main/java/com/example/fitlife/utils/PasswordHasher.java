package com.example.fitlife.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * @param plainTextPassword The password to hash.
     * @return A salted and hashed password string.
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    /**
     * Checks if a plain-text password matches a hashed password.
     *
     * @param plainTextPassword The password to check.
     * @param hashedPassword    The hashed password from the database.
     * @return True if the passwords match, false otherwise.
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
