package com.yc.calljni;

public class CallNativeLib {

    private static CallNativeLib instance;

    // Used to load the 'calljnilib' library on application startup.
    static {
        System.loadLibrary("calljnilib");
    }

    public static CallNativeLib getInstance() {
        if (instance == null) {
            synchronized (CallNativeLib.class) {
                if (instance == null) {
                    instance = new CallNativeLib();
                }
            }
        }
        return instance;
    }

    private void test(){
        try{
            callJavaField("com/yc/calljni/HelloCallBack","name");
            callJavaMethod("com/yc/calljni/HelloCallBack","updateName");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * native调用java代码，c/c++调用java
     */
    public native void callJavaField(String className,String fieldName) ;
    public native boolean callJavaMethod(String className,String methodName) ;

}