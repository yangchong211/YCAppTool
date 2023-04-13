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
package com.yccx.livebuslib.inter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/12/23
 *     desc  : 自定义被观察者（BusObservable）
 *     revise: 定义成接口，方便后期维护和接口隔离
 * </pre>
 */
public interface BusObservable<T> {

    /*这些都是LiveData中的方法*/

    void setValue(T value);

    void postValue(T value);

    void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer);

    void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer);

    void observeForever(@NonNull Observer<? super T> observer);

    void observeStickyForever(@NonNull Observer<T> observer);

    void removeObserver(@NonNull Observer<? super T> observer);

    /*下面的这些为自定义的方法*/

    void postValueDelay(T value,long delay);

    @Deprecated
    void postValueInterval(T value,long interval,@NonNull String taskName);

    @Deprecated
    void stopPostInterval(@NonNull String taskName);

}
