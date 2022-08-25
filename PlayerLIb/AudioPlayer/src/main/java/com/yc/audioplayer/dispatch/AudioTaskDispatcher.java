package com.yc.audioplayer.dispatch;

import android.os.Process;

import com.yc.audioplayer.deque.AudioTtsDeque;
import com.yc.audioplayer.manager.AudioManager;
import com.yc.audioplayer.wrapper.AbstractAudioWrapper;
import com.yc.audioplayer.bean.AudioPlayData;
import com.yc.audioplayer.inter.InterPlayListener;
import com.yc.videotool.VideoLogUtils;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCVideoPlayer
 *     time  : 2018/8/6
 *     desc  : task任务分发处理器
 *     revise:
 * </pre>
 */
public class AudioTaskDispatcher implements InterPlayListener {

    private AudioTtsDeque mTaskDeque ;
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

    }

    /**
     * 初始化
     * @param manager                     AudioManager
     */
    public void initialize(final AudioManager manager) {
        this.mAudioManager = manager;
        //创建tts消息队列
        this.mTaskDeque = new AudioTtsDeque();
        this.mRunning = true;
        VideoLogUtils.d("AudioTaskDispatcher initialize: ");
        this.mTtsThread = new Thread() {
            @Override
            public void run() {
                //设置进程优先级
                Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
                while (mRunning) {
                    try {
                        VideoLogUtils.d("AudioTaskDispatcher is running ");
                        //从tts消息队列获取当前数据
                        mCurrentPlayData = mTaskDeque.get();
                        //播放当前数据
                        mAudioManager.play(mCurrentPlayData);
                        synchronized (manager.mMutex) {
                            VideoLogUtils.d("AudioTaskDispatcher is wait  " + mCurrentPlayData.getTts());
                            //等待
                            manager.mMutex.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        };
        this.mTtsThread.start();
    }

    /**
     * 将播放内容data任务添加到分发器中
     * @param data                              data
     */
    public void addTask(AudioPlayData data) {
        if (data == null) {
            return;
        }
        if (mCurrentPlayData != null &&
                data.mPriority.ordinal() > mCurrentPlayData.mPriority.ordinal()) {
            mAudioManager.stop();
        }
        VideoLogUtils.d("AudioTaskDispatcher data: " + data.getTts() + data.mPriority);
        //添加对
        mTaskDeque.add(data);
    }

    public void release() {
        mRunning = false;
        mTaskDeque.clear();
        mTtsThread.interrupt();
    }

}
