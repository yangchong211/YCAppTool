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
package com.yc.webviewlib.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.ycbjie.webviewlib.R;
import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 自定义带进度条的webView
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public class ProgressWebView extends FrameLayout {

    private X5WebView webView;
    private String urlTitle;

    public ProgressWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ProgressWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_progress_web_view, this, false);
        webView = view.findViewById(R.id.web_view);
        final WebProgress pbProgress = view.findViewById(R.id.progress);
        pbProgress.show();
        pbProgress.setColor(Color.BLUE);
        webView.getX5WebChromeClient().setWebListener(new DefaultWebListener() {
            @Override
            public void hindProgressBar() {
                pbProgress.setVisibility(GONE);
            }

            @Override
            public void showErrorView(@X5WebUtils.ErrorType int type) {
                switch (type){
                    //没有网络
                    case X5WebUtils.ErrorMode.NO_NET:
                        break;
                    //404，网页无法打开
                    case X5WebUtils.ErrorMode.STATE_404:

                        break;
                    //onReceivedError，请求网络出现error
                    case X5WebUtils.ErrorMode.RECEIVED_ERROR:

                        break;
                    //在加载资源时通知主机应用程序发生SSL错误
                    case X5WebUtils.ErrorMode.SSL_ERROR:

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void startProgress(int newProgress) {
                pbProgress.setWebProgress(newProgress);
            }

            @Override
            public void showTitle(String title) {
                urlTitle = title;
            }
        });
    }

    /**
     * 获取X5WebView对象
     * @return                                  获取X5WebView对象
     */
    public X5WebView getWebView(){
        return webView;
    }

    /**
     * 获取监听到url的标题
     * @return
     */
    public String getTitle(){
        return urlTitle;
    }

}
