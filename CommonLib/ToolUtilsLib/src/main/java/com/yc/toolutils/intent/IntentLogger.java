package com.yc.toolutils.intent;

import android.content.ComponentName;
import android.content.Intent;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;


/**
 * @author yangchong
 * GitHub : <a href="https://github.com/yangchong211/YCCommonLib">...</a>
 * email : yangchong211@163.com
 * time  : 2018/11/9
 * desc  : intent意图打印工具
 * revise: 之前搜车封装库
 */
public final class IntentLogger {

    private static final HashMap<Integer, String> FLAGS = new HashMap<>();

    static {
        FLAGS.put(Intent.FLAG_ACTIVITY_CLEAR_TASK, "FLAG_ACTIVITY_CLEAR_TASK");
        FLAGS.put(Intent.FLAG_ACTIVITY_SINGLE_TOP, "FLAG_ACTIVITY_SINGLE_TOP");
        FLAGS.put(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT, "FLAG_ACTIVITY_BROUGHT_TO_FRONT");
        FLAGS.put(Intent.FLAG_ACTIVITY_CLEAR_TOP, "FLAG_ACTIVITY_CLEAR_TOP");
        FLAGS.put(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS, "FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS");
        FLAGS.put(Intent.FLAG_ACTIVITY_FORWARD_RESULT, "FLAG_ACTIVITY_FORWARD_RESULT");
        FLAGS.put(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY, "FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY");
        FLAGS.put(Intent.FLAG_ACTIVITY_MULTIPLE_TASK, "FLAG_ACTIVITY_MULTIPLE_TASK");
        FLAGS.put(Intent.FLAG_ACTIVITY_NEW_DOCUMENT, "FLAG_ACTIVITY_NEW_DOCUMENT");
        FLAGS.put(Intent.FLAG_ACTIVITY_NEW_TASK, "FLAG_ACTIVITY_NEW_TASK");
        FLAGS.put(Intent.FLAG_ACTIVITY_NO_ANIMATION, "FLAG_ACTIVITY_NO_ANIMATION");
        FLAGS.put(Intent.FLAG_ACTIVITY_NO_HISTORY, "FLAG_ACTIVITY_NO_HISTORY");
        FLAGS.put(Intent.FLAG_ACTIVITY_NO_USER_ACTION, "FLAG_ACTIVITY_NO_USER_ACTION");
        FLAGS.put(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP, "FLAG_ACTIVITY_PREVIOUS_IS_TOP");
        FLAGS.put(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT, "FLAG_ACTIVITY_REORDER_TO_FRONT");
        FLAGS.put(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED, "FLAG_ACTIVITY_RESET_TASK_IF_NEEDED");
        FLAGS.put(Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS, "FLAG_ACTIVITY_RETAIN_IN_RECENTS");
        FLAGS.put(Intent.FLAG_ACTIVITY_TASK_ON_HOME, "FLAG_ACTIVITY_TASK_ON_HOME");
        FLAGS.put(Intent.FLAG_DEBUG_LOG_RESOLUTION, "FLAG_DEBUG_LOG_RESOLUTION");
        FLAGS.put(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES, "FLAG_EXCLUDE_STOPPED_PACKAGES");
        FLAGS.put(Intent.FLAG_FROM_BACKGROUND, "FLAG_FROM_BACKGROUND");
        FLAGS.put(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION, "FLAG_GRANT_PERSISTABLE_URI_PERMISSION");
        FLAGS.put(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION, "FLAG_GRANT_PREFIX_URI_PERMISSION");
        FLAGS.put(Intent.FLAG_GRANT_READ_URI_PERMISSION, "FLAG_GRANT_READ_URI_PERMISSION");
        FLAGS.put(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, "FLAG_GRANT_WRITE_URI_PERMISSION");
        FLAGS.put(Intent.FLAG_INCLUDE_STOPPED_PACKAGES, "FLAG_INCLUDE_STOPPED_PACKAGES");
        FLAGS.put(Intent.FLAG_RECEIVER_FOREGROUND, "FLAG_RECEIVER_FOREGROUND");
        FLAGS.put(Intent.FLAG_RECEIVER_NO_ABORT, "FLAG_RECEIVER_NO_ABORT");
        FLAGS.put(Intent.FLAG_RECEIVER_REGISTERED_ONLY, "FLAG_RECEIVER_REGISTERED_ONLY");
        FLAGS.put(Intent.FLAG_RECEIVER_REPLACE_PENDING, "FLAG_RECEIVER_REPLACE_PENDING");
        FLAGS.put(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, "FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET");
    }

    private IntentLogger() {
    }

    public static void print(String tag, Intent intent) {
        if (intent == null) {
            Log.v(tag, "no intent found");
            return;
        }
        Bundle extras = intent.getExtras();
        Log.v(tag, "Intent[@" + Integer.toHexString(intent.hashCode()) + "] content:");
        Log.v(tag, "Action   : " + intent.getAction());
        Log.v(tag, "Category : " + intent.getCategories());
        Log.v(tag, "Data     : " + intent.getDataString());
        printComponentName(tag, intent.getComponent());
        printFlags(tag, intent.getFlags());
        Log.v(tag, "HasExtras: " + hasExtras(extras));
        printExtras(tag, extras);
    }

    public static void printComponentName(String tag, Intent intent) {
        if (intent == null) {
            return;
        }
        printComponentName(tag, intent.getComponent());
    }

    public static void printComponentName(String tag, ComponentName componentName) {
        if (componentName != null) {
            Log.v(tag, "Component: " + componentName.getPackageName()
                    + "/" + componentName.getClassName());
        } else {
            Log.v(tag, "Component: null");
        }
    }

    public static void printFlags(String tag, Intent intent) {
        if (intent == null) {
            return;
        }
        printFlags(tag, intent.getFlags());
    }

    public static void printFlags(String tag, int flags) {
        Log.v(tag, "Flags    : " + Integer.toBinaryString(flags));
        for (int flag : FLAGS.keySet()) {
            if ((flag & flags) != 0) {
                Log.v(tag, "Flag     : " + FLAGS.get(flag));
            }
        }
    }

    public static void printExtras(String tag, Intent intent) {
        if (intent == null) {
            return;
        }
        printExtras(tag, intent.getExtras());
    }

    public static void printExtras(String tag, Bundle extras) {
        if (hasExtras(extras)) {
            return;
        }
        for (String key : extras.keySet()) {
            Object value = extras.get(key);
            if (value instanceof Bundle) {
                printExtras(tag, (Bundle) value);
            } else {
                try {
                    Log.v(tag, "Extra[" + key + "] :" + String.valueOf(extras.get(key)));
                } catch (BadParcelableException e) {
                    Log.w(tag, "Extra contains unknown class instance for [" + key + "]: ", e);
                }
            }
        }
    }

    public static boolean hasExtras(Intent intent) {
        return hasExtras(intent.getExtras());
    }

    public static boolean hasExtras(Bundle extras) {
        try {
            return (extras != null && !extras.isEmpty());
        } catch (BadParcelableException e) {
            Log.w("IntentLogger", "Extra contains unknown class instance: ", e);
            return true;
        }
    }
}
