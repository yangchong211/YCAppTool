package com.yc.ycvideoplayer.music;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yc.music.config.MusicConstants;
import com.yc.music.config.PlayModeEnum;
import com.yc.music.inter.OnPlayerEventListener;
import com.yc.music.model.AudioBean;
import com.yc.music.tool.BaseAppHelper;
import com.yc.music.tool.CoverLoader;
import com.yc.toolutils.AppSpUtils;
import com.yc.video.tool.PlayerUtils;
import com.yc.toolutils.AppLogUtils;

import com.yc.ycvideoplayer.R;

import java.util.Objects;


public class PlayMusicFragment extends Fragment implements View.OnClickListener, OnPlayerEventListener {

    private ImageView ivPlayPageBg;
    private LinearLayout llContent;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvArtist;
    private ImageView ivShare;
    private SeekBar sbVolume;
    private LinearLayout llMusicTool;
    private ImageView ivPlayingFav;
    private ImageView ivPlayingDown;
    private ImageView ivPlayingCmt;
    private ImageView ivPlayingMore;
    private TextView tvCurrentTime;
    private SeekBar sbProgress;
    private TextView tvTotalTime;
    private ImageView ivMode;
    private ImageView ivPrev;
    private ImageView ivPlay;
    private ImageView ivNext;
    private ImageView ivOther;
    private FragmentActivity activity;
    private int mLastProgress;
    /**
     * 是否拖进度，默认是false
     */
    private boolean isDraggingProgress;
    private AudioManager mAudioManager;
    private static final String TAG = "DetailAudioFragment";
    private String type;

    /**
     * 使用FragmentPagerAdapter+ViewPager时，
     * 切换回上一个Fragment页面时（已经初始化完毕），
     * 不会回调任何生命周期方法以及onHiddenChanged()，
     * 只有setUserVisibleHint(boolean isVisibleToUser)会被回调，
     * 所以如果你想进行一些懒加载，需要在这里处理。
     * @param isVisibleToUser               是否显示
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        AppLogUtils.e(TAG+"setUserVisibleHint"+isVisibleToUser);
    }

    /**
     * 当使用add()+show()，hide()跳转新的Fragment时，旧的Fragment回调onHiddenChanged()，
     * 不会回调onStop()等生命周期方法，
     * 而新的Fragment在创建时是不会回调onHiddenChanged()，这点要切记
     * @param hidden                        是否显示
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppLogUtils.e(TAG+"onHiddenChanged"+hidden);
        if (!hidden){
            initData();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container , false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    /**
     * 对Fragment传递数据，建议使用setArguments(Bundle args)，而后在onCreate中使用getArguments()取出，
     * 在 “内存重启”前，系统会帮你保存数据，不会造成数据的丢失。和Activity的Intent恢复机制类似。
     * @param type                          type
     * @return                              PlayMusicFragment实例对象
     */
    public static PlayMusicFragment newInstance(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(TAG, type);
        PlayMusicFragment fragment = new PlayMusicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * 返回监听
     */
    private void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity()).onBackPressed();
        }
        ivBack.setEnabled(false);
        ivBack.postDelayed(new Runnable() {
            @Override
            public void run() {
                ivBack.setEnabled(true);
            }
        },300);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getContext()).unregisterReceiver(mVolumeReceiver);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getContext()).registerReceiver(mVolumeReceiver, filter);
        }
    }


    public int getContentView() {
        return R.layout.fragment_play_music;
    }


    public void initView(View view) {
        if (getArguments() != null) {
            type = getArguments().getString(TAG);
        }
        initFindById(view);
        initPlayMode();
        initVolume();
    }

    private void initFindById(View view) {
        ivPlayPageBg = view.findViewById(R.id.iv_play_page_bg);
        llContent = view.findViewById(R.id.ll_content);
        ivBack = view.findViewById(R.id.iv_back);
        tvTitle = view.findViewById(R.id.tv_title);
        tvArtist = view.findViewById(R.id.tv_artist);
        ivShare = view.findViewById(R.id.iv_share);
        sbVolume = view.findViewById(R.id.sb_volume);
        llMusicTool = view.findViewById(R.id.ll_music_tool);
        ivPlayingFav = view.findViewById(R.id.iv_playing_fav);
        ivPlayingDown = view.findViewById(R.id.iv_playing_down);
        ivPlayingCmt = view.findViewById(R.id.iv_playing_cmt);
        ivPlayingMore = view.findViewById(R.id.iv_playing_more);
        tvCurrentTime = view.findViewById(R.id.tv_current_time);
        sbProgress = view.findViewById(R.id.sb_progress);
        tvTotalTime = view.findViewById(R.id.tv_total_time);
        ivMode = view.findViewById(R.id.iv_mode);
        ivPrev = view.findViewById(R.id.iv_prev);
        ivPlay = view.findViewById(R.id.iv_play);
        ivNext = view.findViewById(R.id.iv_next);
        ivOther = view.findViewById(R.id.iv_other);

    }

    public void initListener() {
        ivBack.setOnClickListener(this);
        ivMode.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivOther.setOnClickListener(this);
        initSeekBarListener();
    }


    private void initSeekBarListener() {
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar == sbProgress) {
                    if (Math.abs(progress - mLastProgress) >= DateUtils.SECOND_IN_MILLIS) {
                        tvCurrentTime.setText(PlayerUtils.formatTime(progress));
                        mLastProgress = progress;
                    }
                }
            }

            /**
             * 通知用户已启动触摸手势,开始触摸时调用
             * @param seekBar               seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (seekBar == sbProgress) {
                    isDraggingProgress = true;
                }
            }


            /**
             * 通知用户已结束触摸手势,触摸结束时调用
             * @param seekBar               seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar == sbProgress) {
                    isDraggingProgress = false;
                    //如果是正在播放，或者暂停，那么直接拖动进度
                    if (BaseAppHelper.get().getMusicService().isPlaying() || BaseAppHelper.get().getMusicService().isPausing()) {
                        //获取进度
                        int progress = seekBar.getProgress();
                        //直接移动进度
                        BaseAppHelper.get().getMusicService().seekTo(progress);
                    } else {
                        //其他情况，直接设置进度为0
                        seekBar.setProgress(0);
                    }
                } else if (seekBar == sbVolume) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(),
                            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                }
            }
        };
        sbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbVolume.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    public void initData() {
        setViewData(BaseAppHelper.get().getMusicService().getPlayingMusic());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_back) {
            onBackPressed();

        } else if (i == R.id.iv_mode) {
            switchPlayMode();

        } else if (i == R.id.iv_play) {
            play();

        } else if (i == R.id.iv_next) {
            next();

        } else if (i == R.id.iv_prev) {
            prev();

        } else if (i == R.id.iv_other) {

        } else {

        }
    }

    private void prev() {
        if (BaseAppHelper.get().getMusicService() != null) {
            BaseAppHelper.get().getMusicService().prev();
        }
    }

    private void next() {
        if (BaseAppHelper.get().getMusicService() != null) {
            BaseAppHelper.get().getMusicService().next();
        }
    }

    private void play() {
        if (BaseAppHelper.get().getMusicService() != null) {
            BaseAppHelper.get().getMusicService().playPause();
        }
    }

    private void switchPlayMode() {
        int playMode = AppSpUtils.getInstance(MusicConstants.SP_NAME).getInt(MusicConstants.PLAY_MODE, 0);
        PlayModeEnum mode = PlayModeEnum.valueOf(playMode);
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                break;
            case SHUFFLE:
                mode = PlayModeEnum.SINGLE;
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                break;
            default:
                break;
        }
        AppSpUtils.getInstance(MusicConstants.SP_NAME).put(MusicConstants.PLAY_MODE, mode.value());
        initPlayMode();
    }


    private void initPlayMode() {
        int playMode = AppSpUtils.getInstance(MusicConstants.SP_NAME).getInt(MusicConstants.PLAY_MODE, 0);
        ivMode.setImageLevel(playMode);
    }

    /**
     * 初始化音量
     */
    private void initVolume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAudioManager = (AudioManager) Objects.requireNonNull(getContext())
                    .getSystemService(Context.AUDIO_SERVICE);
        }
        if (mAudioManager != null) {
            sbVolume.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            sbVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
    }

    /**
     * 发送广播接收者
     */
    private BroadcastReceiver mVolumeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sbVolume.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
    };


    /**
     * 填充页面数据
     *
     * @param playingMusic 正在播放的音乐
     */
    @SuppressLint("SetTextI18n")
    private void setViewData(AudioBean playingMusic) {
        if (playingMusic == null) {
            return;
        }
        tvTitle.setText(playingMusic.getTitle());
        tvArtist.setText(playingMusic.getArtist());
        sbProgress.setProgress((int) BaseAppHelper.get().getMusicService().getCurrentPosition());
        sbProgress.setSecondaryProgress(0);
        sbProgress.setMax((int) playingMusic.getDuration());
        AppLogUtils.e("-----------------------"+(int) playingMusic.getDuration());
        mLastProgress = 0;
        tvCurrentTime.setText("00:00");
        tvTotalTime.setText(PlayerUtils.formatTime(playingMusic.getDuration()));
        setCoverAndBg(playingMusic);
        if (BaseAppHelper.get().getMusicService().isPlaying() || BaseAppHelper.get().getMusicService().isPreparing()) {
            ivPlay.setSelected(true);
        } else {
            ivPlay.setSelected(false);
        }
    }

    private void setCoverAndBg(AudioBean music) {
        ivPlayPageBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music));
    }



    /**
     * ---------------通过MainActivity进行调用-----------------------------
     **/
    @Override
    public void onChange(AudioBean music) {
        setViewData(music);
    }

    @Override
    public void onPlayerStart() {
        ivPlay.setSelected(true);
    }

    @Override
    public void onPlayerPause() {
        ivPlay.setSelected(false);
    }

    @Override
    public void onUpdateProgress(int progress) {
        if(progress>0){
            //如果没有拖动进度，则开始更新进度条进度
            if (!isDraggingProgress) {
                sbProgress.setProgress(progress);
            }
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if(sbProgress.getMax()>0 && percent>0){
            AppLogUtils.e("setOnPlayEventListener---percent---"+ sbProgress.getMax() + "-----" +percent);
            sbProgress.setSecondaryProgress(sbProgress.getMax() * 100 / percent);
        }
    }

    @Override
    public void onTimer(long remain) {

    }


}
