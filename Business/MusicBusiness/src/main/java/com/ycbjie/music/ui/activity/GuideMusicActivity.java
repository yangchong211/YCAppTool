package com.ycbjie.music.ui.activity;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.yccountdownviewlib.CountDownView;
import com.yc.configlayer.arounter.RouterConfig;
import com.yc.imageserver.utils.GlideImageUtils;
import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.music.R;
import com.ycbjie.music.base.BaseAppHelper;
import com.ycbjie.music.service.PlayService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import cn.ycbjie.ycthreadpoollib.PoolThread;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 倒计时广告页面
 *     revise:
 * </pre>
 */
@Route(path = RouterConfig.Music.ACTIVITY_MUSIC_GUIDE_ACTIVITY)
public class GuideMusicActivity extends BaseActivity implements View.OnClickListener ,
        EasyPermissions.PermissionCallbacks{

    private ImageView ivSplashAd;
    private CountDownView cdvTime;
    private PlayServiceConnection mPlayServiceConnection;

    @Override
    protected void onDestroy() {
        if (mPlayServiceConnection != null) {
            unbindService(mPlayServiceConnection);
        }
        super.onDestroy();
        if(cdvTime!=null && cdvTime.isShown()){
            cdvTime.stop();
        }
    }

    /**
     * 屏蔽返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int getContentView() {
        return R.layout.base_activity_guide;
    }



    @Override
    public void initView() {
        StateAppBar.translucentStatusBar(this, true);
        initFindViewById();
        initPermissions();
        GlideImageUtils.loadImageNet(this,null,
                R.drawable.play_page_default_bg,ivSplashAd);
    }

    private void initFindViewById() {
        ivSplashAd = findViewById(R.id.iv_splash_ad);
        cdvTime = findViewById(R.id.cdv_time);
    }


    @Override
    public void initListener() {
        cdvTime.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }


    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private static final String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    private void startPermissionsTask() {
        //检查是否获取该权限
        if (hasPermissions()) {
            //具备权限 直接进行操作
            initCountDownView();
            //音频播放器需要让服务长期存在
            startCheckService();
        } else {
            //权限拒绝 申请权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this,
                    this.getResources().getString(R.string.easy_permissions),
                    RC_LOCATION_CONTACTS_PERM, LOCATION_AND_CONTACTS);
        }
    }


    /**
     * 判断是否添加了权限
     * @return true
     */
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_CONTACTS);
    }


    /**
     * 初始化权限
     */
    private void initPermissions() {
        startPermissionsTask();
    }



    /**
     * 将结果转发到EasyPermissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode,
                permissions, grantResults, GuideMusicActivity.this);
    }

    /**
     * 某些权限已被授予
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //某些权限已被授予
        LogUtils.d("权限", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    /**
     * 某些权限已被拒绝
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //某些权限已被拒绝
        LogUtils.d("权限", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(GuideMusicActivity.this, perms)) {
            AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(GuideMusicActivity.this);
            builder.setTitle("允许权限")
                    .setRationale("没有该权限，此应用程序部分功能可能无法正常工作。打开应用设置界面以修改应用权限")
                    .setPositiveButton("去设置")
                    .setNegativeButton("取消")
                    .setRequestCode(124)
                    .build()
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            // 当用户从应用设置界面返回的时候，可以做一些事情，比如弹出一个土司。
            LogUtils.d("权限", "onPermissionsDenied:" + requestCode + ":");
            initCountDownView();
            //音频播放器需要让服务长期存在
            startCheckService();
        }
    }



    private void initCountDownView() {
        cdvTime.setTime(2);
        cdvTime.start();
        cdvTime.setOnLoadingFinishListener(this::toMainActivity);
    }

    private void toMainActivity() {
        ActivityUtils.startActivity(MusicActivity.class,
                R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        finish();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.cdv_time) {
            cdvTime.stop();
        }
    }

    /**
     * 检测服务
     */
    private void startCheckService() {
        if (BaseAppHelper.get().getPlayService() == null) {
            startService();
            PoolThread executor = AppConfig.INSTANCE.getExecutor();
            executor.setName("startCheckService");
            executor.setDelay(1, TimeUnit.SECONDS);
            //绑定服务
            executor.execute(this::bindService);
        }
    }

    /**
     * 开启服务
     */
    private void startService() {
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
    }


    /**
     * 绑定服务
     * 注意对于绑定服务一定要解绑
     */
    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        mPlayServiceConnection = new PlayServiceConnection();
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }


    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.e("onServiceConnected"+name);
            final PlayService playService = ((PlayService.PlayBinder) service).getService();
            BaseAppHelper.get().setPlayService(playService);
            playService.updateMusicList(null);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.e("onServiceDisconnected"+name);
        }
    }


}
