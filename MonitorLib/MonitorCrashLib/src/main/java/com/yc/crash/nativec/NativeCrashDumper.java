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
     * 使用静态注册
     *
     * @param crashDumpDir        dumpDir路径
     * @param nativeCrashListener 监听listener
     * @param handleMode          模式
     */
    private native void nativeInit(String crashDumpDir,
                                   NativeCrashListener nativeCrashListener, int handleMode);

    /**
     * 模拟native崩溃
     * 使用静态注册，本地方法声明，通过jni去实现。native自动生成的方法名称是：
     * 包名_类名_方法名
     * Java_com_yc_crash_nativec_NativeCrashDumper_nativeCrash
     */
    public native void nativeCrash();

    /**
     * 通过env来处理异常。分为三种，直接清除，抛给java，程序退出
     */
    public native void exception();

    /**
     * 测试异常
     */
    public native void testException();

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
