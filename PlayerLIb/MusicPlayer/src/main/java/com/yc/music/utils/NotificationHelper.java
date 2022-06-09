package com.yc.music.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.yc.music.R;
import com.yc.music.model.AudioBean;
import com.yc.music.receiver.NotificationStatusBarReceiver;
import com.yc.music.service.AbsAudioService;
import com.yc.music.service.PlayAudioService;
import com.yc.music.tool.BaseAppHelper;
import com.yc.music.tool.CoverLoader;
import com.ycbjie.notificationlib.NotificationParams;
import com.ycbjie.notificationlib.NotificationUtils;


public class NotificationHelper {


    private AbsAudioService playService;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0x111;

    public static NotificationHelper get() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final NotificationHelper instance = new NotificationHelper();
    }

    private NotificationHelper() {

    }

    /**
     * 1.创建一个NotificationManager的引用
     * @param playService           PlayService对象
     */
    public void init(AbsAudioService playService) {
        this.playService = playService;
        notificationManager = (NotificationManager)
                playService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 开始播放
     * 把该service创建为前台service
     * @param music             music
     */
    public void showPlay(AudioBean music) {
        if (music == null) {
            return;
        }
        //这个方法是启动Notification到前台
        playService.startForeground(NOTIFICATION_ID,
                buildNotification(playService, music, true));
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
        notificationManager.notify(NOTIFICATION_ID,
                buildNotification(playService, music, false));
    }

    /**
     * 销毁的时候调用
     * @param isRemoveNotification                  是否移除通知栏
     */
    public void onDestroy(boolean isRemoveNotification){
        //如果设置成ture，则会移除通知栏
        playService.stopForeground(isRemoveNotification);
        cancelAll();

    }

    /**
     * 结束所有的
     */
    public void cancelAll() {
        if (notificationManager!=null){
            notificationManager.cancelAll();
            notificationManager = null;
        }
    }

    private Notification buildNotification(Context context, AudioBean music, boolean isPlaying) {
        Class<? extends Activity> musicActivity = BaseAppHelper.get().getMusicActivity();
        NotificationUtils notificationUtils = new NotificationUtils(context);
        NotificationParams notificationParams = new NotificationParams();
        if (musicActivity!=null){
            Intent intent = new Intent(context, musicActivity);
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationParams.setContentIntent(pendingIntent);
        }
        notificationParams
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setTicker("叮咚音乐")
                .setContent(getCustomViews(context, music, isPlaying))
                .setOngoing(true);

        notificationUtils.setNotificationParams(notificationParams);
        Notification notification = notificationUtils.getNotification(
                music.getTitle(),
                music.getArtist(),
                R.drawable.default_cover);
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
        String subtitle = "";
        Bitmap cover = null;

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_player);
        if (cover != null) {
            remoteViews.setImageViewBitmap(R.id.iv_image, cover);
        } else {
            remoteViews.setImageViewResource(R.id.iv_image, R.drawable.default_cover);
        }
        remoteViews.setTextViewText(R.id.tv_title, title);
        remoteViews.setTextViewText(R.id.tv_artist, subtitle);
        if(isPlaying){
            remoteViews.setImageViewResource(R.id.btn_start,R.drawable.notify_btn_dark_pause_normal);
        }else {
            remoteViews.setImageViewResource(R.id.btn_start,R.drawable.notify_btn_dark_play_normal);
        }

        // 设置 点击通知栏的上一首按钮时要执行的意图
//        remoteViews.setOnClickPendingIntent(R.id.btn_pre, getReceiverPendingIntent(context, MusicPlayAction.TYPE_PRE,1));
//        // 设置 点击通知栏的下一首按钮时要执行的意图
//        remoteViews.setOnClickPendingIntent(R.id.btn_next, getReceiverPendingIntent(context, MusicPlayAction.TYPE_NEXT,2));
//        // 设置 点击通知栏的播放暂停按钮时要执行的意图
//        remoteViews.setOnClickPendingIntent(R.id.btn_start, getReceiverPendingIntent(context, MusicPlayAction.TYPE_START_PAUSE,3));
//        // 设置 点击通知栏的根容器时要执行的意图
//        remoteViews.setOnClickPendingIntent(R.id.ll_root, getActivityPendingIntent(context));
        return remoteViews;
    }


    private PendingIntent getActivityPendingIntent(Context context) {
        Intent intent = new Intent(context, null);
//        Intent intent = new Intent(context, MusicActivity.class);
//        intent.putExtra(Constant.EXTRA_NOTIFICATION, true);
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private PendingIntent getReceiverPendingIntent(Context context, String type , int code) {
        Intent intent = new Intent(NotificationStatusBarReceiver.ACTION_STATUS_BAR);
        intent.putExtra(NotificationStatusBarReceiver.EXTRA, type);
        return PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}
