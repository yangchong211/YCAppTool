package com.yc.kernel.impl.exo;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2021/11/15
 *     desc  : exo监听listener，监听播放事件
 *     revise:
 * </pre>
 */
public class DefaultEventListener implements Player.EventListener {

    /**
     * 检测播放列表何时更改
     * 当添加、删除或移动媒体项目时， Listener.onTimelineChanged(Timeline, @TimelineChangeReason)会立即使用 调用
     * @param timeline
     * @param reason
     */
    @Override
    public void onTimelineChanged(Timeline timeline, int reason) {

    }

    /**
     * 在刷新时间线和/或清单时调用
     * @param timeline
     * @param manifest
     * @param reason
     */
    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    /**
     * 当可用或选定的轨道发生变化时调用
     * @param trackGroups
     * @param trackSelections
     */
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    /**
     * 当玩家开始或停止加载源文件时调用。
     * @param isLoading
     */
    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    /**
     * 当播放状态发生变化时调用
     * @param playWhenReady
     * @param playbackState
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    /**
     * 当{@link # isPlaying() }的值改变时调用
     * @param isPlaying
     */
    @Override
    public void onIsPlayingChanged(boolean isPlaying) {

    }

    /**
     * 当播放出现异常时调用
     * @param error
     */
    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
