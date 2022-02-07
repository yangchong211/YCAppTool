package com.yc.toollib.crash;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Printer;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2019/9/2
 *     desc  : 使用Looper拦截崩溃，anr等问题
 *     revise:
 * </pre>
 */
@Deprecated
public class CrashTestDemo {

    private static long startWorkTimeMillis = 0L;
    public static void test(){
        Looper.getMainLooper().setMessageLogging(new Printer() {
            @Override
            public void println(String it) {
                if (it.startsWith(">>>>> Dispatching to Handler")) {
                    startWorkTimeMillis = System.currentTimeMillis();
                } else if (it.startsWith("<<<<< Finished to Handler")) {
                    long duration = System.currentTimeMillis() - startWorkTimeMillis;
                    if (duration > 100) {
                        Log.e("Application---主线程执行耗时过长","$duration 毫秒，$it");
                    }
                }
            }
        });
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Looper.loop();
                    } catch (Throwable e){
                        if (e.getMessage()!=null && e.getMessage().startsWith("Unable to start activity")){
                            android.os.Process.killProcess(android.os.Process.myPid());
                            break;
                        }
                        e.printStackTrace();
                        Log.e("Application---Looper---",e.getMessage());
                    }
                }
            }
        });
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                Log.e("Application-----","uncaughtException---异步线程崩溃，自行上报崩溃信息");
            }
        });
    }


}
