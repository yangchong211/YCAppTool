package com.yc.toolutils.activity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.yc.toolutils.AppToolUtils;

public final class AppContentUtils {


    /**
     * 通过上下文获取到activity，使用到了递归
     *
     * @param context       上下文
     * @return              对象的活动对象，如果它不是活动对象，则为空。
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * Get AppCompatActivity from context
     * @param context           上下文
     * @return AppCompatActivity if it's not null
     */
    private static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 获取资源字符串
     *
     * @param id      资源id
     * @return
     */
    public String getString(int id) {
        Context context = AppToolUtils.getApp();
        return context == null ? "" : context.getResources().getString(id);
    }

    /**
     * 获取资源字符串
     *
     * @param context 上下文
     * @param id      资源id
     * @return
     */
    private String getString(Context context, int id) {
        return context.getResources().getString(id);
    }

}
