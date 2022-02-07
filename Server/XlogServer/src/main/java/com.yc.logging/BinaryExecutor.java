package com.yc.logging;

import androidx.annotation.RestrictTo;
import com.yc.logging.util.Debug;
import com.yc.logging.util.LoggerUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo(RestrictTo.Scope.LIBRARY)
class BinaryExecutor {
    private static final int MAX_QUEUE_SIZE = 1024;

    private static final Map<String, BinaryExecutor> sCachedExecutors =
            Collections.synchronizedMap(new HashMap<String, BinaryExecutor>());

    private final AbstractRollingPolicy mPolicy;

    private final BlockingQueue<AbstractLog> mLogQueue;

    private final Worker mWorker;

    private final Object mMutex = new Object();

    private File mCurrentlyActiveFile;
    private OutputStream mOutputStream;
    private final AtomicBoolean mLogConsumerStarted = new AtomicBoolean(false);

    public static BinaryExecutor getInstance(String buffer) {
        BinaryExecutor executor = sCachedExecutors.get(buffer);
        if (executor == null) {
            synchronized (sCachedExecutors) {
                //noinspection ConstantConditions
                if (executor == null) {
                    executor = new BinaryExecutor(buffer);
                    sCachedExecutors.put(buffer, executor);
                }
            }
        }
        return executor;
    }

    private BinaryExecutor(String buffer) {
        mWorker = new Worker("logger-binary-" + buffer);
        this.mLogQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
        this.mPolicy = new SizeAndTimeBasedRollingPolicy(Type.BINARY, buffer);
    }

    void enqueue(final AbstractLog log) {
        if (log == null) return;
        if (mLogConsumerStarted.compareAndSet(false, true)) {
            start();
        }
        if (LoggerUtils.isMainThread()) {
            this.mLogQueue.offer(log);
        } else {
            try {
                this.mLogQueue.put(log);
            } catch (final InterruptedException ignore) {
            }
        }
    }

    private void start() {
        mPolicy.setDateInCurrentPeriod(System.currentTimeMillis());
        mCurrentlyActiveFile = new File(mPolicy.getActiveFile());

        try {
            openFile(mCurrentlyActiveFile);
        } catch (final IOException e) {
            Debug.logOrThrow("start work thread openFile IOException ", e);
        }

        startLogConsumerWorker();
    }

    private void startLogConsumerWorker() {
        mWorker.setDaemon(true);
        mWorker.start();
    }

    class Worker extends Thread {
        Worker(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (mLogConsumerStarted.get()) {
                try {
                    AbstractLog log = mLogQueue.take();
                    if (log == null) {
                        continue;
                    }
                    try {
                        writeToFile(log.getData());
                    } catch (IOException e) {
                        Debug.e("writeToFile failed e = " + e);
                    }
                } catch (InterruptedException e) {
                    Debug.e("writeToFile failed e = " + e);
                }
            }
        }
    }


    private void rollover() {
        synchronized (this.mMutex) {
            // Note: This method needs to be synchronized because it needs
            // exclusive access while it closes and then re-opens the target file.
            //
            // make sure to close the hereto active log file! Renaming under windows
            // does not work for open files.
            this.closeOutputStream();
            this.mPolicy.rollover();

            mCurrentlyActiveFile = new File(mPolicy.getActiveFile());

            try {
                // update the currentlyActiveFile
                // http://jira.qos.ch/browse/LBCORE-90

                // This will also close the file. This is OK since multiple
                // close operations are safe.
                this.openFile(mCurrentlyActiveFile);
            } catch (final IOException e) {
                Debug.e("rollover openFile IOException e = " + e);
            }
        }
    }

    private void openFile(final File activeFile) throws IOException {
        synchronized (this.mMutex) {
            if (!activeFile.getParentFile().exists()) {
                activeFile.getParentFile().mkdirs();
            }
            mOutputStream = new ResilientFileOutputStream(activeFile, true);
        }
    }

    private void writeToFile(byte[] bytes) throws IOException {
        if (mPolicy.isTriggeringEvent(mCurrentlyActiveFile)) {
            rollover();
        }
        if (mOutputStream == null) {
            return;
        }
        if (bytes == null || bytes.length == 0) {
            return;
        }
        this.mOutputStream.write(bytes);
        this.mOutputStream.flush();
    }

    private void closeOutputStream() {
        if (this.mOutputStream != null) {
            try {
                this.mOutputStream.close();
                this.mOutputStream = null;
            } catch (IOException ignore) {
            }
        }
    }
}
