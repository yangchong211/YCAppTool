package com.yc.mmaplog.test;

import android.util.Log;

import com.yc.mmaplog.appender.AbsAppender;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by yangchong on 2017/11/28.
 */
public class NoBufferInThreadFileAppender extends AbsAppender {

    private String TAG = "NoBufferInThreadFileAppender";

    private File logFile;
    private LinkedBlockingQueue<String> queue;
    private Thread witterThread;
    private boolean isRunning = true;

    public NoBufferInThreadFileAppender(File logFile) {
        this.logFile = logFile;
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        queue = new LinkedBlockingQueue<>();
        witterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    try {
                        String log = queue.take();
                        doAppendInner(log);
                        Log.d(TAG, log);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        witterThread.start();
    }

    @Override
    protected void doAppend(int logLevel, String tag, String msg) {
        try {
            queue.put(logLevel + tag + msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAppendInner(String log) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(logFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (outputStream == null || log == null) {
            return;
        }
        try {
            outputStream.write(log.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void release() {
        super.release();
        isRunning = false;
        witterThread.interrupt();
    }
}
