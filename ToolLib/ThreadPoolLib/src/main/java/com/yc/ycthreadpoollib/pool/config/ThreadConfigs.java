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

package com.yc.ycthreadpoollib.pool.config;

import com.yc.ycthreadpoollib.pool.callback.AsyncCallback;
import com.yc.ycthreadpoollib.pool.callback.ThreadCallback;

import java.util.concurrent.Executor;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : 存储当前任务的某些配置
 *     revise:
 * </pre>
 */

public final class ThreadConfigs {

    /**
     * 线程的名称
     * 通过setName方法设置
     */
    public String name;
    /**
     * 线程执行延迟的时间
     * 通过setDelay方法设置
     */
    public long delay;
    /**
     * 线程执行者
     * JAVA或者ANDROID
     */
    public Executor deliver;
    /**
     * 用户任务的状态回调callback
     */
    public ThreadCallback callback;
    /**
     * 异步callback回调callback
     */
    public AsyncCallback asyncCallback;

}
