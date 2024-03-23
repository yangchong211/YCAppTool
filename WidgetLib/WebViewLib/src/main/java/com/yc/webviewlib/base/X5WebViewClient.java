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

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;

import android.text.TextUtils;
import android.view.KeyEvent;

import com.tencent.smtt.export.external.interfaces.ClientCertRequest;
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yc.networklib.net.AppNetworkUtils;
import com.yc.toolutils.component.AppActivityUtils;
import com.yc.webviewlib.helper.WebSchemeIntent;
import com.yc.webviewlib.inter.InterWebListener;
import com.yc.toolutils.AppEncodeUtils;
import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.utils.X5WebUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 自定义x5的WebViewClient
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 *
 *             作用：主要辅助 WebView 处理JavaScript 的对话框、网站 Logo、网站 title、load 进度等处理
 *             demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public class X5WebViewClient extends WebViewClient {

    private InterWebListener webListener;
    private final WebView webView;
    private final Context context;
    /**
     * 是否加载完毕
     */
    private boolean isLoadFinish = false;

    /**
     * 判断页面是否正在加载
     * 在onPageStarted中为true，在onPageFinished中为false
     */
    private boolean mIsLoading = false;
    /**
     * 获取是否加载完毕
     *
     * @return 布尔值
     */
    public boolean isLoadFinish() {
        return isLoadFinish;
    }

    /**
     * 设置监听时间，包括常见状态页面切换，进度条变化等
     *
     * @param listener listener
     */
    public void setWebListener(InterWebListener listener) {
        this.webListener = listener;
    }

    /**
     * 构造方法
     *
     * @param webView 需要传进来webview
     * @param context 上下文
     */
    public X5WebViewClient(WebView webView, Context context) {
        this.context = context;
        this.webView = webView;
        //将js对象与java对象进行映射
        //webView.addJavascriptInterface(new ImageJavascriptInterface(context), "imagelistener");
    }

    /**
     * 这个方法中可以做拦截
     * 主要的作用是处理各种通知和请求事件
     * <p>
     * 不准确的说法如下：
     * 1.返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
     * 2.返回: return true; 表示webView处理url是根据程序来执行的。 返回: return false; 表示webView处理url是在webView内部执行。
     * 3.还有一种错误说法：WebView上的所有加载都经过这个方法。
     * <p>
     * 准确说法，该方法说明如下所示：
     * 若没有设置 WebViewClient 则由系统（Activity Manager）处理该 url，通常是使用浏览器打开或弹出浏览器选择对话框。
     * 若设置 WebViewClient 且该方法返回 true ，则说明由应用的代码处理该 url，WebView 不处理，也就是程序员自己做处理。
     * 若设置 WebViewClient 且该方法返回 false，则说明由 WebView 处理该 url，即用 WebView 加载该 url。
     *
     * @param view view
     * @param url  链接
     * @return 是否自己处理，true表示自己处理
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //页面关闭后，直接返回，不要执行网络请求和js方法
        boolean activityAlive = AppActivityUtils.isActivityAlive(context);
        if (!activityAlive) {
            return false;
        }
        url = AppEncodeUtils.urlDecode(url);
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        /*WebView.HitTestResult hitTestResult = null;
        if (url.startsWith("http:") || url.startsWith("https:")){
            hitTestResult = view.getHitTestResult();
        }*/
        final Uri uri = Uri.parse(url);
        //scheme跳转支持
        if (uri != null && uri.getScheme() != null && WebSchemeIntent.isSilentType(uri.getScheme())) {
            return WebSchemeIntent.handleSilently(context, uri);
        }
        WebView.HitTestResult hitTestResult = view.getHitTestResult();
        if (hitTestResult == null) {
            return false;
        }
        //HitTestResult 描述
        //WebView.HitTestResult.UNKNOWN_TYPE 未知类型
        //WebView.HitTestResult.PHONE_TYPE 电话类型
        //WebView.HitTestResult.EMAIL_TYPE 电子邮件类型
        //WebView.HitTestResult.GEO_TYPE 地图类型
        //WebView.HitTestResult.SRC_ANCHOR_TYPE 超链接类型
        //WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE 带有链接的图片类型
        //WebView.HitTestResult.IMAGE_TYPE 单纯的图片类型
        //WebView.HitTestResult.EDIT_TEXT_TYPE 选中的文字类型
        if (hitTestResult.getType() == WebView.HitTestResult.UNKNOWN_TYPE) {
            return false;
        }
        boolean handleAlive = WebSchemeIntent.handleAlive(context, uri);
        if (handleAlive) {
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    /**
     * 增加shouldOverrideUrlLoading在api>=24时
     * 主要的作用是处理各种通知和请求事件
     * 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
     *
     * @param view    view
     * @param request request，添加于API21，封装了一个Web资源的请求信息，
     *                包含：请求地址，请求方法，请求头，是否主框架，是否用户点击，是否重定向
     * @return
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        boolean activityAlive = AppActivityUtils.isActivityAlive(context);
        if (!activityAlive) {
            return false;
        }
        String url = request.getUrl().toString();
        url = AppEncodeUtils.urlDecode(url);
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        final Uri uri = Uri.parse(url);
        //scheme跳转支持
        if (uri != null && uri.getScheme() != null && WebSchemeIntent.isSilentType(uri.getScheme())) {
            return WebSchemeIntent.handleSilently(context, uri);
        }
        /*WebView.HitTestResult hitTestResult = null;
        if (url.startsWith("http:") || url.startsWith("https:")){
            hitTestResult = view.getHitTestResult();
        }*/
        WebView.HitTestResult hitTestResult = view.getHitTestResult();
        if (hitTestResult == null) {
            return false;
        }
        //HitTestResult 描述
        //WebView.HitTestResult.UNKNOWN_TYPE 未知类型
        //WebView.HitTestResult.PHONE_TYPE 电话类型
        //WebView.HitTestResult.EMAIL_TYPE 电子邮件类型
        //WebView.HitTestResult.GEO_TYPE 地图类型
        //WebView.HitTestResult.SRC_ANCHOR_TYPE 超链接类型
        //WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE 带有链接的图片类型
        //WebView.HitTestResult.IMAGE_TYPE 单纯的图片类型
        //WebView.HitTestResult.EDIT_TEXT_TYPE 选中的文字类型
        if (hitTestResult.getType() == WebView.HitTestResult.UNKNOWN_TYPE) {
            return false;
        }
        boolean handleAlive = WebSchemeIntent.handleAlive(context, uri);
        if (handleAlive) {
            return true;
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    /**
     * 作用：开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
     *
     * @param webView view
     * @param url     url
     * @param bitmap  bitmap
     */
    @Override
    public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
        super.onPageStarted(webView, url, bitmap);
        //设定加载开始的操作
        X5LogUtils.i("-------onPageStarted-------" + url);
        if (!AppNetworkUtils.isConnected() && webListener != null) {
            //显示异常页面
            webListener.showErrorView(X5WebUtils.ErrorMode.NO_NET);
        }
        isLoadFinish = false;
        mIsLoading = true;
    }

    /**
     * 当页面加载完成会调用该方法
     *
     * @param view view
     * @param url  url链接
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        X5LogUtils.i("-------onPageFinished-------" + url);
        if (mIsLoading) {
            mIsLoading = false;
        }
        if (webListener != null) {
            //隐藏进度条方法
            //这个方法会执行多次
            webListener.hindProgressBar();
        }
        super.onPageFinished(view, url);
        //设置网页在加载的时候暂时不加载图片
        //webView.getSettings().setBlockNetworkImage(false);
        //页面finish后再发起图片加载
        if (!webView.getSettings().getLoadsImagesAutomatically()) {
            webView.getSettings().setLoadsImagesAutomatically(true);
        }
        //html加载完成之后，添加监听图片的点击js函数
        //addImageClickListener();
        addImageArrayClickListener(webView);
        isLoadFinish = true;
        if (webListener != null) {
            webListener.onPageFinished(url);
        }
    }


    /**
     * 当缩放改变的时候会调用该方法
     *
     * @param view     view
     * @param oldScale 之前的缩放比例
     * @param newScale 现在缩放比例
     */
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        X5LogUtils.i("-------onScaleChanged-------" + newScale);
        //视频全屏播放按返回页面被放大的问题
        if (newScale - oldScale > 7) {
            //异常放大，缩回去。
            view.setInitialScale((int) (oldScale / newScale * 100));
        }
    }

    /**
     * 请求网络出现error
     * 作用：加载页面的服务器出现错误时（如404）调用。
     * App里面使用webView控件的时候遇到了诸如404这类的错误的时候，若也显示浏览器里面的那种错误提示页面就显得很丑陋，
     * 那么这个时候我们的app就需要加载一个本地的错误提示页面，即webView如何加载一个本地的页面
     * 该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
     * onReceivedError只有在遇到不可用的(unrecoverable)错误时，才会被调用）
     * 当WebView加载链接www.ycdoubi.com时，"不可用"的情况有可以包括有：
     * 1.没有网络连接
     * 2.连接超时
     * 3.找不到页面www.ycdoubi.com
     *
     * @param view        view
     * @param errorCode   错误🐎
     * @param description description
     * @param failingUrl  失败链接
     */
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        X5LogUtils.i("-------onReceivedError------1-" + failingUrl + "---" + errorCode + "----" + description);
        if (Build.VERSION.SDK_INT < 23) {
            //错误重定向循环
            if (errorCode == ERROR_REDIRECT_LOOP) {
                //避免由于缓存造成的循环重定向
                return;
            }
        }
        if (webListener != null) {
            if (errorCode == ERROR_TIMEOUT) {
                //网络连接超时
                webListener.showErrorView(X5WebUtils.ErrorMode.TIME_OUT);
            } else if (errorCode == ERROR_CONNECT) {
                //断网
                webListener.showErrorView(X5WebUtils.ErrorMode.NO_NET);
            } else if (errorCode == ERROR_PROXY_AUTHENTICATION) {
                //代理异常
                webListener.showErrorView(X5WebUtils.ErrorMode.ERROR_PROXY);
            } else {
                //其他情况
                webListener.showErrorView(X5WebUtils.ErrorMode.RECEIVED_ERROR);
            }
        }
    }

    /**
     * 6.0 之后
     * 向主机应用程序报告Web资源加载错误。这些错误通常表明无法连接到服务器。
     * 不仅为主页。因此，建议在回调过程中执行最低要求的工作。
     * 该方法传回了错误码，根据错误类型可以进行不同的错误分类处理，比如
     * onReceivedError只有在遇到不可用的(unrecoverable)错误时，才会被调用）
     * 当WebView加载链接www.ycdoubi.com时，"不可用"的情况有可以包括有：
     * 1.没有网络连接
     * 2.连接超时
     * 3.找不到页面www.ycdoubi.com
     *
     * @param view    view
     * @param request request，添加于API21，封装了一个Web资源的请求信息，
     *                包含：请求地址，请求方法，请求头，是否主框架，是否用户点击，是否重定向
     * @param error   error，添加于API23，封装了一个Web资源的错误信息，包含错误码和描述
     */
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        X5LogUtils.i("-------onReceivedError------2-" + error.getDescription() + "---" + error.getErrorCode());
        if (Build.VERSION.SDK_INT >= 23) {
            //错误重定向循环
            if (error != null && error.getErrorCode() == ERROR_REDIRECT_LOOP) {
                //避免由于缓存造成的循环重定向
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            X5LogUtils.d("服务器异常" + error.getDescription().toString());

        }
        //ToastUtils.showRoundRectToast("服务器异常6.0之后");
        //当加载错误时，就让它加载本地错误网页文件
        //mWebView.loadUrl("file:///android_asset/errorpage/error.html");
        int errorCode = error.getErrorCode();
        //获取当前的网络请求是否是为main frame创建的.
        boolean forMainFrame = request.isForMainFrame();
        boolean redirect = request.isRedirect();
        if (webListener != null) {
            if (errorCode == ERROR_TIMEOUT) {
                //网络连接超时
                webListener.showErrorView(X5WebUtils.ErrorMode.TIME_OUT);
            } else if (errorCode == ERROR_CONNECT) {
                //断网
                webListener.showErrorView(X5WebUtils.ErrorMode.NO_NET);
            } else if (errorCode == ERROR_PROXY_AUTHENTICATION) {
                //代理异常
                webListener.showErrorView(X5WebUtils.ErrorMode.ERROR_PROXY);
            } else {
                //其他情况
                webListener.showErrorView(X5WebUtils.ErrorMode.RECEIVED_ERROR);
            }
        }
    }


    /**
     * 通知主机应用程序在加载资源时从服务器接收到HTTP错误
     *
     * @param view          view
     * @param request       request，添加于API21，封装了一个Web资源的请求信息，
     *                      包含：请求地址，请求方法，请求头，是否主框架，是否用户点击，是否重定向
     * @param errorResponse errorResponse，封装了一个Web资源的响应信息，
     *                      包含：响应数据流，编码，MIME类型，API21后添加了响应头，状态码与状态描述
     */
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request,
                                    WebResourceResponse errorResponse) {
        super.onReceivedHttpError(view, request, errorResponse);
        int statusCode = errorResponse.getStatusCode();
        String reasonPhrase = errorResponse.getReasonPhrase();
        X5LogUtils.i("-------onReceivedHttpError------3-" + statusCode + "-------" + reasonPhrase);
        if (statusCode == 404) {
            //用javascript隐藏系统定义的404页面信息
            //String data = "Page NO FOUND！";
            //view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
            if (webListener != null) {
                webListener.showErrorView(X5WebUtils.ErrorMode.STATE_404);
            }
        } else if (statusCode == 500) {
            //避免出现默认的错误界面
            //view.loadUrl("about:blank");
            if (webListener != null) {
                webListener.showErrorView(X5WebUtils.ErrorMode.STATE_500);
            }
        } else {
            if (webListener != null) {
                webListener.showErrorView(X5WebUtils.ErrorMode.RECEIVED_ERROR);
            }
        }
    }

    /**
     * 通知主机应用程序已自动处理用户登录请求
     *
     * @param view    view
     * @param realm   数据
     * @param account account
     * @param args    args
     */
    @Override
    public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        super.onReceivedLoginRequest(view, realm, account, args);
        X5LogUtils.i("-------onReceivedLoginRequest-------" + args);
    }

    /**
     * 在加载资源时通知主机应用程序发生SSL错误
     * 作用：处理https请求
     * webView加载一些别人的url时候，有时候会发生证书认证错误的情况，这时候希望能够正常的呈现页面给用户，
     * 我们需要忽略证书错误，需要调用WebViewClient类的onReceivedSslError方法，
     * 调用handler.proceed()来忽略该证书错误。
     *
     * @param view    view
     * @param handler handler，表示一个处理SSL错误的请求，提供了方法操作(proceed/cancel)请求
     * @param error   error
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        X5LogUtils.i("-------onReceivedSslError-------" + error.getUrl());
        if (error != null) {
            String url = error.getUrl();
            if (webListener != null) {
                webListener.showErrorView(X5WebUtils.ErrorMode.SSL_ERROR);
            }
            X5LogUtils.i("onReceivedSslError----异常url----" + url);
        }
        //https忽略证书问题
        if (handler != null) {
            //表示等待证书响应
            handler.proceed();
            // handler.cancel();      //表示挂起连接，为默认方式
            // handler.handleMessage(null);    //可做其他处理
        }
    }

    /**
     * 作用：在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
     *
     * @param webView view
     * @param s       s
     */
    @Override
    public void onLoadResource(WebView webView, String s) {
        super.onLoadResource(webView, s);
        //X5LogUtils.i("-------onLoadResource-------"+ s);
    }

    /**
     * 这个回调添加于API23，仅用于主框架的导航
     * 通知应用导航到之前页面时，其遗留的WebView内容将不再被绘制。
     * 这个回调可以用来决定哪些WebView可见内容能被安全地回收，以确保不显示陈旧的内容
     * 它最早被调用，以此保证WebView.onDraw不会绘制任何之前页面的内容，随后绘制背景色或需要加载的新内容。
     * 当HTTP响应body已经开始加载并体现在DOM上将在随后的绘制中可见时，这个方法会被调用。
     * 这个回调发生在文档加载的早期，因此它的资源(css,和图像)可能不可用。
     * 如果需要更细粒度的视图更新，查看 postVisualStateCallback(long, WebView.VisualStateCallback).
     * 请注意这上边的所有条件也支持 postVisualStateCallback(long ,WebView.VisualStateCallback)
     *
     * @param webView view
     * @param s       s
     */
    @Override
    public void onPageCommitVisible(WebView webView, String s) {
        super.onPageCommitVisible(webView, s);
    }

    /**
     * 此方法废弃于API21，调用于非UI线程，拦截资源请求并返回响应数据，返回null时WebView将继续加载资源
     * 注意：API21以下的AJAX请求会走onLoadResource，无法通过此方法拦截
     * <p>
     * 其中 WebResourceRequest 封装了请求，WebResourceResponse 封装了响应
     * 封装了一个Web资源的响应信息，包含：响应数据流，编码，MIME类型，API21后添加了响应头，状态码与状态描述
     *
     * @param webView view
     * @param s       s
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
        X5LogUtils.i("---shouldInterceptRequest-------->---" + s);
        WebResourceResponse webResourceResponse = super.shouldInterceptRequest(webView, s);
        return webResourceResponse;
        //return super.shouldInterceptRequest(webView, s);
    }

    /**
     * 此方法添加于API21，调用于非UI线程，拦截资源请求并返回数据，返回null时WebView将继续加载资源
     * <p>
     * 其中 WebResourceRequest 封装了请求，WebResourceResponse 封装了响应
     * 封装了一个Web资源的响应信息，包含：响应数据流，编码，MIME类型，API21后添加了响应头，状态码与状态描述
     *
     * @param webView            view
     * @param webResourceRequest request，添加于API21，封装了一个Web资源的请求信息，
     *                           包含：请求地址，请求方法，请求头，是否主框架，是否用户点击，是否重定向
     * @return
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        String method = webResourceRequest.getMethod();
        X5LogUtils.i("---shouldInterceptRequest-------->21---" + method + "----" + webResourceRequest.getUrl());
        WebResourceResponse webResourceResponse = super.shouldInterceptRequest(webView, webResourceRequest);
        return webResourceResponse;
        //return super.shouldInterceptRequest(webView, webResourceRequest);
    }

    /**
     * 其中 WebResourceRequest 封装了请求，WebResourceResponse 封装了响应
     * 封装了一个Web资源的响应信息，包含：响应数据流，编码，MIME类型，API21后添加了响应头，状态码与状态描述
     *
     * @param webView            view
     * @param webResourceRequest request，添加于API21，封装了一个Web资源的请求信息，
     *                           包含：请求地址，请求方法，请求头，是否主框架，是否用户点击，是否重定向
     * @param bundle             bundle
     * @return
     */
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest, Bundle bundle) {
        WebResourceResponse webResourceResponse = super.shouldInterceptRequest(webView, webResourceRequest, bundle);
        return webResourceResponse;
        //return super.shouldInterceptRequest(webView, webResourceRequest, bundle);
    }

    /**
     * @param webView  view
     * @param message  message
     * @param message1 message1
     */
    @Override
    public void onTooManyRedirects(WebView webView, Message message, Message message1) {
        super.onTooManyRedirects(webView, message, message1);
    }

    /**
     * 是否重新提交表单，默认不重发
     *
     * @param webView  view
     * @param message  message
     * @param message1 message1
     */
    @Override
    public void onFormResubmission(WebView webView, Message message, Message message1) {
        super.onFormResubmission(webView, message, message1);
    }

    /**
     * 通知应用可以将当前的url存储在数据库中，意味着当前的访问url已经生效并被记录在内核当中。
     * 此方法在网页加载过程中只会被调用一次，网页前进后退并不会回调这个函数。
     *
     * @param webView view
     * @param s       s
     * @param b       b
     */
    @Override
    public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
        super.doUpdateVisitedHistory(webView, s, b);
    }

    /**
     * 此方法添加于API21，在UI线程被调用
     * 处理SSL客户端证书请求，必要的话可显示一个UI来提供KEY。
     * 有三种响应方式：proceed()/cancel()/ignore()，默认行为是取消请求
     * 如果调用proceed()或cancel()，Webview 将在内存中保存响应结果且对相同的"host:port"不会再次调用 onReceivedClientCertRequest
     * 多数情况下，可通过KeyChain.choosePrivateKeyAlias启动一个Activity供用户选择合适的私钥
     *
     * @param webView           view
     * @param clientCertRequest request，表示一个证书请求，提供了方法操作(proceed/cancel/ignore)请求
     */
    @Override
    public void onReceivedClientCertRequest(WebView webView, ClientCertRequest clientCertRequest) {
        super.onReceivedClientCertRequest(webView, clientCertRequest);
    }

    /**
     * 处理HTTP认证请求，默认行为是取消请求
     *
     * @param webView         view
     * @param httpAuthHandler handler，表示一个HTTP认证请求，提供了方法操作(proceed/cancel)请求
     * @param s               s
     * @param s1              s1
     */
    @Override
    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String s, String s1) {
        super.onReceivedHttpAuthRequest(webView, httpAuthHandler, s, s1);
    }

    /**
     * 给应用一个机会处理按键事件
     * 如果返回true，WebView不处理该事件，否则WebView会一直处理，默认返回false
     *
     * @param webView  view
     * @param keyEvent event
     * @return
     */
    @Override
    public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
        return super.shouldOverrideKeyEvent(webView, keyEvent);
    }

    /**
     * 处理未被WebView消费的按键事件
     * WebView总是消费按键事件，除非是系统按键或shouldOverrideKeyEvent返回true
     * 此方法在按键事件分派时被异步调用
     *
     * @param webView  view
     * @param keyEvent event
     * @return
     */
    @Override
    public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
        super.onUnhandledKeyEvent(webView, keyEvent);
    }

    /**
     * android与js交互：
     * 首先我们拿到html中加载图片的标签img.
     * 然后取出其对应的src属性
     * 循环遍历设置图片的点击事件
     * 将src作为参数传给java代码
     * 这个循环将所图片放入数组，当js调用本地方法时传入。
     * 当然如果采用方式一获取图片的话，本地方法可以不需要传入这个数组
     * 通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
     *
     * @param webView webview
     */
    private void addImageArrayClickListener(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "var array=new Array(); " +
                "for(var j=0;j<objs.length;j++){" +
                "    array[j]=objs[j].src; " +
                "}" +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistener.openImage(this.src,array);  " +
                "    }  " +
                "}" +
                "})()");
    }

    /**
     * 通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
     *
     * @param webView webview
     */
    private void addImageClickListener(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistener.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

}

