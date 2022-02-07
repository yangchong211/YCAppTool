package com.yc.toollib.crash;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yc.toollib.crash.compat.ActivityKillerV15_V20;
import com.yc.toollib.crash.compat.ActivityKillerV21_V23;
import com.yc.toollib.crash.compat.ActivityKillerV24_V25;
import com.yc.toollib.crash.compat.ActivityKillerV26;
import com.yc.toollib.crash.compat.ActivityKillerV28;
import com.yc.toollib.crash.compat.IActivityKiller;
import com.yc.toollib.tool.ToolLogUtils;

import java.lang.reflect.Field;

import me.weishu.reflection.Reflection;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/30
 *     desc  : 异常避免帮助类
 *     revise:
 * </pre>
 */
public final class CrashHelper {

    private static IActivityKiller sActivityKiller;
    private static ExceptionHandler sExceptionHandler;
    /**
     * 标记位，避免重复安装卸载
     */
    private static boolean sInstalled = false;
    private static boolean sIsSafeMode;
    /**
     * Cockroach实例
     */
    private static CrashHelper INSTANCE;

    private CrashHelper() {

    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (CrashHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CrashHelper();
                }
            }
        }
        return INSTANCE;
    }

    public void setExceptionHandler(ExceptionHandler sExceptionHandler) {
        CrashHelper.sExceptionHandler = sExceptionHandler;
    }

    public void install(Context ctx) {
        if (sInstalled) {
            return;
        }
        try {
            //解除 android P 反射限制
            Reflection.unseal(ctx);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        sInstalled = true;
        initActivityKiller();
    }

    /**
     * 替换ActivityThread.mH.mCallback，实现拦截Activity生命周期，直接忽略生命周期的异常的话会导致黑屏，目前
     * 会调用ActivityManager的finishActivity结束掉生命周期抛出异常的Activity
     */
    private void initActivityKiller() {
        //各版本android的ActivityManager获取方式，finishActivity的参数，token(binder对象)的获取不一样
        if (Build.VERSION.SDK_INT >= 28) {
            sActivityKiller = new ActivityKillerV28();
        } else if (Build.VERSION.SDK_INT >= 26) {
            sActivityKiller = new ActivityKillerV26();
        } else if (Build.VERSION.SDK_INT == 25 || Build.VERSION.SDK_INT == 24) {
            sActivityKiller = new ActivityKillerV24_V25();
        } else if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 23) {
            sActivityKiller = new ActivityKillerV21_V23();
        } else if (Build.VERSION.SDK_INT >= 15 && Build.VERSION.SDK_INT <= 20) {
            sActivityKiller = new ActivityKillerV15_V20();
        } else if (Build.VERSION.SDK_INT < 15) {
            sActivityKiller = new ActivityKillerV15_V20();
        }
        try {
            hookmH();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void hookmH() throws Exception {

        final int LAUNCH_ACTIVITY = 100;
        final int PAUSE_ACTIVITY = 101;
        final int PAUSE_ACTIVITY_FINISHING = 102;
        final int STOP_ACTIVITY_HIDE = 104;
        final int RESUME_ACTIVITY = 107;
        final int DESTROY_ACTIVITY = 109;
        final int NEW_INTENT = 112;
        final int RELAUNCH_ACTIVITY = 126;
        Class activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null);

        Field mhField = activityThreadClass.getDeclaredField("mH");
        mhField.setAccessible(true);
        final Handler mhHandler = (Handler) mhField.get(activityThread);
        Field callbackField = Handler.class.getDeclaredField("mCallback");
        callbackField.setAccessible(true);
        callbackField.set(mhHandler, new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (Build.VERSION.SDK_INT >= 28) {
                    //android P 生命周期全部走这
                    final int EXECUTE_TRANSACTION = 159;
                    if (msg.what == EXECUTE_TRANSACTION) {
                        try {
                            if (mhHandler != null) {
                                mhHandler.handleMessage(msg);
                            }
                        } catch (Throwable throwable) {
                            sActivityKiller.finishLaunchActivity(msg);
                            notifyException(throwable);
                        }
                        return true;
                    }
                    return false;
                }
                switch (msg.what) {
                    // startActivity--> activity.attach  activity.onCreate  r.activity!=null  activity.onStart  activity.onResume
                    case LAUNCH_ACTIVITY:
                        try {
                            if (mhHandler != null) {
                                mhHandler.handleMessage(msg);
                            }
                        } catch (Throwable throwable) {
                            sActivityKiller.finishLaunchActivity(msg);
                            notifyException(throwable);
                        }
                        return true;
                    case RESUME_ACTIVITY:
                        //回到activity onRestart onStart onResume
                        try {
                            if (mhHandler != null) {
                                mhHandler.handleMessage(msg);
                            }
                        } catch (Throwable throwable) {
                            sActivityKiller.finishResumeActivity(msg);
                            notifyException(throwable);
                        }
                        return true;
                    case PAUSE_ACTIVITY_FINISHING:
                        //按返回键 onPause
                        try {
                            mhHandler.handleMessage(msg);
                        } catch (Throwable throwable) {
                            sActivityKiller.finishPauseActivity(msg);
                            notifyException(throwable);
                        }
                        return true;
                    case PAUSE_ACTIVITY:
                        //开启新页面时，旧页面执行 activity.onPause
                        try {
                            if (mhHandler != null) {
                                mhHandler.handleMessage(msg);
                            }
                        } catch (Throwable throwable) {
                            sActivityKiller.finishPauseActivity(msg);
                            notifyException(throwable);
                        }
                        return true;
                    case STOP_ACTIVITY_HIDE:
                        //开启新页面时，旧页面执行 activity.onStop
                        try {
                            if (mhHandler != null) {
                                mhHandler.handleMessage(msg);
                            }
                        } catch (Throwable throwable) {
                            sActivityKiller.finishStopActivity(msg);
                            notifyException(throwable);
                        }
                        return true;
                    case DESTROY_ACTIVITY:
                        // 关闭activity onStop  onDestroy
                        try {
                            if (mhHandler != null) {
                                mhHandler.handleMessage(msg);
                            }
                        } catch (Throwable throwable) {
                            notifyException(throwable);
                        }
                        return true;
                }
                return false;
            }
        });
    }


    private void notifyException(Throwable throwable) {
        if (sExceptionHandler == null) {
            return;
        }
        if (isSafeMode()) {
            sExceptionHandler.bandageExceptionHappened(throwable);
        } else {
            sExceptionHandler.uncaughtExceptionHappened(Looper.getMainLooper().getThread(), throwable);
            safeMode();
        }
    }

    protected boolean isSafeMode() {
        return sIsSafeMode;
    }

    /**
     * 开启保护模式
     */
    protected void safeMode() {
        sIsSafeMode = true;
        if (sExceptionHandler != null) {
            sExceptionHandler.enterSafeMode();
        }
        //开启一个循环
        while (true) {
            try {
                Looper.loop();
            } catch (Throwable e) {
                isChoreographerException(e);
                if (sExceptionHandler != null) {
                    sExceptionHandler.bandageExceptionHappened(e);
                }
            }
        }
    }

    /**
     * view measure layout draw时抛出异常会导致Choreographer挂掉
     * 建议直接杀死app。以后的版本会只关闭黑屏的Activity
     * @param e                             e
     */
    protected void isChoreographerException(Throwable e) {
        if (e == null || sExceptionHandler == null) {
            return;
        }
        StackTraceElement[] elements = e.getStackTrace();
        if (elements == null) {
            return;
        }
        for (int i = elements.length - 1; i > -1; i--) {
            if (elements.length - i > 20) {
                return;
            }
            StackTraceElement element = elements[i];
            if ("android.view.Choreographer".equals(element.getClassName())
                    && "Choreographer.java".equals(element.getFileName())
                    && "doFrame".equals(element.getMethodName())) {
                sExceptionHandler.mayBeBlackScreen(e);
                return;
            }
        }
    }

    protected void setSafe(Thread thread, Throwable ex) {
        ToolLogUtils.w(CrashHandler.TAG, "setSafe--- thread-----"+thread.getName());
        //判断是否是同一个线程
        if (thread == Looper.getMainLooper().getThread()) {
            CrashHelper.getInstance().isChoreographerException(ex);
            CrashHelper.getInstance().safeMode();
            ToolLogUtils.w(CrashHandler.TAG, "setSafe--- safeMode-----");
        }
    }

}
