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

import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.yc.video.bridge.ControlWrapper;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/9
 *     desc  : 自定义控制器接口
 *     revise: 如果需要添加自定义播放器视图，则需要继承InterControlView接口
 *             关于视频播放器播放状态和视图状态，都需要自定义视图去控制view的状态
 *             举一个例子：比如广告视图，
 * </pre>
 */
public interface IControlView {

    /**
     * 这个是绑定视图操作
     * @param controlWrapper                自定义控制器包装类
     */
    void attach(@NonNull ControlWrapper controlWrapper);

    /**
     * 获取该自定义视图view对象
     * @return                              视图view对象
     */
    View getView();

    /**
     * 视图显示发生变化监听
     * @param isVisible                     是否可见
     * @param anim                          动画
     */
    void onVisibilityChanged(boolean isVisible, Animation anim);

    /**
     * 播放状态
     * -1               播放错误
     * 0                播放未开始
     * 1                播放准备中
     * 2                播放准备就绪
     * 3                正在播放
     * 4                暂停播放
     * 5                正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
     * 6                暂停缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
     * 7                播放完成
     * 8                开始播放中止
     * @param playState                     播放状态，主要是指播放器的各种状态
     */
    void onPlayStateChanged(int playState);

    /**
     * 播放模式
     * 普通模式，小窗口模式，正常模式三种其中一种
     * MODE_NORMAL              普通模式
     * MODE_FULL_SCREEN         全屏模式
     * MODE_TINY_WINDOW         小屏模式
     * @param playerState                   播放模式
     */
    void onPlayerStateChanged(int playerState);

    /**
     * 设置进度操作
     * @param duration                      时间
     * @param position                      进度position
     */
    void setProgress(int duration, int position);

    /**
     * 锁屏状态监听
     * @param isLocked                      是否锁屏
     */
    void onLockStateChanged(boolean isLocked);

}
