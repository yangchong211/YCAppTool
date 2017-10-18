package com.ns.yc.lifehelper.ui.other.mobilePlayer.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantKeys;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.service.AudioPlayService;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.MobilePlayerActivity;
import com.ns.yc.lifehelper.utils.localFile.bean.AudioItem;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.view.LyricView;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.EventBusUtils;
import com.pedaily.yc.ycdialoglib.ToastUtil;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：音乐播放器页面
 * 修订历史：
 * ================================================
 */
public class MobileAudioPlayActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_visual_effect)
    ImageView ivVisualEffect;
    @Bind(R.id.tv_artist)
    TextView tvArtist;
    @Bind(R.id.lyric_view)
    LyricView lyricView;
    @Bind(R.id.tv_play_time)
    TextView tvPlayTime;
    @Bind(R.id.sb_audio)
    SeekBar sbAudio;
    @Bind(R.id.btn_play_mode)
    Button btnPlayMode;
    @Bind(R.id.btn_pre)
    Button btnPre;
    @Bind(R.id.btn_play)
    Button btnPlay;
    @Bind(R.id.btn_next)
    Button btnNext;
    private MobileAudioPlayActivity activity;
    private AudioItem items;
    private int currentPosition;
    private int what;
    private AudioPlayService playService;
    /**
     * 更新播放时间的显示
     */
    private static final int UPDATE_PLAY_TIME_SHOW = 0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_PLAY_TIME_SHOW:
                    updatePlayTimeShow();
                    break;
            }
        }
    };
    private int size;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(activity);
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Subscribe
    public void onReceivePlayServic(AudioPlayService playService) {
        this.playService = playService;
        EventBusUtils.post(activity);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_mobile_audio_play;
    }

    @Override
    public void initView() {
        activity = MobileAudioPlayActivity.this;
        EventBusUtils.register(activity);
        initIntentData();
        initToolBar();
        initAnimMusic();
        startService();
    }

    private void initIntentData() {
        if(getIntent()!=null){
            what = getIntent().getIntExtra(ConstantKeys.MOBILE_WHAT, -1);
            items = (AudioItem) getIntent().getSerializableExtra(ConstantKeys.MOBILE_AUDIO_ITEMS);
            currentPosition = getIntent().getIntExtra(ConstantKeys.CURRENT_POSITION, -1);
            size = getIntent().getIntExtra(ConstantKeys.MOBILE_SIZE, -1);
        }
    }


    private void initToolBar() {
        if(items!=null){
            toolbarTitle.setText(items.getName());
        }else {
            toolbarTitle.setText("音乐播放器");
        }
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnPlayMode.setOnClickListener(this);
        sbAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    playService.seekTo(progress);   // 对音频进行跳转
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                onBackPressed();
                break;
            case R.id.btn_next:                     //下一首
                playNext();
                break;
            case R.id.btn_play:                     //音乐播放与暂停
                playToggle();
                break;
            case R.id.btn_pre:                      //上一首
                playPre();
                break;
            case R.id.btn_play_mode:                //切换播放模式
                switchPlayMode();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        int what = getIntent().getIntExtra(ConstantKeys.MOBILE_WHAT, -1);
        if (what == AudioPlayService.TYPE_NOTIFICATION_ROOT) {
            // 如果是点击了通知栏进来的，则手动开始主界面
            Intent intent = new Intent(this, MobilePlayerActivity.class);
            intent.putExtra(ConstantKeys.MOBILE_WHAT, what);
            startActivity(intent);
        }
        if(playService!=null){
            playService.cancelNotification();
        }
        super.onBackPressed();
    }

    /**
     * 初始化音乐动画效果
     */
    private void initAnimMusic() {
        AnimationDrawable rocketAnimation = (AnimationDrawable) ivVisualEffect.getBackground();
        rocketAnimation.start();    // 开启帧动画
    }


    /**
     * 开启音乐服务
     */
    private void startService() {
        Intent intent = new Intent(this, AudioPlayService.class);
        intent.putExtra(ConstantKeys.MOBILE_AUDIO_ITEMS, items);
        intent.putExtra(ConstantKeys.CURRENT_POSITION, currentPosition);
        intent.putExtra(ConstantKeys.MOBILE_WHAT, what);
        intent.putExtra(ConstantKeys.MOBILE_SIZE,size);
        startService(intent);
    }


    /**
     * 更新UI，由Service调用
     */
    public void updateUi(AudioItem item) {
        lyricView.setMusicPath(item.getPath());
        toolbarTitle.setText(item.getName());       // 显示音乐名称
        tvArtist.setText(item.getArtist());         // 显示音乐的歌手
        updatePlayButtonBackground();               // 更新播放按钮的背景
        updatePlayTimeShow();                       // 更新播放时间的显示
        updatePlayModeButtonBackground(playService.getCurrentPlayMode());   // 根据当前播放模式显示对应的图片
    }

    /**
     * 更新播放按钮的背景
     */
    private void updatePlayButtonBackground() {
        if (playService.isPlaying()) {
            // 如果音乐正在播放，显示暂停按钮
            btnPlay.setBackgroundResource(R.drawable.btn_audio_pause);
        } else {
            // 如果音乐暂停了，则显示播放按钮
            btnPlay.setBackgroundResource(R.drawable.btn_audio_play);

        }
    }

    /**
     * 更新播放时间的显示
     */
    private void updatePlayTimeShow() {
        int currentPosition =  playService.getCurrentPosition();
        int duration = playService.getDuration();
        lyricView.setCurrentPosition(currentPosition);
        tvPlayTime.setText(AppUtil.formatMillis(currentPosition) + "/" + AppUtil.formatMillis(duration));
        sbAudio.setMax(duration);
        sbAudio.setProgress(currentPosition);
        handler.sendEmptyMessageDelayed(UPDATE_PLAY_TIME_SHOW, 30); // 每30毫秒更新一下显示的内容
    }


    /**
     * 播放开关
     */
    private void playToggle() {
        if (playService.isPlaying()) {
            // 如果音乐正在播放，则暂停
            playService.pause();
        } else {
            // 如果音乐暂停了，则播放
            playService.start();
        }
        updatePlayButtonBackground();
    }

    /**
     * 下一首
     */
    private void playNext() {
        playService.next();
    }

    /**
     * 上一首
     */
    private void playPre() {
        playService.pre();
    }


    /**
     * 切换播放模式
     */
    private void switchPlayMode() {
        int currentPlayMode = playService.switchPlayMode();
        updatePlayModeButtonBackground(currentPlayMode);
    }

    /**
     * 根据当前播放模式显示对应的图片
     */
    private void updatePlayModeButtonBackground(int currentPlayMode) {
        switch (currentPlayMode) {
            case AudioPlayService.PLAY_MODE_ORDER:
                btnPlayMode.setBackgroundResource(R.drawable.btn_playmode_order);
                ToastUtil.showToast(activity,"顺序播放并且循环");
                break;
            case AudioPlayService.PLAY_MODE_SINGLE:
                btnPlayMode.setBackgroundResource(R.drawable.btn_playmode_single);
                ToastUtil.showToast(activity,"单曲播放");
                break;
            case AudioPlayService.PLAY_MODE_RANDOM:
                btnPlayMode.setBackgroundResource(R.drawable.btn_playmode_random);
                ToastUtil.showToast(activity,"随机播放");
                break;
        }

    }

    /**
     * 当播放服务即将要释放MediaPlayer的时候会调用这个方法
     */
    public void onReleaseMediaPlayer() {
        handler.removeMessages(UPDATE_PLAY_TIME_SHOW);  // MediaPlayer释放的时候，这个循环就先停止
    }


}
