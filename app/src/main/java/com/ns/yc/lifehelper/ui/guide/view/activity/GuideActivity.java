package com.ns.yc.lifehelper.ui.guide.view.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.lifehelper.R;
import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.ui.guide.contract.GuideContract;
import com.ns.yc.lifehelper.ui.guide.presenter.GuidePresenter;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ycbjie.library.utils.image.ImageUtils;
import com.ns.yc.yccountdownviewlib.CountDownView;
import com.squareup.picasso.Callback;
import com.ycbjie.music.base.BaseAppHelper;
import com.ycbjie.music.service.PlayService;

import java.util.concurrent.TimeUnit;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import cn.ycbjie.ycthreadpoollib.PoolThread;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 倒计时广告页面
 *     revise:
 * </pre>
 */
public class GuideActivity extends BaseActivity<GuidePresenter> implements
        GuideContract.View ,View.OnClickListener {

    private ImageView ivSplashAd;
    private CountDownView cdvTime;
    private PlayServiceConnection mPlayServiceConnection;

    private GuideContract.Presenter presenter = new GuidePresenter(this);

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
        return R.layout.activity_guide;
    }



    @Override
    public void initView() {
        StateAppBar.translucentStatusBar(this, true);
        initFindViewById();
        presenter.startGuideImage();
        initCountDownView();
        //音频播放器需要让服务长期存在
        startCheckService();
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
        presenter.cacheFindNewsData();
        presenter.cacheFindBottomNewsData();
        presenter.cacheHomePileData();
    }


    private void initCountDownView() {
        cdvTime.setTime(5);
        cdvTime.start();
        cdvTime.setOnLoadingFinishListener(this::toMainActivity);
    }

    private void toMainActivity() {
        ActivityUtils.startActivity(MainActivity.class,
                R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        //finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cdv_time:
                cdvTime.stop();
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 展示图片
     */
    @Override
    public void showGuideLogo(String logo) {
        Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                //加载成功
            }

            @Override
            public void onError() {
                //加载失败
                ivSplashAd.setBackgroundResource(R.drawable.bg_cloud_night);
            }
        };
        ImageUtils.loadImgByPicasso(this,logo,R.drawable.bg_cloud_night,ivSplashAd,callback);
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
