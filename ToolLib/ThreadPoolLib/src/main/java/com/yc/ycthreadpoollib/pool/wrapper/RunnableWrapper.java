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

package com.yc.ycthreadpoollib.pool.wrapper;


import java.util.concurrent.Callable;

import com.yc.ycthreadpoollib.pool.utils.ThreadToolUtils;
import com.yc.ycthreadpoollib.pool.config.ThreadConfigs;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : RunnableWrapper
 *     revise:
 * </pre>
 */
public final class RunnableWrapper implements Runnable {

    private final String name;
    private final NormalCallback normal;
    private Runnable runnable;
    private Callable callable;

    public RunnableWrapper(ThreadConfigs configs) {
        this.name = configs.name;
        this.normal = new NormalCallback(configs.callback, configs.deliver, configs.asyncCallback);
    }

    /**
     * 启动异步任务，普通的
     * @param runnable              runnable
     * @return                      对象
     */
    public RunnableWrapper setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    /**
     * 异步任务，回调用于接收可调用任务的结果
     * @param callable              callable
     * @return                      对象
     */
    public RunnableWrapper setCallable(Callable callable) {
        this.callable = callable;
        return this;
    }

    /**
     * 自定义xxRunnable继承Runnable，实现run方法
     * 详细可以看我的GitHub：https://github.com/yangchong211
     */
    @Override
    public void run() {
        //获取线程
        Thread current = Thread.currentThread();
        //重置线程
        ThreadToolUtils.resetThread(current, name, normal);
        //开始
        normal.onStart(name);
        //注意需要判断runnable，callable非空
        // avoid NullPointException
        if (runnable != null) {
            runnable.run();
        } else if (callable != null) {
            try {
                Object result = callable.call();
                //监听成功
                normal.onSuccess(result);
            } catch (Exception e) {
                //监听异常
                normal.onError(name, e);
            }
        }
        //监听完成
        normal.onCompleted(name);
    }


}
