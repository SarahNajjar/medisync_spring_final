package com.example.medisyncfrontend.Utils;

/**
 * Holds the data of the user who is currently authenticated
 * on the frontend side.
 */
public final class SessionManager {

    // ---------- stored values ----------
    private static String  loggedInUsername;
    private static Integer loggedInUserId;   // <-- new

    private SessionManager() { }             // utility-class: no instances

    // ---------- username ----------
    public static void setLoggedInUser(String username) {
        SessionManager.loggedInUsername = username;
    }

    public static String getLoggedInUser() {
        return loggedInUsername;
    }

    // ---------- user-id (new) ----------
    public static void setLoggedInUserId(int id) {
        SessionManager.loggedInUserId = id;
    }

    public static Integer getLoggedInUserId() {
        return loggedInUserId;
    }

    // ---------- housekeeping ----------
    public static void clearSession() {
        loggedInUsername = null;
        loggedInUserId   = null;
    }
}
