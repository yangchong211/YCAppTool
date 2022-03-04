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

package com.yc.webviewlib.inter;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : web的接口回调，主要是视频相关回调，比如全频，取消全频，隐藏和现实webView
 *     revise:
 * </pre>
 */
public interface VideoWebListener {

    /**
     * 显示全屏view
     */
    void showVideoFullView();

    /**
     * 隐藏全屏view
     */
    void hindVideoFullView();

    /**
     * 显示webview
     */
    void showWebView();

    /**
     * 隐藏webview
     */
    void hindWebView();

}
