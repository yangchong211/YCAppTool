package com.yc.notifymessage;

import android.util.Log;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 日志工具类
 *     revise:
 * </pre>
 */
public final class NotifyLoggerUtils {

    private static Logger sLogger = null;

    public static void log(String content) {
        if (sLogger == null) {
            return;
        }
        log("",content);
    }

    public static void log(Object object, String content) {
        if (sLogger == null) {
            return;
        }
        String instance = "";
        if (object != null) {
            instance = object.toString();
        }
        String fullLog = instance + " " + content;
        sLogger.log(fullLog);
        if (sLogger.debugLogEnable()) {
            Log.i("LoggerUtils", fullLog);
        }
    }

    public static void setLogger(Logger logger) {
        sLogger = logger;
    }

    public static Logger getLogger() {
        return sLogger;
    }

    public interface Logger {
        /**
         * 在回调中打日志。这个地方的日志，暴露给开发者调用
         * @param content                   日志
         */
        void log(String content);

        /**
         * 开启后容器内部会通过 Log.i 打日志
         * 建议初期在线上开启，方便出统计上相关信息
         * @return                          true表示开启
         */
        boolean debugLogEnable();
    }
}
