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
package com.yc.activitymanager.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : <a href="https://github.com/yangchong211">...</a>
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : activity生命周期
 *     revise:
 * </pre>
 */
public abstract class AbsLifecycleListener implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(@Nullable Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@Nullable Activity activity) {

    }

    @Override
    public void onActivityResumed(@Nullable Activity activity) {

    }

    @Override
    public void onActivityPaused(@Nullable Activity activity) {

    }

    @Override
    public void onActivityStopped(@Nullable Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@Nullable Activity activity, @Nullable Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@Nullable Activity activity) {

    }
}
