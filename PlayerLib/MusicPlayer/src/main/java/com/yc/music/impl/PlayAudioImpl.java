package com.yc.music.impl;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.yc.music.config.MusicConstants;
import com.yc.music.config.MusicPlayAction;
import com.yc.music.config.PlayModeEnum;
import com.yc.music.inter.InterPlayAudio;
import com.yc.music.inter.OnPlayerEventListener;
import com.yc.music.manager.AudioFocusManager;
import com.yc.music.manager.MediaSessionManager;
import com.yc.music.model.AudioBean;
import com.yc.music.receiver.AudioBroadcastReceiver;
import com.yc.music.receiver.AudioEarPhoneReceiver;
import com.yc.music.service.PlayAudioService;
import com.yc.music.tool.BaseAppHelper;
import com.yc.music.tool.QuitTimerHelper;
import com.yc.music.utils.NotificationHelper;
import com.yc.toolutils.AppLogUtils;
import com.yc.toolutils.AppSpUtils;


import java.io.IOException;
import java.util.List;
import java.util.Random;

import static com.yc.music.service.PlayAudioService.UPDATE_PLAY_PROGRESS_SHOW;

public class PlayAudioImpl implements InterPlayAudio {

    private Context context;
    private PlayAudioService service;
    /**
     * 正在播放的歌曲的序号
     */
    private int mPlayingPosition = -1;
    /**
     * 播放器
     */
    private MediaPlayer mPlayer;
    /**
     * 允许与媒体控制器、音量键、媒体按钮和传输控件交互
     */
    private MediaSessionManager mMediaSessionManager;
    /**
     * 捕获/丢弃音乐焦点处理
     */
    private AudioFocusManager mAudioFocusManager;
    /**
     * 来电/耳机拔出时暂停播放
     * 在播放时调用，在暂停时注销
     */
    private final AudioEarPhoneReceiver mNoisyReceiver = new AudioEarPhoneReceiver();
    private final IntentFilter mFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    /**
     * 其他广播
     * 比如：屏幕灭了后再次亮了，会显示锁屏页面
     * 这个在onCreate中创建，在onDestroy中销毁
     */
    private final AudioBroadcastReceiver mAudioReceiver = new AudioBroadcastReceiver();
    /**
     * 广播接受者标识，避免多次注册广播
     */
    private boolean mReceiverTag = false;
    /**
     * 播放状态
     */
    private int mPlayState = MusicPlayAction.STATE_IDLE;

    @Override
    public void init(PlayAudioService service) {
        this.context = service;
        this.service = service;
        createMediaPlayer();
        initMediaSessionManager();
        initAudioFocusManager();
        initEarPhoneBroadcastReceiver();
        initAudioBroadcastReceiver();
    }

    @Override
    public void play(int position) {
        if (getAudioMusics().isEmpty()) {
            return;
        }

        if (position < 0) {
            position = getAudioMusics().size() - 1;
        } else if (position >= getAudioMusics().size()) {
            //如果是最后一首音乐，则播放时直接播放第一首音乐
            position = 0;
        }

        mPlayingPosition = position;
        AudioBean music = getPlayingMusic();
        String id = music.getId();
        AppLogUtils.e("PlayService"+"----id----"+ id);
        //保存当前播放的musicId，下次进来可以记录状态
        long musicId = Long.parseLong(id);
        AppSpUtils.getInstance(MusicConstants.SP_NAME).put(MusicConstants.MUSIC_ID,musicId);
        play(music);
    }

    @Override
    public void play() {
        if (!isPreparing() && !isPausing()) {
            return;
        }
        if(getAudioMusics()==null){
            return;
        }
        if(mAudioFocusManager.requestAudioFocus()){
            if(mPlayer!=null){
                mPlayer.start();
                mPlayState = MusicPlayAction.STATE_PLAYING;
                //开始发送消息，执行进度条进度更新
                QuitTimerHelper.getInstance().getHandler().sendEmptyMessage(UPDATE_PLAY_PROGRESS_SHOW);
                List<OnPlayerEventListener> onPlayerEventListeners = BaseAppHelper.get().getOnPlayerEventListeners();
                for (int i=0 ; i<onPlayerEventListeners.size() ; i++){
                    onPlayerEventListeners.get(i).onPlayerStart();
                }
                //当点击播放按钮时(播放详情页面或者底部控制栏)，同步通知栏中播放按钮状态
                NotificationHelper.get().showPlay(getPlayingMusic());
                //注册监听来电/耳机拔出时暂停播放广播
                if(!mReceiverTag){
                    mReceiverTag = true;
                    context.registerReceiver(mNoisyReceiver, mFilter);
                }
                mMediaSessionManager.updatePlaybackState();
            }
        }
    }

    @Override
    public void play(AudioBean music) {
        createMediaPlayer();
        try {
            mPlayer.reset();
            //把音频路径传给播放器
            mPlayer.setDataSource(music.getPath());
            //准备
            mPlayer.prepareAsync();
            //设置状态为准备中
            mPlayState = MusicPlayAction.STATE_PREPARING;
            //监听
            mPlayer.setOnPreparedListener(mOnPreparedListener);
            mPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mPlayer.setOnCompletionListener(mOnCompletionListener);
            mPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mPlayer.setOnErrorListener(mOnErrorListener);
            mPlayer.setOnInfoListener(mOnInfoListener);
            //当播放的时候，需要刷新界面信息
            List<OnPlayerEventListener> onPlayerEventListeners = BaseAppHelper.get().getOnPlayerEventListeners();
            for (int i=0 ; i<onPlayerEventListeners.size() ; i++){
                onPlayerEventListeners.get(i).onChange(music);
            }
            //更新通知栏
            NotificationHelper.get().showPlay(music);

            //更新
            mMediaSessionManager.updateMetaData(music);
            mMediaSessionManager.updatePlaybackState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (isDefault()) {
            return;
        }
        pause();
        if(mPlayer!=null){
            mPlayer.reset();
            mPlayState = MusicPlayAction.STATE_IDLE;
        }
    }

    @Override
    public void playPause() {
        if (isPreparing()) {
            stop();
        } else if (isPlaying()) {
            pause();
        } else if (isPausing()) {
            play();
        } else {
            play(getPlayingPosition());
        }
    }

    @Override
    public void release() {
        //放弃音频焦点
        mAudioFocusManager.abandonAudioFocus();
        mMediaSessionManager.release();
        //注销广播接收者
        context.unregisterReceiver(mAudioReceiver);
        //销毁MediaPlayer
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void pause() {
        if(mPlayer!=null){
            //暂停
            mPlayer.pause();
            //切换状态
            mPlayState = MusicPlayAction.STATE_PAUSE;
            //移除，注意一定要移除，否则一直走更新方法
            QuitTimerHelper.getInstance().getHandler().removeMessages(UPDATE_PLAY_PROGRESS_SHOW);
            //监听
            List<OnPlayerEventListener> onPlayerEventListeners = BaseAppHelper.get().getOnPlayerEventListeners();
            for (int i=0 ; i<onPlayerEventListeners.size() ; i++){
                onPlayerEventListeners.get(i).onPlayerPause();
            }
            //当点击暂停按钮时(播放详情页面或者底部控制栏)，同步通知栏中暂停按钮状态
            NotificationHelper.get().showPause(getPlayingMusic());
            //注销监听来电/耳机拔出时暂停播放广播
            //判断广播是否注册
            if (mReceiverTag) {
                //Tag值 赋值为false 表示该广播已被注销
                mReceiverTag = false;
                context.unregisterReceiver(mNoisyReceiver);
            }

            mMediaSessionManager.updatePlaybackState();
        }
    }

    @Override
    public void seekTo(int progress) {
        //只有当播放或者暂停的时候才允许拖动bar
        if (isPlaying() || isPausing()) {
            mPlayer.seekTo(progress);
            List<OnPlayerEventListener> onPlayerEventListeners = BaseAppHelper.get().getOnPlayerEventListeners();
            for (int i=0 ; i<onPlayerEventListeners.size() ; i++){
                onPlayerEventListeners.get(i).onUpdateProgress(progress);
            }
            mMediaSessionManager.updatePlaybackState();
        }
    }

    @Override
    public void next() {
        //建议都添加这个判断
        if (getAudioMusics().isEmpty()) {
            return;
        }
        int playMode = AppSpUtils.getInstance(MusicConstants.SP_NAME).getInt(MusicConstants.PLAY_MODE, 0);
        int size = getAudioMusics().size();
        PlayModeEnum mode = PlayModeEnum.valueOf(playMode);
        switch (mode) {
            //随机
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(size);
                play(mPlayingPosition);
                break;
            //单曲
            case SINGLE:
                play(mPlayingPosition);
                break;
            //顺序播放并且循环
            case LOOP:
            default:
                if (isHaveNext()){
                    // 如果不是最后一首，则还有下一首
                    mPlayingPosition++;
                } else {
                    // 如果是最后一首，则切换回第一首
                    mPlayingPosition = 0;
                }
                AppLogUtils.e("PlayService"+"----mPlayingPosition----"+ mPlayingPosition);
                play(mPlayingPosition);
                break;
        }
    }

    @Override
    public void prev() {
        //建议都添加这个判断
        if (getAudioMusics().isEmpty()) {
            return;
        }
        int playMode = AppSpUtils.getInstance(MusicConstants.SP_NAME).getInt(MusicConstants.PLAY_MODE, 0);
        int size = getAudioMusics().size();
        PlayModeEnum mode = PlayModeEnum.valueOf(playMode);
        switch (mode) {
            //随机
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(size);
                play(mPlayingPosition);
                break;
            //单曲
            case SINGLE:
                play(mPlayingPosition);
                break;
            //顺序播放并且循环
            case LOOP:
            default:
                if (isHavePre()){
                    // 如果不是第一首，则还有上一首
                    mPlayingPosition--;
                } else {
                    // 如果没有上一首，则切换到最后一首
                    mPlayingPosition = size;
                }
                play(mPlayingPosition);
                break;
        }
    }

    @Override
    public void updatePlayProgress() {
        //更新
        updatePlayProgressShow();
    }

    /**
     * 创建MediaPlayer对象
     */
    private void createMediaPlayer() {
        if(mPlayer==null){
            //MediaCodec codec = new MediaCodec();
            mPlayer = new MediaPlayer();
        }
    }



    /** 音频准备好的监听器 */
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        /** 当音频准备好可以播放了，则这个方法会被调用  */
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (isPreparing()) {
                play();
            }
        }
    };


    /** 当音频播放结束的时候的监听器 */
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        /** 当音频播放结果的时候这个方法会被调用 */
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    };


    /** 当音频缓冲的监听器 */
    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            // 缓冲百分比
            List<OnPlayerEventListener> onPlayerEventListeners = BaseAppHelper.get().getOnPlayerEventListeners();
            for (int i=0 ; i<onPlayerEventListeners.size() ; i++){
                onPlayerEventListeners.get(i).onBufferingUpdate(percent);
            }
        }
    };


    /** 跳转完成时的监听 */
    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }
    };

    /**
     * 播放错误的监听
     */
    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    /**
     * 设置音频信息监听器
     */
    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }
    };

    /**
     * 是否正在播放
     * @return          true表示正在播放
     */
    public boolean isPlaying() {
        return mPlayState == MusicPlayAction.STATE_PLAYING;
    }


    /**
     * 是否暂停
     * @return          true表示暂停
     */
    public boolean isPausing() {
        return mPlayState == MusicPlayAction.STATE_PAUSE;
    }


    /**
     * 是否正在准备中
     * @return          true表示正在准备中
     */
    public boolean isPreparing() {
        return mPlayState == MusicPlayAction.STATE_PREPARING;
    }


    /**
     * 是否正在准备中
     * @return          true表示正在准备中
     */
    public boolean isDefault() {
        return mPlayState == MusicPlayAction.STATE_IDLE;
    }

    /**
     * 更新播放进度的显示，时间的显示
     */
    private void updatePlayProgressShow() {
        if (isPlaying()) {
            int currentPosition =  mPlayer.getCurrentPosition();
            List<OnPlayerEventListener> onPlayerEventListeners = BaseAppHelper.get().getOnPlayerEventListeners();
            for (int i=0 ; i<onPlayerEventListeners.size() ; i++){
                onPlayerEventListeners.get(i).onUpdateProgress(currentPosition);
            }
        }
        AppLogUtils.e("updatePlayProgressShow");
        // 每30毫秒更新一下显示的内容，注意这里时间不要太短，因为这个是一个循环
        // 经过测试，60毫秒更新一次有点卡，30毫秒最为顺畅
        QuitTimerHelper.getInstance().getHandler().sendEmptyMessageDelayed(UPDATE_PLAY_PROGRESS_SHOW, 300);
    }


    /**
     * 获取正在播放的本地歌曲的序号
     */
    public int getPlayingPosition() {
        return mPlayingPosition;
    }


    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public AudioBean getPlayingMusic() {
        return getAudioMusics().get(mPlayingPosition);
    }


    /**
     * 获取播放的进度
     * @return          long类型值
     */
    public long getCurrentPosition() {
        if (isPlaying() || isPausing()) {
            return mPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * 判斷是否有上一首音頻
     * @return          true表示有
     */
    public boolean isHavePre() {
        if(getAudioMusics() !=null && getAudioMusics().size()>0){
            if(mPlayingPosition != 0){
                // 如果不是第一首，则还有上一首
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 判斷是否有下一首音頻
     * @return          true表示有
     */
    public boolean isHaveNext() {
        if(getAudioMusics() !=null && getAudioMusics().size()>0){
            if (mPlayingPosition != getAudioMusics().size() - 1) {
                // 如果不是最后一首，则还有下一首
                return true;
            } else {
                // 如果是最后一首，则切换回第一首
                return false;
            }
        }else {
            return false;
        }
    }

    private List<AudioBean> getAudioMusics(){
        return BaseAppHelper.get().getMusicList();
    }

    /**------------------------------------------------------------------------------------------*/


    /**
     * 允许与媒体控制器、音量键、媒体按钮和传输控件交互。
     * 播放器除了播放了音乐之外什么都没做，就可以分别在任务管理、锁屏、负一屏控制我的播放器
     */
    private void initMediaSessionManager() {
        mMediaSessionManager = new MediaSessionManager(service);
    }

    /**
     * 捕获/丢弃音乐焦点处理
     */
    private void initAudioFocusManager() {
        mAudioFocusManager = new AudioFocusManager(service);
    }

    /**
     * 初始化耳机插入和拔出监听
     */
    private void initEarPhoneBroadcastReceiver() {
        //这块直接在清单文件注册
    }

    /**
     * 初始化IntentFilter添加action意图
     * 主要是监听屏幕亮了与灭了
     */
    private void initAudioBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        //来电/耳机
        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        //锁屏
        filter.addAction(MusicConstants.LOCK_SCREEN_ACTION);
        //当屏幕灭了
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //当屏幕亮了
        filter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(mAudioReceiver, filter);
    }


    /**
     * 删除或下载歌曲后刷新正在播放的本地歌曲的序号
     */
    public void updatePlayingPosition() {
        int position = 0;
        long id = AppSpUtils.getInstance(MusicConstants.SP_NAME).getLong(MusicConstants.MUSIC_ID,-1);
        if(getAudioMusics().isEmpty()){
            return;
        }
        for (int i = 0; i < getAudioMusics().size(); i++) {
            String musicId = getAudioMusics().get(i).getId();
            AppLogUtils.e("PlayService"+"----musicId----"+ musicId);
            if (Long.parseLong(musicId) == id) {
                position = i;
                break;
            }
        }
        mPlayingPosition = position;
        long musicId = Long.parseLong(getAudioMusics().get(mPlayingPosition).getId());
        AppSpUtils.getInstance(MusicConstants.SP_NAME).put(MusicConstants.MUSIC_ID,musicId);
    }


}
