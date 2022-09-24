package com.yc.window.permission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/2/10
 *     desc  : 用于在内部自动申请权限
 *     revise:
 * </pre>
 */
public class PermissionActivity extends AppCompatActivity {

    private static List<PermissionListener> mPermissionListenerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FloatWindowUtils.goToSetting(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //用23以上编译即可出现canDrawOverlays
            if (FloatWindowUtils.hasPermission(this)) {
                mPermissionListener.onSuccess();
            } else {
                mPermissionListener.onFail();
            }
        }
        mPermissionListenerList = null;
        finish();
    }

    public static synchronized void request(Context context, PermissionListener permissionListener) {
        if (mPermissionListenerList == null) {
            mPermissionListenerList = new ArrayList<>();
        }
        mPermissionListenerList.add(permissionListener);
        mPermissionListener = new PermissionListener() {
            @Override
            public void onSuccess() {
                for (PermissionListener listener : mPermissionListenerList) {
                    listener.onSuccess();
                }
            }

            @Override
            public void onFail() {
                for (PermissionListener listener : mPermissionListenerList) {
                    listener.onFail();
                }
            }
        };
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private static PermissionListener mPermissionListener;

    public interface PermissionListener {
        /**
         * 成功
         */
        void onSuccess();
        /**
         * 失败
         */
        void onFail();
    }
}
