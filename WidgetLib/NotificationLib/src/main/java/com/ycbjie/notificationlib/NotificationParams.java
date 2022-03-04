package com.ycbjie.notificationlib;

import android.app.PendingIntent;
import android.net.Uri;
import android.widget.RemoteViews;

import static android.app.Notification.PRIORITY_DEFAULT;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://www.jianshu.com/p/514eb6193a06
 *     time  : 2018/2/10
 *     desc  : 通知栏参数设置
 *     revise:
 * </pre>
 */
public final class NotificationParams {

    public boolean ongoing = false;
    public RemoteViews remoteViews = null;
    public PendingIntent intent = null;
    public String ticker = "";
    public int priority = PRIORITY_DEFAULT;
    public boolean onlyAlertOnce = false;
    public long when = 0;
    public Uri sound = null;
    public int defaults = 0;
    public long[] pattern = null;
    public int[] flags;

    public NotificationParams(){
        this.ongoing = false;
        this.remoteViews = null;
        this.intent = null;
        this.ticker = "";
        this.priority = PRIORITY_DEFAULT;
        this.when = 0;
        this.sound = null;
    }

    /**
     * 让通知左右滑的时候是否可以取消通知
     * @param ongoing                   是否可以取消通知
     * @return
     */
    public NotificationParams setOngoing(boolean ongoing){
        this.ongoing = ongoing;
        return this;
    }

    /**
     * 设置自定义view通知栏布局
     * @param remoteViews               view
     * @return
     */
    public NotificationParams setContent(RemoteViews remoteViews){
        this.remoteViews = remoteViews;
        return this;
    }

    /**
     * 设置内容点击
     * @param intent                    intent
     * @return
     */
    public NotificationParams setContentIntent(PendingIntent intent){
        this.intent = intent;
        return this;
    }

    /**
     * 设置状态栏的标题
     * @param ticker                    状态栏的标题
     * @return
     */
    public NotificationParams setTicker(String ticker){
        this.ticker = ticker;
        return this;
    }


    /**
     * 设置优先级
     * 注意：
     * Android 8.0以及上，在 NotificationChannel 的构造函数中指定，总共要五个级别；
     * Android 7.1（API 25）及以下的设备，还得调用NotificationCompat 的 setPriority方法来设置
     *
     * @param priority                  优先级，默认是Notification.PRIORITY_DEFAULT
     * @return
     */
    public NotificationParams setPriority(int priority){
        this.priority = priority;
        return this;
    }

    /**
     * 是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
     * @param onlyAlertOnce             是否只提示一次，默认是false
     * @return
     */
    public NotificationParams setOnlyAlertOnce(boolean onlyAlertOnce){
        this.onlyAlertOnce = onlyAlertOnce;
        return this;
    }

    /**
     * 设置通知时间，默认为系统发出通知的时间，通常不用设置
     * @param when                      when
     * @return
     */
    public NotificationParams setWhen(long when){
        this.when = when;
        return this;
    }

    /**
     * 设置sound
     * @param sound                     sound
     * @return
     */
    public NotificationParams setSound(Uri sound){
        this.sound = sound;
        return this;
    }


    /**
     * 设置默认的提示音
     * @param defaults                  defaults
     * @return
     */
    public NotificationParams setDefaults(int defaults){
        this.defaults = defaults;
        return this;
    }

    /**
     * 自定义震动效果
     * @param pattern                  pattern
     * @return
     */
    public NotificationParams setVibrate(long[] pattern){
        this.pattern = pattern;
        return this;
    }

    /**
     * 设置flag标签
     * @param flags                     flags
     * @return
     */
    public NotificationParams setFlags(int... flags){
        this.flags = flags;
        return this;
    }

}
