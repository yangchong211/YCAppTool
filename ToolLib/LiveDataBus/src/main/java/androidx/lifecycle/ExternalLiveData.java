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

package androidx.lifecycle;

import androidx.annotation.NonNull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static androidx.lifecycle.Lifecycle.State.CREATED;
import static androidx.lifecycle.Lifecycle.State.DESTROYED;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/12/23
 *     desc  : 自定义ExternalLiveData<T>
 *     revise: 主要是重写observe方法，具体可以参考LiveData中的observe源码
 * </pre>
 */
public class ExternalLiveData<T> extends MutableLiveData<T> {

    public static final int START_VERSION = LiveData.START_VERSION;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            // ignore
            return;
        }
        try {
            //use ExternalLifecycleBoundObserver instead of LifecycleBoundObserver
            LifecycleBoundObserver wrapper = new ExternalLifecycleBoundObserver(owner, observer);
            LifecycleBoundObserver existing = (LifecycleBoundObserver) callMethodPutIfAbsent(observer, wrapper);
            if (existing != null && !existing.isAttachedTo(owner)) {
                throw new IllegalArgumentException("Cannot add the same observer"
                        + " with different lifecycles");
            }
            if (existing != null) {
                return;
            }
            owner.getLifecycle().addObserver(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getVersion() {
        return super.getVersion();
    }

    public Lifecycle.State observerActiveLevel() {
        return CREATED;
    }

    class ExternalLifecycleBoundObserver extends LifecycleBoundObserver {

        ExternalLifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<? super T> observer) {
            super(owner, observer);
        }

        @Override
        boolean shouldBeActive() {
            return mOwner.getLifecycle().getCurrentState().isAtLeast(observerActiveLevel());
        }
    }

    private Object getFieldObservers() throws Exception {
        Field fieldObservers = LiveData.class.getDeclaredField("mObservers");
        fieldObservers.setAccessible(true);
        return fieldObservers.get(this);
    }

    private Object callMethodPutIfAbsent(Object observer, Object wrapper) throws Exception {
        Object mObservers = getFieldObservers();
        Class<?> classOfSafeIterableMap = mObservers.getClass();
        Method putIfAbsent = classOfSafeIterableMap.getDeclaredMethod("putIfAbsent",
                Object.class, Object.class);
        putIfAbsent.setAccessible(true);
        return putIfAbsent.invoke(mObservers, observer, wrapper);
    }
}
