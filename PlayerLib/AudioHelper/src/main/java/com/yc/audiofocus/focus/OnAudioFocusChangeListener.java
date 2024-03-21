package com.yc.audiofocus.focus;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 可用于监听当前应用程序的音频焦点的获取与丢失
 *     revise:
 * </pre>
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
