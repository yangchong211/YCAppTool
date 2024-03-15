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
package com.yc.video.controller;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.networklib.net.AppNetworkUtils;
import com.yc.toolutils.AppLogUtils;
import com.yc.video.bridge.ControlWrapper;
import com.yc.video.config.ConstantKeys;
import com.yc.video.inter.IVideoController;
import com.yc.video.inter.IViewController;
import com.yc.video.inter.IVideoPlayer;
import com.yc.video.player.VideoViewManager;
import com.yc.video.player.VideoPlayer;
import com.yc.video.tool.StatesCutoutUtils;
import com.yc.video.tool.PlayerUtils;
import com.yc.video.inter.IControlView;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 控制器基类
 *     revise: 此类集成各种事件的处理逻辑，包括
 *             1.播放器状态改变: {@link #handlePlayerStateChanged(int)}
 *             2.播放状态改变: {@link #handlePlayStateChanged(int)}
 *             3.控制视图的显示和隐藏: {@link #handleVisibilityChanged(boolean, Animation)}
 *             4.播放进度改变: {@link #handleSetProgress(int, int)}
 *             5.锁定状态改变: {@link #handleLockStateChanged(boolean)}
 *             6.设备方向监听: {@link #onOrientationChanged(int)}
 *
 * </pre>
 */
public abstract class BaseVideoController extends FrameLayout implements IVideoController,
        IViewController,OrientationHelper.OnOrientationChangeListener {

    //播放器包装类，集合了MediaPlayerControl的api和IVideoController的api
    protected ControlWrapper mControlWrapper;
    @Nullable
    protected Activity mActivity;
    //控制器是否处于显示状态
    protected boolean mShowing;
    //是否处于锁定状态
    protected boolean mIsLocked;
    //播放视图隐藏超时
    protected int mDefaultTimeout = 5000;
    //是否开启根据屏幕方向进入/退出全屏
    private boolean mEnableOrientation;
    //屏幕方向监听辅助类
    protected OrientationHelper mOrientationHelper;
    //用户设置是否适配刘海屏
    private boolean mAdaptCutout;
    //是否有刘海
    private Boolean mHasCutout;
    //刘海的高度
    private int mCutoutHeight;
    //是否开始刷新进度
    private boolean mIsStartProgress;

    //保存了所有的控制组件
    //使用LinkedHashMap有序
    protected LinkedHashMap<IControlView, Boolean> mControlComponents = new LinkedHashMap<>();

    private Animation mShowAnim;
    private Animation mHideAnim;

    public BaseVideoController(@NonNull Context context) {
        //创建
        this(context, null);
    }

    public BaseVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        //创建
        this(context, attrs, 0);
    }

    public BaseVideoController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mShowAnim != null){
            mShowAnim.cancel();
            mShowAnim = null;
        }
        if (mHideAnim != null){
            mHideAnim.cancel();
            mHideAnim = null;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mControlWrapper.isPlaying() && (mEnableOrientation || mControlWrapper.isFullScreen())) {
            if (hasWindowFocus) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //检查系统是否开启自动旋转
                        mOrientationHelper.enable();
                    }
                }, 800);
            } else {
                //取消监听
                mOrientationHelper.disable();
            }
        }
    }

    protected void initView(Context context) {
        if (getLayoutId() != 0) {
            LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true);
        }
        mOrientationHelper = new OrientationHelper(context.getApplicationContext());
        mEnableOrientation = VideoViewManager.getConfig().mEnableOrientation;
        mAdaptCutout = VideoViewManager.getConfig().mAdaptCutout;
        mShowAnim = new AlphaAnimation(0f, 1f);
        mShowAnim.setDuration(300);
        mHideAnim = new AlphaAnimation(1f, 0f);
        mHideAnim.setDuration(300);
        mActivity = PlayerUtils.scanForActivity(context);
    }

    /**
     * 设置控制器布局文件，子类必须实现
     */
    protected abstract int getLayoutId();

    /**
     * 重要：此方法用于将{@link VideoPlayer} 和控制器绑定
     */
    @CallSuper
    public void setMediaPlayer(IVideoPlayer mediaPlayer) {
        mControlWrapper = new ControlWrapper(mediaPlayer, this);
        //绑定ControlComponent和Controller
        for (Map.Entry<IControlView, Boolean> next : mControlComponents.entrySet()) {
            IControlView component = next.getKey();
            component.attach(mControlWrapper);
        }
        //开始监听设备方向
        mOrientationHelper.setOnOrientationChangeListener(this);
    }

    /**
     * 添加控制组件，最后面添加的在最下面，合理组织添加顺序，可让ControlComponent位于不同的层级
     * @param controlViews                         view
     */
    @Override
    public void addControlComponent(IControlView... controlViews) {
        for (IControlView item : controlViews) {
            addControlComponent(item, false);
        }
    }

    /**
     * 添加控制组件，最后面添加的在最下面，合理组织添加顺序，可让InterControlView位于不同的层级
     * @param controlView                           view
     * @param isPrivate                             是否为独有的组件，如果是就不添加到控制器中
     */
    @Override
    public void addControlComponent(IControlView controlView, boolean isPrivate) {
        mControlComponents.put(controlView, isPrivate);
        if (mControlWrapper != null) {
            controlView.attach(mControlWrapper);
        }
        View view = controlView.getView();
        if (view != null && !isPrivate) {
            addView(view, 0);
        }
    }

    /**
     * 移除控制组件
     * @param controlView                           view
     */
    @Override
    public void removeControlComponent(IControlView controlView) {
        removeView(controlView.getView());
        mControlComponents.remove(controlView);
    }

    /**
     * 移除所有的组件
     */
    @Override
    public void removeAllControlComponent() {
        for (Map.Entry<IControlView, Boolean> next : mControlComponents.entrySet()) {
            removeView(next.getKey().getView());
        }
        mControlComponents.clear();
    }

    /**
     * 移除所有独有的组件
     */
    @Override
    public void removeAllPrivateComponents() {
        Iterator<Map.Entry<IControlView, Boolean>> it = mControlComponents.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<IControlView, Boolean> next = it.next();
            if (next.getValue()) {
                it.remove();
            }
        }
    }

    /**
     * {@link VideoPlayer}调用此方法向控制器设置播放状态。
     * 这里使用注解限定符，不要使用1，2这种直观数字，不方便知道意思
     * 播放状态，主要是指播放器的各种状态
     * -1               播放错误
     * 0                播放未开始
     * 1                播放准备中
     * 2                播放准备就绪
     * 3                正在播放
     * 4                暂停播放
     * 5                正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
     * 6                暂停缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
     * 7                播放完成
     * 8                开始播放中止
     */
    @CallSuper
    public void setPlayState(@ConstantKeys.CurrentStateType int playState) {
        //设置播放器的状态
        handlePlayStateChanged(playState);
    }

    /**
     * {@link VideoPlayer}调用此方法向控制器设置播放器模式
     * 播放模式
     * 普通模式，小窗口模式，正常模式三种其中一种
     * MODE_NORMAL              普通模式
     * MODE_FULL_SCREEN         全屏模式
     * MODE_TINY_WINDOW         小屏模式
     */
    @CallSuper
    public void setPlayerState(@ConstantKeys.PlayModeType final int playerState) {
        //调用此方法向控制器设置播放器状态
        handlePlayerStateChanged(playerState);
    }

    /**
     * 设置播放视图自动隐藏超时
     */
    public void setDismissTimeout(int timeout) {
        if (timeout > 0) {
            mDefaultTimeout = timeout;
        } else {
            mDefaultTimeout = 5000;
        }
    }

    /**
     * 隐藏播放视图
     */
    @Override
    public void hide() {
        if (mShowing) {
            stopFadeOut();
            handleVisibilityChanged(false, mHideAnim);
            mShowing = false;
        }
    }

    /**
     * 显示播放视图
     */
    @Override
    public void show() {
        if (!mShowing) {
            handleVisibilityChanged(true, mShowAnim);
            startFadeOut();
            mShowing = true;
        }
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    /**
     * 开始计时
     */
    @Override
    public void startFadeOut() {
        //重新开始计时
        stopFadeOut();
        postDelayed(mFadeOut, mDefaultTimeout);
    }

    /**
     * 取消计时
     */
    @Override
    public void stopFadeOut() {
        if (mFadeOut!=null){
            removeCallbacks(mFadeOut);
        }
    }

    /**
     * 隐藏播放视图Runnable
     */
    protected final Runnable mFadeOut = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * 设置是否锁屏
     * @param locked                        是否锁定
     */
    @Override
    public void setLocked(boolean locked) {
        mIsLocked = locked;
        handleLockStateChanged(locked);
    }

    /**
     * 判断是否锁屏
     * @return                              返回是否锁屏
     */
    @Override
    public boolean isLocked() {
        return mIsLocked;
    }

    /**
     * 开始刷新进度，注意：需在STATE_PLAYING时调用才会开始刷新进度
     */
    @Override
    public void startProgress() {
        if (mIsStartProgress) {
            return;
        }
        post(mShowProgress);
        mIsStartProgress = true;
    }

    /**
     * 停止刷新进度
     */
    @Override
    public void stopProgress() {
        if (!mIsStartProgress) {
            return;
        }
        removeCallbacks(mShowProgress);
        mIsStartProgress = false;
    }

    /**
     * 刷新进度Runnable
     */
    protected Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            int pos = setProgress();
            if (mControlWrapper.isPlaying()) {
                float speed = mControlWrapper.getSpeed();
                //postDelayed(this, 1000);
                postDelayed(this, (long) ((1000  - pos % 1000) / speed));
            } else {
                mIsStartProgress = false;
            }
        }
    };

    private int setProgress() {
        int position = (int) mControlWrapper.getCurrentPosition();
        int duration = (int) mControlWrapper.getDuration();
        handleSetProgress(duration, position);
        return position;
    }

    /**
     * 设置是否适配刘海屏
     */
    public void setAdaptCutout(boolean adaptCutout) {
        mAdaptCutout = adaptCutout;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        checkCutout();
    }

    /**
     * 检查是否需要适配刘海
     */
    private void checkCutout() {
        if (!mAdaptCutout) return;
        if (mActivity != null && mHasCutout == null) {
            mHasCutout = StatesCutoutUtils.allowDisplayToCutout(mActivity);
            if (mHasCutout) {
                //竖屏下的状态栏高度可认为是刘海的高度
                mCutoutHeight = (int) PlayerUtils.getStatusBarHeightPortrait(mActivity);
            }
        }
        AppLogUtils.d("hasCutout: " + mHasCutout + " cutout height: " + mCutoutHeight);
    }

    /**
     * 是否有刘海屏
     */
    @Override
    public boolean hasCutout() {
        return mHasCutout != null && mHasCutout;
    }

    /**
     * 刘海的高度
     */
    @Override
    public int getCutoutHeight() {
        return mCutoutHeight;
    }

    /**
     * 显示移动网络播放提示
     *
     * @return 返回显示移动网络播放提示的条件，false:不显示, true显示
     * 此处默认根据手机网络类型来决定是否显示，开发者可以重写相关逻辑
     */
    public boolean showNetWarning() {
        return AppNetworkUtils.isMobileAvailable()
                && !VideoViewManager.instance().playOnMobileNetwork();
    }

    /**
     * 播放和暂停
     */
    protected void togglePlay() {
        mControlWrapper.togglePlay();
    }

    /**
     * 横竖屏切换
     */
    protected void toggleFullScreen() {
        if (PlayerUtils.isActivityLiving(mActivity)){
            mControlWrapper.toggleFullScreen(mActivity);
        }
    }

    /**
     * 子类中请使用此方法来进入全屏
     *
     * @return 是否成功进入全屏
     */
    protected boolean startFullScreen() {
        if (!PlayerUtils.isActivityLiving(mActivity)) {
            return false;
        }
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mControlWrapper.startFullScreen();
        return true;
    }

    /**
     * 子类中请使用此方法来退出全屏
     *
     * @return 是否成功退出全屏
     */
    protected boolean stopFullScreen() {
        if (!PlayerUtils.isActivityLiving(mActivity)) {
            return false;
        }
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mControlWrapper.stopFullScreen();
        return true;
    }

    /**
     * 改变返回键逻辑，用于activity
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 是否自动旋转， 默认不自动旋转
     */
    public void setEnableOrientation(boolean enableOrientation) {
        mEnableOrientation = enableOrientation;
    }

    private int mOrientation = 0;

    @CallSuper
    @Override
    public void onOrientationChanged(int orientation) {
        if (mActivity == null || mActivity.isFinishing()) return;

        //记录用户手机上一次放置的位置
        int lastOrientation = mOrientation;

        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            //手机平放时，检测不到有效的角度
            //重置为原始位置 -1
            mOrientation = -1;
            return;
        }

        if (orientation > 350 || orientation < 10) {
            int o = mActivity.getRequestedOrientation();
            //手动切换横竖屏
            if (o == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && lastOrientation == 0) return;
            if (mOrientation == 0) return;
            //0度，用户竖直拿着手机
            mOrientation = 0;
            onOrientationPortrait(mActivity);
        } else if (orientation > 80 && orientation < 100) {

            int o = mActivity.getRequestedOrientation();
            //手动切换横竖屏
            if (o == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && lastOrientation == 90) return;
            if (mOrientation == 90) return;
            //90度，用户右侧横屏拿着手机
            mOrientation = 90;
            onOrientationReverseLandscape(mActivity);
        } else if (orientation > 260 && orientation < 280) {
            int o = mActivity.getRequestedOrientation();
            //手动切换横竖屏
            if (o == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && lastOrientation == 270) return;
            if (mOrientation == 270) return;
            //270度，用户左侧横屏拿着手机
            mOrientation = 270;
            onOrientationLandscape(mActivity);
        }
    }

    /**
     * 竖屏
     */
    protected void onOrientationPortrait(Activity activity) {
        //屏幕锁定的情况
        if (mIsLocked) return;
        //没有开启设备方向监听的情况
        if (!mEnableOrientation) return;

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mControlWrapper.stopFullScreen();
    }

    /**
     * 横屏
     */
    protected void onOrientationLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (mControlWrapper.isFullScreen()) {
            handlePlayerStateChanged(ConstantKeys.PlayMode.MODE_FULL_SCREEN);
        } else {
            mControlWrapper.startFullScreen();
        }
    }

    /**
     * 反向横屏
     */
    protected void onOrientationReverseLandscape(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        if (mControlWrapper.isFullScreen()) {
            handlePlayerStateChanged(ConstantKeys.PlayMode.MODE_FULL_SCREEN);
        } else {
            mControlWrapper.startFullScreen();
        }
    }

    //------------------------ start handle event change ------------------------//

    private void handleVisibilityChanged(boolean isVisible, Animation anim) {
        if (!mIsLocked) {
            //没锁住时才向ControlComponent下发此事件
            for (Map.Entry<IControlView, Boolean> next : mControlComponents.entrySet()) {
                IControlView component = next.getKey();
                component.onVisibilityChanged(isVisible, anim);
            }
        }
        onVisibilityChanged(isVisible, anim);
    }

    /**
     * 子类重写此方法监听控制的显示和隐藏
     *
     * @param isVisible 是否可见
     * @param anim      显示/隐藏动画
     */
    protected void onVisibilityChanged(boolean isVisible, Animation anim) {

    }

    private void handlePlayStateChanged(int playState) {
        for (Map.Entry<IControlView, Boolean> next : mControlComponents.entrySet()) {
            IControlView component = next.getKey();
            component.onPlayStateChanged(playState);
        }
        onPlayStateChanged(playState);
    }

    /**
     * 子类重写此方法并在其中更新控制器在不同播放状态下的ui
     */
    @CallSuper
    protected void onPlayStateChanged(int playState) {
        switch (playState) {
            case ConstantKeys.CurrentState.STATE_IDLE:
                mOrientationHelper.disable();
                mOrientation = 0;
                mIsLocked = false;
                mShowing = false;
                removeAllPrivateComponents();
                break;
            case ConstantKeys.CurrentState.STATE_BUFFERING_PLAYING:
                mIsLocked = false;
                mShowing = false;
                break;
            case ConstantKeys.CurrentState.STATE_ERROR:
                mShowing = false;
                break;
        }
    }

    /**
     * 播放器状态改变
     * @param playerState                       播放器状态
     */
    private void handlePlayerStateChanged(int playerState) {
        for (Map.Entry<IControlView, Boolean> next : mControlComponents.entrySet()) {
            IControlView component = next.getKey();
            component.onPlayerStateChanged(playerState);
        }
        onPlayerStateChanged(playerState);
    }

    /**
     * 子类重写此方法并在其中更新控制器在不同播放器状态下的ui
     * 普通模式，小窗口模式，正常模式三种其中一种
     * MODE_NORMAL              普通模式
     * MODE_FULL_SCREEN         全屏模式
     * MODE_TINY_WINDOW         小屏模式
     */
    @CallSuper
    protected void onPlayerStateChanged(@ConstantKeys.PlayMode int playerState) {
        switch (playerState) {
            case ConstantKeys.PlayMode.MODE_NORMAL:
                //视频正常播放是设置监听
                if (mEnableOrientation) {
                    //检查系统是否开启自动旋转
                    mOrientationHelper.enable();
                } else {
                    //取消监听
                    mOrientationHelper.disable();
                }
                if (hasCutout()) {
                    StatesCutoutUtils.adaptCutoutAboveAndroidP(getContext(), false);
                }
                break;
            case ConstantKeys.PlayMode.MODE_FULL_SCREEN:
                //在全屏时强制监听设备方向
                mOrientationHelper.enable();
                if (hasCutout()) {
                    StatesCutoutUtils.adaptCutoutAboveAndroidP(getContext(), true);
                }
                break;
            case ConstantKeys.PlayMode.MODE_TINY_WINDOW:
                //小窗口取消重力感应监听
                mOrientationHelper.disable();
                break;
            default:
                break;
        }
    }

    private void handleSetProgress(int duration, int position) {
        for (Map.Entry<IControlView, Boolean> next : mControlComponents.entrySet()) {
            IControlView component = next.getKey();
            component.setProgress(duration, position);
        }
        setProgress(duration, position);
    }

    /**
     * 刷新进度回调，子类可在此方法监听进度刷新，然后更新ui
     *
     * @param duration 视频总时长
     * @param position 视频当前时长
     */
    protected void setProgress(int duration, int position) {

    }

    private void handleLockStateChanged(boolean isLocked) {
        for (Map.Entry<IControlView, Boolean> next : mControlComponents.entrySet()) {
            IControlView component = next.getKey();
            component.onLockStateChanged(isLocked);
        }
        onLockStateChanged(isLocked);
    }

    /**
     * 子类可重写此方法监听锁定状态发生改变，然后更新ui
     */
    protected void onLockStateChanged(boolean isLocked) {

    }

    //------------------------ end handle event change ------------------------//
}
