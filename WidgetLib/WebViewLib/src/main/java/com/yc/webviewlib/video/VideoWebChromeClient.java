package com.yc.webviewlib.video;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.FrameLayout;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebChromeClient;
import com.yc.webviewlib.inter.VideoWebListener;
import com.yc.webviewlib.utils.X5LogUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 全频播放视频
 *     revise: 专门处理视频，后续看VideoChromeClient类代码
 * </pre>
 */
public class VideoWebChromeClient extends WebChromeClient {

    private final Context context;
    private View customView;
    private IX5WebChromeClient.CustomViewCallback customViewCallback;
    private FullscreenHolder videoFullView;
    private VideoWebListener mListener;
    /**
     * 设置是否使用该自定义视频，默认使用
     */
    private boolean isShowCustomVideo = true;

    /**
     * 设置是否使用
     * @param showCustomVideo                   是否使用自定义视频视图
     */
    public void setCustomVideo(boolean showCustomVideo) {
        isShowCustomVideo = showCustomVideo;
    }

    /**
     * 设置视频播放监听，主要是比如全频，取消全频，隐藏和现实webView
     * @param videoWebListener                  listener
     */
    public void setVideoListener(VideoWebListener videoWebListener){
        this.mListener = videoWebListener;
    }

    /**
     * 构造方法
     * @param context                          上下文
     */
    public VideoWebChromeClient(Context context) {
        super();
        this.context = context;
    }


    /**
     *  通知应用当前页进入了全屏模式，此时应用必须显示一个包含网页内容的自定义View
     * 播放网络视频时全屏会被调用的方法，播放视频切换为横屏
     * @param view                              view
     * @param callback                          callback
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        X5LogUtils.i("--Video-----onShowCustomView-------");
        if (isShowCustomVideo){
            if (context instanceof Activity){
                X5LogUtils.i("--Video-----onShowCustomView----展示视频---");
                Activity activity = (Activity) context;
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                // 如果一个视图已经存在，那么立刻终止并新建一个
                if (customView != null) {
                    callback.onCustomViewHidden();
                    if (mListener!=null){
                        mListener.hindWebView();
                    }
                    return;
                }
                fullViewAddView(view);
                customView = view;
                customViewCallback = callback;
                if (mListener!=null){
                    mListener.showVideoFullView();
                }
            }
        }
    }

    /**
     * 添加view到decorView容齐中
     * @param view                              view
     */
    private void fullViewAddView(View view) {
        //增强逻辑判断，尤其是getWindow()
        if (context!=null && context instanceof Activity){
            Activity activity = (Activity) context;
            if (activity.getWindow()!=null){
                FrameLayout decor = (FrameLayout) activity.getWindow().getDecorView();
                videoFullView = new FullscreenHolder(activity);
                videoFullView.addView(view);
                X5LogUtils.i("--Video-----onShowCustomView----添加view到decorView容齐中---");
                decor.addView(videoFullView);
            }
        }
    }

    /**
     * 获取视频控件view
     * @return                                  view
     */
    private FrameLayout getVideoFullView() {
        return videoFullView;
    }

    /**
     * 销毁的时候需要移除一下视频view
     */
    public void removeVideoView(){
        if (videoFullView!=null){
            videoFullView.removeAllViews();
        }
    }

    /**
     * 通知应用当前页退出了全屏模式，此时应用必须隐藏之前显示的自定义View
     * 视频播放退出全屏会被调用的
     */
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onHideCustomView() {
        X5LogUtils.i("--Video-----onHideCustomView-------");
        if (isShowCustomVideo){
            if (customView == null) {
                // 不是全屏播放状态
                return;
            }
            if (context!=null && context instanceof Activity){
                X5LogUtils.i("--Video-----onHideCustomView----切换方向---");
                Activity activity = (Activity) context;
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            customView.setVisibility(View.GONE);
            if (getVideoFullView() != null) {
                X5LogUtils.i("--Video-----onHideCustomView----移除---");
                getVideoFullView().removeView(customView);
            }
            customView = null;
            customViewCallback.onCustomViewHidden();
            if (mListener!=null){
                mListener.showWebView();
            }
        }
    }

    /**
     * 判断是否是全屏
     */
    public boolean inCustomView() {
        return (customView != null);
    }

    /**
     * 逻辑是：先判断是否全频播放，如果是，则退出全频播放
     * 全屏时按返加键执行退出全屏方法
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public void hideCustomView() {
        this.onHideCustomView();
        if (context!=null && context instanceof Activity){
            Activity activity = (Activity) context;
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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


}
