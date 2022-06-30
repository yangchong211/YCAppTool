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

import android.os.Looper;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;

import java.lang.reflect.Field;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/07/18
 *     desc  : bus工具类
 *     revise:
 * </pre>
 */
public class BusLibUtils {

    /**
     * 判断是否是主线程
     * @return
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 空指针校验
     * @param object                                object
     */
    public static void checkNull(Object object){
        if (object == null){
            throw new NullPointerException("the live bus parameter must be non-null");
        }
    }

    /**
     * 获取生命周期观察者映射的大小
     * @param lifecycle                             lifecycle
     * @return
     */
    public static int getLifecycleObserverMapSize(Lifecycle lifecycle) {
        if (lifecycle == null) {
            return 0;
        }
        if (!(lifecycle instanceof LifecycleRegistry)) {
            return 0;
        }
        try {
            Field observerMapField = LifecycleRegistry.class.getDeclaredField("mObserverMap");
            observerMapField.setAccessible(true);
            Object mObserverMap = observerMapField.get(lifecycle);
            Class<?> superclass = mObserverMap.getClass().getSuperclass();
            Field mSizeField = null;
            if (superclass != null) {
                mSizeField = superclass.getDeclaredField("mSize");
                mSizeField.setAccessible(true);
                return (int) mSizeField.get(mObserverMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更改LifecycleOwner的状态
     * @param lifecycle                             lifecycle
     * @param state                                 state
     */
    public static void setLifecycleState(Lifecycle lifecycle, Lifecycle.State state) {
        if (lifecycle == null) {
            return;
        }
        if (!(lifecycle instanceof LifecycleRegistry)) {
            return;
        }
        try {
            Field mState = LifecycleRegistry.class.getDeclaredField("mState");
            mState.setAccessible(true);
            mState.set(lifecycle, state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置observer size
     * @param lifecycle                             lifecycle
     * @param size                                  size
     */
    public static void setLifecycleObserverMapSize(Lifecycle lifecycle, int size) {
        if (lifecycle == null) {
            return;
        }
        if (!(lifecycle instanceof LifecycleRegistry)) {
            return;
        }
        try {
            Field observerMapField = LifecycleRegistry.class.getDeclaredField("mObserverMap");
            observerMapField.setAccessible(true);
            Object mObserverMap = observerMapField.get(lifecycle);
            Class<?> superclass = mObserverMap.getClass().getSuperclass();
            Field mSizeField = null;
            if (superclass != null) {
                mSizeField = superclass.getDeclaredField("mSize");
                mSizeField.setAccessible(true);
                mSizeField.set(mObserverMap, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
