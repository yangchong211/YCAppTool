package com.yc.toollib.crash;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import com.yc.toollib.tool.ToolLogUtils;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 异常处理类
 *     revise:
 * </pre>
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
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
    private static CrashHandler INSTANCE;
    /**
     * 监听
     */
    private CrashListener listener;
    /**
     * 是否写崩溃日志到file文件夹，默认开启
     */
    private boolean isWriteLog = true;
    /**
     * 点击按钮异常后设置处理崩溃而是关闭当前activity
     */
    private boolean isFinishActivity = false;


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

    public void setWriteLog(boolean writeLog) {
        isWriteLog = writeLog;
    }

    public void setFinishActivity(boolean finishActivity) {
        isFinishActivity = finishActivity;
    }

    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx                       上下文
     */
    public void init(Application ctx , CrashListener listener) {
        LifecycleCallback.getInstance().init(ctx);
        if (isFinishActivity){
            CrashHelper.getInstance().install(ctx);
        }
        mContext = ctx;
        this.listener = listener;
        //获取系统默认的UncaughtExceptionHandler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        ToolLogUtils.d(TAG, "init mDefaultHandler : " + mDefaultHandler);
        //打印：init mDefaultHandler : com.android.internal.os.RuntimeInit$KillApplicationHandler@7b5887e
        //将当前实例设为系统默认的异常处理器
        //设置一个处理者当一个线程突然因为一个未捕获的异常而终止时将自动被调用。
        //未捕获的异常处理的控制第一个被当前线程处理，如果该线程没有捕获并处理该异常，其将被线程的ThreadGroup对象处理，最后被默认的未捕获异常处理器处理。
        Thread.setDefaultUncaughtExceptionHandler(this);
        ToolLogUtils.d(TAG, "init mDefaultHandler : " + Thread.getDefaultUncaughtExceptionHandler());
        //打印：init mDefaultHandler : com.yc.toollib.crash.CrashHandler@755b1df
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     * 该方法来实现对运行时线程进行异常处理
     */
    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
        boolean isHandle = handleException(ex);
        ToolLogUtils.d(TAG, "uncaughtException--- handleException----"+isHandle);
        initCustomBug(ex);
        if (mDefaultHandler != null && !isHandle) {
            //收集完信息后，交给系统自己处理崩溃
            //uncaughtException (Thread t, Throwable e) 是一个抽象方法
            //当给定的线程因为发生了未捕获的异常而导致终止时将通过该方法将线程对象和异常对象传递进来。
            ToolLogUtils.d(TAG, "uncaughtException--- ex----");
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            //否则自己处理
            if (mContext instanceof Application){
                ToolLogUtils.w(TAG, "handleException--- ex----重启activity-");
                if (listener!=null){
                    listener.againStartApp();
                }
            }
        }
        if (isFinishActivity){
            CrashHelper.getInstance().setSafe(thread,ex);
        }
    }

    /**
     * 初始化百度
     * @param ex
     */
    private void initCustomBug(Throwable ex) {
        //自定义上传crash，支持开发者上传自己捕获的crash数据
        //StatService.recordException(mContext, ex);
        if (listener!=null){
            //捕获监听中异常，防止外部开发者使用方代码抛出异常时导致的反复调用
            try {
                listener.recordException(ex);
            } catch (Throwable e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义错误处理,收集错误信息
     * 发送错误报告等操作均在此完成.
     * 开发者可以根据自己的情况来自定义异常处理逻辑
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            ToolLogUtils.w(TAG, "handleException--- ex==null");
            return false;
        }
        //收集crash信息
        String msg = ex.getLocalizedMessage();
        if (msg == null) {
            return false;
        }
        ToolLogUtils.w(TAG, "handleException--- ex-----"+msg);
        ex.printStackTrace();
        //收集设备信息
        //保存错误报告文件
        if (isWriteLog){
            CrashFileUtils.saveCrashInfoInFile(mContext,ex);
        }
        return true;
    }


}
