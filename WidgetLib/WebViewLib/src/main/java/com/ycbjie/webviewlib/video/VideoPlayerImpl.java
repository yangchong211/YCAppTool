package com.ycbjie.webviewlib.video;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.ycbjie.webviewlib.R;
import com.ycbjie.webviewlib.inter.VideoWebListener;
import com.ycbjie.webviewlib.utils.X5LogUtils;
import com.ycbjie.webviewlib.utils.X5WebUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 全频播放视频接口实现类，做具体操作
 *     revise:
 * </pre>
 */
public class VideoPlayerImpl implements InterVideo, EventInterceptor {

    private Activity mActivity;
    private WebView mWebView;
    private Set<Pair<Integer, Integer>> mFlags;
    private View mMovieView = null;
    private ViewGroup mMovieParentView = null;
    private View progressVideo;
    private IX5WebChromeClient.CustomViewCallback mCallback;
    private VideoWebListener mListener;
    /**
     * 设置是否使用该自定义视频，默认使用
     */
    private boolean isShowCustomVideo = true;

    public VideoPlayerImpl(Activity activity, WebView webView) {
        this.mActivity = activity;
        this.mWebView = webView;
        mFlags = new HashSet<>();
    }

    public void setListener(VideoWebListener mListener) {
        this.mListener = mListener;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        if (isShowCustomVideo){
            if (!X5WebUtils.isActivityAlive(mActivity)) {
                return;
            }
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            // 保存当前屏幕的状态
        /*Window mWindow = mActivity.getWindow();
        Pair<Integer, Integer> mPair;
        if ((mWindow.getAttributes().flags & WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) == 0) {
            mPair = new Pair<>(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 0);
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            mFlags.add(mPair);
        }
        if ((mWindow.getAttributes().flags & WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED) == 0) {
            mPair = new Pair<>(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, 0);
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            mFlags.add(mPair);
        }*/

            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (mMovieView != null) {
                callback.onCustomViewHidden();
                return;
            }
            if (mWebView != null) {
                mWebView.setVisibility(View.GONE);
                if (mListener!=null){
                    mListener.hindWebView();
                }
            }
            //添加view到decorView容齐中
            fullViewAddView(view);
            this.mCallback = callback;
            this.mMovieView = view;
            if (mListener!=null){
                mListener.showVideoFullView();
            }
        }
    }

    /**
     * 添加view到decorView容齐中
     * @param view                              view
     */
    private void fullViewAddView(View view) {
        if (mMovieParentView == null) {
            FrameLayout mDecorView = (FrameLayout) mActivity.getWindow().getDecorView();
            mMovieParentView = new FullscreenHolder(mActivity);
            mMovieParentView.addView(view);
            mMovieParentView.setVisibility(View.VISIBLE);
            X5LogUtils.i("--Video-----onShowCustomView----添加view到decorView容齐中---");
            mDecorView.addView(mMovieParentView);
        }
    }


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onHideCustomView() {
        if (isShowCustomVideo){
            if (mMovieView == null || mActivity==null) {
                // 不是全屏播放状态
                return;
            }
            X5LogUtils.i("--Video-----onShowCustomView----切换方向---");
            if (mActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            //mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*if (!mFlags.isEmpty()) {
            for (Pair<Integer, Integer> mPair : mFlags) {
                mActivity.getWindow().setFlags(mPair.second, mPair.first);
            }
            mFlags.clear();
        }*/
            if (mMovieParentView != null && mMovieView != null) {
                mMovieView.setVisibility(View.GONE);
                mMovieParentView.removeView(mMovieView);
            }
            if (mMovieParentView != null) {
                mMovieParentView.setVisibility(View.GONE);
                if (mListener!=null){
                    mListener.hindVideoFullView();
                }
            }
            if (this.mCallback != null) {
                mCallback.onCustomViewHidden();
            }
            this.mMovieView = null;
            if (mWebView != null) {
                mWebView.setVisibility(View.VISIBLE);
                if (mListener!=null){
                    mListener.showWebView();
                }
            }
        }
    }

    @Override
    public View getVideoLoadingProgressView() {
        if (progressVideo == null && mActivity!=null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity);
            progressVideo = inflater.inflate(R.layout.view_web_video_progress, null);
        }
        return progressVideo;
    }

    @Override
    public boolean isVideoState() {
        return mMovieView != null;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public boolean event() {
        if (isVideoState()) {
            this.onHideCustomView();
            if (X5WebUtils.isActivityAlive(mActivity)){
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 销毁的时候需要移除一下视频view
     */
    public void removeAllViews() {
        if (mMovieView!=null){
            mMovieParentView.removeAllViews();
        }
    }

    /**
     * 设置是否使用自定义视频视图
     * @param showCustomVideo                   是否使用自定义视频视图
     */
    public void setShowCustomVideo(boolean showCustomVideo) {
        this.isShowCustomVideo = showCustomVideo;
    }

}
