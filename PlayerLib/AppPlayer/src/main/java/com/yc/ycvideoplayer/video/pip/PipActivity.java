package com.yc.ycvideoplayer.video.pip;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.yc.ycvideoplayer.ConstantVideo;

import com.yc.ycvideoplayer.R;
import com.yc.video.player.VideoPlayer;
import com.yc.video.player.VideoViewManager;
import com.yc.video.ui.pip.FloatVideoManager;
import com.yc.video.ui.view.BasisVideoController;

public class PipActivity extends AppCompatActivity{

    private FloatVideoManager mPIPManager;
    private FrameLayout mPlayerContainer;
    private Button mBtnFloat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pip_video);
        initFindViewById();
        initVideoPlayer();
        initListener();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPIPManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPIPManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPIPManager.reset();
    }


    @Override
    public void onBackPressed() {
        if (mPIPManager.onBackPress()) return;
        super.onBackPressed();
    }


    private void initFindViewById() {
        mPlayerContainer = findViewById(R.id.player_container);
        mBtnFloat = findViewById(R.id.btn_float);
    }

    private void initVideoPlayer() {
        mPIPManager = FloatVideoManager.getInstance(this);
        VideoPlayer videoView = VideoViewManager.instance().get(FloatVideoManager.PIP);
        BasisVideoController controller = new BasisVideoController(this);
        videoView.setController(controller);
        if (mPIPManager.isStartFloatWindow()) {
            mPIPManager.stopFloatWindow();
            controller.setPlayerState(videoView.getCurrentPlayerState());
            controller.setPlayState(videoView.getCurrentPlayState());
        } else {
            mPIPManager.setActClass(PipActivity.class);
            ImageView thumb = controller.getThumb();
            Glide.with(this)
                    .load(R.drawable.image_default)
                    .placeholder(android.R.color.darker_gray)
                    .into(thumb);
            videoView.setUrl(ConstantVideo.VideoPlayerList[0]);
        }
        mPlayerContainer.addView(videoView);
    }

    private void initListener() {
        mBtnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPIPManager.startFloatWindow();
                mPIPManager.resume();
            }
        });
    }


}
