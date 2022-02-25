package com.ycbjie.live.service;

import android.app.Notification;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.ycbjie.live.alive.YcKeepAlive;
import com.ycbjie.live.constant.YcConstant;
import com.ycbjie.live.receiver.NotificationClickReceiver;
import com.ycbjie.live.utils.NotificationUtils;
import com.ycbjie.live.utils.ServiceUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 定时器保活，安卓5.0及以上
 *     revise:
 * </pre>
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public final class JobHandlerService extends JobService {

    private static final int JOB_SERVICE_ID = 11000;

    /**
     * 停止保活服务
     *
     * @param context
     */
    public static void stop(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(
                    Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.cancel(JOB_SERVICE_ID);
        }
        context.stopService(new Intent(context, JobHandlerService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startService(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(JOB_SERVICE_ID,
                    new ComponentName(getPackageName(),
                            JobHandlerService.class.getName())).setPersisted(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //执行的最小延迟时间
                builder.setMinimumLatency(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
                //执行的最长延时时间
                builder.setOverrideDeadline(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
                //线性重试方案
                builder.setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);
            } else {
                builder.setPeriodic(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS);
                builder.setRequiresDeviceIdle(true);
            }
            //需要网络
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            // 当插入充电器，执行该任务
            builder.setRequiresCharging(true);
            jobScheduler.schedule(builder.build());
        }
        return START_STICKY;
    }

    private void startService(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (YcKeepAlive.sForegroundNotification != null) {
                Intent intent2 = new Intent(getApplicationContext(), NotificationClickReceiver.class);
                intent2.setAction(YcConstant.ACTION_CLICK_NOTIFICATION);
                Notification notification = NotificationUtils.createNotification(
                        context, YcKeepAlive.sForegroundNotification.getTitle(),
                        YcKeepAlive.sForegroundNotification.getDescription(),
                        YcKeepAlive.sForegroundNotification.getIconRes(), intent2);
                startForeground(YcConstant.KEY_NOTIFICATION_ID, notification);
            }
        }
        YcKeepAlive.startDoubleProcessService(context);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        if (!ServiceUtils.isServiceRunning(getApplicationContext(), YcConstant.KEY_LOCAL_SERVICE_NAME)
                || !ServiceUtils.isRunningTaskExist(getApplicationContext(), getPackageName() + ":remote")) {
            startService(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (!ServiceUtils.isServiceRunning(getApplicationContext(), YcConstant.KEY_LOCAL_SERVICE_NAME)
                || !ServiceUtils.isRunningTaskExist(getApplicationContext(), getPackageName() + ":remote")) {
            startService(this);
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止双进程保活策略
        YcKeepAlive.stopDoubleProcessService(this);
    }


}
