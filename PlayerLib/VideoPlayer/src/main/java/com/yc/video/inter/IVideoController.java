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

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : 视频控制器接口
 *     revise: 定义一些设置视图属性接口
 * </pre>
 */
public interface IVideoController {

    /**
     * 开始控制视图自动隐藏倒计时
     */
    void startFadeOut();

    /**
     * 取消控制视图自动隐藏倒计时
     */
    void stopFadeOut();

    /**
     * 控制视图是否处于显示状态
     */
    boolean isShowing();

    /**
     * 设置锁定状态
     * @param locked 是否锁定
     */
    void setLocked(boolean locked);

    /**
     * 是否处于锁定状态
     */
    boolean isLocked();

    /**
     * 开始刷新进度
     */
    void startProgress();

    /**
     * 停止刷新进度
     */
    void stopProgress();

    /**
     * 显示控制视图
     */
    void hide();

    /**
     * 隐藏控制视图
     */
    void show();

    /**
     * 是否需要适配刘海
     */
    boolean hasCutout();

    /**
     * 获取刘海的高度
     */
    int getCutoutHeight();

    /**
     * 控制器意外销毁，比如手动退出，意外崩溃等等
     */
    void destroy();
}
