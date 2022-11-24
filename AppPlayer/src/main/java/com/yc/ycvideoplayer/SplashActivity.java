package com.yc.ycvideoplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StateAppBar.translucentStatusBar(this, true);
        initPermissions();
    }


    /**
     * 这个地方一定先要初始化权限设置，因为对于音频播放，会扫描本地音乐，涉及权限。如果不允许，可能会导致崩溃。
     * 但是这样体验可能不太好，如果不允许权限，则直接退出。
     * 后来发现华为音乐，混沌大学，QQ音乐等，没有打开权限，都是直接退出的。
     */
    private void initPermissions() {
        locationPermissionsTask();
    }

    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private static final String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationPermissionsTask() {
        //检查是否获取该权限
        if (hasPermissions()) {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        } else {
            //权限拒绝 申请权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this,
                    getString(R.string.easy_permissions), RC_LOCATION_CONTACTS_PERM, LOCATION_AND_CONTACTS);
        }
    }


    /**
     * 判断是否添加了权限
     *
     * @return true
     */
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_CONTACTS);
    }


    /**
     * 将结果转发到EasyPermissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,
                grantResults, this);
    }


    /**
     * 某些权限已被授予
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //某些权限已被授予
        Log.d("权限", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }


    /**
     * 某些权限已被拒绝
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //某些权限已被拒绝
        Log.d("权限", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(SplashActivity.this, perms)) {
            Log.d("权限", "somePermissionPermanentlyDenied:" + perms.get(0));
            AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(SplashActivity.this);
            builder.setTitle("允许权限")
                    .setRationale("没有该权限，此应用程序部分功能可能无法正常工作。打开应用设置界面以修改应用权限")
                    .setPositiveButton("去设置")
                    .setNegativeButton("取消")
                    .setRequestCode(RC_LOCATION_CONTACTS_PERM)
                    .build()
                    .show();
        }else {
            ToastUtils.showRoundRectToast("没有存储空间权限，无法扫描本地歌曲！");
            finish();
        }
    }


}
