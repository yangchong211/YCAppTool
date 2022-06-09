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
package com.yc.kernel.inter;

import com.yc.kernel.utils.PlayerConstant;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 播放器event监听
 *     revise:
 * </pre>
 */
public interface VideoPlayerListener {

    /**
     * 异常
     * 1          表示错误的链接
     * 2          表示解析异常
     * 3          表示其他的异常
     * @param type                          错误类型
     */
    void onError(@PlayerConstant.ErrorType int type , String error);

    /**
     * 完成
     */
    void onCompletion();

    /**
     * 视频信息
     * @param what                          what
     * @param extra                         extra
     */
    void onInfo(int what, int extra);

    /**
     * 准备
     */
    void onPrepared();

    /**
     * 视频size变化监听
     * @param width                         宽
     * @param height                        高
     */
    void onVideoSizeChanged(int width, int height);


}
