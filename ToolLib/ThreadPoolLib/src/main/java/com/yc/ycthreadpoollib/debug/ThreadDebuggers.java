

package com.yc.ycthreadpoollib.debug;


public class ThreadDebuggers {

    public static IThreadDebugger create() {
        return new ThreadDebuggerImpl();
    }

    public static IThreadDebugger createWithCommonThreadKey() {
        return create()
                .add(CommonThreadKey.OpenSource.OKHTTP)
                .add(CommonThreadKey.OpenSource.OKIO)
                .add(CommonThreadKey.OpenSource.RETROFIT)
                .add(CommonThreadKey.OpenSource.CRASHLYTICS)
                .add(CommonThreadKey.OpenSource.LEAKCANARY)
                .add(CommonThreadKey.OpenSource.RX_JAVA, "RxJava")
                .add(CommonThreadKey.OpenSource.PICASSO)
                .add(CommonThreadKey.OpenSource.FILEDOWNLOADER)
                .add(CommonThreadKey.System.MAIN)
                .add(CommonThreadKey.System.CHROME)
                .add(CommonThreadKey.System.ASYNC_TASK)
                .add(CommonThreadKey.System.BINDER)
                .add(CommonThreadKey.System.FINALIZER)
                .add(CommonThreadKey.System.WIFI)
                .add(CommonThreadKey.System.RENDER_THREAD)
                .add(CommonThreadKey.System.HEAP_TASK_DAEMON)
                .add(CommonThreadKey.System.REFERENCE_QUEUE_DAEMON)
                .add(CommonThreadKey.System.FINALIZER_DAEMON)
                .add(CommonThreadKey.System.FINALIZER_WATCHDOG_DAEMON)
                .add(CommonThreadKey.System.JDWP)
                .add(CommonThreadKey.Media.AUDIO)
                .add(CommonThreadKey.Media.MEDIA)
                .add(CommonThreadKey.Media.EXO_PLAYER)
                .add(CommonThreadKey.Others.THREAD_DEBUGGER)
                .add(CommonThreadKey.Others.QUEUE);
    }
}
