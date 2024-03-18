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


import java.util.concurrent.Executor;

import com.yc.ycthreadpoollib.pool.callback.ThreadCallback;
import com.yc.ycthreadpoollib.pool.callback.AsyncCallback;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : 回调委托类，监听
 *     revise:
 * </pre>
 */
 final class NormalCallback implements ThreadCallback, AsyncCallback {

    private final ThreadCallback callback;
    private final AsyncCallback async;
    /**
     * Executor
     */
    private final Executor deliver;

    NormalCallback(ThreadCallback callback, Executor deliver, AsyncCallback async) {
        this.callback = callback;
        this.deliver = deliver;
        this.async = async;
    }

    /**
     * 回调成功
     * @param o
     */
    @Override
    public void onSuccess(final Object o) {
        if (async == null) {
            return;
        }
        deliver.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //noinspection unchecked
                    async.onSuccess(o);
                } catch (Throwable t) {
                    onFailed(t);
                }
            }
        });
    }

    /**
     * 回调失败
     * @param t         异常
     */
    @Override
    public void onFailed(final Throwable t) {
        if (async == null) {
            return;
        }
        deliver.execute(new Runnable() {
            @Override
            public void run() {
                async.onFailed(t);
            }
        });
    }

    /**
     * 回调异常
     * @param name                  线程name
     * @param t                     异常
     */
    @Override
    public void onError(final String name, final Throwable t) {
        onFailed(t);
        if (callback == null) {
            return;
        }
        deliver.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(name, t);
            }
        });
    }

    /**
     * 回调完成
     * @param name                  线程name
     */
    @Override
    public void onCompleted(final String name) {
        if (callback == null) {
            return;
        }
        deliver.execute(new Runnable() {
            @Override
            public void run() {
                callback.onCompleted(name);
            }
        });
    }

    /**
     * 回调开始
     * @param name                  线程name
     */
    @Override
    public void onStart(final String name) {
        if (callback == null) {
            return;
        }
        deliver.execute(new Runnable() {
            @Override
            public void run() {
                callback.onStart(name);
            }
        });
    }
}
