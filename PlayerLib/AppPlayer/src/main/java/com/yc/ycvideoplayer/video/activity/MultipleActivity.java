package com.yc.ycvideoplayer.video.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.ycvideoplayer.ConstantVideo;

import com.yc.ycvideoplayer.R;
import com.yc.video.player.VideoPlayer;
import com.yc.video.player.VideoPlayerBuilder;
import com.yc.video.ui.view.BasisVideoController;

import java.util.ArrayList;
import java.util.List;


public class MultipleActivity extends AppCompatActivity {

    private static final String VOD_URL_1 = ConstantVideo.VideoPlayerList[3];
    private static final String VOD_URL_2 = ConstantVideo.VideoPlayerList[0];
    private VideoPlayer player1;
    private VideoPlayer player2;
    private List<VideoPlayer> mVideoViews = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_play);

        initFindViewById();
        initVideoPlayer();
    }

    private void initFindViewById() {
        player1 = findViewById(R.id.video_player1);
        player2 = findViewById(R.id.video_player2);
    }

    private void initVideoPlayer() {
        //必须设置
        player1.setUrl(VOD_URL_1);
        VideoPlayerBuilder.Builder builder = VideoPlayerBuilder.newBuilder();
        builder.setEnableAudioFocus(false);
        VideoPlayerBuilder videoPlayerBuilder = new VideoPlayerBuilder(builder);
        player1.setVideoBuilder(videoPlayerBuilder);
        BasisVideoController controller1 = new BasisVideoController(this);
        player1.setController(controller1);
        mVideoViews.add(player1);

        //必须设置
        player2.setUrl(VOD_URL_2);
        VideoPlayerBuilder.Builder builder2 = VideoPlayerBuilder.newBuilder();
        builder.setEnableAudioFocus(false);
        VideoPlayerBuilder videoPlayerBuilder2 = new VideoPlayerBuilder(builder2);
        player2.setVideoBuilder(videoPlayerBuilder2);
        BasisVideoController controller2 = new BasisVideoController(this);
        player2.setController(controller2);
        mVideoViews.add(player2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (VideoPlayer vv : mVideoViews) {
            vv.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (VideoPlayer vv : mVideoViews) {
            vv.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (VideoPlayer vv : mVideoViews) {
            vv.release();
        }
    }

    @Override
    public void onBackPressed() {
        for (VideoPlayer vv : mVideoViews) {
            if (vv.onBackPressed())
                return;
        }
        super.onBackPressed();
    }
}
