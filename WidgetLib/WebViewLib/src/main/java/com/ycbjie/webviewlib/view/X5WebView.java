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
package com.ycbjie.webviewlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebBackForwardList;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebHistoryItem;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.ycbjie.webviewlib.base.X5WebChromeClient;
import com.ycbjie.webviewlib.base.X5WebViewClient;
import com.ycbjie.webviewlib.client.JsX5WebViewClient;
import com.ycbjie.webviewlib.helper.SaveImageProcessor;
import com.ycbjie.webviewlib.inter.InterValueCallback;
import com.ycbjie.webviewlib.tools.WebViewException;
import com.ycbjie.webviewlib.utils.X5LogUtils;
import com.ycbjie.webviewlib.utils.X5WebUtils;

import static android.os.Build.VERSION_CODES.KITKAT;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 自定义x5的webView
 *     revise: 可以使用这个类，方便统一初始化WebSettings的一些属性
 *             如果不用这里的，想单独初始化setting属性，也可以直接使用BridgeWebView
 *             demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public class X5WebView extends BridgeWebView {

    private X5WebViewClient x5WebViewClient;
    private X5WebChromeClient x5WebChromeClient;
    private volatile boolean mInitialized;
    public static boolean isLongClick = true;

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mInitialized = false;
    }

    public X5WebView(Context arg0) {
        this(arg0,null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        initWebViewSettings();
        if (getCustomWebViewClient() == null){
            x5WebViewClient = new JsX5WebViewClient(this , getContext());
            this.setWebViewClient(x5WebViewClient);
        } else {
            this.setWebViewClient(getCustomWebViewClient());
        }
        if (getCustomWebViewClient() == null){
            x5WebChromeClient = new X5WebChromeClient(this, getContext());
            this.setWebChromeClient(x5WebChromeClient);
        } else {
            this.setWebChromeClient(getCustomWebChromeClient());
        }
        //设置可以点击
        this.getView().setClickable(true);
        mInitialized = true;
        initListener();
    }

    /**
     * 做一些公共的初始化操作
     */
    private void initWebViewSettings() {
        WebSettings ws = this.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        // 设置内置的缩放控件。若为false，则该WebView不可缩放
        ws.setBuiltInZoomControls(true);
        // 隐藏原生的缩放控件
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        // 缓存模式如下：
        // LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        // LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        // LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        // LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setAppCacheMaxSize(Long.MAX_VALUE);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 告诉WebView启用JavaScript执行。默认的是false。
        // 注意：这个很重要   如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        //ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        //防止中文乱码
        ws.setDefaultTextEncodingName("UTF-8");
        /*
         * 排版适应屏幕
         * 用WebView显示图片，可使用这个参数
         * 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否新窗口打开(加了后可能打不开网页)
        //ws.setSupportMultipleWindows(true);
        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置字体默认缩放大小
        ws.setTextZoom(100);
        // 不缩放
        this.setInitialScale(100);
        if(Build.VERSION.SDK_INT >= KITKAT) {
            //设置网页在加载的时候暂时不加载图片
            ws.setLoadsImagesAutomatically(true);
        } else {
            ws.setLoadsImagesAutomatically(false);
        }
        //默认关闭硬件加速
        setOpenLayerType(false);
        //默认不开启密码保存功能
        setSavePassword(false);
        //移除高危风险js监听
        setRemoveJavascriptInterface();
        //设置白天模式
        setDayOrNight(true);
    }

    /**
     * 设置字体大小
     * @param fontSize                      字体大小
     */
    public void setTextSize(int fontSize){
        WebSettings settings = this.getSettings();
        settings.setSupportZoom( true);
        switch (fontSize) {
            case  1:
                settings.setTextSize(WebSettings.TextSize.SMALLEST);
                break;
            case  2:
                settings.setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case  3:
                settings.setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case  4:
                settings.setTextSize(WebSettings.TextSize.LARGER);
                break;
            case  5:
                settings.setTextSize(WebSettings.TextSize.LARGEST);
                break;
            default:
                settings.setTextSize(WebSettings.TextSize.NORMAL);
                break;
        }
    }

    /**
     * 通过屏幕密度调整分辨率
     */
    public void setDensityZoom(){
        WebSettings settings = this.getSettings();
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                //75
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                //100
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                //150
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        settings.setDefaultZoom(zoomDensity);
    }

    private void initListener() {
        if (isLongClick){
            this.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // 这里不能像普通WebView一样将view强转为WebView然后获取HitTestResult，因为x5传来的view不是
                    // 标准的WebView
                    WebView.HitTestResult result = X5WebView.this.getHitTestResult();
                    if (result == null) {
                        return false;
                    }
                    int type = result.getType();
                    if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        // 图片
                        return new SaveImageProcessor().showActionMenu(X5WebView.this);
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 开发者可以自己实现该方法
     * @return
     */
    public X5WebViewClient getCustomWebViewClient(){
        return null;
    }

    /**
     * 开发者可以自己实现该方法
     * @return
     */
    public X5WebChromeClient getCustomWebChromeClient(){
        return null;
    }


    /**
     * 获取设置的X5WebChromeClient对象
     * @return                          X5WebChromeClient对象
     */
    public X5WebChromeClient getX5WebChromeClient(){
        if (getCustomWebViewClient() == null){
            return this.x5WebChromeClient;
        }
        return getCustomWebChromeClient();
    }

    /**
     * 获取设置的X5WebViewClient对象
     * @return                          X5WebViewClient对象
     */
    public X5WebViewClient getX5WebViewClient(){
        if (getCustomWebViewClient() == null){
            return this.x5WebViewClient;
        }
        return getCustomWebViewClient();
    }


    @Override
    public void setWebChromeClient(WebChromeClient webChromeClient) {
        if (webChromeClient!=null){
            if (webChromeClient instanceof X5WebChromeClient){
                super.setWebChromeClient(webChromeClient);
            } else {
                throw new WebViewException("please use client must be extends X5WebChromeClient");
            }
        } else {
            super.setWebChromeClient(null);
        }
    }

    @Override
    public void setWebViewClient(WebViewClient webViewClient) {
        if (webViewClient!=null){
            if (webViewClient instanceof JsX5WebViewClient){
                super.setWebViewClient(webViewClient);
            } else {
                throw new WebViewException("please use client must be extends JsX5WebViewClient");
            }
        } else {
            super.setWebViewClient(null);
        }
    }

    /**
     * 设置是否自定义视频视图
     * @param isShowCustomVideo         设置是否自定义视频视图
     */
    public void setShowCustomVideo(boolean isShowCustomVideo){
        getX5WebChromeClient().setShowCustomVideo(isShowCustomVideo);
    }

    /**
     * 刷新界面可以用这个方法
     */
    public void reLoadView(){
        //防止短时间多次触发load方法
        X5WebView.super.reload();
    }

    /**
     * 是否设置地理位置(Geolocation)
     *
     * 注意需要添加以下权限，并且运行开启定位权限
     * <uses-permission android:name="android.permission.INTERNET"/>
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
     * @param isEnable                  布尔值
     */
    public void setGeolocationEnabled(boolean isEnable){
        if (isEnable){
            //启用数据库
            this.getSettings().setDatabaseEnabled(true);
            //启用地理定位，默认为true
            this.getSettings().setGeolocationEnabled(true);
            //设置定位的数据库路径
            String dir = this.getContext().getDir("database", Context.MODE_PRIVATE).getPath();
            //设置缓存定位路径
            this.getSettings().setGeolocationDatabasePath(dir);
        } else {
            this.getSettings().setGeolocationEnabled(false);
        }
    }

    /**
     * 是否开启软硬件加速
     * @param layerType                布尔值
     */
    public void setOpenLayerType(boolean layerType){
        if (layerType){
            //开启软硬件加速，开启软硬件加速这个性能提升还是很明显的，但是会耗费更大的内存 。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        }
    }

    /**
     * WebView返回上一页.
     * 1.强制在{@link WebView}初始化的前内不能关闭所在的Activity,
     * 否则在Android6.x的手机上频繁快速打开/关闭所在的Activity会造成chrome内核崩溃, 崩溃后除非kill进程否则一直是白屏.
     * 2.在Android4.4及以下版本的webview会出现调用{@link #goBack()}失效的问题, 这边采用{@link #loadUrl(String)}自建URL栈来处理这个问题.
     *
     * @return True 表示处理返回成功
     */
    public final boolean pageGoBack() {
        if (mInitialized) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                return getX5WebViewClient().pageGoBack(this);
            } else {
                if (!pageCanGoBack()) {
                    return false;
                } else if (X5WebUtils.shouldSkipUrl(getPreviousUrl())) {
                    goBackOrForward(-2);
                } else {
                    goBack();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断以前Url是否是有效的
     * @return                          是否是有效的
     */
    public boolean validPreviousUrl() {
        //获取webView加载栈
        WebBackForwardList list = this.copyBackForwardList();
        //获取当前加载在加载栈中的位置
        final int curIndex = list.getCurrentIndex();
        //获取上一个索引的位置
        final int preIndex = curIndex > 0 ? curIndex - 1 : -1;
        if (preIndex >= 0) {
            //获取加载栈中第index页面
            WebHistoryItem item = list.getItemAtIndex(preIndex);
            return item != null && (!X5WebUtils.shouldSkipUrl(item.getUrl()) || preIndex != 0);
        }
        return false;
    }

    /**
     * 获取先前的url
     * @return                          先前的url
     */
    public String getPreviousUrl() {
        //获取webView加载栈
        WebBackForwardList list = this.copyBackForwardList();
        //获取当前加载在加载栈中的位置
        final int curIndex = list.getCurrentIndex();
        //获取上一个索引的位置
        final int preIndex = curIndex > 0 ? curIndex - 1 : -1;
        if (preIndex >= 0) {
            //获取加载栈中第index页面
            WebHistoryItem item = list.getItemAtIndex(preIndex);
            return item != null ? item.getUrl() : null;
        }
        return null;
    }

    /**
     * 是否能返回上一页.
     * 配合{@link #pageGoBack}使用.
     *
     * @return True 可以返回上一页
     */
    public final boolean pageCanGoBack() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return getX5WebViewClient().pageCanGoBack();
        } else {
            return validPreviousUrl() && this.canGoBack();
        }
    }

    /**
     * WebView 默认开启密码保存功能，但是存在漏洞。
     * 如果该功能未关闭，在用户输入密码时，会弹出提示框，询问用户是否保存密码，如果选择”是”，
     * 密码会被明文保到 /data/data/com.package.name/databases/webview.db 中，这样就有被盗取密码的危险
     * @param save
     */
    public void setSavePassword(boolean save){
        if (save){
            this.getSettings().setSavePassword(true);
        } else {
            this.getSettings().setSavePassword(false);
        }
    }

    /**
     * 在4.2之前，js存在漏洞。不过现在4.2的手机很少了
     */
    private void setRemoveJavascriptInterface() {
        this.removeJavascriptInterface("searchBoxJavaBridge_");
        this.removeJavascriptInterface("accessibility");
        this.removeJavascriptInterface("accessibilityTraversal");
    }

    /**
     * 销毁时候调用该方法
     */
    public void destroy() {
        try {
            //有音频播放的web页面的销毁逻辑
            //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
            //但是注意：webview调用destory时,webview仍绑定在Activity上
            //这是由于自定义webview构建时传入了该Activity的context对象
            //因此需要先从父容器中移除webview,然后再销毁webview:
            ViewGroup parent = (ViewGroup) getParent();
            if (parent != null) {
                parent.removeView(this);
            }
            removeAllViews();
            destroyDrawingCache();
            clearCache(true);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            super.destroy();
        }
    }


    /**
     * 可以等页面加载完成后，执行Js代码，找到底部栏并将其隐藏
     * 如何找h5页面元素：
     *      在H5页面中找到某个元素还是有很多方法的，比如getElementById()、getElementsByClassName()、
     *      getElementsByTagName()等，具体根据页面来选择
     * 隐藏底h5的东西
     *      这个主要找到需要隐藏内容标签，可以找div中的class，然后反射拿到该方法，调用隐藏逻辑
     * 步骤操作如下：
     * 1.首先通过getElementByClassName方法找到'class'为'xxx'的所有元素，返回的是一个数组，
     * 2.在这个页面中，只有底部栏的'class'为'xxx'，所以取数组中的第一个元素对应的就是底部栏元素
     * 3.然后将底部栏的display属性设置为'none'，表示底部栏不显示，这样就可以将底部栏隐藏
     *
     * 可能存在问题：
     * onPageFinished没有执行，导致这段代码没有走
     */
    public void hideHtmlDivView(String className) {
        try {
            //定义javaScript方法
            String javascript = "javascript:function hideDiv() { "
                    + "document.getElementsByClassName('" +className+
                    "')[0].style.display='none'}";
            //加载方法
            this.loadUrl(javascript);
            //执行方法
            this.loadUrl("javascript:hideDiv();");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏html某个div标签的内容
     * @param className                             div标签className
     */
    public void hideHtmlDivContent(String className) {
        try {
            //定义javaScript方法
            String javascript = "javascript:function hideDiv() { "
                    + "document.getElementsByClassName('" +className+
                    "')[0].style.display='none'}";
            //加载方法
            this.evaluateJavascript(javascript);
            //执行方法
            this.evaluateJavascript("javascript:hideDiv();");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取html某个div标签的内容
     * @param className                             div标签className
     * @param callback                              回调数据
     */
    @Deprecated
    public void getHtmlDivView(String className , final InterValueCallback<String> callback) {
        try {
            //定义javaScript方法
            String javaScript = "javascript:function getDivContent() { "
                    + "return document.getElementsByClassName('" + className + "')[0].innerText; }";
            //加载方法
            this.loadUrl(javaScript);
            //执行方法
            this.evaluateJavascript("javascript:getDivContent();", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    //此处为 js 返回的结果
                    X5LogUtils.i("抓包返回数据---"+s);
                    if (callback!=null){
                        callback.onReceiveValue(s);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取html某个div标签的内容
     * @param className                             div标签className
     * @param callback                              回调数据
     */
    public void getHtmlDivContent(String className , final InterValueCallback<String> callback) {
        try {
            //定义javaScript方法
            String javaScript = "javascript:function getDivContent() { "
                    + "return document.getElementsByClassName('" + className + "')[0].innerText; }";
            //加载方法
            this.evaluateJavascript(javaScript);
            //执行方法
            this.evaluateJavascript("javascript:getDivContent();", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    //此处为 js 返回的结果
                    X5LogUtils.i("抓包返回数据---"+s);
                    if (callback!=null){
                        callback.onReceiveValue(s);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
