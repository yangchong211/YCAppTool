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
package com.yc.videosurface;

import android.graphics.Bitmap;
import android.view.View;
import androidx.annotation.NonNull;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 自定义渲染view接口
 *     revise:
 * </pre>
 */
public interface ISurfaceView {

    /**
     * 关联AbstractPlayer
     * @param iPlayerSurface                        iPlayerSurface
     */
    void attachToPlayer(@NonNull IPlayerSurface iPlayerSurface);

    /**
     * 设置视频宽高
     * @param videoWidth                    宽
     * @param videoHeight                   高
     */
    void setVideoSize(int videoWidth, int videoHeight);

    /**
     * 设置视频旋转角度
     * @param degree                        角度值
     */
    void setVideoRotation(int degree);

    /**
     * 设置screen scale type
     * @param scaleType                     类型
     */
    void setScaleType(int scaleType);

    /**
     * 获取真实的RenderView
     * @return                              view
     */
    View getView();

    /**
     * 截图
     * @return                              bitmap
     */
    Bitmap doScreenShot();

    /**
     * 释放资源
     */
    void release();

}