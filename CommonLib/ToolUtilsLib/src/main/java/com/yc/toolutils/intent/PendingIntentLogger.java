package com.yc.toolutils.intent;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author yangchong
 * GitHub : <a href="https://github.com/yangchong211/YCCommonLib">...</a>
 * email : yangchong211@163.com
 * time  : 2018/11/9
 * desc  : 延迟intent意图打印工具
 * revise: 之前搜车封装库
 */
public final class PendingIntentLogger {

    private static final HashMap<Integer, String> FLAGS = new HashMap<>();

    static {
        FLAGS.put(PendingIntent.FLAG_CANCEL_CURRENT, "FLAG_CANCEL_CURRENT");
        FLAGS.put(PendingIntent.FLAG_NO_CREATE, "FLAG_NO_CREATE");
        FLAGS.put(PendingIntent.FLAG_ONE_SHOT, "FLAG_ONE_SHOT");
        FLAGS.put(PendingIntent.FLAG_UPDATE_CURRENT, "FLAG_UPDATE_CURRENT");
    }

    private PendingIntentLogger() {
        throw new AssertionError();
    }

    public static void print(String tag, PendingIntent intent) {
        if (intent == null) {
            Log.v(tag, "no pending intent found");
            return;
        }
        Log.v(tag, "PendingIntent[@" + Integer.toHexString(intent.hashCode()) + "] content:");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            dumpJellyBeanMr1(tag, intent);
        } else {
            dumpDefault(tag, intent);
        }
    }

    public static void printContentIntent(String tag, PendingIntent intent) {
        try {
            Method getIntent = intent.getClass().getDeclaredMethod("getIntent");
            getIntent.setAccessible(true);
            Intent content = (Intent) getIntent.invoke(intent);
            IntentLogger.print(tag, content);
        } catch (NoSuchMethodException e) {
            Log.e(tag, "", e);
        } catch (InvocationTargetException e) {
            Log.e(tag, "", e);
        } catch (IllegalAccessException e) {
            Log.e(tag, "", e);
        }
    }

    public static void printTag(String tag, PendingIntent intent) {
        try {
            Method getIntent = intent.getClass().getDeclaredMethod("getTag", String.class);
            getIntent.setAccessible(true);
            String content = (String) getIntent.invoke(intent, "");
            Log.v(tag, "Descriptive tag: " + content);
        } catch (NoSuchMethodException e) {
            Log.e(tag, "", e);
        } catch (InvocationTargetException e) {
            Log.e(tag, "", e);
        } catch (IllegalAccessException e) {
            Log.e(tag, "", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void dumpJellyBeanMr1(String tag, PendingIntent intent) {
        Log.v(tag, "Who has created: " + intent.getCreatorPackage() + ", uid=[" + intent.getCreatorUid() + "]");
        printTag(tag, intent);
        printContentIntent(tag, intent);
    }

    private static void dumpDefault(String tag, PendingIntent intent) {
        Log.v(tag, "Who has created: " + intent.getTargetPackage());
        printTag(tag, intent);
        printContentIntent(tag, intent);
    }
}
