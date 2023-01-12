package com.yc.audiofocus;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.yc.appcontextlib.AppToolUtils;

public final class AudioFocusHelper {

    @Nullable
    private AudioManager mAudioManager;

    private OnAudioFocusChangeListener mListener;

    private AudioFocusRequest mAudioFocusRequest;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener;

    private boolean mLossTransient;
    private boolean mLossTransientCanDuck;

    private boolean mRequested;

    /**
     * 创建一个 {@link AudioFocusHelper} 对象。
     *
     * @param context  Context 对象，不能为 null
     * @param listener 事件监听器，不能为 null
     */
    public AudioFocusHelper(@NonNull Context context,
                            @NonNull OnAudioFocusChangeListener listener) {
        AppToolUtils.checkNotNull(context);
        AppToolUtils.checkNotNull(listener);

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mListener = listener;
        initAudioFocusChangeListener();
    }

    public AudioFocusHelper(@NonNull Context context,
                            @NonNull AudioManager.OnAudioFocusChangeListener listener) {
        AppToolUtils.checkNotNull(context);
        AppToolUtils.checkNotNull(listener);

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioFocusChangeListener = listener;
    }

    private void initAudioFocusChangeListener() {
        mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS:
                        mListener.onLoss();
                        mLossTransient = false;
                        mLossTransientCanDuck = false;
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        mLossTransient = true;
                        mListener.onLossTransient();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        mLossTransientCanDuck = true;
                        mListener.onLossTransientCanDuck();
                        break;
                    case AudioManager.AUDIOFOCUS_GAIN:
                        mListener.onGain(mLossTransient, mLossTransientCanDuck);
                        mLossTransient = false;
                        mLossTransientCanDuck = false;
                        break;
                }
            }
        };
    }

    /**
     * 获取音频焦点。
     *
     * @param streamType   受焦点请求影响的主要音频流类型。该参数通常是 <code>AudioManager.STREAM_MUSIC</code>。更多音频
     *                     流类型，请查看 <a target="_blank" href="https://developer.android.google.cn/reference/android/media/AudioManager">AudioManager</a>
     *                     类中前缀为 <code>STREAM_</code> 的整形常量。
     * @param durationHint 可以是以下 4 个值之一：
     *                     <ol>
     *                         <li><code>AudioManager.AUDIOFOCUS_GAIN</code>：表示获取未知时长的音频焦点；</li>
     *                         <li><code>AudioManager.AUDIOFOCUS_GAIN_TRANSIENT</code>：表示短暂的获取音频焦点；</li>
     *                         <li><code>AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK</code>：表示短暂的获取音频焦点，同时指示先前的焦点所有者可以通过降低音量（duck），并继续播放；</li>
     *                         <li><code>AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE</code>：（API level 19）：表示短暂获取音频焦点，在此期间，其他任何应用程序或系统组件均不应播放任何内容。</li>
     *                     </ol>
     *                     更多内容，请查看 <a target="_blank" href="https://developer.android.google.cn/reference/android/media/AudioManager">AudioManager</a> 文档。
     * @return AudioManager.AUDIOFOCUS_REQUEST_GRANTED（申请成功）或者 AudioManager.AUDIOFOCUS_REQUEST_FAILED（申请失败）
     */
    public int requestAudioFocus(int streamType, int durationHint) {
        if (mAudioManager == null) {
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED;
        }

        mRequested = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return requestAudioFocusAPI26(streamType, durationHint);
        }

        return mAudioManager.requestAudioFocus(
                mAudioFocusChangeListener,
                streamType,
                durationHint);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private int requestAudioFocusAPI26(int streamType, int durationHint) {
        if (mAudioManager == null) {
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED;
        }

        mAudioFocusRequest = new AudioFocusRequest.Builder(durationHint)
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setLegacyStreamType(streamType)
                        .build())
                .setOnAudioFocusChangeListener(mAudioFocusChangeListener)
                .build();

        return mAudioManager.requestAudioFocus(mAudioFocusRequest);
    }

    /**
     * （主动）放弃音频焦点。
     */
    public void abandonAudioFocus() {
        if (mAudioManager == null || !mRequested) {
            return;
        }

        mRequested = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            abandonAudioFocusAPI26();
            return;
        }

        mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void abandonAudioFocusAPI26() {
        AppToolUtils.checkNotNull(mAudioManager);
        mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);
    }

    /**
     * 可用于监听当前应用程序的音频焦点的获取与丢失。
     */
    public interface OnAudioFocusChangeListener {
        /**
         * 音频焦点永久性丢失（此时应暂停播放）
         */
        void onLoss();

        /**
         * 音频焦点暂时性丢失（此时应暂停播放）
         */
        void onLossTransient();

        /**
         * 音频焦点暂时性丢失（此时只需降低音量，不需要暂停播放）
         */
        void onLossTransientCanDuck();

        /**
         * 重新获取到音频焦点。
         * <p>
         * 如果应用因 {@link #onLossTransientCanDuck()} 事件而降低了音量（lossTransientCanDuck 参数为 true），
         * 那么此时应恢复正常的音量。
         *
         * @param lossTransient        指示音频焦点是否是暂时性丢失，如果是，则此时可以恢复播放。
         * @param lossTransientCanDuck 指示音频焦点是否是可降低音量的暂时性丢失，如果是，则此时只需恢复音量即可。
         */
        void onGain(boolean lossTransient, boolean lossTransientCanDuck);
    }
}
