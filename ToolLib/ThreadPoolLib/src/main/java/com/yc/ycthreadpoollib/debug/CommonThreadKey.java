

package com.yc.ycthreadpoollib.debug;


public interface CommonThreadKey {

    interface OpenSource {
        String OKHTTP = "OkHttp";
        String RETROFIT = "Retrofit";
        String CRASHLYTICS = "Crashlytics";
        String LEAKCANARY = "LeakCanary";
        String RX_JAVA = "Rx";
        String PICASSO = "Picasso";
        String FILEDOWNLOADER = "FileDownloader";
        String OKIO = "okio";
    }

    interface System {
        String MAIN = "main";
        String CHROME = "Chrome";
        String ASYNC_TASK = "AsyncTask";
        String BINDER = "Binder";
        String FINALIZER = "Finalizer";
        String WIFI = "WiFi";
        String RENDER_THREAD = "RenderThread";
        String HEAP_TASK_DAEMON = "HeapTaskDaemon";
        String REFERENCE_QUEUE_DAEMON = "ReferenceQueueDaemon";
        String FINALIZER_DAEMON = "FinalizerDaemon";
        String FINALIZER_WATCHDOG_DAEMON = "FinalizerWatchdogDaemon";
        String JDWP = "JDWP";
    }

    interface Media {
        String AUDIO = "Audio";
        String MEDIA = "Media";
        String EXO_PLAYER = "ExoPlayer";

    }

    interface Others {
        String QUEUE = "Queue";
        String THREAD_DEBUGGER = "ThreadDebugger";
    }
}
