package com.ycbjie.webviewlib.video;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.ycbjie.webviewlib.inter.VideoWebListener;
import com.ycbjie.webviewlib.utils.X5LogUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 自定义全频播放视频的时候，处理全屏逻辑
 *     revise:
 * </pre>
 */
public class VideoChromeClient extends WebChromeClient {

    /**
     * 网页点击全屏按钮会触发WebChromeClient的onShowCustomView方法，全屏后缩回来会触发onHideCustomView方法
     * 给你WebView所在Activity的清单文件中添加以下属性
     * 需要加：android:configChanges="orientation|screenSize"
     * 非必须：android:hardwareAccelerated="true"
     */

    private InterVideo mIVideo;
    private VideoPlayerImpl video;

    /**
     * 构造方法
     * @param context                          上下文
     */
    public VideoChromeClient(Context context , WebView webView ) {
        super();
        video = new VideoPlayerImpl((Activity) context, webView);
        this.mIVideo = video;
    }

    /**
     * 设置视频播放监听，主要是比如全频，取消全频，隐藏和现实webView
     * @param videoWebListener                  listener
     */
    public void setVideoListener(VideoWebListener videoWebListener){
        if (video!=null){
            video.setListener(videoWebListener);
        }
    }

    /**
     * 设置是否使用
     * @param showCustomVideo                   是否使用自定义视频视图
     */
    public void setCustomVideo(boolean showCustomVideo) {
        if (video!=null){
            video.setShowCustomVideo(showCustomVideo);
        }
    }

    /**
     *  通知应用当前页进入了全屏模式，此时应用必须显示一个包含网页内容的自定义View
     * 播放网络视频时全屏会被调用的方法，播放视频切换为横屏
     * @param view                              view
     * @param customViewCallback                callback
     */
    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        if (mIVideo != null) {
            mIVideo.onShowCustomView(view, customViewCallback);
        } else {
            super.onShowCustomView(view, customViewCallback);
        }
    }

    /**
     * 通知应用当前页退出了全屏模式，此时应用必须隐藏之前显示的自定义View
     * 视频播放退出全屏会被调用的
     */
    @Override
    public void onHideCustomView() {
        if (mIVideo != null) {
            mIVideo.onHideCustomView();
        } else {
            super.onHideCustomView();
        }
    }

    /**
     * 当全屏的视频正在缓冲时，此方法返回一个占位视图(比如旋转的菊花)。
     * 视频加载时进程loading
     */
    @Override
    public View getVideoLoadingProgressView() {
        if (mIVideo != null) {
            return mIVideo.getVideoLoadingProgressView();
        } else {
            return super.getVideoLoadingProgressView();
        }
    }

    /**
     * 销毁的时候需要移除一下视频view
     */
    public void removeVideoView(){
        if (video!=null){
            video.removeAllViews();
        }
    }

    /**
     * 获取video状态，判断是否是全屏
     * @return
     */
    public boolean inCustomView(){
        return video.isVideoState();
    }

    /**
     * 隐藏视频
     * 逻辑是：先判断是否全频播放，如果是，则退出全频播放
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView(){
        boolean event = video.event();
        if (event){
            X5LogUtils.i("-----hideVideo-----隐藏视频----");
        }
    }

}
