package com.ns.yc.lifehelper.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ns.yc.lifehelper.base.AppManager;
import com.ns.yc.lifehelper.utils.DialogUtils;

/**
 * Created by PC on 2017/9/29.
 * 作者：PC
 */

public class TimerService extends Service {

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
        super.onCreate();
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                //DialogUtils.showWindowToast("全局吐司");
                //IllegalStateException: You need to use a Theme.AppCompat theme (or descendant) with this activity.
                //DialogUtils.showWindowDialog(TimerService.this);
                //DialogUtils.showWindowDialog();
                Activity activity = AppManager.getAppManager().currentActivity();
                //DialogUtils.showActivityDialog(activity);
                DialogUtils.showActivityDialog(activity);
            }
        }.start();
    }


    /**
     * 每次通过startService()方法启动Service时都会被回调。
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand invoke");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务销毁时的回调
     */
    @Override
    public void onDestroy() {
        System.out.println("onDestroy invoke");
        super.onDestroy();
    }

}
