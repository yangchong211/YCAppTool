package com.yc.ycvideoplayer.video.tiny;

import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.videosurface.MeasureHelper;
import com.yc.ycvideoplayer.BaseActivity;
import com.yc.ycvideoplayer.ConstantVideo;

import com.yc.ycvideoplayer.R;
import com.yc.video.config.ConstantKeys;
import com.yc.video.player.VideoPlayer;
import com.yc.video.ui.view.BasisVideoController;



/**
 * @author yc
 */
public class TestFullActivity extends BaseActivity implements View.OnClickListener {

    private VideoPlayer mVideoPlayer;
    private Button mBtnTiny1;
    private Button mBtnTiny2;

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

    @Override
    public int getContentView() {
        return R.layout.activity_full_video1;
    }

    @Override
    public void initView() {
        StateAppBar.translucentStatusBar(this, true);
        adaptCutoutAboveAndroidP();
        mVideoPlayer = findViewById(R.id.video_player);
        mBtnTiny1 = (Button) findViewById(R.id.btn_tiny_1);
        mBtnTiny2 = (Button) findViewById(R.id.btn_tiny_2);

        BasisVideoController controller = new BasisVideoController(this);
        //设置视频背景图
        Glide.with(this).load(R.drawable.image_default).into(controller.getThumb());
        //设置控制器
        mVideoPlayer.setController(controller);
        mVideoPlayer.setUrl(ConstantVideo.VideoPlayerList[0]);
        mVideoPlayer.setScreenScaleType(MeasureHelper.PlayerScreenScaleType.SCREEN_SCALE_16_9);
        mVideoPlayer.start();
    }

    @Override
    public void initListener() {
        mBtnTiny1.setOnClickListener(this);
        mBtnTiny2.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tiny_1:
                mVideoPlayer.startFullScreen();
                break;
            case R.id.btn_tiny_2:
                mVideoPlayer.startTinyScreen();
                break;
            default:
                break;
        }
    }

    private void adaptCutoutAboveAndroidP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }


}
