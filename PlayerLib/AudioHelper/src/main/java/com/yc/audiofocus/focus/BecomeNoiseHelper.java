package com.yc.audiofocus.focus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import androidx.annotation.NonNull;

import com.yc.toolutils.context.AppToolUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : Audio输出通道切换
 *     revise:
 * </pre>
 */
public final class BecomeNoiseHelper {

    private final Context mContext;
    private final OnBecomeNoiseListener mListener;
    private final BroadcastReceiver mBecomeNoiseReceiver;
    private boolean mRegistered;

    /**
     * 创建一个 {@link BecomeNoiseHelper} 对象。
     *
     * @param context  Context 对象，不能为 null
     * @param listener 事件监听器，不能为 null
     */
    public BecomeNoiseHelper(@NonNull Context context, @NonNull final OnBecomeNoiseListener listener) {
        AppToolUtils.checkNotNull(context);
        AppToolUtils.checkNotNull(listener);

        mContext = context.getApplicationContext();
        mListener = listener;
        mBecomeNoiseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                    mListener.onBecomeNoise();
                }
            }
        };
    }

    /**
     * 注册 <b>AudioManager.ACTION_AUDIO_BECOMING_NOISY</b> 广播监听器。
     */
    public void registerBecomeNoiseReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        mContext.registerReceiver(mBecomeNoiseReceiver, intentFilter);

        mRegistered = true;
    }

    /**
     * 取消注册 <b>AudioManager.ACTION_AUDIO_BECOMING_NOISY</b> 广播监听器。
     */
    public void unregisterBecomeNoiseReceiver() {
        if (mRegistered) {
            mContext.unregisterReceiver(mBecomeNoiseReceiver);
            mRegistered = false;
        }
    }

    /**
     * 用于监听 become noise 事件。
     */
    public interface OnBecomeNoiseListener {
        /**
         * 当监听到 become noise 事件时会调用该方法，此时应暂停播放。
         * Audio输出通道切换：Audio输出通道切换的典型场景—— 用耳机听音乐时，拔出耳机
         */
        void onBecomeNoise();
    }
}
