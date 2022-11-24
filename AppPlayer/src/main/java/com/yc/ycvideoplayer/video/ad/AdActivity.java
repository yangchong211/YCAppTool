package com.yc.ycvideoplayer.video.ad;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yc.toastutils.ToastUtils;
import com.yc.videocache.HttpProxyCacheServer;

import com.yc.videosurface.MeasureHelper;
import com.yc.ycvideoplayer.ConstantVideo;
import com.yc.ycvideoplayer.R;
import com.yc.videocache.cache.ProxyVideoCacheManager;

import com.yc.video.config.ConstantKeys;
import com.yc.video.player.SimpleStateListener;
import com.yc.video.player.VideoPlayer;
import com.yc.video.ui.view.BasisVideoController;

public class AdActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoPlayer mVideoPlayer;
    private Button mBtnScaleNormal;
    private Button mBtnScale169;
    private Button mBtnScale43;
    private Button mBtnCrop;
    private Button mBtnGif;
    private static final String URL_AD = "https://gslb.miaopai.com/stream/IR3oMYDhrON5huCmf7sHCfnU5YKEkgO2.mp4";
    BasisVideoController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_video);
        initFindViewById();
        initVideoPlayer();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoPlayer != null) {
            mVideoPlayer.resume();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoPlayer != null) {
            mVideoPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoPlayer != null) {
            mVideoPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
        if (mVideoPlayer == null || !mVideoPlayer.onBackPressed()) {
            super.onBackPressed();
        }
    }

    private void initFindViewById() {
        mVideoPlayer = findViewById(R.id.video_player);
        mBtnScaleNormal = findViewById(R.id.btn_scale_normal);
        mBtnScale169 = findViewById(R.id.btn_scale_169);
        mBtnScale43 = findViewById(R.id.btn_scale_43);
        mBtnCrop = findViewById(R.id.btn_crop);
        mBtnGif = findViewById(R.id.btn_gif);
    }

    private void initVideoPlayer() {
        controller = new BasisVideoController(this);
        AdControlView adControlView = new AdControlView(this);
        adControlView.setListener(new AdControlView.AdControlListener() {
            @Override
            public void onAdClick() {
                ToastUtils.showRoundRectToast( "广告点击跳转");
            }

            @Override
            public void onSkipAd() {
                playVideo();
            }
        });
        controller.addControlComponent(adControlView);
        //设置视频背景图
        Glide.with(this).load(R.drawable.image_default).into(controller.getThumb());
        //设置控制器
        mVideoPlayer.setController(controller);
        HttpProxyCacheServer cacheServer = ProxyVideoCacheManager.getProxy(this);
        String proxyUrl = cacheServer.getProxyUrl(URL_AD);
        //HttpProxyCacheServer server = new HttpProxyCacheServer(this);
        //String proxyVideoUrl = server.getProxyUrl(URL_AD);


        mVideoPlayer.setUrl(proxyUrl);
        mVideoPlayer.start();
        //监听播放结束
        mVideoPlayer.addOnStateChangeListener(new SimpleStateListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                if (playState == ConstantKeys.CurrentState.STATE_BUFFERING_PLAYING) {
                    playVideo();
                }
            }
        });
    }


    /**
     * 播放正片
     */
    private void playVideo() {
        mVideoPlayer.release();
        controller.removeAllControlComponent();
        controller.addDefaultControlComponent("正片");
        //重新设置数据
        mVideoPlayer.setUrl(ConstantVideo.VideoPlayerList[0]);
        //开始播放
        mVideoPlayer.start();
    }

    private void initListener() {
        mBtnScaleNormal.setOnClickListener(this);
        mBtnScale169.setOnClickListener(this);
        mBtnScale43.setOnClickListener(this);
        mBtnCrop.setOnClickListener(this);
        mBtnGif.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == mBtnScale169){
            mVideoPlayer.setScreenScaleType(MeasureHelper.PlayerScreenScaleType.SCREEN_SCALE_16_9);
        } else if (v == mBtnScaleNormal){
            mVideoPlayer.setScreenScaleType(MeasureHelper.PlayerScreenScaleType.SCREEN_SCALE_DEFAULT);
        }else if (v == mBtnScale43){
            mVideoPlayer.setScreenScaleType(MeasureHelper.PlayerScreenScaleType.SCREEN_SCALE_4_3);
        } else if (v == mBtnCrop){

        } else if (v == mBtnGif){

        }
    }
}
