package com.yc.audiofocus.focus;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.toolutils.context.AppToolUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 音视频专门处理焦点抢占工具
 *     revise: 处理音视频通话焦点抢占，处理音视频播放焦点抢占。单独抽离出来做成库
 * </pre>
 */
public final class AudioFocusHelper implements IAudioFocus {

    @Nullable
    private final AudioManager mAudioManager;

    private OnAudioFocusChangeListener mListener;

    private AudioFocusRequest mAudioFocusRequest;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener;
    private boolean mLossTransient;
    private boolean mLossTransientCanDuck;
    private boolean mRequested;
    private int mCurrentFocus = 0;

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
                if (mCurrentFocus == focusChange) {
                    return;
                }
                //由于onAudioFocusChange有可能在子线程调用，
                //故通过此方式切换到主线程去执行
                DelegateTaskExecutor.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        //处理音频焦点抢占
                        handleAudioFocusChange(focusChange);
                    }
                });
                mCurrentFocus = focusChange;
            }
        };
    }

    private void handleAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
                //焦点丢失，这个是永久丢失焦点，如被其他播放器抢占
                mListener.onLoss();
                mLossTransient = false;
                mLossTransientCanDuck = false;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //焦点暂时丢失，，如来电
                mLossTransient = true;
                mListener.onLossTransient();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                //此时需降低音量，瞬间丢失焦点，如通知
                mLossTransientCanDuck = true;
                mListener.onLossTransientCanDuck();
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                //重新获得焦点
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                //暂时获得焦点
                mListener.onGain(mLossTransient, mLossTransientCanDuck);
                mLossTransient = false;
                mLossTransientCanDuck = false;
                break;
            default:
                break;
        }
    }

    /**
     * 请求获取音频焦点
     */
    @Override
    public void requestAudioFocus() {
        if (mCurrentFocus == AudioManager.AUDIOFOCUS_GAIN) {
            //如果已经是获得焦点，则直接返回
            return;
        }
        requestAudioFocus(AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    /**
     * （主动）放弃音频焦点。
     */
    @Override
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void abandonAudioFocusAPI26() {
        AppToolUtils.checkNotNull(mAudioManager);
        mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);
    }

}
