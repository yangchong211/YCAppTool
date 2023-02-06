package com.yc.kernel.factory;

import android.content.Context;

import com.yc.kernel.impl.exo.ExoMediaPlayer;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : exo视频播放器Factory
 *     revise: 抽象工厂具体实现类
 * </pre>
 */
public class ExoPlayerFactory implements PlayerFactory<ExoMediaPlayer> {

    /**
     * 创建exo工厂类
     *
     * @return ExoPlayerFactory
     */
    public static ExoPlayerFactory create() {
        return new ExoPlayerFactory();
    }

    /**
     * 创建exo播放器
     *
     * @param context 上下文
     * @return ExoMediaPlayer
     */
    @Override
    public ExoMediaPlayer createPlayer(Context context) {
        return new ExoMediaPlayer(context);
    }
}
