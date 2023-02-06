package com.yc.kernel.impl.exo;

import android.os.SystemClock;
import android.view.Surface;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.util.Util;
import com.yc.toolutils.AppLogUtils;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2021/11/15
 *     desc  : exo使用分析监听器。可以打开开关，方便查看视频播放器每个阶段日志
 *     revise:
 * </pre>
 */
public class DefaultAnalyticsListener implements AnalyticsListener {

    private static final String DEFAULT_TAG = "YCAnalyticsListener";
    private static final int MAX_TIMELINE_ITEM_LINES = 3;
    private static final NumberFormat TIME_FORMAT;
    @Nullable
    private final MappingTrackSelector trackSelector;
    private final String tag;
    private final Timeline.Window window;
    private final Timeline.Period period;
    private final long startTimeMs;

    public DefaultAnalyticsListener(@Nullable MappingTrackSelector trackSelector) {
        this(trackSelector, DEFAULT_TAG);
    }

    public DefaultAnalyticsListener(@Nullable MappingTrackSelector trackSelector, String tag) {
        this.trackSelector = trackSelector;
        this.tag = tag;
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        this.startTimeMs = SystemClock.elapsedRealtime();
    }

    @Override
    public void onLoadingChanged(EventTime eventTime, boolean isLoading) {
        this.logd(eventTime, "loading", Boolean.toString(isLoading));
    }

    @Override
    public void onPlayerStateChanged(EventTime eventTime, boolean playWhenReady, int state) {
        this.logd(eventTime, "state", playWhenReady + ", " + getStateString(state));
    }

    @Override
    public void onPlaybackSuppressionReasonChanged(EventTime eventTime, int playbackSuppressionReason) {
        this.logd(eventTime, "playbackSuppressionReason", getPlaybackSuppressionReasonString(playbackSuppressionReason));
    }

    @Override
    public void onIsPlayingChanged(EventTime eventTime, boolean isPlaying) {
        this.logd(eventTime, "isPlaying", Boolean.toString(isPlaying));
    }

    @Override
    public void onRepeatModeChanged(EventTime eventTime, int repeatMode) {
        this.logd(eventTime, "repeatMode", getRepeatModeString(repeatMode));
    }

    @Override
    public void onShuffleModeChanged(EventTime eventTime, boolean shuffleModeEnabled) {
        this.logd(eventTime, "shuffleModeEnabled", Boolean.toString(shuffleModeEnabled));
    }

    @Override
    public void onPositionDiscontinuity(EventTime eventTime, int reason) {
        this.logd(eventTime, "positionDiscontinuity", getDiscontinuityReasonString(reason));
    }

    @Override
    public void onSeekStarted(EventTime eventTime) {
        this.logd(eventTime, "seekStarted");
    }

    @Override
    public void onPlaybackParametersChanged(EventTime eventTime, PlaybackParameters playbackParameters) {
        this.logd(eventTime, "playbackParameters", Util.formatInvariant("speed=%.2f, pitch=%.2f, skipSilence=%s", new Object[]{playbackParameters.speed, playbackParameters.pitch, playbackParameters.skipSilence}));
    }

    @Override
    public void onTimelineChanged(EventTime eventTime, int reason) {
        int periodCount = eventTime.timeline.getPeriodCount();
        int windowCount = eventTime.timeline.getWindowCount();
        this.logd("timeline [" + this.getEventTimeString(eventTime) + ", periodCount=" + periodCount + ", windowCount=" + windowCount + ", reason=" + getTimelineChangeReasonString(reason));

        int i;
        for (i = 0; i < Math.min(periodCount, 3); ++i) {
            eventTime.timeline.getPeriod(i, this.period);
            this.logd("  period [" + getTimeString(this.period.getDurationMs()) + "]");
        }

        if (periodCount > 3) {
            this.logd("  ...");
        }

        for (i = 0; i < Math.min(windowCount, 3); ++i) {
            eventTime.timeline.getWindow(i, this.window);
            this.logd("  window [" + getTimeString(this.window.getDurationMs()) + ", " + this.window.isSeekable + ", " + this.window.isDynamic + "]");
        }

        if (windowCount > 3) {
            this.logd("  ...");
        }

        this.logd("]");
    }

    @Override
    public void onPlayerError(EventTime eventTime, ExoPlaybackException e) {
        this.loge(eventTime, "playerFailed", e);
    }

    @Override
    public void onTracksChanged(EventTime eventTime, TrackGroupArray ignored, TrackSelectionArray trackSelections) {
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = this.trackSelector != null ? this.trackSelector.getCurrentMappedTrackInfo() : null;
        if (mappedTrackInfo == null) {
            this.logd(eventTime, "tracks", "[]");
        } else {
            this.logd("tracks [" + this.getEventTimeString(eventTime) + ", ");
            int rendererCount = mappedTrackInfo.getRendererCount();

            int selectionIndex;
            String formatSupport;
            for (int rendererIndex = 0; rendererIndex < rendererCount; ++rendererIndex) {
                TrackGroupArray rendererTrackGroups = mappedTrackInfo.getTrackGroups(rendererIndex);
                TrackSelection trackSelection = trackSelections.get(rendererIndex);
                if (rendererTrackGroups.length > 0) {
                    this.logd("  Renderer:" + rendererIndex + " [");

                    for (selectionIndex = 0; selectionIndex < rendererTrackGroups.length; ++selectionIndex) {
                        TrackGroup trackGroup = rendererTrackGroups.get(selectionIndex);
                        formatSupport = getAdaptiveSupportString(trackGroup.length, mappedTrackInfo.getAdaptiveSupport(rendererIndex, selectionIndex, false));
                        this.logd("    Group:" + selectionIndex + ", adaptive_supported=" + formatSupport + " [");

                        for (int trackIndex = 0; trackIndex < trackGroup.length; ++trackIndex) {
                            String status = getTrackStatusString(trackSelection, trackGroup, trackIndex);
                            formatSupport = RendererCapabilities.getFormatSupportString(mappedTrackInfo.getTrackSupport(rendererIndex, selectionIndex, trackIndex));
                            this.logd("      " + status + " Track:" + trackIndex + ", " + Format.toLogString(trackGroup.getFormat(trackIndex)) + ", supported=" + formatSupport);
                        }

                        this.logd("    ]");
                    }

                    if (trackSelection != null) {
                        for (selectionIndex = 0; selectionIndex < trackSelection.length(); ++selectionIndex) {
                            Metadata metadata = trackSelection.getFormat(selectionIndex).metadata;
                            if (metadata != null) {
                                this.logd("    Metadata [");
                                this.printMetadata(metadata, "      ");
                                this.logd("    ]");
                                break;
                            }
                        }
                    }

                    this.logd("  ]");
                }
            }

            TrackGroupArray unassociatedTrackGroups = mappedTrackInfo.getUnmappedTrackGroups();
            if (unassociatedTrackGroups.length > 0) {
                this.logd("  Renderer:None [");

                for (int groupIndex = 0; groupIndex < unassociatedTrackGroups.length; ++groupIndex) {
                    this.logd("    Group:" + groupIndex + " [");
                    TrackGroup trackGroup = unassociatedTrackGroups.get(groupIndex);

                    for (selectionIndex = 0; selectionIndex < trackGroup.length; ++selectionIndex) {
                        String status = getTrackStatusString(false);
                        this.logd("      " + status + " Track:" + selectionIndex + ", " + Format.toLogString(trackGroup.getFormat(selectionIndex)));
                    }

                    this.logd("    ]");
                }

                this.logd("  ]");
            }

            this.logd("]");
        }
    }

    @Override
    public void onSeekProcessed(EventTime eventTime) {
        this.logd(eventTime, "seekProcessed");
    }

    @Override
    public void onMetadata(EventTime eventTime, Metadata metadata) {
        this.logd("metadata [" + this.getEventTimeString(eventTime) + ", ");
        this.printMetadata(metadata, "  ");
        this.logd("]");
    }

    @Override
    public void onDecoderEnabled(EventTime eventTime, int trackType, DecoderCounters counters) {
        this.logd(eventTime, "decoderEnabled", Util.getTrackTypeString(trackType));
    }

    @Override
    public void onAudioSessionId(EventTime eventTime, int audioSessionId) {
        this.logd(eventTime, "audioSessionId", Integer.toString(audioSessionId));
    }

    @Override
    public void onAudioAttributesChanged(EventTime eventTime, AudioAttributes audioAttributes) {
        this.logd(eventTime, "audioAttributes", audioAttributes.contentType + "," + audioAttributes.flags + "," + audioAttributes.usage + "," + audioAttributes.allowedCapturePolicy);
    }

    @Override
    public void onVolumeChanged(EventTime eventTime, float volume) {
        this.logd(eventTime, "volume", Float.toString(volume));
    }

    @Override
    public void onDecoderInitialized(EventTime eventTime, int trackType, String decoderName, long initializationDurationMs) {
        this.logd(eventTime, "decoderInitialized", Util.getTrackTypeString(trackType) + ", " + decoderName);
    }

    @Override
    public void onDecoderInputFormatChanged(EventTime eventTime, int trackType, Format format) {
        this.logd(eventTime, "decoderInputFormat", Util.getTrackTypeString(trackType) + ", " + Format.toLogString(format));
    }

    @Override
    public void onDecoderDisabled(EventTime eventTime, int trackType, DecoderCounters counters) {
        this.logd(eventTime, "decoderDisabled", Util.getTrackTypeString(trackType));
    }

    @Override
    public void onAudioUnderrun(EventTime eventTime, int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
        this.loge(eventTime, "audioTrackUnderrun", bufferSize + ", " + bufferSizeMs + ", " + elapsedSinceLastFeedMs + "]", (Throwable) null);
    }

    @Override
    public void onDroppedVideoFrames(EventTime eventTime, int count, long elapsedMs) {
        this.logd(eventTime, "droppedFrames", Integer.toString(count));
    }

    @Override
    public void onVideoSizeChanged(EventTime eventTime, int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        this.logd(eventTime, "videoSize", width + ", " + height);
    }

    @Override
    public void onRenderedFirstFrame(EventTime eventTime, @Nullable Surface surface) {
        this.logd(eventTime, "renderedFirstFrame", String.valueOf(surface));
    }

    @Override
    public void onMediaPeriodCreated(EventTime eventTime) {
        this.logd(eventTime, "mediaPeriodCreated");
    }

    @Override
    public void onMediaPeriodReleased(EventTime eventTime) {
        this.logd(eventTime, "mediaPeriodReleased");
    }

    @Override
    public void onLoadStarted(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData) {
    }

    @Override
    public void onLoadError(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {
        this.printInternalError(eventTime, "loadError", error);
    }

    @Override
    public void onLoadCanceled(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData) {
    }

    @Override
    public void onLoadCompleted(EventTime eventTime, MediaSourceEventListener.LoadEventInfo loadEventInfo, MediaSourceEventListener.MediaLoadData mediaLoadData) {
    }

    @Override
    public void onReadingStarted(EventTime eventTime) {
        this.logd(eventTime, "mediaPeriodReadingStarted");
    }

    @Override
    public void onBandwidthEstimate(EventTime eventTime, int totalLoadTimeMs, long totalBytesLoaded, long bitrateEstimate) {
    }

    @Override
    public void onSurfaceSizeChanged(EventTime eventTime, int width, int height) {
        this.logd(eventTime, "surfaceSize", width + ", " + height);
    }

    @Override
    public void onUpstreamDiscarded(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {
        this.logd(eventTime, "upstreamDiscarded", Format.toLogString(mediaLoadData.trackFormat));
    }

    @Override
    public void onDownstreamFormatChanged(EventTime eventTime, MediaSourceEventListener.MediaLoadData mediaLoadData) {
        this.logd(eventTime, "downstreamFormat", Format.toLogString(mediaLoadData.trackFormat));
    }

    @Override
    public void onDrmSessionAcquired(EventTime eventTime) {
        this.logd(eventTime, "drmSessionAcquired");
    }

    @Override
    public void onDrmSessionManagerError(EventTime eventTime, Exception e) {
        this.printInternalError(eventTime, "drmSessionManagerError", e);
    }

    @Override
    public void onDrmKeysRestored(EventTime eventTime) {
        this.logd(eventTime, "drmKeysRestored");
    }

    @Override
    public void onDrmKeysRemoved(EventTime eventTime) {
        this.logd(eventTime, "drmKeysRemoved");
    }

    @Override
    public void onDrmKeysLoaded(EventTime eventTime) {
        this.logd(eventTime, "drmKeysLoaded");
    }

    @Override
    public void onDrmSessionReleased(EventTime eventTime) {
        this.logd(eventTime, "drmSessionReleased");
    }

    protected void logd(String msg) {
        AppLogUtils.d(this.tag + msg);
    }

    protected void loge(String msg, @Nullable Throwable tr) {
        AppLogUtils.e(this.tag + msg, tr);
    }

    private void logd(EventTime eventTime, String eventName) {
        this.logd(this.getEventString(eventTime, eventName));
    }

    private void logd(EventTime eventTime, String eventName, String eventDescription) {
        this.logd(this.getEventString(eventTime, eventName, eventDescription));
    }

    private void loge(EventTime eventTime, String eventName, @Nullable Throwable throwable) {
        this.loge(this.getEventString(eventTime, eventName), throwable);
    }

    private void loge(EventTime eventTime, String eventName, String eventDescription, @Nullable Throwable throwable) {
        this.loge(this.getEventString(eventTime, eventName, eventDescription), throwable);
    }

    private void printInternalError(EventTime eventTime, String type, Exception e) {
        this.loge(eventTime, "internalError", type, e);
    }

    private void printMetadata(Metadata metadata, String prefix) {
        for (int i = 0; i < metadata.length(); ++i) {
            this.logd(prefix + metadata.get(i));
        }

    }

    private String getEventString(EventTime eventTime, String eventName) {
        return eventName + " [" + this.getEventTimeString(eventTime) + "]";
    }

    private String getEventString(EventTime eventTime, String eventName, String eventDescription) {
        return eventName + " [" + this.getEventTimeString(eventTime) + ", " + eventDescription + "]";
    }

    private String getEventTimeString(EventTime eventTime) {
        String windowPeriodString = "window=" + eventTime.windowIndex;
        if (eventTime.mediaPeriodId != null) {
            windowPeriodString = windowPeriodString + ", period=" + eventTime.timeline.getIndexOfPeriod(eventTime.mediaPeriodId.periodUid);
            if (eventTime.mediaPeriodId.isAd()) {
                windowPeriodString = windowPeriodString + ", adGroup=" + eventTime.mediaPeriodId.adGroupIndex;
                windowPeriodString = windowPeriodString + ", ad=" + eventTime.mediaPeriodId.adIndexInAdGroup;
            }
        }

        return "eventTime=" + getTimeString(eventTime.realtimeMs - this.startTimeMs) + ", mediaPos=" + getTimeString(eventTime.currentPlaybackPositionMs) + ", " + windowPeriodString;
    }

    private static String getTimeString(long timeMs) {
        return timeMs == C.TIME_UNSET ? "?" : TIME_FORMAT.format((double) ((float) timeMs / 1000.0F));
    }

    private static String getStateString(int state) {
        switch (state) {
            case Player.STATE_BUFFERING:
                return "BUFFERING";
            case Player.STATE_ENDED:
                return "ENDED";
            case Player.STATE_IDLE:
                return "IDLE";
            case Player.STATE_READY:
                return "READY";
            default:
                return "?";
        }
    }

    private static String getAdaptiveSupportString(int trackCount, int adaptiveSupport) {
        if (trackCount < 2) {
            return "N/A";
        } else {
            switch (adaptiveSupport) {
                case RendererCapabilities.ADAPTIVE_SEAMLESS:
                    return "YES";
                case RendererCapabilities.ADAPTIVE_NOT_SEAMLESS:
                    return "YES_NOT_SEAMLESS";
                case RendererCapabilities.ADAPTIVE_NOT_SUPPORTED:
                    return "NO";
                default:
                    throw new IllegalStateException();
            }
        }
    }

    private static String getTrackStatusString(@Nullable TrackSelection selection, TrackGroup group, int trackIndex) {
        return getTrackStatusString(selection != null && selection.getTrackGroup() == group && selection.indexOf(trackIndex) != -1);
    }

    private static String getTrackStatusString(boolean enabled) {
        return enabled ? "[X]" : "[ ]";
    }

    private static String getRepeatModeString(int repeatMode) {
        switch (repeatMode) {
            case Player.REPEAT_MODE_OFF:
                return "OFF";
            case Player.REPEAT_MODE_ONE:
                return "ONE";
            case Player.REPEAT_MODE_ALL:
                return "ALL";
            default:
                return "?";
        }
    }

    private static String getDiscontinuityReasonString(int reason) {
        switch (reason) {
            case Player.DISCONTINUITY_REASON_PERIOD_TRANSITION:
                return "PERIOD_TRANSITION";
            case Player.DISCONTINUITY_REASON_SEEK:
                return "SEEK";
            case Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT:
                return "SEEK_ADJUSTMENT";
            case Player.DISCONTINUITY_REASON_AD_INSERTION:
                return "AD_INSERTION";
            case Player.DISCONTINUITY_REASON_INTERNAL:
                return "INTERNAL";
            default:
                return "?";
        }
    }

    private static String getTimelineChangeReasonString(int reason) {
        switch (reason) {
            case Player.TIMELINE_CHANGE_REASON_PREPARED:
                return "PREPARED";
            case Player.TIMELINE_CHANGE_REASON_RESET:
                return "RESET";
            case Player.TIMELINE_CHANGE_REASON_DYNAMIC:
                return "DYNAMIC";
            default:
                return "?";
        }
    }

    private static String getPlaybackSuppressionReasonString(int playbackSuppressionReason) {
        switch (playbackSuppressionReason) {
            case Player.PLAYBACK_SUPPRESSION_REASON_NONE:
                return "NONE";
            case Player.PLAYBACK_SUPPRESSION_REASON_TRANSIENT_AUDIO_FOCUS_LOSS:
                return "TRANSIENT_AUDIO_FOCUS_LOSS";
            default:
                return "?";
        }
    }

    static {
        TIME_FORMAT = NumberFormat.getInstance(Locale.getDefault());
        TIME_FORMAT.setMinimumFractionDigits(2);
        TIME_FORMAT.setMaximumFractionDigits(2);
        TIME_FORMAT.setGroupingUsed(false);
    }

}
