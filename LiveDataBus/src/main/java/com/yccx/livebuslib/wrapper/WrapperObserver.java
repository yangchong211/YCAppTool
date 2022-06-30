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
package com.yccx.livebuslib.wrapper;


import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/12/23
 *     desc  : Observer<T>
 *     revise:
 * </pre>
 */
public class WrapperObserver<T> implements Observer<T> {

    private final Observer<T> observer;
    private final String filterClass;
    private final String filterMethod;

    public WrapperObserver(Observer<T> observer) {
        this.observer = observer;
        this.filterClass = null;
        this.filterMethod = null;
    }

    public WrapperObserver(Observer<T> observer, String filterClass, String filterMethod) {
        this.observer = observer;
        this.filterClass = filterClass;
        this.filterMethod = filterMethod;
    }

    /**
     * 调用方法是指刷新数据
     * @param t                         t数据
     */
    @Override
    public void onChanged(@Nullable T t) {
        if (isCallOnObserve()) {
            return;
        }
        //捕获异常，避免出现异常之后，收不到后续的消息事件
        try {
            observer.onChanged(t);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCallOnObserve() {
        //返回一个表示堆栈转储的堆栈跟踪元素数组
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (filterClass==null || filterMethod==null){
                if (stackTrace != null && stackTrace.length > 0) {
                    for (StackTraceElement element : stackTrace) {
                        if ("android.lifecycle.LiveData".equals(element.getClassName()) &&
                                "observeForever".equals(element.getMethodName())) {
                            return true;
                        }
                    }
                }
            } else {
                if (stackTrace != null && stackTrace.length > 0) {
                    for (StackTraceElement element : stackTrace) {
                        if (filterClass.equals(element.getClassName()) &&
                                filterMethod.equals(element.getMethodName())) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
