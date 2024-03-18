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

package com.yc.ycthreadpoollib.pool.deliver;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import java.util.concurrent.Executor;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : 相当于如果是Android，那么
 *     revise:
 * </pre>
 */
public final class AndroidDeliver implements Executor {

    private static final AndroidDeliver instance = new AndroidDeliver();
    /**
     * 创建主线程handler
     */
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static AndroidDeliver getInstance() {
        return instance;
    }

    @Override
    public void execute(@Nullable final Runnable runnable) {
        //返回应用程序的looper，它位于应用程序的主线程中。
        Looper mainLooper = Looper.getMainLooper();
        //如果当前looper就是当前主线程，那么调用run后不再执行下面的语句
        if (Looper.myLooper() == mainLooper && runnable != null) {
            runnable.run();
            return;
        }
        //开启子线程
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //注意：这里需要增加非空判断
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
    }
}
