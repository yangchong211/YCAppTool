package com.yc.jnidemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class EpicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


//    public static void findAndHookMethod() {
//        try {
//            Class<?> targetClass = TelephonyManager.class;
//            String targetMethod = "getDeviceId";
//            Object[] paramsWithDefaultHandler = {int.class};
//            //核心方法
//            DexposedBridge.findAndHookMethod(targetClass, targetMethod, paramsWithDefaultHandler);
//            //核心方法
//            DexposedBridge.findAndHookMethod(targetClass, targetMethod, new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
//                }
//
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    super.afterHookedMethod(param);
//                }
//            });
//        } catch (NoSuchMethodError error) {
//            error.printStackTrace();
//        }
//    }

}
