package com.example.guidelink;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_GUIDE_ID = "guideId";
    private static final String KEY_USERNAME = "username";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Create user login session
    public void createLoginSession(int userId, String username) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.apply(); // Save changes
    }

    public void createAcceptSession(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply(); // Save changes
    }

    // Create guide session
    public void createGuideSession(int guideId) {
        editor.putInt(KEY_GUIDE_ID, guideId);
        editor.apply(); // Save changes
    }

    // Check if user session exists
    public boolean isUserLoggedIn() {
        return sharedPreferences.contains(KEY_USER_ID);
    }

    // Check if guide session exists
    public boolean isGuideLoggedIn() {
        return sharedPreferences.contains(KEY_GUIDE_ID);
    }

    // Get user ID
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1); // Default to -1 if not set
    }

    // Get guide ID
    public int getGuideId() {
        return sharedPreferences.getInt(KEY_GUIDE_ID, -1); // Default to -1 if not set
    }

    // Get username
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null); // Default to null if not set
    }

    // Clear user session
    public void clearUserSession() {
        editor.remove(KEY_USER_ID);
        editor.remove(KEY_USERNAME);
        editor.apply();
    }

    // Clear guide session
    public void clearGuideSession() {
        editor.remove(KEY_GUIDE_ID);
        editor.apply();
    }

    // Clear all sessions (user and guide)
    public void clearAllSessions() {
        editor.clear();
        editor.apply();
    }
}
