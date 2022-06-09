package com.yc.music.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.yc.music.utils.NotificationHelper;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2017/5/6
 *     desc  : 服务，开启前台服务
 *     revise:
 * </pre>
 */
public class AbsAudioService extends Service {

    /**
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
     */
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.get().init(this);
    }

    /**
     * 每次通过startService()方法启动Service时都会被回调。
     * @param intent                intent
     * @param flags                 flags
     * @param startId               startId
     * @return
     * onStartCommand方法返回值作用：
     * START_STICKY：粘性，service进程被异常杀掉，系统重新创建进程与服务，会重新执行onCreate()、onStartCommand(Intent)
     * START_STICKY_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。
     * START_NOT_STICKY：非粘性，Service进程被异常杀掉，系统不会自动重启该Service。
     * START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //结束notification通知
        NotificationHelper.get().onDestroy(true);
    }

    /**
     * 绑定服务时才会调用
     * 必须要实现的方法
     * @param intent        intent
     * @return              IBinder对象
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AbsAudioService.PlayBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public class PlayBinder extends Binder {
        public AbsAudioService getService() {
            //返回自己
            return AbsAudioService.this;
        }
    }



}
