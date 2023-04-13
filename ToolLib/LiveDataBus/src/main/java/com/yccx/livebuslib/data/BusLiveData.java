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
import androidx.lifecycle.ExternalLiveData;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.yc.apphandlerlib.WeakHandler;
import com.yccx.livebuslib.event.LiveDataBus;
import com.yccx.livebuslib.inter.BusObservable;
import com.yccx.livebuslib.wrapper.SafeCastObserver;
import com.yccx.livebuslib.wrapper.WrapperObserver;

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
@Deprecated
public class BusLiveData<T> extends ExternalLiveData<T> implements BusObservable<T> {

    private Map<Observer, Observer> observerMap = new HashMap<>();
    private WeakHandler mainHandler = new WeakHandler(Looper.getMainLooper());
    private Map<String, IntervalValueTask> intervalTasks = new HashMap<>();
    private String mKey ;

    public BusLiveData(String key) {
        this.mKey = key;
    }

    private class PostValueTask implements Runnable {

        private T newValue;

        public PostValueTask(@NonNull T newValue) {
            this.newValue = newValue;
        }

        @Override
        public void run() {
            if (newValue!=null){
                setValue(newValue);
            }
        }
    }

    /**
     * 主线程发送事件
     * @param value                                 value
     */
    @Override
    public void setValue(T value) {
        //调用父类即可
        super.setValue(value);
    }

    /**
     * 子线程发送事件
     * @param value                                 value
     */
    @Override
    public void postValue(T value) {
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
        IntervalValueTask intervalTask = new IntervalValueTask(value,interval);
        intervalTasks.put(taskName,intervalTask);
        mainHandler.postDelayed(intervalTask,interval);
    }

    /**
     * 停止轮训间隔发送事件
     */
    @Deprecated
    @Override
    public void stopPostInterval(@NonNull String taskName) {
        IntervalValueTask intervalTask  = intervalTasks.get(taskName);
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
        //super.observe(owner, observer);
        super.observe(owner, new SafeCastObserver<>(observer));
    }

    /**
     * 将给定的观察者添加到观察者列表中。
     * 这个调用类似于{@link LiveData#observe(LifecycleOwner, Observer)}和一个LifecycleOwner,
     * which总是积极的。这意味着给定的观察者将接收所有事件，并且永远不会被自动删除。
     * 您应该手动调用{@link #removeObserver(Observer)}来停止观察这LiveData。
     * @param observer                              observer
     */
    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        if (!observerMap.containsKey(observer)) {
            observerMap.put(observer, new WrapperObserver(observer));
        }
        super.observeForever(observerMap.get(observer));
    }

    /**
     * 从观察者列表中删除给定的观察者。
     * @param observer                              observer
     */
    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        Observer realObserver = null;
        if (observerMap.containsKey(observer)) {
            realObserver = observerMap.remove(observer);
        } else {
            realObserver = observer;
        }
        super.removeObserver(realObserver);
        if (!hasObservers() && LiveDataBus.get().getBus()!=null) {
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
    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
//        super.observeSticky(owner, new SafeCastObserver<>(observer));
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
     * 返回观察者活跃水平
     * @return
     */
    @Override
    public Lifecycle.State observerActiveLevel() {
        return LiveDataBus.get().isLifecycleObserverAlwaysActive() ? Lifecycle.State.CREATED : Lifecycle.State.STARTED;
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
