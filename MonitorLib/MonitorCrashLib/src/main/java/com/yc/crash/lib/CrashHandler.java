package com.yc.crash.lib;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.tencent.mmkv.MMKV;
import com.yc.activitymanager.ActivityManager;
import com.yc.appencryptlib.Md5EncryptUtils;
import com.yc.eventuploadlib.ExceptionReporter;
import com.yc.toolutils.AppLogUtils;

import java.util.Arrays;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 异常处理类
 *     revise:
 * </pre>
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 存储崩溃次数的文件
     */
    private static final String CRASH_HANDLER = "CrashHandler";
    public static final String TAG = "CrashHandler : ";
    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /**
     * 程序的Context对象
     */
    private Context mContext;
    /**
     * CrashHandler实例
     */
    private static volatile CrashHandler INSTANCE;
    /**
     * 监听
     */
    private CrashListener listener;
    /**
     * 是否写崩溃日志到file文件夹，默认开启
     */
    private boolean isWriteLog = true;
    /**
     * 崩溃日志的md5
     */
    private String md5Key;
    /**
     * mmkv，很牛逼的缓存数据方式
     * 注意不要使用sp存储，sp存储在崩溃时会出现数据保存异常问题，因此改为mmkv
     */
    private MMKV mmkv;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (CrashHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashHandler();
                }
            }
        }
        return INSTANCE;
    }

    public CrashHandler setWriteLog(boolean writeLog) {
        isWriteLog = writeLog;
        return this;
    }

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx 上下文
     */
    public void init(Application ctx, CrashListener listener) {
        mContext = ctx;
        this.listener = listener;
        try {
            String initialize = MMKV.initialize(mContext);
            AppLogUtils.d(TAG, "init mmkv : " + initialize);
            mmkv = MMKV.mmkvWithID(CRASH_HANDLER);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionReporter.report(TAG, e);
        }
        //获取系统默认的UncaughtExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (mDefaultHandler != null) {
            AppLogUtils.d(TAG, "init getDefaultUncaughtExceptionHandler : "
                    + mDefaultHandler.getClass().getSimpleName());
        } else {
            AppLogUtils.d(TAG, "init getDefaultUncaughtExceptionHandler : null");
        }
        //打印：init mDefaultHandler : com.android.internal.os.RuntimeInit$KillApplicationHandler@7b5887e
        //将当前实例设为系统默认的异常处理器
        //设置一个处理者当一个线程突然因为一个未捕获的异常而终止时将自动被调用。
        //未捕获的异常处理的控制第一个被当前线程处理，如果该线程没有捕获并处理该异常，其将被线程的ThreadGroup对象处理，最后被默认的未捕获异常处理器处理。
        Thread.setDefaultUncaughtExceptionHandler(this);
        AppLogUtils.d(TAG, "init setDefaultUncaughtExceptionHandler this : "
                + Thread.getDefaultUncaughtExceptionHandler().getClass().getSimpleName());
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     * 该方法来实现对运行时线程进行异常处理
     */
    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
        uncaughtException1(thread, ex);
        //uncaughtException2(thread,ex);
    }

    private void uncaughtException2(Thread thread, Throwable ex) {
        Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (exceptionHandler instanceof CrashHandler) {
            if (ex.getStackTrace().length != 0) {
                md5Key = Md5EncryptUtils.encryptMD5ToString(Arrays.toString(ex.getStackTrace()));
                AppLogUtils.d(TAG, ex.getMessage() + "  md5 key : " + md5Key);
            }
            handleException(ex);
            initCustomBug(ex);
            AppLogUtils.d(TAG, "uncaughtException kill app");

            //`setDefaultUncaughtExceptionHandler`被调用多次如何理解
            //如果被多次调用的话，会以最后一次传递的 handler 为准。
            //所以如果用了第三方的统计模块，可能会出现失灵的情况。目前解决方案如下：
            //对于这种情况，在设置默认 `handler` 之前
            //可以先通过 `getDefaultUncaughtExceptionHandler()` 方法获取并保留旧的`handler`，
            //然后在默认`handler`的`uncaughtException`方法中调用其他`handler`的`uncaughtException`方法，保证都会收到异常信息。
            if (mDefaultHandler != null) {
                mDefaultHandler.uncaughtException(thread, ex);
            }
            // 退出程序
            ActivityManager.getInstance().appExist();
        }
    }

    private void uncaughtException1(Thread thread, Throwable ex) {
        if (ex.getStackTrace().length != 0) {
            md5Key = Md5EncryptUtils.encryptMD5ToString(Arrays.toString(ex.getStackTrace()));
            AppLogUtils.d(ex.getMessage() + "  md5 key : " + md5Key);
        }
        boolean isHandleException = handleException(ex);
        initCustomBug(ex);
        if (mDefaultHandler != null && isHandleException) {
            //`setDefaultUncaughtExceptionHandler`被调用多次如何理解
            //如果被多次调用的话，会以最后一次传递的 handler 为准。
            //所以如果用了第三方的统计模块，可能会出现失灵的情况。目前解决方案如下：
            //对于这种情况，在设置默认 `handler` 之前
            //可以先通过 `getDefaultUncaughtExceptionHandler()` 方法获取并保留旧的`handler`，
            //然后在默认`handler`的`uncaughtException`方法中调用其他`handler`的`uncaughtException`方法，保证都会收到异常信息。

            //收集完信息后，交给系统自己处理崩溃，或者交给外部其他地方调用setDefaultUncaughtExceptionHandler处理
            AppLogUtils.d(TAG, "uncaughtException do something");
            mDefaultHandler.uncaughtException(thread, ex);
        }
        AppLogUtils.d(TAG, "uncaughtException kill app");
        // 退出程序
        ActivityManager.getInstance().appExist();
    }

    /**
     * 初始化百度
     *
     * @param ex
     */
    private void initCustomBug(Throwable ex) {
        //自定义上传crash，支持开发者上传自己捕获的crash数据
        if (listener != null) {
            //捕获监听中异常，防止外部开发者使用方代码抛出异常时导致的反复调用
            try {
                if (md5Key != null && md5Key.length() > 0 && mmkv != null) {
                    int count = mmkv.getInt(md5Key, 0);
                    AppLogUtils.d("md5 key : " + md5Key + " 回调崩溃 : " + count + "次");
                    listener.recordException(ex, count);
                } else {
                    listener.recordException(ex, 1);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex 异常
     * @return true:如果处理了该异常信息；否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            AppLogUtils.w(TAG, "handleException--- ex==null");
            return false;
        }
        //收集crash信息
        String msg = ex.getLocalizedMessage();
        if (msg == null) {
            return false;
        }
        boolean success;
        try {
            //记录崩溃的次数
            if (md5Key != null && md5Key.length() > 0 && mmkv != null) {
                int count = mmkv.getInt(md5Key, 0);
                mmkv.putInt(md5Key, count + 1);
                AppLogUtils.d("md5 key : " + md5Key + " 记录崩溃 : " + (count + 1) + "次");
                CrashHelperUtils.setHeadContent("崩溃" + (count + 1) + "次");
            }
            AppLogUtils.w(TAG, "handleException--- ex-----" + msg);
            ex.printStackTrace();
            //收集设备信息
            //保存错误报告文件
            if (isWriteLog) {
                CrashHelperUtils.saveCrashInfoInFile(mContext, ex);
            }
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

}
