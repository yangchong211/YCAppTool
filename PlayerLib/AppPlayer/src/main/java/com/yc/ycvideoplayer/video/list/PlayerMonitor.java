package com.yc.ycvideoplayer.video.list;

import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.yc.toolutils.AppLogUtils;

import com.yc.video.bridge.ControlWrapper;
import com.yc.video.inter.IControlView;
import com.yc.video.tool.PlayerUtils;


public class PlayerMonitor implements IControlView {

    private ControlWrapper mControlWrapper;
    private static final String TAG = "PlayerMonitor";

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {
        AppLogUtils.d(TAG+ "---" +"onVisibilityChanged: " + isVisible);
    }

    @Override
    public void onPlayStateChanged(int playState) {
        AppLogUtils.d(TAG+ "---" +"onPlayStateChanged: " + PlayerUtils.playState2str(playState));
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        AppLogUtils.d(TAG+ "---" +"onPlayerStateChanged: " + PlayerUtils.playerState2str(playerState));
    }

    @Override
    public void setProgress(int duration, int position) {
        AppLogUtils.d(TAG+ "---" +"setProgress: duration: " + duration + " position: " + position + " buffered percent: " + mControlWrapper.getBufferedPercentage());
        AppLogUtils.d(TAG+ "---" +"network speed: " + mControlWrapper.getTcpSpeed());
    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        AppLogUtils.d(TAG+ "---" +"onLockStateChanged: " + isLocked);
    }
}
