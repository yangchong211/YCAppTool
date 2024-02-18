package com.yc.crash.nativec;


import java.io.File;

public class NativeCrashDumper {

    static {
        //java.lang.UnsatisfiedLinkError: dalvik.system.PathClassLoader[DexPathList
        //couldn't find "libcrash_dumper.so"
        System.loadLibrary("crash_dumper");
    }

    private static final class InstanceHolder {
        static final NativeCrashDumper instance = new NativeCrashDumper();
    }

    public static NativeCrashDumper getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * 初始化
     *
     * @param crashDumpDir        dumpDir路径
     * @param nativeCrashListener 监听listener
     * @param handleMode          模式
     */
    private native void nativeInit(String crashDumpDir,
                                   NativeCrashListener nativeCrashListener, int handleMode);

    /**
     * 模拟native崩溃
     */
    public native void nativeCrash();

    public boolean init(String crashDumpDir,
                        NativeCrashListener nativeCrashListener, NativeHandleMode handleMode) {
        if (crashDumpDir == null) {
            return false;
        }
        File dir = new File(crashDumpDir);
        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            nativeInit(crashDumpDir, nativeCrashListener, handleMode.mode);
            return true;
        }
        return false;
    }

}
