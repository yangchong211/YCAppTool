package com.yc.cameraopen;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import androidx.annotation.RequiresApi;


public class ImageHandler {

    private static final String TAG = ImageHandler.class.getSimpleName();
    private HandlerThread thread;
    private Handler handler;

    private ImageHandler() {
    }

    public static ImageHandler getInstance() {
        return Inner.INSTANCE;
    }

    private static class Inner {
        private static final ImageHandler INSTANCE = new ImageHandler();
    }

    public void startThread() {
        if (thread == null) {
            thread = new HandlerThread("YTImageProcessor");
            thread.start();
            handler = new Handler(thread.getLooper());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void stopThread() {
        if (thread != null) {
            thread.quitSafely();
            try {
                thread.join();
                thread = null;
                handler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Handler getHandler() {
        if (handler == null && thread == null){
            startThread();
        }
        return handler;
    }
}
