package com.ns.yc.lifehelper.service;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by PC on 2017/9/30.
 * 作者：PC
 */

public class WindowDialogService extends Service {

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


    private void showDialog(int isCancle){
        Dialog dialog;
       /* if(!isFirst){
            dialog = new Dialog(this, R.style.AppTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_custom_window, null);
            dialog.setContentView(view);
            tv = (TextView) view.findViewById(R.id.mylaodint_text_id);
            ImageView progressImageView = (ImageView) view.findViewById(R.id.myloading_image_id);
            AnimationDrawable animationDrawable = (AnimationDrawable) progressImageView.getDrawable();
            animationDrawable.start();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = ConstantsYiBaiSong.WinWidth/ 3;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        if(iscancle==1){
            dialog.setCanceledOnTouchOutside(true);
        }else{
            dialog.setCanceledOnTouchOutside(false);
        }
        if(!dialog.isShowing()){
            dialog.show();
        }*/
    }


}
