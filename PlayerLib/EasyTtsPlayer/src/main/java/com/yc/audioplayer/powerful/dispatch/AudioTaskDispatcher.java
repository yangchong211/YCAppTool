package com.yc.audioplayer.powerful.dispatch;

import android.os.Process;

import com.yc.audioplayer.powerful.bean.TtsPlayerConfig;
import com.yc.audioplayer.powerful.bean.AudioPlayData;
import com.yc.audioplayer.powerful.inter.InterPlayListener;
import com.yc.audioplayer.powerful.service.AudioManager;
import com.yc.audioplayer.powerful.service.AudioService;
import com.yc.audioplayer.powerful.inter.AbstractAudioWrapper;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : task任务分发处理器
 *     revise:
 * </pre>
 */
public class AudioTaskDispatcher implements InterPlayListener {

    private AudioTtsDeque mTaskDeque;
    private AudioPlayData mCurrentPlayData;
    private AbstractAudioWrapper mAudioManager;
    private boolean mRunning = true;
    private Thread mTtsThread;

    private static class Holder {
        private static final AudioTaskDispatcher INSTANCE = new AudioTaskDispatcher();
    }

    private AudioTaskDispatcher() {

    }

    public static AudioTaskDispatcher getInstance() {
        //使用单利模式
        return Holder.INSTANCE;
    }

    @Override
    public void onCompleted() {
        //完成状态
        mAudioManager.onCompleted();
    }

    @Override
    public void onError(String error) {
        mAudioManager.onError(error);
    }

    /**
     * 初始化
     *
     * @param manager AudioManager
     */
    public void initialize(final AudioManager manager) {
        this.mAudioManager = manager;
        //创建tts消息队列
        this.mTaskDeque = new AudioTtsDeque();
        this.mRunning = true;
        TtsPlayerConfig config = AudioService.getInstance().getConfig();
        config.getLogger().log("AudioTaskDispatcher initialize: ");
        this.mTtsThread = new Thread() {
            @Override
            public void run() {
                doWhileThing(manager);
            }
        };
        mTtsThread.setName("tts-audio-thread");
        mTtsThread.start();
    }

    private void doWhileThing(final AudioManager manager) {
        TtsPlayerConfig config = AudioService.getInstance().getConfig();
        //设置进程优先级
        Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
        while (mRunning) {
            try {
                if (config != null && config.getLogger() != null) {
                    config.getLogger().log("AudioTaskDispatcher is running");
                }
                //从tts消息队列获取当前数据
                mCurrentPlayData = mTaskDeque.get();
                //播放当前数据
                mAudioManager.play(mCurrentPlayData);
                synchronized (manager.mMutex) {
                    if (config != null && config.getLogger() != null) {
                        config.getLogger().log("AudioTaskDispatcher is wait  " + mCurrentPlayData.getTts());
                    }
                    //等待
                    manager.mMutex.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (config != null && config.getLogger() != null) {
                    config.getLogger().error("AudioTaskDispatcher is error  " + e.getMessage());
                }
                if (config != null && config.getExceptionTrack() != null) {
                    config.getExceptionTrack().onException("AudioTaskDispatcher", e);
                }
                return;
            }
        }
    }

    /**
     * 将播放内容data任务添加到分发器中
     *
     * @param data data
     */
    public void addTask(AudioPlayData data) {
        if (data == null) {
            return;
        }
        if (mCurrentPlayData != null &&
                data.mPriority.ordinal() > mCurrentPlayData.mPriority.ordinal()) {
            mAudioManager.stop();
        }
        TtsPlayerConfig config = AudioService.getInstance().getConfig();
        if (config != null && config.getLogger() != null) {
            config.getLogger().log("AudioTaskDispatcher data: " + data);
        }
        //添加对
        mTaskDeque.add(data);
    }

    public void release() {
        mRunning = false;
        mTaskDeque.clear();
        mTtsThread.interrupt();
    }

}
