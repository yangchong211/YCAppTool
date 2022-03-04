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
package com.yc.webviewlib.base;

import android.os.Message;
import android.webkit.ValueCallback;

import com.tencent.smtt.export.external.extension.proxy.ProxyWebChromeClientExtension;
import com.tencent.smtt.sdk.WebView;
import com.yc.webviewlib.inter.InterExtensionListener;

public class X5ProxyWebChromeClient extends ProxyWebChromeClientExtension {

    private InterExtensionListener extensionListener;
    private WebView mWebView;

    public X5ProxyWebChromeClient(InterExtensionListener extensionListener) {
        this.extensionListener = extensionListener;
    }

    /**
     *页面前进后退切换完成事件通知，目前step无实际赋值，此接口只是一个完成通知
     */
    @Override
    public void onBackforwardFinished(int i) {
        super.onBackforwardFinished(i);
        if (extensionListener!=null){
            extensionListener.onBackforwardFinished(i);
        }
    }

    /*
     * callback：处理后的回调；
     * schemePlusHost：域名；
     * username：用户名；
     * password：密码；
     * nameElement：用户名输入框名称；
     * passwordElement：密码输入框名称；
     * isReplace：是否是替换操作
     */
    @Override
    public boolean onSavePassword(ValueCallback<String> callback,
            String schemePlusHost, String username, String password,
            String nameElement, String passwordElement, boolean isReplace) {
        //这里可以弹窗提示用户
        //这里调用将会保存用户名和密码，如果只保存用户名可以将密码置为null，如果两者均不存在则不需要调用该接口
        mWebView.getX5WebViewExtension()
                .sendRememberMsg(schemePlusHost, username, password, nameElement, passwordElement);
        //处理完后需要回调该接口，执行了保存操作参数为true，否则为false
        callback.onReceiveValue("true");
        //这里要返回true，否则内核会提示用户保存密码
        return true;
    }

    @Override
    public boolean onSavePassword(String s, String s1, String s2, boolean b, Message message) {
        return super.onSavePassword(s, s1, s2, b, message);
    }





}
