package com.yc.testjnilib;

public class NativeLib {

    private static NativeLib instance;

    // Used to load the 'testjnilib' library on application startup.
    static {
        System.loadLibrary("testjnilib");
    }

    public static NativeLib getInstance() {
        if (instance == null) {
            synchronized (NativeLib.class) {
                if (instance == null) {
                    instance = new NativeLib();
                }
            }
        }
        return instance;
    }

    /**
     * java调用native代码，java调用c/c++
     */
    public native String stringFromJNI();

    /**
     * 打开定义native方法的java类，如下所示：是红色警告。
     * 原因是，C++代码层没有对应的遵循特定JNI格式的JNI函数。
     * 其实这个项目没有使用静态注册方法，而是使用了动态注册方法。
     *
     * @return
     */
    public native String getNameFromJNI();
    public native String getMd5(String str);
    public native void initLib(String version);

    public native String callThirdSoMethod();
}