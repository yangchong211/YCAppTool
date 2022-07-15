package com.yc.monitorapplib.util;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {

    public static final String PREF_SETTINGS_HIDE_SYSTEM_APPS = "hide_system_apps";
    public static final String PREF_SETTINGS_HIDE_UNINSTALL_APPS = "hide_uninstall_apps";
    public static final String PREF_LIST_SORT = "sort_list";
    public static final String FCM_ID = "fcm_id";
    private static final String PREF_NAME = "preference_application";

    private static PreferenceManager mManager;
    private static SharedPreferences mShare;

    private PreferenceManager() {
    }

    public static void init(Context context) {
        mManager = new PreferenceManager();
        mShare = context.getApplicationContext().getSharedPreferences(PREF_NAME, 0);
    }

    public static PreferenceManager getInstance() {
        return mManager;
    }

    public void putBoolean(String key, boolean value) {
        mShare.edit().putBoolean(key, value).apply();
    }

    public void putInt(String key, int value) {
        mShare.edit().putInt(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return mShare.getBoolean(key, false);
    }

    public int getInt(String key) {
        return mShare.getInt(key, 0);
    }

    public boolean getUninstallSettings(String key) {
        return mShare.getBoolean(key, true);
    }

    public boolean getSystemSettings(String key) {
        return mShare.getBoolean(key, true);
    }

    public void putString(String key, String value) {
        mShare.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return mShare.getString(key, "");
    }
}
