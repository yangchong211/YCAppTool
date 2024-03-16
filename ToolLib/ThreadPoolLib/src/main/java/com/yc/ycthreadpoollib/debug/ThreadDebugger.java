

package com.yc.ycthreadpoollib.debug;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public final class ThreadDebugger {

    public static boolean NEED_PRINT_COST = false;

    /**
     * Install the thread debugger with the command thread keys.
     */
    public static IThreadDebugger install() {
        return install(ThreadDebuggers.createWithCommonThreadKey());
    }

    /**
     * Install the thread debugger with the check duration 2000 millisecond.
     *
     * @param debugger the customize debugger. You can create this through {@link ThreadDebuggers}.
     * @param callback the thread changed callback, this callback will be triggered when there are
     *                 some threads has changed in the process.
     */
    public static IThreadDebugger install(final IThreadDebugger debugger,
                                          IThreadChangedCallback callback) {
        return install(debugger, 2000, callback);
    }

    /**
     * Install the thread debugger with the check duration 2000 millisecond.
     *
     * @param debugger the customize debugger. You can create this through {@link ThreadDebuggers}.
     */
    public static IThreadDebugger install(final IThreadDebugger debugger) {
        return install(debugger, null);
    }

    private static Handler HANDLER;

    /**
     * Install the thread debugger.
     *
     * @param debugger          the customize debugger. You can create this through {@link ThreadDebuggers}.
     * @param updateMilliSecond the update duration used for checking threads situation in the process.
     * @param callback          the thread changed callback, this callback will be triggered when
     *                          there are some threads has changed in the process.
     */
    public static synchronized IThreadDebugger install(final IThreadDebugger debugger,
                                                       final int updateMilliSecond,
                                                       final IThreadChangedCallback callback) {
        uninstall();

        HandlerThread handlerThread = new HandlerThread(CommonThreadKey.Others.THREAD_DEBUGGER);
        handlerThread.start();

        HANDLER = new Handler(handlerThread.getLooper(), new Handler.Callback() {

            @Override
            public boolean handleMessage(Message message) {
                long start = 0;
                if (NEED_PRINT_COST) {
                    start = SystemClock.uptimeMillis();
                }

                debugger.refresh();

                if (callback != null && debugger.isChanged()) {
                    callback.onChanged(debugger);
                }

                if (NEED_PRINT_COST) {
                    Log.d("ThreadDebugger", "refreshing debugger coast: " +
                            (SystemClock.uptimeMillis() - start));
                }

                HANDLER.sendEmptyMessageDelayed(0, updateMilliSecond);
                return false;
            }
        });

        HANDLER.sendEmptyMessageDelayed(0, updateMilliSecond);

        return debugger;
    }

    /**
     * @return {@code true} If uninstall the thread debugger successfully. If it occur failed, there
     * is already no thread debugger in this process.
     */
    public static synchronized boolean uninstall() {
        if (HANDLER == null) {
            return false;
        }
        HANDLER.removeMessages(0);
        HANDLER.getLooper().quit();
        return true;
    }

}
