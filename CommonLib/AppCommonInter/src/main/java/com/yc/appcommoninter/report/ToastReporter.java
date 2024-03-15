package com.yc.appcommoninter.report;

import androidx.annotation.NonNull;

/**
 * @author yangchong
 * blog  : yangchong211@163.com
 * time  : 2022/5/23
 * desc  : 吐司帮助类
 * revise:
 */
public abstract class ToastReporter {

    protected abstract void reportToast(String toast);

    private static ToastReporter sToastReporter;

    public static void setToastReporter(@NonNull ToastReporter toastReporter) {
        sToastReporter = toastReporter;
    }

    public static void report(String toast) {
        if (sToastReporter != null) {
            sToastReporter.reportToast(toast);
        }
    }

}

