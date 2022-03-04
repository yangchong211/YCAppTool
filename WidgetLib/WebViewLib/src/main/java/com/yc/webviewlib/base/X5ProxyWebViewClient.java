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

import android.webkit.ValueCallback;

import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension;
import com.tencent.smtt.sdk.WebView;
import com.yc.webviewlib.inter.InterExtensionListener;

public class X5ProxyWebViewClient extends ProxyWebViewClientExtension {

    private InterExtensionListener extensionListener;
    private WebView mWebView;

    public X5ProxyWebViewClient(InterExtensionListener extensionListener) {
        this.extensionListener = extensionListener;
    }

    /**
     * 当js在非用户操作下打开新页面被内核拦截且宿主也不允许当前页面自动打开时，对应页面会被内核阻止
     * 如果在IX5WebSettingsExtension设置了setJavaScriptOpenWindowsBlockedNotifyEnabled为true时
     * 此时会回调宿主当前拦截的所有页面是否允许被打开
     * 如果callback回调true，后续对应页面的弹窗再被拦截时则会直接按照此授权处理,不再回调该接口通知宿主
     * 如果callback回调false，页面下次弹出窗口被拦截仍会通知宿主，但此时hadAllowShow的值为true
     * @param host 页面域名
     * @param blockedUrlList 被阻止打开的页面url列表
     * @param callback true:打开拦截页面且后续不再拦截，false:打开宿主页面后续继续拦截,如果已经做过该操作,则后续回调接口中hadAllowShow为true
     * @param hadAllowShow 是否允许展示过该host的弹出窗口，当曾经设置过callback<false>时该值为true,否则为false
     * @return 宿主处理了该接口返回true，否则返回false
     */
    @Override
    public boolean notifyJavaScriptOpenWindowsBlocked(String host, String[] blockedUrlList,
                                                      ValueCallback<Boolean> callback, boolean hadAllowShow) {
        return super.notifyJavaScriptOpenWindowsBlocked(host, blockedUrlList, callback, hadAllowShow);
    }

    @Override
    public void onPromptScaleSaved() {
        super.onPromptScaleSaved();
        if (extensionListener!=null){
            extensionListener.onPromptScaleSaved();
        }
    }


}
