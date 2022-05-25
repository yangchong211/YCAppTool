package com.yc.baseclasslib.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 父类Service，没有界面的后台服务
 *     revise:
 * </pre>
 */
public abstract class BaseService extends Service {

    /**
     * Service和Activity是运行在当前app所在进程的mainThread(UI主线程)里面，
     *
     * 如果选择是开启服务还是绑定服务？
     * 如果只是想开个服务在后台运行的话，直接startService即可；
     * 如果需要相互之间进行传值或者操作的话，就应该通过bindService。
     *
     *
     * 调用context.startService() ->onCreate()- >onStartCommand()->Service running-->
     * 调用context.stopService() ->onDestroy()
     *
     * 调用context.bindService()->onCreate()->onBind()->Service running-->
     * 调用>onUnbind() -> onDestroy()
     */


    /**
     * 服务第一次被创建时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 服务停止时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 服务启动时调用
     * @param intent    intent
     * @param flags     flags
     * @param startId   startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务被绑定时调用
     * @param intent    intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 服务被解绑时调用
     * @param intent    intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 重新绑定时调用
     * @param intent    intent
     */
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

}
