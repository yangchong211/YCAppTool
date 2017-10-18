package com.ns.yc.lifehelper.ui.other.mobilePlayer.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantKeys;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.bean.VideoItem;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.pedaily.yc.ycdialoglib.ToastUtil;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/11
 * 描    述：视频播放页面页面
 * 修订历史：
 * ================================================
 */
public class MobileVideoPlayActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.video_view)
    VideoView videoView;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_battery)
    ImageView ivBattery;
    @Bind(R.id.tv_system_time)
    TextView tvSystemTime;
    @Bind(R.id.btn_voice)
    Button btnVoice;
    @Bind(R.id.sb_voice)
    SeekBar sbVoice;
    @Bind(R.id.ll_top_ctrl)
    LinearLayout llTopCtrl;
    @Bind(R.id.tv_current_position)
    TextView tvCurrentPosition;
    @Bind(R.id.sb_video)
    SeekBar sbVideo;
    @Bind(R.id.tv_duration)
    TextView tvDuration;
    @Bind(R.id.btn_exit)
    Button btnExit;
    @Bind(R.id.btn_pre)
    Button btnPre;
    @Bind(R.id.btn_pause)
    Button btnPause;
    @Bind(R.id.btn_next)
    Button btnNext;
    @Bind(R.id.btn_fullscreen)
    Button btnFullscreen;
    @Bind(R.id.ll_bottom_ctrl)
    LinearLayout llBottomCtrl;
    @Bind(R.id.brightness_view)
    View brightnessView;
    @Bind(R.id.ll_loading)
    LinearLayout llLoading;

    /**
     * 更新系统时间的显示
     */
    private static final int UPDATE_SYSTEM_TIME_SHOW = 0;
    /**
     * 更新播放进度的显示
     */
    private static final int UPDATE_PLAY_PROGRESS_SHOW = 1;
    /**
     * 隐藏控制面板
     */
    private static final int HIDE_CTRL_LAYOUT = 2;
    /**
     * 手势识别类，可以识别出触摸是单击还是双击或者长按、滚动等等
     */
    private GestureDetector gestureDetector;
    /**
     * 以中间为分割线，记录手机触摸滑动是在左边还是右边
     */
    private boolean atLeftDown;
    /**
     * 按下的时候获取屏幕的亮度
     */
    private float currentBrightness;

    private ArrayList<VideoItem> items;
    private int currentPosition;
    private VideoItem currentVideoItem;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_SYSTEM_TIME_SHOW:           //更新系统时间
                    updateSystemTimeShow();
                    break;
                case UPDATE_PLAY_PROGRESS_SHOW:         //更新播放进度
                    updatePlayProgressShow();
                    break;
                case HIDE_CTRL_LAYOUT:                  //隐藏控制面板
                    ctrlLayoutShowToggle();
                    break;
            }
        }
    };
    private AudioManager audioManager;
    private int maxVolume;
    private int currentVolume;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            //注意，一定要销毁handler
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 1、因为触摸事件，可以在onTouchEvent这个方法中处理
        // 2、识别手势，使用GestureDetector
        // 把触摸事件传给手势识别器进行识别
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (llBottomCtrl.getTranslationY() == 0) {
            // 如果控制面板是显示的
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:   // 如果是非空闲状态，则不要隐藏控制面板
                    handler.removeMessages(HIDE_CTRL_LAYOUT);
                    break;
                case MotionEvent.ACTION_UP:
                    handler.sendEmptyMessageDelayed(HIDE_CTRL_LAYOUT, 5000);
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_mobile_video_play;
    }


    @Override
    public void initView() {
        Log.e("启动次数","====");
        initWindowState();
        initSystemState();
        initVideoView();
        initSeekBar();
    }

    @Override
    public void initListener() {
        btnExit.setOnClickListener(this);
        btnFullscreen.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnPre.setOnClickListener(this);
        btnVoice.setOnClickListener(this);

        //声音SeekBar监听
        sbVoice.setOnSeekBarChangeListener(mVoiceOnSeekBarChangeListener);
        //视频SeekBar监听
        sbVideo.setOnSeekBarChangeListener(mVideoOnSeekBarChangeListener);
        //手指滑动的监听
        gestureDetector = new GestureDetector(this, mOnGestureListener);
    }


    @Override
    public void initData() {
        initIntentData();
    }

    private void initWindowState() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏
        //5.0
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //透明状态栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 隐藏状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏
        }*/
    }

    /**
     * 初始化界面操作，声音，时间，亮度，系统电量操作
     */
    private void initSystemState() {
        updateSystemTimeShow();     // 更新系统时间的显示
        updateSystemBatteryShow();  // 更新系统电量的显示
        initVolume();               // 初始化音量
        initCtrlLayout();           // 隐藏控制面板
    }

    /**
     * 更新系统时间的显示
     * 每隔一秒就更新下时间
     */
    private void updateSystemTimeShow() {
        CharSequence currentSystemTime = DateFormat.format("kk:mm:ss", new Date());
        tvSystemTime.setText(currentSystemTime); // 显示当前系统时间
        handler.sendEmptyMessageDelayed(UPDATE_SYSTEM_TIME_SHOW, 1000); // 下一秒钟再更新系统时间的显示
    }

    /**
     * 更新系统电量的显示
     * 发送广播
     */
    private void updateSystemBatteryShow() {
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);  // 取出系统当前电量
                updateBatteryBackground(level);
            }
        };
        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);    // 拦截电量改变的广播
        registerReceiver(receiver, intentfilter);                                       // 电量改变的广播，在注册的时候就会立马发过来一个。
    }

    /**
     * 在接收到广播通知的时候
     * 更新系统电量的显示
     */
    private void updateBatteryBackground(int level) {
        System.out.println("level = " + level);
        int resId;
        if (level == 0) {
            resId = R.drawable.ic_battery_0;
        } else if (level <= 10) {
            resId = R.drawable.ic_battery_10;
        } else if (level <= 20) {
            resId = R.drawable.ic_battery_20;
        } else if (level <= 40) {
            resId = R.drawable.ic_battery_40;
        } else if (level <= 60) {
            resId = R.drawable.ic_battery_60;
        } else if (level <= 80) {
            resId = R.drawable.ic_battery_80;
        } else {
            resId = R.drawable.ic_battery_100;
        }
        ivBattery.setImageResource(resId);
    }


    /**
     * 初始化音量
     */
    private void initVolume() {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取音量最大值
        int currentVolume = getStreamVolume();
        sbVoice.setMax(maxVolume);
        sbVoice.setProgress(currentVolume);
    }


    /**
     * 获取当前的音量值
     */
    private int getStreamVolume() {
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }


    /**
     * 隐藏控制面板
     */
    private void initCtrlLayout() {
        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);    // 创建出来的值是0
        llTopCtrl.measure(measureSpec, measureSpec);    // 使用未指定的模式进行测量
        llBottomCtrl.measure(measureSpec, measureSpec); // 等同于llBottomCtrl.measure(0, 0);
        ViewCompat.setTranslationY(llTopCtrl, -llTopCtrl.getMeasuredHeight());
        ViewCompat.setTranslationY(llBottomCtrl, llBottomCtrl.getMeasuredHeight());
    }


    private void initVideoView() {
        //当视频准备好可以播放的时候会调用这个方法
        videoView.setOnPreparedListener(mOnPreparedListener);
        //缓存卡顿的监听器
        videoView.setOnInfoListener(mOnInfoListener);
        //视频播放完后会调用这个方法
        videoView.setOnCompletionListener(mCompletionListener);
        //缓冲进度的监听器
        videoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
    }


    private void initSeekBar() {

    }


    private void initIntentData() {
        if (!LibsChecker.checkVitamioLibs(this)){
            return;
        }
        llLoading.setVisibility(View.VISIBLE);

        // 从我们自己应用的列表跳转过来的
        if(getIntent()!=null){
            Uri videoUri = getIntent().getData();
            if (videoUri != null) {
                // 从第三方应用跳转过来的
                videoView.setVideoURI(videoUri);
                btnPre.setEnabled(false);
                btnNext.setEnabled(false);
            } else {
                // 从我们自己应用的列表跳转过来的
                items = (ArrayList<VideoItem>) getIntent().getSerializableExtra(ConstantKeys.MOBILE_VIDEO_ITEMS);
                currentPosition = getIntent().getIntExtra(ConstantKeys.CURRENT_POSITION, -1);
                startOpenVideo();
            }
        }
    }


    /**
     * 直接打开视频
     */
    private void startOpenVideo() {
        if (items == null || items.isEmpty() || currentPosition == -1) {
            return;
        }
        llLoading.setVisibility(View.VISIBLE);
        // 如果不是第0个，说明还有上一个，则上一个按钮enable为true
        btnPre.setEnabled(currentPosition != 0);
        // 如果不是最后1个，说明还有惠一个，则下一个按钮enable为true
        btnNext.setEnabled(currentPosition != items.size() - 1);

        currentVideoItem = items.get(currentPosition);
        videoView.setVideoPath(currentVideoItem.data);          // 这个方法内部会进行异步准备视频
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:                                 //退出
                finish();
                break;
            case R.id.btn_pre:                                  //上一部
                playPre();
                break;
            case R.id.btn_pause:                                //播放，暂停
                playToggle();
                break;
            case R.id.btn_next:                                 //下一部
                playNext();
                break;
            case R.id.btn_fullscreen:                           //全屏
                playFullScreen();
                break;
            case R.id.btn_voice:                                //声音，静音
                muteToggle();
                break;
        }
    }


    /**
     * 视频准备的监听器
     */
    MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        /** 当视频准备好可以播放的时候会调用这个方法 */
        @Override
        public void onPrepared(MediaPlayer mp) {
            videoView.start();                          // 开始播放视频
            Uri videoUri = getIntent().getData();
            if (videoUri != null) {
                // 如果是从第三方应用跳转过来的
                tvTitle.setText(videoUri.getPath());
            } else {
                // 如果是从自己的应用列表跳转过来的
                tvTitle.setText(currentVideoItem.title);    // 显示视频标题
            }
            updatePauseButtonBackground();              // 更新暂停按钮的背景
            updateFullscreenBackground();               // 更新全屏按钮的背景
            updatePlayProgressShow();                   // 更新播放进度的显示
            hideLoading();
        }
    };

    /**
     * 更新视频播放进度
     */
    private void updatePlayProgressShow() {
        int currentPosition = (int) videoView.getCurrentPosition();   // 获取视频当前播放的位置
        int duration = (int) videoView.getDuration();                 // 获取视频的总时长
        tvCurrentPosition.setText(AppUtil.formatMillis(currentPosition)); // 显示视频当前播放的进度
        tvDuration.setText(AppUtil.formatMillis(duration));               // 显示视频总时长
        sbVideo.setProgress(currentPosition);
        sbVideo.setMax(duration);
        handler.sendEmptyMessageDelayed(UPDATE_PLAY_PROGRESS_SHOW, 30);
    }


    /**
     * 缓存卡顿的监听器
     */
    MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:    // 说明视频正在努力缓存，这个时候视频播不了
                    llLoading.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:      // 缓存到可以播放了
                    hideLoading();
                    break;
            }
            return true;
        }
    };


    /**
     * 视频播放完后下一首
     */
    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //  播放完成自动播放下一首
            playNext();
        }
    };


    /** 缓冲进度的监听器 */
    MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        /**
         * @param mp
         * @param percent 缓冲的百分比
         */
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            float percentFloat = percent / 100f;
            int secondaryProgress = (int) (videoView.getDuration() * percentFloat);
            sbVideo.setSecondaryProgress(secondaryProgress);                        // 显示视频的缓冲进度
        }
    };



    /** 隐藏正在加载 */
    private void hideLoading() {
        ViewCompat.animate(llLoading)
                .alpha(0.0f)
                .setDuration(1500)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        llLoading.setVisibility(View.GONE);
                        ViewCompat.setAlpha(llLoading, 1.0f);
                    }
                });
    }


    /**
     * 播放上一首
     * 如果是第一个视频，那么就没有上一首
     */
    private void playPre() {
        if(currentPosition==0){
            ToastUtil.showToast(this,"没有上一首");
            return;
        }
        // 如果不是第0位置，则说明还有上一个
        currentPosition--;
        startOpenVideo();
    }


    /**
     * 播放开关，要么开，要么关
     */
    private void playToggle() {
        if(videoView.isPlaying()){
            videoView.pause();
        }else {
            videoView.start();
        }
        updatePauseButtonBackground();
    }

    /**
     * 播放下一首
     * 如果是最后一个视频，那么就没有下一首呢
     */
    private void playNext() {
        if (items == null) return;
        // 如果不是最后一个位置，则说明还有下一个
        if (currentPosition != items.size() - 1) {
            currentPosition++;
            startOpenVideo();
        }else {
            ToastUtil.showToast(this,"没有下一首");
        }
    }

    /**
     * 全屏播放视频
     * 暂时不考虑切换横屏的方式，切换方式涉及生命周期较为麻烦，后期深入研究
     */
    private boolean fullScreen = false;
    private void playFullScreen() {
        if(fullScreen){
            // 如果原来是全屏的，则切换为默认比例
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
        }else {
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
        }
        fullScreen = !fullScreen;
        updateFullscreenBackground();
    }


    /**
     * 更新暂停按钮的背景
     */
    private void updatePauseButtonBackground() {
        if (videoView.isPlaying()) {
            // 如果之前正在播放，则显示暂停
            btnPause.setBackgroundResource(R.drawable.mobile_btn_pause);
        } else {
            // 如果之前正在暂停，则显示播放
            btnPause.setBackgroundResource(R.drawable.mobile_btn_play);
        }
    }


    /**
     * 更新全屏按钮的背景
     */
    private void updateFullscreenBackground() {
        if (!fullScreen) {
            // 如果原来是全屏的，则显示一个默认比例的按钮
            btnFullscreen.setBackgroundResource(R.drawable.mobile_btn_default_screen);
        } else {
            // 如果原来是默认比例的，则显示一个全屏的按钮
            btnFullscreen.setBackgroundResource(R.drawable.mobile_btn_full_screen);
        }
    }


    /**
     * 静音开关
     */
    private void muteToggle() {
        //如果获取当前音量值大于0
        if(getStreamVolume()>0){
            // 如果当前没有静音，则先保存当前的音量值，然后静音
            currentVolume = getStreamVolume();
            setStreamVolume(0);
            sbVoice.setProgress(0);
        }else {
            // 如果当前是静音的，则恢复之前的音量
            setStreamVolume(currentVolume);
            sbVoice.setProgress(currentVolume);
        }
    }


    /**
     * 音量SeekBar进度条改变的监听器
     */
    SeekBar.OnSeekBarChangeListener mVoiceOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        // fromUser指示进度的改变是否是由用户触发的
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                //根据手势移动改变音量
                setStreamVolume(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    /**
     * 设置音量值
     * @param volume
     */
    private void setStreamVolume(int volume) {
        // 参数1是音量类型，参数2是音量值，参数3用于控制是否显示系统的音量面板，0不显示，1显示
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }


    /**
     * 视频SeekBar进度条改变的监听器
     */
    SeekBar.OnSeekBarChangeListener mVideoOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoView.seekTo(progress); // 把视频跳转到指定的位置
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };



    /**
     * 识别手势的监听器
     */
    GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        /** 长按 */
        @Override
        public void onLongPress(MotionEvent e) {
            btnPause.performClick();
            super.onLongPress(e);
        }

        /** 滑动 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 计算滑动距离
            float distance = e1.getY() - e2.getY();
            // 左边
            if(atLeftDown){
                // 如果在屏幕左边按下，则改变音量大小
                changeVolumeByDistance(distance);
            }else {
                // 如果在屏幕右边按下，则改变屏幕亮度
                changeBightByDistance(-distance);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        /** 按下 */
        @Override
        public boolean onDown(MotionEvent e) {
            // 如果按下的x位置小于屏幕宽的一半，则是在屏幕的左边按下的
            atLeftDown = e.getX() < ScreenUtils.getScreenWidth() / 2;
            // 获取View的亮度值
            currentBrightness = ViewCompat.getAlpha(brightnessView);
            return super.onDown(e);
        }


        /** 双击 */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            btnFullscreen.performClick();               // 执行单击
            return super.onDoubleTap(e);
        }

        /** 单击 */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            ctrlLayoutShowToggle();
            return super.onSingleTapConfirmed(e);
        }
    };


    /**
     * 如果在屏幕左边按下，则改变音量大小
     * @param distance      滑动位移
     */
    private void changeVolumeByDistance(float distance) {
        // 计算音量和屏幕高的比例（音量最大值/屏幕高）    两个整数相除是拿不到小数点的
        float scale = (float) maxVolume / ScreenUtils.getScreenHeight();
        // 计算滑动距离对应的音量值
        int moveVolume = (int) (distance * scale);
        // 在原来音量的基础上加上滑动对应的音量值
        int result = currentVolume + moveVolume;
        // 预防超出范围
        if (result > maxVolume) {
            result = maxVolume;
        } else if (result < 0) {
            result = 0;
        }
        sbVoice.setProgress(result);
        // 设置音量
        setStreamVolume(result);
    }


    /**
     * 如果在屏幕右边按下，则改变屏幕亮度
     * @param distance     滑动位移
     */
    private void changeBightByDistance(float distance) {
        // 计算亮度和屏幕高的比例（亮度最大值/屏幕高）    两个整数相除是拿不到小数点的
        float scale = 1.0f / ScreenUtils.getScreenHeight();
        // 计算滑动距离对应的亮度
        float moveBrightness = distance * scale;
        // 在原来亮度的基础上加上滑动对应的亮度
        float result = currentBrightness + moveBrightness;
        // 预防超出范围
        if (result > 0.8f) {
            result = 0.8f;
        } else if (result < 0) {
            result = 0;
        }
        System.out.println("result = " + result);
        ViewCompat.setAlpha(brightnessView, result);
    }


    /**
     * 单击后控制面板的显示与隐藏
     */
    private void ctrlLayoutShowToggle() {
        if (ViewCompat.getTranslationY(llBottomCtrl) == 0) {
            // 如果当前是显示的，则隐藏
            ViewCompat.animate(llBottomCtrl).translationY(llBottomCtrl.getHeight());
            ViewCompat.animate(llTopCtrl).translationY(-llTopCtrl.getHeight());
        } else {
            // 如果当前是隐藏的，则显示
            ViewCompat.animate(llBottomCtrl).translationY(0);
            ViewCompat.animate(llTopCtrl).translationY(0);
            handler.sendEmptyMessageDelayed(HIDE_CTRL_LAYOUT, 5000);    //  5秒后自动隐藏控制面板
        }
    }


}
