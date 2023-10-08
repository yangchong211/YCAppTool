package com.yc.safetyjni;

public class SafetyJniLib {

    private static SafetyJniLib instance;

    // Used to load the 'safetyjnilib' library on application startup.
    static {
        System.loadLibrary("safetyjnilib");
    }

    public static SafetyJniLib getInstance() {
        if (instance == null) {
            synchronized (SafetyJniLib.class) {
                if (instance == null) {
                    instance = new SafetyJniLib();
                }
            }
        }
        return instance;
    }

    public native String stringFromJNI();

    public native String getAppSign();
}
