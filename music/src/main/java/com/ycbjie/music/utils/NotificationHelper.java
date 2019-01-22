package com.ycbjie.music.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.ycbjie.library.constant.Constant;
import com.ycbjie.music.R;
import com.ycbjie.music.model.action.MusicPlayAction;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.receiver.NotificationStatusBarReceiver;
import com.ycbjie.music.service.PlayService;
import com.ycbjie.music.ui.activity.MusicActivity;
import com.ycbjie.notificationlib.NotificationUtils;


public class NotificationHelper {


    private PlayService playService;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0x111;

    public static NotificationHelper get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static NotificationHelper instance = new NotificationHelper();
    }

    private NotificationHelper() {

    }

    /**
     * 1.创建一个NotificationManager的引用
     * @param playService           PlayService对象
     */
    public void init(PlayService playService) {
        this.playService = playService;
        notificationManager = (NotificationManager) playService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 开始播放
     * @param music             music
     */
    public void showPlay(AudioBean music) {
        if (music == null) {
            return;
        }
        playService.startForeground(NOTIFICATION_ID, buildNotification(playService, music, true));
        //这个方法是启动Notification到前台
    }


    /**
     * 暂停
     * @param music             music
     */
    public void showPause(AudioBean music) {
        //这个方法是停止Notification
        if (music == null) {
            return;
        }
        playService.stopForeground(false);
        notificationManager.notify(NOTIFICATION_ID, buildNotification(playService, music, false));

    }


    /**
     * 结束所有的
     */
    public void cancelAll() {
        notificationManager.cancelAll();
    }

    private Notification buildNotification(Context context, AudioBean music, boolean isPlaying) {
        Intent intent = new Intent(context, MusicActivity.class);
        intent.putExtra(Constant.EXTRA_NOTIFICATION, true);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationUtils.isVibration = false;
        NotificationUtils notificationUtils = new NotificationUtils(context);
        notificationUtils
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setTicker("叮咚音乐")
                .setContent(getCustomViews(context, music, isPlaying))
                .setOngoing(true);
        Notification notification = notificationUtils.getNotification(music.getTitle(), music.getArtist(), R.mipmap.ic_launcher);
        return notification;
    }


    /**
     * 设置自定义通知栏布局
     * @param context                   上下文
     * @param music
     * @return                          RemoteViews
     */
    private RemoteViews getCustomViews(Context context, AudioBean music, boolean isPlaying) {
        String title = music.getTitle();
        String subtitle = FileMusicUtils.getArtistAndAlbum(music.getArtist(), music.getAlbum());
        Bitmap cover = CoverLoader.getInstance().loadThumbnail(music);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_player);
        if (cover != null) {
            remoteViews.setImageViewBitmap(R.id.iv_image, cover);
        } else {
            remoteViews.setImageViewResource(R.id.iv_image, R.mipmap.ic_launcher);
        }
        remoteViews.setTextViewText(R.id.tv_title, title);
        remoteViews.setTextViewText(R.id.tv_artist, subtitle);
        if(isPlaying){
            remoteViews.setImageViewResource(R.id.btn_start,R.drawable.notify_btn_dark_pause_normal);
        }else {
            remoteViews.setImageViewResource(R.id.btn_start,R.drawable.notify_btn_dark_play_normal);
        }

        // 设置 点击通知栏的上一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_pre, getReceiverPendingIntent(context, MusicPlayAction.TYPE_PRE,1));
        // 设置 点击通知栏的下一首按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_next, getReceiverPendingIntent(context, MusicPlayAction.TYPE_NEXT,2));
        // 设置 点击通知栏的播放暂停按钮时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.btn_start, getReceiverPendingIntent(context, MusicPlayAction.TYPE_START_PAUSE,3));
        // 设置 点击通知栏的根容器时要执行的意图
        remoteViews.setOnClickPendingIntent(R.id.ll_root, getActivityPendingIntent(context));
        return remoteViews;
    }


    private PendingIntent getActivityPendingIntent(Context context) {
        Intent intent = new Intent(context, MusicActivity.class);
        intent.putExtra(Constant.EXTRA_NOTIFICATION, true);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent getReceiverPendingIntent(Context context, String type , int code) {
        Intent intent = new Intent(NotificationStatusBarReceiver.ACTION_STATUS_BAR);
        intent.putExtra(NotificationStatusBarReceiver.EXTRA, type);
        return PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
