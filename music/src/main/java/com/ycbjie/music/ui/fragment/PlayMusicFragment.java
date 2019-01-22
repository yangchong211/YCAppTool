package com.ycbjie.music.ui.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pedaily.yc.ycdialoglib.dialogFragment.BottomDialogFragment;
import com.ycbjie.library.base.config.AppConfig;
import com.ycbjie.library.base.mvp.BaseFragment;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.inter.listener.OnListItemClickListener;
import com.ycbjie.library.utils.time.TimeUtils;
import com.ycbjie.music.R;
import com.ycbjie.music.base.BaseAppHelper;
import com.ycbjie.music.executor.search.AbsSearchLrc;
import com.ycbjie.music.inter.OnPlayerEventListener;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.model.enums.PlayModeEnum;
import com.ycbjie.music.ui.adapter.DialogMusicListAdapter;
import com.ycbjie.music.utils.CoverLoader;
import com.ycbjie.music.utils.FileMusicUtils;
import com.ycbjie.music.weight.YCLrcCustomView;

import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import cn.ycbjie.ycthreadpoollib.PoolThread;


public class PlayMusicFragment extends BaseFragment implements View.OnClickListener, OnPlayerEventListener {

    private ImageView ivPlayPageBg;
    private LinearLayout llContent;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvArtist;
    private ImageView ivShare;
    private SeekBar sbVolume;
    private YCLrcCustomView lrcView;
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
        LogUtils.e(TAG+"setUserVisibleHint"+isVisibleToUser);
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
        LogUtils.e(TAG+"onHiddenChanged"+hidden);
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
        PoolThread executor = AppConfig.INSTANCE.getExecutor();
        executor.setDelay(300, TimeUnit.MILLISECONDS);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ivBack.setEnabled(true);
            }
        });
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


    @Override
    public int getContentView() {
        return R.layout.fragment_play_music;
    }


    @Override
    public void initView(View view) {
        if (getArguments() != null) {
            type = getArguments().getString(TAG);
        }
        initFindById(view);
        initPlayMode();
        initVolume();
        getChildFragmentManager();
    }

    private void initFindById(View view) {
        ivPlayPageBg = view.findViewById(R.id.iv_play_page_bg);
        llContent = view.findViewById(R.id.ll_content);
        ivBack = view.findViewById(R.id.iv_back);
        tvTitle = view.findViewById(R.id.tv_title);
        tvArtist = view.findViewById(R.id.tv_artist);
        ivShare = view.findViewById(R.id.iv_share);
        sbVolume = view.findViewById(R.id.sb_volume);
        lrcView = view.findViewById(R.id.lrc_view);
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


    @Override
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
                        tvCurrentTime.setText(TimeUtils.formatTime(progress));
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
                        lrcView.updateTime(progress);
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

    @Override
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
            showListDialog();

        } else {
        }
    }

    private void prev() {
        if (BaseAppHelper.get().getMusicService() != null) {
            ToastUtils.showShort(R.string.state_prev);
            BaseAppHelper.get().getMusicService().prev();
        }
    }

    private void next() {
        if (BaseAppHelper.get().getMusicService() != null) {
            ToastUtils.showShort(R.string.state_next);
            BaseAppHelper.get().getMusicService().next();
        }
    }

    private void play() {
        if (BaseAppHelper.get().getMusicService() != null) {
            BaseAppHelper.get().getMusicService().playPause();
        }
    }

    private void switchPlayMode() {
        int playMode = SPUtils.getInstance(Constant.SP_NAME).getInt(Constant.PLAY_MODE, 0);
        PlayModeEnum mode = PlayModeEnum.valueOf(playMode);
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                ToastUtils.showShort(R.string.mode_shuffle);
                break;
            case SHUFFLE:
                mode = PlayModeEnum.SINGLE;
                ToastUtils.showShort(R.string.mode_one);
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                ToastUtils.showShort(R.string.mode_loop);
                break;
            default:
                break;
        }
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.PLAY_MODE, mode.value());
        initPlayMode();
    }

    public void showListDialog() {
        final List<AudioBean> musicList = BaseAppHelper.get().getMusicList();
        final BottomDialogFragment dialog = new BottomDialogFragment();
        dialog.setFragmentManager(getChildFragmentManager());
        dialog.setViewListener(new BottomDialogFragment.ViewListener() {
            @Override
            public void bindView(View v) {
                RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
                TextView tv_play_type = v.findViewById(R.id.tv_play_type);
                TextView tv_collect = v.findViewById(R.id.tv_collect);
                ImageView iv_close = v.findViewById(R.id.iv_close);

                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                final DialogMusicListAdapter mAdapter = new DialogMusicListAdapter(activity, musicList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.updatePlayingPosition(BaseAppHelper.get().getMusicService());
                final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                        SizeUtils.dp2px(1), activity.getResources().getColor(R.color.grayLine));
                recyclerView.addItemDecoration(line);
                mAdapter.setOnItemClickListener(new OnListItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        List<AudioBean> musicList = BaseAppHelper.get().getMusicList();
                        if(musicList!=null && musicList.size()>0 && musicList.size()>position && position>=0){
                            BaseAppHelper.get().getMusicService().play(position);
                            mAdapter.updatePlayingPosition(BaseAppHelper.get().getMusicService());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = v.getId();
                        if (i == R.id.tv_play_type) {
                            switchPlayMode();

                        } else if (i == R.id.tv_collect) {
                            ToastUtils.showLong("收藏，后期在做");

                        } else if (i == R.id.iv_close) {
                            dialog.dismissDialogFragment();

                        } else {
                        }
                    }
                };
                tv_play_type.setOnClickListener(listener);
                tv_collect.setOnClickListener(listener);
                iv_close.setOnClickListener(listener);
            }
        });
        dialog.setLayoutRes(R.layout.dialog_bottom_list_view);
        dialog.setDimAmount(0.5f);
        dialog.setTag("BottomDialogFragment");
        dialog.setCancelOutside(true);
        //这个高度可以自己设置，十分灵活
        dialog.setHeight(ScreenUtils.getScreenHeight() * 7 / 10);
        dialog.show();
    }



    private void initPlayMode() {
        int playMode = SPUtils.getInstance(Constant.SP_NAME).getInt(Constant.PLAY_MODE, 0);
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
        LogUtils.e("-----------------------"+(int) playingMusic.getDuration());
        mLastProgress = 0;
        tvCurrentTime.setText("00:00");
        tvTotalTime.setText(TimeUtils.formatTime(playingMusic.getDuration()));
        setCoverAndBg(playingMusic);
        setLrc(playingMusic);
        if (BaseAppHelper.get().getMusicService().isPlaying() || BaseAppHelper.get().getMusicService().isPreparing()) {
            ivPlay.setSelected(true);
            //mAlbumCoverView.start();
        } else {
            ivPlay.setSelected(false);
            //mAlbumCoverView.pause();
        }
    }

    private void setCoverAndBg(AudioBean music) {
        //mAlbumCoverView.setCoverBitmap(CoverLoader.getInstance().loadRound(music));
        ivPlayPageBg.setImageBitmap(CoverLoader.getInstance().loadBlur(music));
    }


    /**
     * 设置歌词
     *
     * @param playingMusic 正在播放的音乐
     */
    private void setLrc(final AudioBean playingMusic) {
        if (playingMusic.getType() == AudioBean.Type.LOCAL) {
            String lrcPath = FileMusicUtils.getLrcFilePath(playingMusic);
            if (!TextUtils.isEmpty(lrcPath)) {
                loadLrc(lrcPath);
            } else {
                new AbsSearchLrc(playingMusic.getArtist(), playingMusic.getTitle()) {
                    @Override
                    public void onPrepare() {
                        loadLrc("");
                        setLrcLabel("正在搜索歌词");
                    }

                    @Override
                    public void onExecuteSuccess(@NonNull String lrcPath) {
                        loadLrc(lrcPath);
                        setLrcLabel("暂时无歌词");
                    }

                    @Override
                    public void onExecuteFail(Exception e) {
                        setLrcLabel("加载歌词失败");
                    }
                }.execute();
            }
        } else {
            String lrcPath = FileMusicUtils.getLrcDir() +
                    FileMusicUtils.getLrcFileName(playingMusic.getArtist(), playingMusic.getTitle());
            loadLrc(lrcPath);
        }
    }


    private void loadLrc(String path) {
        File file = new File(path);
        lrcView.loadLrc(file);
    }


    private void setLrcLabel(String label) {
        lrcView.setLabel(label);
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
            lrcView.updateTime(progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if(sbProgress.getMax()>0 && percent>0){
            LogUtils.e("setOnPlayEventListener---percent---"+ sbProgress.getMax() + "-----" +percent);
            sbProgress.setSecondaryProgress(sbProgress.getMax() * 100 / percent);
        }
    }

    @Override
    public void onTimer(long remain) {

    }


}
