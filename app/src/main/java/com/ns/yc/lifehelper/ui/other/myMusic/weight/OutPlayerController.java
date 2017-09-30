package com.ns.yc.lifehelper.ui.other.myMusic.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ns.yc.lifehelper.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/25
 * 描    述：我的音乐底部播放栏
 * 修订历史：
 * ================================================
 */
public class OutPlayerController extends FrameLayout {


    @Bind(R.id.thumb)
    ImageView thumb;
    @Bind(R.id.next)
    ImageView next;
    @Bind(R.id.play)
    ImageView play;
    @Bind(R.id.play_list)
    ImageView playList;
    @Bind(R.id.song_name)
    TextView songName;
    @Bind(R.id.singer)
    TextView singer;
    @Bind(R.id.play_progress)
    ProgressBar playProgress;
    @Bind(R.id.controller)
    LinearLayout controller;

    private OutPlayerControllerListener listener;
    private Context mContext;
    private boolean isPlaying;

    public OutPlayerController(Context context) {
        this(context, null);
    }

    public OutPlayerController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutPlayerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    void init() {
        ButterKnife.bind(LayoutInflater.from(mContext).inflate(R.layout.music_out_player_controller, this));
    }

    @OnClick({R.id.next, R.id.play, R.id.play_list, R.id.controller})
    public void onClick(View view) {
        if (listener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.next:
                setPlaying(true);
                listener.next();
                break;
            case R.id.play:
                Glide.with(mContext)
                        .load(isPlaying ? R.drawable.ic_music_pause : R.drawable.ic_music_play)
                        .placeholder(R.drawable.ic_music_pic_loading).into(play);
                listener.play();
                break;
            case R.id.play_list:
                listener.playList();
                break;
            case R.id.controller:
                listener.controller();
                break;
        }
    }

    public interface OutPlayerControllerListener {
        void play();

        void next();

        void playList();

        void controller();
    }

    public void setPlayerListener(OutPlayerControllerListener listener) {
        this.listener = listener;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        Glide.with(mContext)
                .load(isPlaying ? R.drawable.ic_music_pause : R.drawable.ic_music_play)
                .placeholder(R.drawable.ic_music_pic_loading).into(play);
    }

    public void setThumb(byte[] data) {
        Glide.with(mContext)
                .load(data).placeholder(R.drawable.ic_music_pic_loading)
                .error(R.drawable.ic_music_pic_error)
                .into(thumb);
    }

    public void setSongName(String s) {
        songName.setText(s);
    }

    public void setSinger(String s) {
        singer.setText(s);
    }

    public void setPlayProgress(int progress) {
        playProgress.setProgress(progress);
    }

    public int getPlayProgress() {
        return playProgress.getProgress();
    }

    public int getProgressMax() {
        return playProgress.getMax();
    }

}
