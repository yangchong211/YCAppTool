package com.yc.audioplayer.deque;

import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.bean.TtsPlayerConfig;
import com.yc.audioplayer.service.AudioService;
import com.yc.videotool.VideoLogUtils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
 *     time  : 2018/8/6
 *     desc  : tts消息队列
 *     revise:
 * </pre>
 */
public class AudioTtsDeque {

    /**
     * 创建锁对象
     */
    private final Lock mLock = new ReentrantLock();
    private final Condition mNotEmpty = mLock.newCondition();
    private final LinkedBlockingDeque<AudioPlayData> mHighDeque = new LinkedBlockingDeque<>();
    private final LinkedBlockingDeque<AudioPlayData> mMiddleDeque = new LinkedBlockingDeque<>();
    private final LinkedBlockingDeque<AudioPlayData> mNormalDeque = new LinkedBlockingDeque<>();

    /**
     * 将播放内容tts添加到对应级别待播放队列中
     * @param tts                           tts
     */
    public void add(AudioPlayData tts) {
        mLock.lock();
        try {
            TtsPlayerConfig config = AudioService.getInstance().getConfig();
            switch (tts.mPriority) {
                //最高优先级
                case HIGH_PRIORITY:
                    mHighDeque.add(tts);
                    if (config!=null && config.getLogger()!=null){
                        config.getLogger().log("TTS queue add high: " + tts.getTts());
                    }
                    break;
                //中优先级
                case MIDDLE_PRIORITY:
                    mMiddleDeque.add(tts);
                    if (config!=null && config.getLogger()!=null){
                        config.getLogger().log("TTS queue add  middle: " + tts.getTts());
                    }
                    break;
                //普通级别
                case NORMAL_PRIORITY:
                    mNormalDeque.add(tts);
                    if (config!=null && config.getLogger()!=null){
                        config.getLogger().log("TTS queue add  normal: " + tts.getTts());
                    }
                    break;
                default:
                    break;
            }
            mNotEmpty.signal();
        } finally {
            mLock.unlock();
        }
    }

    public AudioPlayData get() throws InterruptedException {
        AudioPlayData data;
        mLock.lock();
        try {
            TtsPlayerConfig config = AudioService.getInstance().getConfig();
            while ((data = getTts()) == null) {
                if (config!=null && config.getLogger()!=null){
                    config.getLogger().log("TTS queue no data to play ");
                }
                mNotEmpty.await();
            }
            if (config!=null && config.getLogger()!=null){
                config.getLogger().log("TTS queue  will play is" + data.getTts() + " rawId " + data.getRawId());
            }
        } finally {
            mLock.unlock();
        }
        return data;
    }

    /**
     * 获取tts播放内容，按照优先级从P0至P2的顺序依次取出
     * @return
     */
    public AudioPlayData getTts() {
        //先从高开始取
        AudioPlayData tts = mHighDeque.poll();
        if (tts == null) {
            //如果高没有，则从中开始取
            tts = mMiddleDeque.poll();
        }
        if (tts == null) {
            //否则则获取优先级最低的normal队列
            tts = mNormalDeque.poll();
        }
        TtsPlayerConfig config = AudioService.getInstance().getConfig();
        if (config!=null && config.getLogger()!=null){
            config.getLogger().log("TTS queue get data is " + tts);
        }
        return tts;
    }

    public void clear() {
        mHighDeque.clear();
        mMiddleDeque.clear();
        mNormalDeque.clear();
    }
}
