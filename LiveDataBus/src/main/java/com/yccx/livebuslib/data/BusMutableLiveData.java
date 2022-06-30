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

package com.yccx.livebuslib.data;


import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.yccx.livebuslib.event.LiveDataBus;
import com.yccx.livebuslib.helper.BusWeakHandler;
import com.yccx.livebuslib.inter.BusObservable;
import com.yccx.livebuslib.utils.BusLibUtils;
import com.yccx.livebuslib.utils.BusLogUtils;
import com.yccx.livebuslib.wrapper.SafeCastObserver;
import com.yccx.livebuslib.wrapper.WrapperObserver;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/12/23
 *     desc  : 自定义LiveData<T>
 *     revise:
 * </pre>
 */
public class BusMutableLiveData<T> extends MutableLiveData<T> implements BusObservable<T> {

    private Map<Observer, Observer> observerMap = new HashMap<>();
    private BusWeakHandler mainHandler = new BusWeakHandler(Looper.getMainLooper());
    private Map<String,IntervalValueTask> intervalTasks = new HashMap<>();
    private String mKey ;

    public BusMutableLiveData(String key) {
        this.mKey = key;
    }

    private class PostValueTask implements Runnable {

        private T newValue;

        public PostValueTask(@NonNull T newValue) {
            this.newValue = newValue;
        }

        @Override
        public void run() {
            setValue(newValue);
        }
    }

    /**
     * 主线程发送事件
     * @param value                                 value
     */
    @Override
    public void setValue(T value) {
        BusLibUtils.checkNull(value);
        if (BusLibUtils.isMainThread()){
            //调用父类即可
            super.setValue(value);
        } else {
            throw new IllegalStateException("You can only operate on the main thread");
        }
    }

    /**
     * 子线程发送事件
     * @param value                                 value
     */
    @Override
    public void postValue(T value) {
        BusLibUtils.checkNull(value);
        //注意，去掉super方法，
        //super.postValue(value);
        mainHandler.post(new PostValueTask(value));
    }

    /**
     * 发送延迟事件
     * @param value                                 value
     * @param delay                                 延迟时间
     */
    @Override
    public void postValueDelay(T value, long delay) {
        BusLibUtils.checkNull(value);
        mainHandler.postDelayed(new PostValueTask(value) , delay);
        //mainHandler.postAtTime(new PostValueTask(value) , delay);
    }

    /**
     * 发送延迟事件，间隔轮训
     * @param value                                 value
     * @param interval                              间隔
     */
    @Deprecated
    @Override
    public void postValueInterval(final T value, final long interval,@NonNull String taskName) {
        if(taskName.isEmpty()){
            return;
        }
        IntervalValueTask  intervalTask = new IntervalValueTask(value,interval);
        intervalTasks.put(taskName,intervalTask);
        mainHandler.postDelayed(intervalTask,interval);
    }

    /**
     * 停止轮训间隔发送事件
     */
    @Deprecated
    @Override
    public void stopPostInterval(@NonNull String taskName) {
        IntervalValueTask  intervalTask  = intervalTasks.get(taskName);
        if(intervalTask!= null){
            //移除callback
            mainHandler.removeCallbacks(intervalTask);
            intervalTasks.remove(taskName);
        }
    }

    /**
     * 在给定的观察者的生命周期内将给定的观察者添加到观察者列表所有者。事件是在主线程上分派的。
     * 如果LiveData已经有数据集合，它将被传递给观察者。
     * @param owner                                 owner
     * @param observer                              observer
     */
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        //之前做法
        //super.observe(owner, observer);
        //hook(observer);
        //下面操作可以修复订阅者会收到订阅之前发布的消息的问题
        //获取LifecycleOwner的当前状态
        setHook(owner,observer);

        //super.observe(owner, createStateObserver(observer));
    }

    /**
     * 将给定的观察者添加到观察者列表中。
     * 这个调用类似于{@link LiveData#observe(LifecycleOwner, Observer)}和一个LifecycleOwner,
     * which总是积极的。这意味着给定的观察者将接收所有事件，并且永远不会被自动删除。
     * 您应该手动调用{@link #removeObserver(Observer)}来停止观察这LiveData。
     * @param observer                              observer
     */
    @Override
    public void observeForever(@NonNull Observer<T> observer) {
        if (!observerMap.containsKey(observer)) {
            observerMap.put(observer, createForeverObserver(observer));
        }
        Observer mObserver = observerMap.get(observer);
        super.observeForever(mObserver);
    }

    /**
     * 从观察者列表中删除给定的观察者。
     * @param observer                              observer
     */
    @Override
    public void removeObserver(@NonNull Observer<T> observer) {
        Observer realObserver = null;
        if (observerMap.containsKey(observer)) {
            realObserver = observerMap.remove(observer);
        } else {
            realObserver = observer;
        }
        if (realObserver != null) {
            super.removeObserver(realObserver);
        }
        if (!hasObservers() && LiveDataBus.get().getBus()!=null) {
            // 当对应liveData没有相关的观察者的时候
            // 就可以移除掉维护的LiveData
            LiveDataBus.get().getBus().remove(mKey);
        }
    }

    /**
     * 在给定的观察者的生命周期内将给定的观察者添加到观察者列表所有者。
     * 事件是在主线程上分派的。如果LiveData已经有数据集合，它将被传递给观察者。
     * @param owner                                 owner
     * @param observer                              observer
     */
    @Override
    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
        super.observe(owner, new SafeCastObserver<>(observer));
    }

    /**
     * 将给定的观察者添加到观察者列表中。这个调用类似于{@link LiveData#observe(LifecycleOwner, Observer)}
     * 和一个LifecycleOwner, which总是积极的。这意味着给定的观察者将接收所有事件，并且永远不会 被自动删除。
     * 您应该手动调用{@link #removeObserver(Observer)}来停止 观察这LiveData。
     * @param observer                              observer
     */
    @Override
    public void observeStickyForever(@NonNull Observer<T> observer) {
        super.observeForever(observer);
    }

    /**
     * 当活动观察者的数量从1变为0时调用。
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        /*if (!hasObservers() && LiveDataBus.get().getBus()!=null) {
            // 当对应liveData没有相关的观察者的时候
            // 就可以移除掉维护的LiveData
            LiveDataBus.get().getBus().remove(mKey);
        }*/
    }

    private <T> WrapperObserver createForeverObserver(Observer<T> observer) {
        return new WrapperObserver(observer, "android.arch.lifecycle.LiveData", "observeForever");
    }

    private <T> WrapperObserver createStateObserver(Observer<T> observer) {
        return new WrapperObserver(observer, "android.arch.lifecycle.LiveData$LifecycleBoundObserver", "onStateChanged");
    }

    private void setHook(LifecycleOwner owner, Observer<T> observer) {
        SafeCastObserver<T> safeCastObserver = new SafeCastObserver<>(observer);
        Lifecycle lifecycle = owner.getLifecycle();
        Lifecycle.State currentState = lifecycle.getCurrentState();
        //获取生命周期观察者映射的大小
        int observerSize = BusLibUtils.getLifecycleObserverMapSize(lifecycle);
        //比较此状态是否大于或等于给定的{@code状态}。如果该状态大于或等于给定的{@code状态}，则为true
        //目前是跟started状态对比
        boolean needChangeState = currentState.isAtLeast(Lifecycle.State.STARTED);
        BusLogUtils.d("--observe----------"+currentState.name()+"-----"+observerSize+"------"+needChangeState);
        if (needChangeState) {
            //更改LifecycleOwner的状态，把LifecycleOwner的状态改为INITIALIZED
            BusLibUtils.setLifecycleState(lifecycle, Lifecycle.State.INITIALIZED);
            //将observerSize设置为0，否则super.observe(owner, observer)的时候会无限循环
            BusLibUtils.setLifecycleObserverMapSize(lifecycle, -1);
        }
        //调用父类的方法
        super.observe(owner, safeCastObserver);
        if (needChangeState) {
            //返回到LifecycleOwner状态
            BusLibUtils.setLifecycleState(lifecycle, currentState);
            //重置observer size，因为又添加了一个observer，所以数量+1
            BusLibUtils.setLifecycleObserverMapSize(lifecycle, observerSize + 1);
            //设置观察员活跃
            hookObserverActive(safeCastObserver, true);
        }
        //设置更改Observer的version
        hookObserverVersion(safeCastObserver);
    }

    /**
     * 利用反射修改属性
     * @param observer                              observer
     */
    private void hook(@NonNull Observer<T> observer){
        try {
            Class<LiveData> classLiveData = LiveData.class;
            Field fieldObservers = classLiveData.getDeclaredField("mObservers");
            fieldObservers.setAccessible(true);
            Object objectObservers = fieldObservers.get(this);
            Class<?> classObservers = objectObservers.getClass();
            Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
            methodGet.setAccessible(true);
            Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
            Object objectWrapper = null;
            if (objectWrapperEntry instanceof Map.Entry) {
                objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
            }
            if (objectWrapper == null) {
                throw new NullPointerException("Wrapper can not be bull!");
            }
            Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
            Field fieldLastVersion = null;
            if (classObserverWrapper != null) {
                fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
                fieldLastVersion.setAccessible(true);
                Field fieldVersion = classLiveData.getDeclaredField("mVersion");
                fieldVersion.setAccessible(true);
                Object objectVersion = fieldVersion.get(this);
                fieldLastVersion.set(objectWrapper, objectVersion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hookObserverActive(@NonNull Observer<T> observer, boolean active) {
        try {
            //get wrapper's version
            Object objectWrapper = getObserverWrapper(observer);
            if (objectWrapper == null) {
                return;
            }
            Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
            Field mActive = null;
            if (classObserverWrapper != null) {
                mActive = classObserverWrapper.getDeclaredField("mActive");
                mActive.setAccessible(true);
                mActive.set(objectWrapper, active);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getObserverWrapper(@NonNull Observer<T> observer) throws Exception {
        Field fieldObservers = LiveData.class.getDeclaredField("mObservers");
        fieldObservers.setAccessible(true);
        Object objectObservers = fieldObservers.get(this);
        Class<?> classObservers = objectObservers.getClass();
        Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
        methodGet.setAccessible(true);
        Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
        Object objectWrapper = null;
        if (objectWrapperEntry instanceof Map.Entry) {
            objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
        }
        return objectWrapper;
    }

    private void hookObserverVersion(@NonNull Observer<T> observer) {
        try {
            Object objectWrapper = getObserverWrapper(observer);
            if (objectWrapper == null) {
                return;
            }
            Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
            Field fieldLastVersion = null;
            if (classObserverWrapper != null) {
                fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
                fieldLastVersion.setAccessible(true);
                Field fieldVersion = LiveData.class.getDeclaredField("mVersion");
                fieldVersion.setAccessible(true);
                Object objectVersion = fieldVersion.get(this);
                fieldLastVersion.set(objectWrapper, objectVersion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class IntervalValueTask implements Runnable {

        private T newValue;
        private long interval;

        public IntervalValueTask(T newValue, long interval) {
            this.newValue = newValue;
            this.interval = interval;
        }

        @Override
        public void run() {
            setValue(newValue);
            mainHandler.postDelayed(this,interval);
        }
    }
}
