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
package com.yc.ycthreadpoollib.pool.callback;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  :
 *     desc  : 一个回调接口，用于通知用户任务的状态回调委托类
 *             线程的名字可以自定义
 *     revise:
 * </pre>
 */
public interface ThreadCallback {

    /**
     * 当线程发生错误时，将调用此方法。
     * @param threadName            正在运行线程的名字
     * @param t                     异常
     */
    void onError(String threadName, Throwable t);

    /**
     * 通知用户知道它已经完成
     * @param threadName            正在运行线程的名字
     */
    void onCompleted(String threadName);

    /**
     * 通知用户任务开始运行
     * @param threadName            正在运行线程的名字
     */
    void onStart(String threadName);


}
