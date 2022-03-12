package com.yc.alive.ui;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import com.yc.alive.service.AliveOnePxService;
import com.yc.alive.util.AliveLogUtils;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;


/**
 * 1 像素 Activity
 */
@RestrictTo(LIBRARY)
public class AliveOnePxActivity extends Activity {

    private static final String TAG = "KAOnePxActivity";

    private ServiceConnection mConn;

    public static void launch(Context context) {
        try {
            Intent intent = new Intent(context, AliveOnePxActivity.class);
            if (context instanceof Application) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Throwable e) {
            AliveLogUtils.d(TAG, "launch fail " + e.getMessage());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AliveLogUtils.d(TAG, "onCreate");

        Window window = getWindow();
        // 放在左上角
        window.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams attributes = window.getAttributes();
        // 宽高设计为1个像素
        attributes.width = 1;
        attributes.height = 1;
        // 起始坐标
        attributes.x = 0;
        attributes.y = 0;
        window.setAttributes(attributes);

        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                AliveLogUtils.d(TAG, "onServiceConnected");

                try {
                    AliveOnePxService.MyBinder binder = (AliveOnePxService.MyBinder) service;
                    binder.getService().bindActivity(AliveOnePxActivity.this);
                    // 可能存在 onCreate 执行在接收 SCREEN_ON 之后
                    if (binder.getService().needFinish()) {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                AliveLogUtils.d(TAG, "onServiceDisconnected");
            }
        };

        bindService(new Intent(this, AliveOnePxService.class), mConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AliveLogUtils.d(TAG, "onDestroy");

        if (mConn != null) {
            unbindService(mConn);
            mConn = null;
        }
    }
}
