package com.yc.logupload.config;

import android.app.Application;
import android.content.Context;

import com.yc.logupload.report.email.EmailConfig;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 初始化配置
 *     revise :
 * </pre>
 */
public final class UploadInitHelper {

    private static boolean sInitial;
    private static UploadConfig sLoggerConfig;
    private static EmailConfig sEmailConfig;

    public synchronized static void init(Context context, UploadConfig config) {
        if (sInitial) {
            return;
        }
        sInitial = true;
        sLoggerConfig = config;
        Context appContext = context instanceof Application ?
                context : context.getApplicationContext();
    }

    public static boolean isInitial() {
        return sInitial;
    }

    public static void setUploadConfig(UploadConfig config){
        sLoggerConfig = config;
    }

    public static UploadConfig getConfig() {
        return sLoggerConfig;
    }

    public static EmailConfig getEmailConfig() {
        return sEmailConfig;
    }

    public static void setEmailConfig(EmailConfig sEmailConfig) {
        UploadInitHelper.sEmailConfig = sEmailConfig;
    }
}
