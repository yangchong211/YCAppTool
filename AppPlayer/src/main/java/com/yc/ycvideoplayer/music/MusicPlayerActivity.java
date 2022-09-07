package com.yc.ycvideoplayer.music;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.music.inter.OnPlayerEventListener;
import com.yc.music.model.AudioBean;
import com.yc.music.service.PlayAudioService;
import com.yc.music.tool.BaseAppHelper;
import com.yc.music.tool.CoverLoader;
import com.yc.toolutils.AppLogUtils;
import com.yc.ycvideoplayer.R;
import com.yc.ycvideoplayer.video.list.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private RecyclerView recyclerView;
    private FrameLayout mFlPlayBar;
    private ImageView mIvPlayBarCover;
    private TextView mTvPlayBarTitle;
    private TextView mTvPlayBarArtist;
    private ImageView mIvPlayBarList;
    private ImageView mIvPlayBarPlay;
    private ImageView mIvPlayBarNext;
    private ProgressBar mPbPlayBar;
    private boolean isPlayFragmentShow = false;
    private PlayMusicFragment mPlayFragment;
    private MusicAdapter musicAdapter;

    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            hidePlayingFragment();
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        initFindViewById();
        initRecyclerView();
        initListener();
        initData();
        initPlayServiceListener();
    }

    private void initFindViewById() {
        recyclerView = findViewById(R.id.recyclerView);
        mToolbar = findViewById(R.id.toolbar);
        mFlPlayBar = findViewById(R.id.fl_play_bar);
        mIvPlayBarCover = findViewById(R.id.iv_play_bar_cover);
        mTvPlayBarTitle = findViewById(R.id.tv_play_bar_title);
        mTvPlayBarArtist = findViewById(R.id.tv_play_bar_artist);
        mIvPlayBarList = findViewById(R.id.iv_play_bar_list);
        mIvPlayBarPlay = findViewById(R.id.iv_play_bar_play);
        mIvPlayBarNext = findViewById(R.id.iv_play_bar_next);
        mPbPlayBar = findViewById(R.id.pb_play_bar);
    }

    private void initRecyclerView() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        List<AudioBean> musicList = BaseAppHelper.get().getMusicList();
        musicAdapter = new MusicAdapter(musicList);
        musicAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<AudioBean> musicList = BaseAppHelper.get().getMusicList();
                if(musicList!=null && musicList.size()>0 && musicList.size()>position && position>=0){
                    BaseAppHelper.get().getMusicService().play(position);
                    musicAdapter.updatePlayingPosition(position);
                    musicAdapter.notifyDataSetChanged();
                }
            }
        });
        recyclerView.setAdapter(musicAdapter);
    }

    private void initListener() {
        mIvPlayBarPlay.setOnClickListener(this);
        mFlPlayBar.setOnClickListener(this);
        mIvPlayBarNext.setOnClickListener(this);
    }

    private void initData() {
        if (BaseAppHelper.get().getMusicList()==null || BaseAppHelper.get().getMusicList().size()==0){
            new AsyncTask<Void, Void, List<AudioBean>>() {
                @Override
                protected List<AudioBean> doInBackground(Void... params) {
                    return FileMusicScanManager.getInstance().scanMusic(MusicPlayerActivity.this);
                }

                @Override
                protected void onPostExecute(List<AudioBean> musicList) {
                    //然后添加所有扫描到的音乐
                    AppLogUtils.d("onPostExecute" + musicList.size());
                    BaseAppHelper.get().setMusicList(musicList);
                    List<AudioBean> netMusic = new ArrayList<>();
                    AudioBean audioBean1 = new AudioBean();
                    audioBean1.setPath("http://img.zhugexuetang.com/lleXB2SNF5UFp1LfNpPI0hsyQjNs");
                    audioBean1.setId("1");
                    audioBean1.setTitle("音频1");
                    musicList.add(audioBean1);
                    AudioBean audioBean2 = new AudioBean();
                    audioBean2.setPath("http://img.zhugexuetang.com/ljUa-X-oDbLHu7n9AhkuMLu2Yz3k");
                    audioBean2.setId("2");
                    audioBean2.setTitle("音频2");
                    musicList.add(audioBean2);
                    AudioBean audioBean3 = new AudioBean();
                    audioBean3.setPath("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
                    audioBean3.setId("3");
                    audioBean3.setTitle("音频3");
                    netMusic.add(audioBean1);
                    netMusic.add(audioBean2);
                    netMusic.add(audioBean3);
                    BaseAppHelper.get().addMusicList(netMusic);
                    musicAdapter.notifyDataSetChanged();
                }
            }.execute();
        } else {
            //当在播放音频详细页面切换歌曲的时候，需要刷新底部控制器，和音频详细页面的数据
            List<AudioBean> musicList = BaseAppHelper.get().getMusicList();
            PlayAudioService musicService = BaseAppHelper.get().getMusicService();
            if(musicList.size()>0){
                int mPlayPosition;
                if (musicService.getPlayingMusic() != null ) {
                    mPlayPosition = musicService.getPlayingPosition();
                } else {
                    mPlayPosition = 0;
                }
                onChangeImpl(musicList.get(mPlayPosition));
            }else {
                onChangeImpl(musicService.getPlayingMusic());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start:
                BaseAppHelper.get().getMusicService().start();
                break;
            case R.id.fl_play_bar:
                showPlayingFragment();
                break;
            case R.id.iv_play_bar_next:
                BaseAppHelper.get().getMusicService().next();
                break;
            case R.id.iv_play_bar_play:
                if (BaseAppHelper.get().getMusicService().isDefault()) {
                    if (BaseAppHelper.get().getMusicList().size() > 0) {
                        int mPlayPosition;
                        if (BaseAppHelper.get().getMusicService().getPlayingMusic() != null ) {
                            mPlayPosition = BaseAppHelper.get().getMusicService().getPlayingPosition();
                        } else {
                            mPlayPosition = 0;
                        }
                        BaseAppHelper.get().getMusicService().play(BaseAppHelper.get()
                                .getMusicList().get(mPlayPosition));
                    }
                } else {
                    BaseAppHelper.get().getMusicService().playPause();
                }
                break;
        }
    }

    /**
     * 初始化服务播放音频播放进度监听器
     * 这个是要是通过监听即时更新主页面的底部控制器视图
     * 同时还要同步播放详情页面mPlayFragment的视图
     */
    public void initPlayServiceListener() {
        BaseAppHelper.get().setOnPlayEventListener(new OnPlayerEventListener() {
            /**
             * 切换歌曲
             * 主要是切换歌曲的时候需要及时刷新界面信息
             */
            @Override
            public void onChange(AudioBean music) {
                AppLogUtils.d("OnPlayerEventListener   onChange ");
                onChangeImpl(music);
                if (mPlayFragment!=null){
                    mPlayFragment.onChange(music);
                }
            }

            /**
             * 继续播放
             * 主要是切换歌曲的时候需要及时刷新界面信息，比如播放暂停按钮
             */
            @Override
            public void onPlayerStart() {
                AppLogUtils.d("OnPlayerEventListener   onPlayerStart ");
                mIvPlayBarPlay.setSelected(true);
                if (mPlayFragment!=null){
                    mPlayFragment.onPlayerStart();
                }
            }

            /**
             * 暂停播放
             * 主要是切换歌曲的时候需要及时刷新界面信息，比如播放暂停按钮
             */
            @Override
            public void onPlayerPause() {
                AppLogUtils.d("OnPlayerEventListener   onPlayerPause ");
                mIvPlayBarPlay.setSelected(false);
                if (mPlayFragment!=null){
                    mPlayFragment.onPlayerPause();
                }
            }

            /**
             * 更新进度
             * 主要是播放音乐或者拖动进度条时，需要更新进度
             */
            @Override
            public void onUpdateProgress(int progress) {
                mPbPlayBar.setProgress(progress);
                if (mPlayFragment!=null){
                    mPlayFragment.onUpdateProgress(progress);
                }
            }

            @Override
            public void onBufferingUpdate(int percent) {
                if (mPlayFragment != null && mPlayFragment.isAdded()) {
                    mPlayFragment.onBufferingUpdate(percent);
                }
            }

            /**
             * 更新定时停止播放时间
             */
            @Override
            public void onTimer(long remain) {

            }
        });
    }

    /**
     * 当在播放音频详细页面切换歌曲的时候，需要刷新底部控制器，和音频详细页面的数据
     * 之前关于activity，Fragment，service之间用EventBus通信
     * 案例：https://github.com/yangchong211/LifeHelper
     * 本项目中直接通过定义接口来实现功能，尝试中……
     *
     * @param music LocalMusic
     */
    private void onChangeImpl(AudioBean music) {
        if (music == null) {
            return;
        }
        PlayAudioService musicService = BaseAppHelper.get().getMusicService();
        Bitmap cover = CoverLoader.getInstance().loadThumbnail(music);
        mIvPlayBarCover.setImageBitmap(cover);
        mTvPlayBarTitle.setText(music.getTitle());
        mIvPlayBarPlay.setSelected(musicService.isPlaying() || musicService.isPreparing());
        //更新进度条
        mPbPlayBar.setMax((int) music.getDuration());
        mPbPlayBar.setProgress((int) musicService.getCurrentPosition());


        recyclerView.scrollToPosition(musicService.getPlayingPosition());
        musicAdapter.updatePlayingPosition(musicService.getPlayingPosition());
        musicAdapter.notifyDataSetChanged();
    }


    /**
     * 展示页面
     */
    private void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = PlayMusicFragment.newInstance("OnLine");
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }


    /**
     * 隐藏页面
     */
    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }


}
