package com.example.concertsystem.util;

import java.security.SecureRandom;

public class HelperUtil{
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String convertToLowerCase(String input) {
        if (input == null) {
            return null;
        }
        return input.toLowerCase();
    }
    public static String generateVerificationCode(int length) {
        StringBuilder builder = new StringBuilder();
        SecureRandom random = new SecureRandom();

        // Generate random characters from the ALPHA_NUMERIC_STRING
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALPHA_NUMERIC_STRING.length());
            char randomChar = ALPHA_NUMERIC_STRING.charAt(randomIndex);
            builder.append(randomChar);
        }

        return builder.toString();
    }

}
