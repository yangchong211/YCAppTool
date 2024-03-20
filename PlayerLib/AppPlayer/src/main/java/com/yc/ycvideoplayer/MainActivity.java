package com.yc.ycvideoplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.music.service.PlayAudioService;
import com.yc.music.tool.BaseAppHelper;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.AppLogUtils;
import com.yc.videoview.FloatWindow;
import com.yc.videoview.tool.FloatMoveType;
import com.yc.videoview.tool.FloatScreenType;
import com.yc.videoview.tool.FloatWindowUtils;
import com.yc.ycvideoplayer.audio.AudioActivity;
import com.yc.ycvideoplayer.music.MusicPlayerActivity;
import com.yc.ycvideoplayer.video.activity.TypeActivity;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/18
 * 描    述：Main主页面
 * 修订历史：
 * ================================================
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;
    private TextView mTv5;
    private TextView mTv6;
    private TextView mTv7;
    private PlayServiceConnection mPlayServiceConnection;


    @Override
    protected void onDestroy() {
        if (mPlayServiceConnection != null) {
            unbindService(mPlayServiceConnection);
        }
        super.onDestroy();
        FloatWindow.destroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        initView();
    }

    private void initView() {
        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);
        mTv4 = findViewById(R.id.tv_4);
        mTv5 = findViewById(R.id.tv_5);
        mTv6 = findViewById(R.id.tv_6);
        mTv7 = findViewById(R.id.tv_7);

        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mTv3.setOnClickListener(this);
        mTv4.setOnClickListener(this);
        mTv5.setOnClickListener(this);
        mTv6.setOnClickListener(this);
        mTv7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                break;
            case R.id.tv_2:
                startActivity(TypeActivity.class);
                break;
            case R.id.tv_3:
                break;
            case R.id.tv_4:
                startCheckService();
                startActivity(MusicPlayerActivity.class);
                break;
            case R.id.tv_5:
                break;
            case R.id.tv_6:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!FloatWindowUtils.hasPermission(this)) {
                        requestAlertWindowPermission();
                    } else {
                        windowDialog();
                    }
                }
                break;
            case R.id.tv_7:
                startActivity(AudioActivity.class);
                break;
        }
    }

    @RequiresApi(api = 23)
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 1);
    }

    @RequiresApi(api = 23)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= 23) {
            if (FloatWindowUtils.hasPermission(this)) {

            } else {
                this.finish();
            }
        }
    }


    private void windowDialog() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(R.drawable.ic_play_btn_loved);
        FloatWindow
                .with(getApplicationContext())
                .setView(imageView)
                //.setWidth(WindowScreen.WIDTH, 0.4f)
                //.setHeight(WindowScreen.WIDTH, 0.3f)
                //这个是设置位置
                .setX(FloatScreenType.WIDTH, 0.8f)
                .setY(FloatScreenType.HEIGHT, 0.3f)
                .setMoveType(FloatMoveType.SLIDE)
                .setFilter(false)
                //.setFilter(true, WindowActivity.class, EmptyActivity.class)
                .setMoveStyle(500, new BounceInterpolator())
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FloatWindow.get()!=null){
            FloatWindow.get().hide();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (FloatWindow.get()!=null){
            FloatWindow.get().show();
        }
    }


    /**
     * 再点一次退出程序时间设置
     */
    private static final long WAIT_TIME = 2000L;
    private long touchTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //双击返回桌面
            if (System.currentTimeMillis() - touchTime < WAIT_TIME) {
                //finish();
                //可将activity 退到后台，注意不是finish()退出。
                //判断Activity是否是task根
                //使用moveTaskToBack是为了让app退出时，不闪屏，退出柔和一些
                if (this.isTaskRoot()){
                    //参数为false——代表只有当前activity是task根，指应用启动的第一个activity时，才有效;
                    moveTaskToBack(false);
                } else {
                    //参数为true——则忽略这个限制，任何activity都可以有效。
                    //使用此方法，便不会执行Activity的onDestroy()方法
                    moveTaskToBack(true);
                }
            } else {
                touchTime = System.currentTimeMillis();
                //参考易车，抖音自定义吐司
                ToastUtils.showRoundRectToast("再按一次退出");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void startActivity(Class c){
        startActivity(new Intent(this,c));
    }


    /**
     * 检测服务
     */
    private void startCheckService() {
        if (BaseAppHelper.get().getMusicService() == null) {
            startService();
            mTv1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bindService();
                }
            },1000);
        }
    }

    /**
     * 开启服务
     */
    private void startService() {
        Intent intent = new Intent(this, PlayAudioService.class);
        startService(intent);
    }


    /**
     * 绑定服务
     * 注意对于绑定服务一定要解绑
     */
    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayAudioService.class);
        mPlayServiceConnection = new PlayServiceConnection();
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }


    private static class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AppLogUtils.e("onServiceConnected"+name);
            final PlayAudioService playService = (PlayAudioService) ((PlayAudioService.PlayBinder) service).getService();
            BaseAppHelper.get().setPlayService(playService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AppLogUtils.e("onServiceDisconnected"+name);
        }
    }



}
