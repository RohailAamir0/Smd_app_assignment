package com.example.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "cinefast_session_pref_v3";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_REMEMBER_ME = "rememberMe";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveSession(String userId, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public void setRememberMe(boolean rememberMe) {
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public boolean isRememberMe() {
        return prefs.getBoolean(KEY_REMEMBER_ME, false);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
