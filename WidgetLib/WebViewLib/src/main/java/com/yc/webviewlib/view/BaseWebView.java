package com.yc.webviewlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.GeolocationPermissions;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebIconDatabase;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewDatabase;
import com.yc.toolutils.click.FastClickUtils;
import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.toastutils.ToastUtils;
import com.yc.webviewlib.base.RequestInfo;
import com.yc.webviewlib.utils.X5LogUtils;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 自定义WebView的base类
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public class BaseWebView extends WebView {

    /**
     * 调用 evaluateJavascriptUrl 方法加载
     */
    private static final int EXEC_SCRIPT = 1;
    /**
     * 调用 loadUrl 直接加载
     */
    private static final int LOAD_URL = 2;
    /**
     * 调用 loadUrl 加载并且带有header
     */
    private static final int LOAD_URL_WITH_HEADERS = 3;
    /**
     * loadUrl方法在19以上超过2097152个字符失效
     */
    private static final int URL_MAX_CHARACTER_NUM = 2097152;
    private MyHandler mainThreadHandler = null;

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {

        private final WeakReference<Context> mContextReference;

        MyHandler(Context context) {
            super(Looper.getMainLooper());
            mContextReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            final Context context = mContextReference.get();
            if (context != null) {
                switch (msg.what) {
                    case EXEC_SCRIPT:
                        evaluateJavascriptUrl((String) msg.obj);
                        break;
                    case LOAD_URL:
                        BaseWebView.super.loadUrl((String) msg.obj);
                        break;
                    case LOAD_URL_WITH_HEADERS:
                        RequestInfo info = (RequestInfo) msg.obj;
                        BaseWebView.super.loadUrl(info.url, info.headers);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public BaseWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        mainThreadHandler = new MyHandler(getContext());
    }

    /**
     * 暂停所有布局，解析，和所有webView的JavaScript计时器。
     * 这是一个全局请求，不局限于这个WebView。如果应用程序已暂停，这将非常有用。
     */
    @Override
    public void pauseTimers() {
        super.pauseTimers();
    }

    /**
     * 停止加载
     */
    @Override
    public void stopLoading() {
        super.stopLoading();
    }

    /**
     * 尽最大努力暂停任何可以安全暂停的处理，比如动画和地理定位。
     * 注意，这个调用不会暂停JavaScript。使用{@link # pausetimer}全局暂停JavaScript。
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 在先前调用{@link #onPause}后恢复WebView。
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获取当前页的进度
     * @return                      当前页的进度在0到100之间
     */
    @Override
    public int getProgress() {
        return super.getProgress();
    }

    /**
     * 获取HTML内容的高度
     * @return                      HTML内容的高度
     */
    @Override
    public int getContentHeight() {
        return super.getContentHeight();
    }

    /**
     * 获取当前页面的原始URL。这并不总是与传递给WebViewClient的URL相同。
     * onPageStarted，因为虽然加载URL已经开始，当前页面可能没有改变。另外，可能有重定向导致到最初请求的不同URL。
     * @return                      链接url
     */
    @Override
    public String getOriginalUrl() {
        return super.getOriginalUrl();
    }

    /**
     * 获取当前页面的URL。这并不总是与传递给WebViewClient的URL相同。
     * onPageStarted，因为虽然加载URL已经开始，当前页面可能没有改变。
     * @return                      链接ul
     */
    @Override
    public String getUrl() {
        return super.getUrl();
    }

    /**
     * 向下翻页
     * @param b                     向下翻页
     * @return
     */
    @Override
    public boolean pageDown(boolean b) {
        return super.pageDown(b);
    }

    /**
     * 将这个WebView的内容滚动到视图大小的一半。
     * @param b                     向上滚动
     * @return
     */
    @Override
    public boolean pageUp(boolean b) {
        return super.pageUp(b);
    }

    /**
     * 缓存清除
     * 针对性删除
     */
    public void clearCache(){
        //清除cookie
        CookieManager.getInstance().removeAllCookies(null);
        //清除storage相关缓存
        WebStorage.getInstance().deleteAllData();;
        //清除用户密码信息
        WebViewDatabase.getInstance(getContext()).clearUsernamePassword();
        //清除httpauth信息
        WebViewDatabase.getInstance(getContext()).clearHttpAuthUsernamePassword();
        //清除表单数据
        WebViewDatabase.getInstance(getContext()).clearFormData();
        //清除页面icon图标信息
        WebIconDatabase.getInstance().removeAllIcons();
        //删除地理位置授权，也可以删除某个域名的授权（参考接口类）
        GeolocationPermissions.getInstance().clearAll();
    }

    /**
     * 清除网页访问留下的缓存
     * 由于内核缓存是全局的因此这个方法不仅仅针对webView而是针对整个应用程序
     * @param b         是否清除
     */
    @Override
    public void clearCache(boolean b) {
        super.clearCache(b);
    }

    /**
     * 清除当前webView访问的历史记录
     * 只会webView访问历史记录里的所有记录除了当前访问记录
     */
    @Override
    public void clearHistory() {
        super.clearHistory();
    }

    /**
     * 这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
     */
    @Override
    public void clearFormData() {
        super.clearFormData();
    }

    /**
     * 一次性删除所有缓存
     * @param isClearCookie                         是否清除cookie操作
     */
    public void clearAllWebViewCache(boolean isClearCookie){
        //清除cookie
        QbSdk.clearAllWebViewCache(getContext(),isClearCookie);
    }

    /**
     * 刘海屏适配
     * @param displayCutoutEnable                   是否适配
     */
    public void setDisplayCutoutEnable(boolean displayCutoutEnable){
        // 对于刘海屏机器如果webview被遮挡会自动padding
        this.getSettingsExtension().setDisplayCutoutEnable(displayCutoutEnable);
    }

    /**
     * 设置无痕模式
     * @param visitedLinks                          true表示无痕模式
     */
    public void setShouldTrackVisitedLinks(boolean visitedLinks){
        //无痕模式
        this.getSettingsExtension().setShouldTrackVisitedLinks(visitedLinks);
    }

    /**
     * 强制缩放
     * @param scaleEnabled                          true表示强制缩放
     */
    public void setForcePinchScaleEnabled(boolean scaleEnabled){
        //对于无法缩放的页面当用户双指缩放时会提示强制缩放，再次操作将触发缩放功能
        this.getSettingsExtension().setForcePinchScaleEnabled(scaleEnabled);
    }

    /**
     * 前进后退缓存
     * @param cacheEnable                           true表示缓存
     */
    public void setContentCacheEnable(boolean cacheEnable){
        //开启后前进后退将不再重新加载页面，默认关闭，开启方法如下
        this.getSettingsExtension().setContentCacheEnable(cacheEnable);
    }

    /**
     * 夜间模式
     * @param dayOrNight                            true(日间模式)
     */
    @Override
    public void setDayOrNight(boolean dayOrNight){
        // enable:true(日间模式)，enable：false（夜间模式）
        this.getSettingsExtension().setDayOrNight(dayOrNight);
    }

    /**
     * 设置是否允许抓包
     * APP自身必须调用WebView.setWebContentsDebuggingEnabled(true); 才会允许被DevTools调试
     * @param isOpen                                默认允许
     */
    public void setFidderOpen(boolean isOpen){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(isOpen);
        }
    }

    /**
     * 重新加载
     */
    @Override
    public void reload() {
        //防止短时间多次触发load方法
        if (FastClickUtils.isFastDoubleClick()){
            return;
        }
        if (DelegateTaskExecutor.getInstance().isMainThread()){
            super.reload();
        } else {
            Handler handler = getHandler();
            if (handler!=null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BaseWebView.super.reload();
                    }
                });
            }
            //throw new WebViewException("Please load in the main thread");
        }
    }

    /**
     * 页面可见开启js交互
     */
    public void resume(){
        //生命周期
        this.getSettings().setJavaScriptEnabled(true);
    }

    /**
     * 页面不可见关闭js交互
     */
    public void stop() {
        //生命周期
        this.getSettings().setJavaScriptEnabled(false);
    }

    private void evaluateJavascriptUrl(String script) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //BaseWebView.super.evaluateJavascript(script, null);
            BaseWebView.super.evaluateJavascript(script, new ValueCallback<String>(){
                @Override
                public void onReceiveValue(String s) {
                    X5LogUtils.i("---evaluateJavascript-1--"+s);
                }
            });
        } else {
            //super.loadUrl("javascript:" + script);
            if (script.length()>=URL_MAX_CHARACTER_NUM){
                BaseWebView.super.evaluateJavascript(script, new ValueCallback<String>(){
                    @Override
                    public void onReceiveValue(String s) {
                        X5LogUtils.i("---evaluateJavascript-2--"+s);
                    }
                });
            } else {
                BaseWebView.super.loadUrl(script);
            }
        }
    }

    /**
     * This method can be called in any thread, and if it is not called in the main thread,
     * it will be automatically distributed to the main thread.
     * @param script
     */
    public void evaluateJavascript(final String script) {
        if (script==null || script.length()==0){
            return;
        }
        Message msg = mainThreadHandler.obtainMessage(EXEC_SCRIPT, script);
        mainThreadHandler.sendMessage(msg);
    }

    /**
     * This method can be called in any thread, and if it is not called in the main thread,
     * it will be automatically distributed to the main thread.
     * @param script
     */
    @Override
    public void evaluateJavascript(final String script, final ValueCallback<String> callback) {
        if (script==null || script.length()==0){
            return;
        }
        if (DelegateTaskExecutor.getInstance().isMainThread()){
            BaseWebView.super.evaluateJavascript(script,callback);
        } else {
            Handler handler = getHandler();
            if (handler!=null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        BaseWebView.super.evaluateJavascript(script,callback);
                    }
                });
            }
        }
    }

    /**
     * 这个方法可以在任何线程中调用，如果在主线程中没有调用它，它将自动分配给主线程。通过handler实现不同线程
     * @param url                                   url
     */
    @Override
    public void loadUrl(String url) {
        if (url==null || url.length()==0){
            return;
        }
        Message msg = mainThreadHandler.obtainMessage(LOAD_URL, url);
        mainThreadHandler.sendMessage(msg);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (url==null || url.length()==0){
            return;
        }
        Message msg = mainThreadHandler.obtainMessage(LOAD_URL_WITH_HEADERS, new RequestInfo(url, additionalHttpHeaders));
        mainThreadHandler.sendMessage(msg);
    }

    /**
     * 开发者调用
     * 子线程发送消息
     * @param url						url
     */
    public void postUrl(String url){
        if (url==null || url.length()==0){
            return;
        }
        if (DelegateTaskExecutor.getInstance().isMainThread()){
            loadUrl(url);
        } else {
            Message message = mainThreadHandler.obtainMessage();
            message.what = LOAD_URL;
            message.obj = url;
            mainThreadHandler.sendMessage(message);
        }
    }


    /**
     * 设置字体大小
     * @param type                      类型
     */
    public void setTextSize(int type){
        WebSettings settings = this.getSettings();
        settings.setSupportZoom( true);
        switch (type) {
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
            default:
                break;
        }
        settings.setDefaultZoom(zoomDensity);
    }

    /**
     * 添加下载监听操作。跳转外部浏览器进行下载
     */
    public void setDownloadListener(){
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                Context context = getContext();
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    if (Build.VERSION.SDK_INT >= 30) {
                        context.startActivity(intent);
                    } else {
                        ToastUtils.showRoundRectToast("can not find browser");
                    }
                }
            }
        });
    }

}
