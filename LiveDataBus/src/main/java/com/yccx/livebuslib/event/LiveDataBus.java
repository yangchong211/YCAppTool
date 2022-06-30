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
package com.yccx.livebuslib.event;


import com.yccx.livebuslib.data.BusMutableLiveData;
import com.yccx.livebuslib.utils.BusLibUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/12/23
 *     desc  : liveDataBus事件总线
 *     revise:
 * </pre>
 */
public final class LiveDataBus {

    private final Map<String, BusMutableLiveData<Object>> bus;
    /**
     * 生命周期观察者总是活动的默认是true
     */
    private boolean lifecycleObserverAlwaysActive = true;

    private LiveDataBus() {
        bus = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final LiveDataBus DEFAULT_BUS = new LiveDataBus();
    }

    public static LiveDataBus get() {
        return SingletonHolder.DEFAULT_BUS;
    }

    public synchronized <T> BusMutableLiveData<T> with(String key, Class<T> type) {
        BusLibUtils.checkNull(key);
        BusLibUtils.checkNull(type);
        if (!bus.containsKey(key)) {
            bus.put(key, new BusMutableLiveData<>(key));
        }
        return (BusMutableLiveData<T>) bus.get(key);
    }

    public BusMutableLiveData<Object> with(String key) {
        BusLibUtils.checkNull(key);
        return with(key, Object.class);
    }

    public Map<String, BusMutableLiveData<Object>> getBus() {
        return bus;
    }

    /**
     * 设置生命周期观察者总是活动
     * @param active                            boolean数据
     */
    public void lifecycleObserverAlwaysActive(boolean active) {
        lifecycleObserverAlwaysActive = active;
    }

    public boolean isLifecycleObserverAlwaysActive() {
        return lifecycleObserverAlwaysActive;
    }
}
