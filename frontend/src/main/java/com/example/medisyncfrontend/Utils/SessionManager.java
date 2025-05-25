package com.example.medisyncfrontend.Utils;

/**
 * Holds the data of the user who is currently authenticated
 * on the frontend side.
 */
public final class SessionManager {

    private static String loggedInUsername;
    private static Integer loggedInUserId;

    private SessionManager() {
        // Prevent instantiation
    }

    public static void setLoggedInUser(String username) {
        SessionManager.loggedInUsername = username;
    }

    public static String getLoggedInUser() {
        return loggedInUsername;
    }

    public static void setLoggedInUserId(int id) {
        SessionManager.loggedInUserId = id;
    }

    public static Integer getLoggedInUserId() {
        return loggedInUserId;
    }

    public static void clearSession() {
        loggedInUsername = null;
        loggedInUserId = null;
    }


}
