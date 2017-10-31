package com.ns.yc.lifehelper.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.blankj.utilcode.util.SPUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.ConstantKeys;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.activity.MobileAudioPlayActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.fragment.MobileAudioFragment;
import com.ns.yc.lifehelper.utils.EventBusUtils;
import com.ns.yc.lifehelper.utils.localFile.bean.AudioItem;
import com.pedaily.yc.ycdialoglib.toast.ToastUtil;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Random;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：音乐播放器服务
 * 修订历史：
 * ================================================
 */
public class AudioPlayService extends Service {

    /** 点击了通知栏上的上一首按钮*/
    public static final int TYPE_NOTIFICATION_PRE = 0;
    /** 点击了通知栏上的下一首按钮*/
    public static final int TYPE_NOTIFICATION_NEXT = 1;
    /** 点击了通知栏上的根容器*/
    public static final int TYPE_NOTIFICATION_ROOT = 2;
    /** 点击了通知栏上的播放暂停按钮*/
    public static final int TYPE_NOTIFICATION_START = 3;
    /** 播放模式：顺序播放并且循环 */
    public static final int PLAY_MODE_ORDER = 0;
    /** 播放模式：单曲播放 */
    public static final int PLAY_MODE_SINGLE = 1;
    /** 播放模式：随机播放 */
    public static final int PLAY_MODE_RANDOM = 2;
    private int currentPlayMode;

    private AudioItem items;
    private int currentPosition;
    private MediaPlayer mMediaPlayer;
    private MobileAudioPlayActivity ui;
    private int size;
    private Random random;
    private NotificationManager notificationManager;

    /**
     * 绑定服务时才会调用
     * 必须要实现的方法
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        EventBusUtils.register(this);
        random = new Random();
        initNotificationManager();
        super.onCreate();
    }

    private void initNotificationManager() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    /**
     * 每次通过startService()方法启动Service时都会被回调。
     * @param intent
     * @param flags
     * @param startId
     * @return
     * onStartCommand方法返回值作用：
     * START_STICKY：粘性，service进程被异常杀掉，系统重新创建进程与服务，会重新执行onCreate()、onStartCommand(Intent)
     * START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     * START_NOT_STICKY：非粘性，Service进程被异常杀掉，系统不会自动重启该Service。
     * START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand invoke");
        EventBusUtils.post(this);
        // 读取持久化的播放模式
        currentPlayMode = SPUtils.getInstance(Constant.SP_NAME).getInt(ConstantKeys.MOBILE_PLAY_MODE);
        int what = intent.getIntExtra(ConstantKeys.MOBILE_WHAT, -1);
        switch (what) {
            case TYPE_NOTIFICATION_PRE:     // 如果点击了通知的上一首按钮
                pre();
                break;
            case TYPE_NOTIFICATION_NEXT:    // 如果点击了通知的下一首按钮
                next();
                break;
            case TYPE_NOTIFICATION_START:   // 点击了通知栏上的播放暂停按钮
                if(isPlaying()){
                    // 如果音乐正在播放，则暂停
                    pause();
                }else {
                    // 如果音乐暂停了，则播放
                    start();
                }
                break;
            case TYPE_NOTIFICATION_ROOT:    // 如果点击了通知的根容器
                if(items!=null){
                    ui.updateUi(items);
                }
                break;
            default:                        // 从正常播放界面跳转过来的
                // 取出音频数据
                items = (AudioItem) intent.getSerializableExtra(ConstantKeys.MOBILE_AUDIO_ITEMS);
                currentPosition = intent.getIntExtra(ConstantKeys.CURRENT_POSITION, -1);
                size = intent.getIntExtra(ConstantKeys.MOBILE_SIZE, -1);
                startPlayMusic();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
        System.out.println("onDestroy invoke");
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Subscribe
    public void onReceiveUi(MobileAudioPlayActivity ui) {
        this.ui = ui;
    }

    /**
     * 开始播放音乐
     */
    private void startPlayMusic() {
        if (items == null || currentPosition == -1) {
            return;
        }

        items = MobileAudioFragment.musics.get(currentPosition);

        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        sendBroadcast(i);

        ui.onReleaseMediaPlayer();
        release();

        try {
            mMediaPlayer = new MediaPlayer();   // 创建播放器
            mMediaPlayer.setDataSource(items.getPath());         // 把音频路径传给播放器
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);        // 准备播放监听
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);    //
            mMediaPlayer.prepareAsync();                                    // 准备
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    /** 音频准备好的监听器 */
    MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        /** 当音频准备好可以播放了，则这个方法会被调用  */
        @Override
        public void onPrepared(MediaPlayer mp) {
            start();
            if(ui!=null){
                ui.updateUi(items);
            }else {
                ToastUtil.showToast(AudioPlayService.this,"ui不能为空");
            }
        }
    };


    /** 当音频播放结果的时候的监听器 */
    MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        /** 当音频播放结果的时候这个方法会被调用 */
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    };



    /** 开始播放音乐 */
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            // 发送，创建通知栏
            sendNotification();
        }
    }


    /** 暂停播放音乐 */
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }


    /** 下一首 */
    public void next() {
        switch (currentPlayMode) {
            case PLAY_MODE_ORDER:
                if (currentPosition != size - 1) {
                    // 如果不是最后一首，则还有下一首
                    currentPosition++;
                } else {
                    // 如果是最后一首，则切换回第一首
                    currentPosition = 0;
                }
                break;
            case PLAY_MODE_SINGLE:

                break;
            case PLAY_MODE_RANDOM:
                currentPosition = random.nextInt(size);
                break;
        }
        startPlayMusic();
    }


    /** 上一首 */
    public void pre() {
        if(currentPosition != 0){
            // 如果不是第一首，则还有上一首
            currentPosition--;
        } else {
            // 如果没有上一首，则切换到最后一首
            currentPosition = size;
        }
        startPlayMusic();
    }


    /** 切换播放模式 */
    public int switchPlayMode() {
        switch (currentPlayMode) {
            case PLAY_MODE_ORDER:   // 如果当前是顺序播放，则切换为单曲播放
                currentPlayMode = PLAY_MODE_SINGLE;
                break;
            case PLAY_MODE_SINGLE:  // 如果当前是单曲，则切换为随机
                currentPlayMode = PLAY_MODE_RANDOM;
                break;
            case PLAY_MODE_RANDOM:  // 如果当前是随机播放，则切换为顺序播放
                currentPlayMode = PLAY_MODE_ORDER;
                break;
            default:
                throw new IllegalArgumentException("见鬼了，遇到一个未知播放模式：currentPlayMode = " + currentPlayMode);
        }
        // 持久化播放模式
        SPUtils.getInstance(Constant.SP_NAME).put(ConstantKeys.MOBILE_PLAY_MODE,currentPlayMode);
        return currentPlayMode;
    }


    /** 返回当前的播放模式 */
    public int getCurrentPlayMode() {
        return currentPlayMode;
    }


    /** 把音频进行跳转 */
    public void seekTo(int position) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(position);
        }
    }

    /**
     * 判断音乐是否在播放
     * @return
     */
    public boolean isPlaying(){
        if(mMediaPlayer!=null && mMediaPlayer.isPlaying()){
            return true;
        }else {
            return false;
        }
    }


    /** 获取音频当前播放的位置 */
    public int getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }


    /** 获取音频的总时长 */
    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }


    /**
     * 发送通知
     */
    private int notifyId = 1;
    public void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("当前正在播放：" + items.getName())         // 设置通知的提示文本
                .setSmallIcon(R.drawable.ic_notification_music_logo)// 设置通知的提示图标
                .setOngoing(true)                                   // 让通知左右滑的时候不能取消通知
                .setContent(getRemoteViews());                      // 设置通知栏的布局
        Notification notification = builder.build();                // 创建通知
        // 发送通知,notifyId用于区分是不是同一个id的通知，同一个id的通知如果存在了，则不能发第二次了
        notificationManager.notify(notifyId, notification);
    }


    /**
     * 取消通知
     */
    public void cancelNotification() {
        if(notificationManager!=null){
            notificationManager.cancel(notifyId);   // 取消通知
        }
    }


    /** 获取RemoteViews对象，这个对象可以自定义通知栏的布局 */
    public RemoteViews getRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_mobile_play);
        // 设置 点击通知栏的上一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_pre, getServicePendingIntent(TYPE_NOTIFICATION_PRE));
        // 设置 点击通知栏的下一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_next, getServicePendingIntent(TYPE_NOTIFICATION_NEXT));
        // 设置 点击通知栏的播放暂停按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_start, getServicePendingIntent(TYPE_NOTIFICATION_START));
        // 设置 点击通知栏的根容器时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.ll_root, getActivityPendingIntent(TYPE_NOTIFICATION_ROOT));
        remoteViews.setTextViewText(R.id.tv_title, items.getName());     // 设置通知栏上标题
        remoteViews.setTextViewText(R.id.tv_artist, items.getArtist());   // 设置通知栏上艺术家
        return remoteViews;
    }


    /** 获取一个Activity类型的PendingIntent对象 */
    private PendingIntent getActivityPendingIntent(int what) {
        Intent intent = new Intent(this, MobileAudioPlayActivity.class);
        intent.putExtra(ConstantKeys.MOBILE_WHAT, what);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, what, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }


    /** 获取一个Service类型PendingIntent对象 */
    private PendingIntent getServicePendingIntent(int what) {
        Intent intent = new Intent(this, AudioPlayService.class);
        intent.putExtra(ConstantKeys.MOBILE_WHAT, what);
        PendingIntent pendingIntent = PendingIntent.getService(this, what, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }


}
