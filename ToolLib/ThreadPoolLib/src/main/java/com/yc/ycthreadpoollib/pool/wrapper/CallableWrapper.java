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

import com.yc.ycthreadpoollib.pool.callback.ThreadCallback;
import com.yc.ycthreadpoollib.pool.config.ThreadConfigs;
import com.yc.ycthreadpoollib.pool.utils.ThreadToolUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : CallableWrapper
 *     revise:
 * </pre>
 */
public final class CallableWrapper<T> implements Callable<T> {

    private final String name;
    private final ThreadCallback callback;
    private final Callable<T> proxy;

    /**
     * 构造方法
     * @param configs               thread配置，主要参数有：线程name，延迟time，回调callback，异步callback
     * @param proxy                 线程优先级
     */
    public CallableWrapper(ThreadConfigs configs, Callable<T> proxy) {
        this.name = configs.name;
        this.proxy = proxy;
        this.callback = new NormalCallback(configs.callback, configs.deliver, configs.asyncCallback);
    }

    /**
     * 详细可以看我的GitHub：https://github.com/yangchong211
     * 自定义Callable继承Callable<T>类，Callable 是在 JDK1.5 增加的。
     * Callable 的 call() 方法可以返回值和抛出异常
     * @return                      泛型
     * @throws Exception            异常
     */
    @Override
    public T call() {
        ThreadToolUtils.resetThread(Thread.currentThread(),name,callback);
        if (callback != null) {
            //开始
            callback.onStart(name);
        }
        T t = null;
        try {
            t = proxy == null ? null : proxy.call();
        } catch (Exception e) {
            e.printStackTrace();
            //异常错误
            if(callback!=null){
                callback.onError(name,e);
            }
        }finally {
            //完成
            if (callback != null)  {
                callback.onCompleted(name);
            }
        }
        return t;
    }


}
