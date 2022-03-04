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

import androidx.annotation.IntRange;

import com.yc.webviewlib.utils.X5WebUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : web的接口回调，包括常见状态页面切换，进度条变化等
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public interface InterWebListener {

    /**
     * 隐藏进度条
     */
    void hindProgressBar();

    /**
     * 展示异常页面
     * @param type                           异常类型
     */
    void showErrorView(@X5WebUtils.ErrorType int type);

    /**
     * 进度条变化时调用，这里添加注解限定符，必须是在0到100之间
     * @param newProgress                   进度0-100
     */
    void startProgress(@IntRange(from = 0,to = 100) int newProgress);

    /**
     * 获取加载网页的标题
     * @param title                         title标题
     */
    void showTitle(String title);

    /**
     * 加载完成
     * @param url                           连接
     */
    void onPageFinished(String url);
}
