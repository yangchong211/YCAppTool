/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.yc.ycthreadpoollib.pool.utils;


import com.yc.ycthreadpoollib.pool.callback.ThreadCallback;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://www.jianshu.com/p/53017c3fc75d
 *     time  : 2017/08/22
 *     desc  : 工具
 *     revise:
 * </pre>
 */
public final class ThreadToolUtils {

    /**
     * 标志：在android平台上
     */
    private static boolean isAndroid;

    /*
     * 静态代码块
     * 判断是否是android环境
     * Class.forName(xxx.xx.xx) 返回的是一个类对象
     * 首先要明白在java里面任何class都要装载在虚拟机上才能运行。
     */
    static {
        try {
            Class.forName("android.os.Build");
            isAndroid = true;
        } catch (Exception e) {
            isAndroid = false;
        }
    }

    /**
     * 判断是否是Android
     *
     * @return true表示Android
     */
    public static boolean isIsAndroid() {
        return isAndroid;
    }

    /**
     * 重置线程名并设置UnCaughtExceptionHandler包装回调，以便在发生异常时通知用户
     *
     * @param thread   The thread who should be reset.
     * @param name     non-null, thread name
     * @param callback a callback to notify user.
     */
    public static void resetThread(Thread thread, final String name, final ThreadCallback callback) {
        //捕获单个线程的异常
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                //如果线程出现了异常，则会回调该方法
                if (callback != null) {
                    callback.onError(name, e);
                }
            }
        });
        thread.setName(name);
    }

    public static void sleepThread(long time) {
        if (time <= 0) {
            return;
        }
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException("Thread has been interrupted", e);
        }
    }

}