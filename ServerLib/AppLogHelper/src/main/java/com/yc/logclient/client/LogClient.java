package com.yc.logclient.client;

import android.content.Context;
import android.util.Log;

import com.yc.logclient.LogManager;
import com.yc.logclient.bean.AppLogBean;
import com.yc.logclient.bean.ThrowableBean;
import com.yc.logclient.inter.ILogInterface;
import com.yc.toolutils.AppLogUtils;
import java.util.ArrayList;

/**
 * 日志工具，负责将String形式的日志 组装成TMLogBean,然后分发给LogManager
 */
public class LogClient implements ILogInterface {

    public static final String TAG = LogClient.class.getSimpleName();
    private LogManager logManager;
    public static int mBaseIndex = 5;


    public void initLogEngine(Context context) {
        AppLogUtils.i("TmLog", "init");
        logManager = LogManager.getInstance(context);
    }

    @Override
    public void logu(int type, int level, String msg) {
        checkLogMangerInited();
        if (logManager != null) {
            logManager.logu(type, level, msg);
        }
    }

    @Override
    public void crash(int type, int leve, String msg, Throwable ex) {
        checkLogMangerInited();
        flushLog();
        try {
            ThrowableBean throwableBean = null;
            if (ex != null) {
                throwableBean = new ThrowableBean(logManager.getShortPackageName(), ex.getClass().getName(), ex.getMessage(), ex.getStackTrace());
            }
            ArrayList<AppLogBean> beans = new ArrayList<>();
            beans.add(new AppLogBean(type, leve, logManager.getShortPackageName(), "", msg, throwableBean));
            if (logManager != null) {
                logManager.logToServices(beans);
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    @Override
    public void anr(int type, int level, String msg) {
        checkLogMangerInited();
        flushLog();
        try {
            ArrayList<AppLogBean> beans = new ArrayList<>();
            beans.add(new AppLogBean(type, level, logManager.getShortPackageName(), "", msg, null));
            Log.d(TAG, "anr_ :" + type + ",msg:" + msg);
            if (logManager != null) {
                logManager.logToServices(beans);
            }
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    @Override
    public void flushLog() {
        checkLogMangerInited();
        if (logManager != null) {
            logManager.flush();
        }
    }

    @Override
    public void collectLogcat(String savepath, long delaytime, boolean clearlogcat) {
        checkLogMangerInited();
        if (logManager != null) {
            logManager.collectLogcat(savepath, delaytime, clearlogcat);
        }
    }

    @Override
    public void saveLogWithPath(String log, String path) {
        checkLogMangerInited();
        if (logManager != null) {
            logManager.saveLogWithPath(log, path);
        }
    }

    public void probeStackLevel() {
        checkLogMangerInited();
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = stackTrace.length - 1; i >= 0; i--) {
            Log.d("probeStackLevel", "index:" + i + ",className:" + stackTrace[i].getClassName() + ",method:" + stackTrace[i].getMethodName() + ",fileName:" + stackTrace[i].getFileName() + ",lineNumber:" + stackTrace[i].getLineNumber());
        }
    }


    /**
     * set方法
     **********************/

    public void enableTooLargeAutoDevide(boolean enable) {
        LogManager.enableTooLargeTransactDevide = enable;
        Log.d(TAG, "enableTooLargeAutoDevide:" + enable);
    }

    public void setAutoDivideRatio(float ratio) {
        LogManager.ratio = ratio;
        Log.d(TAG, "setAutoDivideRatio:" + ratio);
    }

    public void checkLogMangerInited() {
        if (logManager == null) {
            AppLogUtils.e(" ========= Logu.initLogEngine() must be called, before call Logu  =========");
            AppLogUtils.e(" ========= Logu.initLogEngine() must be called, before call Logu  =========");
            AppLogUtils.e(" ========= Logu.initLogEngine() must be called, before call Logu  =========");
            AppLogUtils.e(" ========= Logu.initLogEngine() must be called, before call Logu  =========");
        }
    }

}
