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
 *     GitHub : https://github.com/yangchong211/YCCommonLib
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
     * 首次创建服务时，系统将调用此方法来执行一次性设置程序（在调用 onStartCommand() 或 onBind() 之前）。
     * 如果服务已在运行，则不会调用此方法。该方法只被调用一次
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
     * 每次通过startService()方法启动Service时都会被回调。服务启动时调用
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

    /**
     * 绑定服务时才会调用
     * 必须要实现的方法
     * @param intent        intent
     * @return              IBinder对象
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
