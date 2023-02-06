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
package com.yc.video.controller;

import android.content.Context;
import android.view.OrientationEventListener;

/**
 * 设备方向监听
 */
public class OrientationHelper extends OrientationEventListener {

    private long mLastTime;

    private OnOrientationChangeListener mOnOrientationChangeListener;

    public OrientationHelper(Context context) {
        super(context);
    }

    @Override
    public void onOrientationChanged(int orientation) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastTime < 500) {
            return;
        }
        //500毫秒检测一次
        if (mOnOrientationChangeListener != null) {
            mOnOrientationChangeListener.onOrientationChanged(orientation);
        }
        mLastTime = currentTime;
    }


    public interface OnOrientationChangeListener {
        void onOrientationChanged(int orientation);
    }

    public void setOnOrientationChangeListener(OnOrientationChangeListener onOrientationChangeListener) {
        mOnOrientationChangeListener = onOrientationChangeListener;
    }
}
