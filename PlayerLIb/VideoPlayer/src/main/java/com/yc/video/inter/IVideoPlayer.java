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
package com.yc.video.inter;

import android.graphics.Bitmap;

import com.yc.video.config.ConstantKeys;
import com.yc.videosurface.MeasureHelper;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : VideoPlayer抽象接口
 *     revise: 播放器基础属性获取和设置属性接口
 * </pre>
 */
public interface IVideoPlayer extends IVideoPlayerScreen{

    /**
     * 设置链接
     * @param url                           url
     */
    void setUrl(String url);

    /**
     * 获取播放链接
     * @return                              链接
     */
    String getUrl();

    /**
     * 开始播放
     */
    void start();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 获取视频总时长
     * @return                              long类型
     */
    long getDuration();

    /**
     * 获取当前播放的位置
     * @return                              long类型
     */
    long getCurrentPosition();

    /**
     * 调整播放进度
     * @param pos                           位置
     */
    void seekTo(long pos);

    /**
     * 是否处于播放状态
     * @return                              是否处于播放状态
     */
    boolean isPlaying();

    /**
     * 获取当前缓冲百分比
     * @return                              百分比
     */
    int getBufferedPercentage();

    /**
     * 设置是否静音
     * @param isMute                        是否静音
     */
    void setMute(boolean isMute);

    /**
     * 是否静音
     * @return                              布尔值
     */
    boolean isMute();

    void setScreenScaleType(@MeasureHelper.ScreenScaleType int screenScaleType);

    void setSpeed(float speed);

    float getSpeed();

    long getTcpSpeed();

    void replay(boolean resetPosition);

    void setMirrorRotation(boolean enable);

    int[] getVideoSize();

    void setRotation(float rotation);

}
