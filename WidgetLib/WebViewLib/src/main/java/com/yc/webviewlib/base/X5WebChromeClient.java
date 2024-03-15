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

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.PermissionRequest;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.yc.toolutils.intent.AppIntentUtils;
import com.yc.webviewlib.inter.InterWebListener;
import com.yc.webviewlib.inter.VideoWebListener;
import com.yc.webviewlib.utils.X5LogUtils;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.video.VideoWebChromeClient;

import static android.app.Activity.RESULT_OK;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 自定义x5的WebChromeClient
 *     revise: 如果自定义WebChromeClient，建议继承该类，后期添加视频播放的处理方法
 *             demo地址：https://github.com/yangchong211/YCWebView
 *
 *             作用：WebViewClient主要辅助WebView执行处理各种响应请求事件的
 * </pre>
 */
public class X5WebChromeClient extends VideoWebChromeClient {

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessageForAndroid5;
    /**
     * 注意h5中调用上传图片，resultCode保持一致性
     */
    public static int FILE_CHOOSER_RESULT_CODE = 1;
    public static int FILE_CHOOSER_RESULT_CODE_5 = 2;
    private InterWebListener webListener;
    private boolean isShowContent = false;
    private final Context context;
    private WebView webView;
    public static final int REQUEST_LOCATION = 100;


    /**
     * 设置监听时间，包括常见状态页面切换，进度条变化等
     * @param listener                          listener
     */
    public void setWebListener(InterWebListener listener){
        this.webListener = listener;
    }

    /**
     * 设置视频播放监听，主要是比如全频，取消全频，隐藏和现实webView
     * @param videoWebListener                  listener
     */
    public void setVideoWebListener(VideoWebListener videoWebListener){
        setVideoListener(videoWebListener);
    }

    /**
     * 设置是否使用自定义视频视图，建议使用
     * @param showCustomVideo                   是否使用自定义视频视图
     */
    public void setShowCustomVideo(boolean showCustomVideo) {
        setCustomVideo(showCustomVideo);
    }

    /**
     * 构造方法
     * @param context                          上下文
     */
    public X5WebChromeClient(WebView webView , Context context) {
        //super(context, webView);
        super(context);
        this.context = context;
        this.webView = webView;
    }

    /**
     * 这个方法是监听加载进度变化的，当加载到百分之八十五的时候，页面一般就出来呢
     * 作用：获得网页的加载进度并显示
     * @param view                              view
     * @param newProgress                       进度值
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (webListener!=null){
            webListener.startProgress(newProgress);
            int max = 95;
            if (newProgress> max && !isShowContent){
                webListener.hindProgressBar();
                isShowContent = true;
            }
        }
    }

    /**
     * 这个方法主要是监听标题变化操作的
     * @param view                              view
     * @param title                             标题
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (title.contains("404") || title.contains("网页无法打开")){
            if (webListener!=null){
                webListener.showErrorView(X5WebUtils.ErrorMode.STATE_404);
            }
        } else {
            // 设置title
            if (webListener!=null){
                webListener.showTitle(title);
            }
        }
        X5LogUtils.i("-------onReceivedTitle-------"+title + "----"+view.getUrl());
    }

    /**
     * 处理confirm弹出框
     * result.confirm() 和 return true 一起用，才能保证弹框不会出现。
     * @param webView                           view
     * @param s                                 s
     * @param s1                                s1
     * @param jsResult                          jsResult，用于处理底层JS发起的请求，为客户端提供一些方法指明应进行的操作，比如确认或取消。
     * @return
     */
    @Override
    public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
        return super.onJsConfirm(webView, s, s1, jsResult);
    }

    /**
     * 处理prompt弹出框
     * @param webView                           view
     * @param url                               url链接
     * @param message                           参数message:代表prompt（）的内容（不是url）
     * @param defaultValue                      参数result:代表输入框的返回值
     * @param jsPromptResult                    jsPromptResult
     * @return
     */
    @Override
    public boolean onJsPrompt(WebView webView, String url, String message,
                              String defaultValue, JsPromptResult jsPromptResult) {
        //根据协议的参数，判断是否是所需要的url
        //假定传入进来的 message = "js://openActivity?arg1=111&arg2=222"，代表需要打开本地页面，并且带入相应的参数
        //同时也是约定好的需要拦截的
        /*Uri uri = Uri.parse(message);
        String scheme = uri.getScheme();
        if ("js".equals(scheme)) {
            if (uri.getAuthority().equals("openActivity")) {
                HashMap<String, String> params = new HashMap<>();
                Set<String> collection = uri.getQueryParameterNames();
                for (String name : collection) {
                    params.put(name, uri.getQueryParameter(name));
                }
                Intent intent = new Intent(webView.getContext(), MainActivity.class);
                intent.putExtra("params", params);
                webView.getContext().startActivity(intent);
                //代表应用内部处理完成
                jsPromptResult.confirm("success");
            } else if("demo".equals(uri.getAuthority()));{
                //代表应用内部处理完成
                jsPromptResult.confirm("js调用了Android的方法成功啦");
                return true;
            }
            return true;
        }*/
        return super.onJsPrompt(webView, url, message, defaultValue, jsPromptResult);
    }

    /**
     * 处理alert弹出框
     * 调用javascript的调试过程中，有时候需要使用onJsAlert来输出javascript方法的信息，以帮助进行问题定位。
     * onJsAlert方法即可，为什么onJsAlert只调用了一次
     * 发现onJsAlert只调用了一次,为什么呢,实际上,你可能忽略了一句调用.就是处理JsResult.
     * 需要调用result.confirm()或者result.cancel()来处理jsResult,否则会出问题.
     * @param webView                           view
     * @param s                                 s
     * @param s1                                s1
     * @param jsResult                          jsResult，用于处理底层JS发起的请求，为客户端提供一些方法指明应进行的操作，比如确认或取消。
     * @return
     */
    @Override
    public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
        // 这里处理交互逻辑
        // result.cancel(); 表示用户取消了操作(点击了取消按钮)
        // result.confirm(); 表示用户确认了操作(点击了确认按钮)
        // ...
        // 返回true表示自已处理，返回false表示由系统处理
        return super.onJsAlert(webView, s, s1, jsResult);
    }

    /**
     * 显示一个对话框让用户选择是否离开当前页面
     * @param webView                           view
     * @param s                                 s
     * @param s1                                s1
     * @param jsResult                          jsResult，用于处理底层JS发起的请求，为客户端提供一些方法指明应进行的操作，比如确认或取消。
     * @return
     */
    @Override
    public boolean onJsBeforeUnload(WebView webView, String s, String s1, JsResult jsResult) {
        return super.onJsBeforeUnload(webView, s, s1, jsResult);
    }

    /**
     * 指定源的网页内容在没有设置权限状态下尝试使用地理位置API。
     * 从API24开始，此方法只为安全的源(https)调用，非安全的源会被自动拒绝
     * @param origin                            origin
     * @param geolocationPermissionsCallback    callback
     */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback geolocationPermissionsCallback) {
        //boolean allow = true;   // 是否允许origin使用定位API
        //boolean retain = false; // 内核是否记住这次制授权
        //geolocationPermissionsCallback.invoke(origin, allow, retain);
        super.onGeolocationPermissionsShowPrompt(origin, geolocationPermissionsCallback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int code = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (code != PackageManager.PERMISSION_GRANTED && context instanceof Activity) {
                // 还没有定位的授权，需要动态申请。
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            }
        }
    }

    /**
     * 打开文件夹，扩展浏览器上传文件，3.0++版本
     * @param uploadMsg                         msg
     * @param acceptType                        type
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        openFileChooserImpl(uploadMsg);
    }

    /**
     * 3.0--版本
     * @param uploadMsg                         msg
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooserImpl(uploadMsg);
    }

    /**
     * 打开文件夹
     * @param uploadMsg                         msg
     * @param acceptType                        type
     * @param capture                           capture
     */
    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        X5LogUtils.i("-------openFileChooser-------");
        openFileChooserImpl(uploadMsg);
    }

    /**
     * 为'<input type="file" />'显示文件选择器，返回false使用默认处理
     * For Android > 5.0
     * @param webView                           webview
     * @param uploadMsg                         msg
     * @param fileChooserParams                 参数
     * @return
     */
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg,
                                     FileChooserParams fileChooserParams) {
        X5LogUtils.i("-------onShowFileChooser-------");
        openFileChooserImplForAndroid5(uploadMsg);
        return true;
    }

    /**
     * 获得所有访问历史项目的列表，用于链接着色。
     * @param valueCallback                     callback
     */
    @Override
    public void getVisitedHistory(ValueCallback<String[]> valueCallback) {
        super.getVisitedHistory(valueCallback);
    }

    /**
     * <video /> 控件在未播放时，会展示为一张海报图，HTML中可通过它的'poster'属性来指定。
     * 如果未指定'poster'属性，则通过此方法提供一个默认的海报图。
     * @return                                  bitmap
     */
    @Override
    public Bitmap getDefaultVideoPoster() {
        return super.getDefaultVideoPoster();
    }

    /**
     * 接收JavaScript控制台消息
     * @param consoleMessage                    message
     * @return
     */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        String message = consoleMessage.message();
        ConsoleMessage.MessageLevel messageLevel = consoleMessage.messageLevel();
        String sourceId = consoleMessage.sourceId();
        X5LogUtils.i("-------onConsoleMessage-------"+message+"----"+sourceId);
        return super.onConsoleMessage(consoleMessage);
    }

    /**
     * js超时
     * @return                                  boolean
     */
    @Override
    public boolean onJsTimeout() {
        boolean jsTimeout = super.onJsTimeout();
        X5LogUtils.i("-------onJsTimeout----js是否超时---"+jsTimeout);
        return jsTimeout;
    }

    /**
     * 当前一个调用 onGeolocationPermissionsShowPrompt() 取消时，隐藏相关的UI。
     */
    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    /**
     *
     * @param l                                 l
     * @param l1                                l1
     * @param quotaUpdater                      quotaUpdater，WebStorage 用于管理WebView提供的JS存储API，
     *                                          比如Application Cache API，Web SQL Database API，HTML5 Web Storage API
     */
    @Override
    public void onReachedMaxAppCacheSize(long l, long l1, WebStorage.QuotaUpdater quotaUpdater) {
        super.onReachedMaxAppCacheSize(l, l1, quotaUpdater);
    }

    /**
     *
     * @param s                                 s
     * @param s1                                s1
     * @param l                                 l
     * @param l1                                l1
     * @param l2                                l2
     * @param quotaUpdater                      quotaUpdater，WebStorage 用于管理WebView提供的JS存储API，
     *                                          比如Application Cache API，Web SQL Database API，HTML5 Web Storage API
     */
    @Override
    public void onExceededDatabaseQuota(String s, String s1, long l, long l1,
                                        long l2, WebStorage.QuotaUpdater quotaUpdater) {
        super.onExceededDatabaseQuota(s, s1, l, l1, l2, quotaUpdater);
    }

    /**
     * 接收图标(favicon)
     * @param webView                           view
     * @param bitmap                            bitmap
     */
    @Override
    public void onReceivedIcon(WebView webView, Bitmap bitmap) {
        super.onReceivedIcon(webView, bitmap);
    }

    /**
     * Android中处理Touch Icon的方案
     * @param webView                           view
     * @param s                                 s
     * @param b                                 b
     */
    @Override
    public void onReceivedTouchIconUrl(WebView webView, String s, boolean b) {
        super.onReceivedTouchIconUrl(webView, s, b);
    }

    /**
     * 通知应用打开新窗口
     * @param webView                           view
     * @param b                                 b
     * @param b1                                b1
     * @param message                           message
     * @return
     */
    @Override
    public boolean onCreateWindow(WebView webView, boolean b, boolean b1, Message message) {
        return super.onCreateWindow(webView, b, b1, message);
    }

    /**
     * 请求获取取焦点
     * @param webView                           view
     */
    @Override
    public void onRequestFocus(WebView webView) {
        super.onRequestFocus(webView);
    }

    /**
     * 通知应用关闭窗口
     * @param webView                           view
     */
    @Override
    public void onCloseWindow(WebView webView) {
        super.onCloseWindow(webView);
    }

    /**
     * 通知应用网页内容申请访问指定资源的权限(该权限未被授权或拒绝)
     * @param permissionRequest                 permissionRequest
     */
    @Override
    public void onPermissionRequest(PermissionRequest permissionRequest) {
        super.onPermissionRequest(permissionRequest);
    }

    /**
     * 通知应用权限的申请被取消，隐藏相关的UI。
     * @param permissionRequest                 permissionRequest
     */
    @Override
    public void onPermissionRequestCanceled(PermissionRequest permissionRequest) {
        super.onPermissionRequestCanceled(permissionRequest);
    }

    /**
     * 打开文件夹
     * @param uploadMsg                         msg
     */
    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        if (context!=null && context instanceof Activity){
            Activity activity = (Activity) context;
            mUploadMessage = uploadMsg;
            Intent photoIntent = AppIntentUtils.getPhotoIntent2();
            activity.startActivityForResult(photoIntent, FILE_CHOOSER_RESULT_CODE);
        }
    }

    /**
     * 打开文件夹，Android5.0以上
     * @param uploadMsg                         msg
     */
    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
        if (context!=null && context instanceof Activity){
            Activity activity = (Activity) context;
            mUploadMessageForAndroid5 = uploadMsg;
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("image/*");
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "图片选择");
            activity.startActivityForResult(chooserIntent, FILE_CHOOSER_RESULT_CODE_5);
        }
    }

    /**
     * 5.0以下 上传图片成功后的回调
     */
    public void uploadMessage(Intent intent, int resultCode) {
        if (null == mUploadMessage) {
            return;
        }
        Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
    }

    /**
     * 5.0以上 上传图片成功后的回调
     */
    public void uploadMessageForAndroid5(Intent intent, int resultCode) {
        if (null == mUploadMessageForAndroid5) {
            return;
        }
        Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
        if (result != null) {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
        } else {
            mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
        }
        mUploadMessageForAndroid5 = null;
    }
}
