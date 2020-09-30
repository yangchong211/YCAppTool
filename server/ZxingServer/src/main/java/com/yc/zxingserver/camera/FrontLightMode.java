package com.yc.zxingserver.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yc.zxingserver.scan.Preferences;

/**
 * Enumerates settings of the preference controlling the front light.
 */
public enum FrontLightMode {

    /** Always on. */
    ON,
    /** On only when ambient light is low. */
    AUTO,
    /** Always off. */
    OFF;

    private static FrontLightMode parse(String modeString) {
        return modeString == null ? AUTO : valueOf(modeString);
    }

    public static FrontLightMode readPref(SharedPreferences sharedPrefs) {
        return parse(sharedPrefs.getString(Preferences.KEY_FRONT_LIGHT_MODE, AUTO.toString()));
    }

    public static void put(Context context, FrontLightMode mode) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(Preferences.KEY_FRONT_LIGHT_MODE, mode.toString()).commit();
    }

}