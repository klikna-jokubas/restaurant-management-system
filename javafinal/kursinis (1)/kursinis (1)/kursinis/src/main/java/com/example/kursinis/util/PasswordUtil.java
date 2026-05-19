package com.example.kursinis.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainText, String hashed) {
        if (hashed == null) return false;
        return BCrypt.checkpw(plainText, hashed);
    }

    public static boolean isHashed(String value) {
        return value != null && value.startsWith("$2a$");
    }
}
