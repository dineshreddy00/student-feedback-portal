package com.istudio.app;

public class FeedbackValidator {

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static boolean isValidMessage(String message) {
        return message != null && !message.trim().isEmpty();
    }
}
