package com.yc.toastutils;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/5/6
 *     desc  : 利用hook解决toast崩溃问题
 *              https://www.jianshu.com/p/437f473017d6
 *     GitHub: https://github.com/yangchong211/YCDialog
 *     revise: 7.1.1Toast崩溃解决方案
 *             首先Toast显示依赖于一个窗口，这个窗口被WMS管理（WindowManagerService），
 *             当需要show的时候这个请求会放在WMS请求队列中，并且会传递一个TN类型的Bider对象给WMS，
 *             WMS并生成一个token传递给Android进行显示与隐藏，但是如果UI线程的某个线程发生了阻塞，
 *             并且已经NotificationManager检测已经超时就不删除token记录，此时token已经过期，
 *             阻塞结束的时候再显示的时候就发生了异常。
 *
 *
 * </pre>
 */
public final class HookToast {

    private static Field sField_TN;
    private static Field sField_TN_Handler;

    static {
        try {
            Class<?> clazz = Toast.class;
            //通过反射拿到，获取class对象的指定属性，拿到tn对象
            sField_TN = clazz.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            //然后通过反射拿到Toast中内部类TN的mHandler
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hook(Toast toast) {
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandler(preHandler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class SafelyHandler extends Handler {

        private final Handler impl;

        public SafelyHandler(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                // 捕获这个异常，避免程序崩溃
                super.dispatchMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            //需要委托给原Handler执行
            impl.handleMessage(msg);
        }
    }

}
