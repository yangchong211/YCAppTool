package com.yc.signalhooker;


import androidx.annotation.Keep;

public class SigQuitHooker {

    private static ISignalListener mListener = null;
    private static ILogger mLogger = null;

    static {
        //so库加载
        System.loadLibrary("signal-hooker");
    }

    public static void setSignalListener(ISignalListener listener) {
        mListener = listener;
    }

    public static void setLogger(ILogger logger) {
        mLogger = logger;
    }

    @Keep
    private static void onReceiveAnrSignal() {
        if (mListener != null) {
            mListener.onReceiveAnrSignal();
        }
    }

    @Keep
    private static void onPrintLog(String message) {
        if (mLogger != null) {
            mLogger.onPrintLog(message);
        }
    }

    public static native void initSignalHooker();
}
