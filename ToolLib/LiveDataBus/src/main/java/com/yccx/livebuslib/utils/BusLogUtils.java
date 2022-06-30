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
package com.yccx.livebuslib.utils;

import android.util.Log;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/07/18
 *     desc  : 日志工具类
 *     revise:
 * </pre>
 */
public final class BusLogUtils {


    private static final String TAG = "BusLogUtils";
    private static boolean isLog = true;

    /**
     * 设置是否开启日志
     * @param isLog                 是否开启日志
     */
    public static void setIsLog(boolean isLog) {
        BusLogUtils.isLog = isLog;
    }

    public static void d(String message) {
        if(isLog){
            Log.d(TAG, message);
        }
    }

    public static void i(String message) {
        if(isLog){
            Log.i(TAG, message);
        }

    }

    public static void e(String message, Throwable throwable) {
        if(isLog){
            Log.e(TAG, message, throwable);
        }
    }

}
