package com.ycbjie.todo.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.db.realm.RealmWorkDoHelper;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;
import com.ycbjie.todo.R;
import com.ycbjie.todo.ui.WorkDoActivity;

import java.util.Calendar;

import io.realm.RealmResults;

public class WorkDoNotifyService  extends Service {

    public WorkDoNotifyService() {}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int code = super.onStartCommand(intent, flags, startId);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        RealmResults<CacheTaskDetailEntity> results = RealmWorkDoHelper.getInstance().findUnFinishedTasks(day);
        if (results.size() == 0) {
            return code;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("包含: ");
        for (int i = 0; i < results.size() - 1; i++) {
            sb.append(results.get(i).getTitle());
            sb.append(", ");
        }
        sb.append(results.get(results.size() - 1).getTitle());
        String content = sb.toString();


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, WorkDoActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action(R.mipmap.ic_launcher, "查看", pendingIntent);
        Notification notification = new NotificationCompat
                .Builder(this)
                .setAutoCancel(true)
                .addAction(action)
                .setContentTitle("您今日还有未完成的任务")
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        manager.notify(Constant.AUTO_NOTIFY_NOTIFICATION_ID, notification);
        return code;
    }

}
