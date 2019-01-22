package com.ycbjie.music.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.ycutilslib.fragmentBack.BackHandlerHelper;
import com.pedaily.yc.ycdialoglib.dialogFragment.BottomDialogFragment;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.inter.listener.OnListItemClickListener;
import com.ycbjie.music.R;
import com.ycbjie.music.base.BaseAppHelper;
import com.ycbjie.music.inter.OnPlayerEventListener;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.ui.adapter.DialogMusicListAdapter;
import com.ycbjie.music.ui.fragment.MusicFragment;
import com.ycbjie.music.ui.fragment.PlayMusicFragment;
import com.ycbjie.music.utils.CoverLoader;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import java.util.List;

@Route(path = ARouterConstant.ACTIVITY_MUSIC_ACTIVITY)
public class MusicActivity extends BaseActivity implements View.OnClickListener {


    private FrameLayout flPlayBar;
    private ImageView ivPlayBarCover;
    private TextView tvPlayBarTitle;
    private TextView tvPlayBarArtist;
    private ImageView ivPlayBarList;
    private ImageView ivPlayBarPlay;
    private ImageView ivPlayBarNext;
    private ProgressBar pbPlayBar;
    private PlayMusicFragment mPlayFragment;
    private boolean isPlayFragmentShow = false;
    private MusicFragment mMusicFragment;


    /**
     * 是当某个按键被按下是触发。所以也有人在点击返回键的时候去执行该方法来做判断
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.e("触摸监听", "onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayFragment != null && isPlayFragmentShow) {
                hidePlayingFragment();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_home_main;
    }


    @Override
    public void initView() {
        initFindViewById();
        initPlayServiceListener();
        parseIntent();
        initFragment();
    }


    private void initFragment() {
        if (mMusicFragment == null) {
            mMusicFragment = new MusicFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_main, mMusicFragment, MusicFragment.class.getName());
        ft.show(mMusicFragment);
        ft.commitAllowingStateLoss();
    }

    private void initFindViewById() {
        flPlayBar = findViewById(R.id.fl_play_bar);
        ivPlayBarCover = findViewById(R.id.iv_play_bar_cover);
        tvPlayBarTitle = findViewById(R.id.tv_play_bar_title);
        tvPlayBarArtist =findViewById(R.id.tv_play_bar_artist);
        ivPlayBarList = findViewById(R.id.iv_play_bar_list);
        ivPlayBarPlay = findViewById(R.id.iv_play_bar_play);
        ivPlayBarNext = findViewById(R.id.iv_play_bar_next);
        pbPlayBar = findViewById(R.id.pb_play_bar);

    }

    /**
     * 处理onNewIntent()，以通知碎片管理器 状态未保存。
     * 如果您正在处理新的意图，并且可能是 对碎片状态进行更改时，要确保调用先到这里。
     * 否则，如果你的状态保存，但活动未停止，则可以获得 onNewIntent()调用，发生在onResume()之前，
     * 并试图 此时执行片段操作将引发IllegalStateException。 因为碎片管理器认为状态仍然保存。
     * @param intent intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        parseIntent();
    }


    @Override
    public void initListener() {
        //音乐播放
        flPlayBar.setOnClickListener(this);
        ivPlayBarList.setOnClickListener(this);
        ivPlayBarPlay.setOnClickListener(this);
        ivPlayBarNext.setOnClickListener(this);
    }


    @Override
    public void initData() {
        //当在播放音频详细页面切换歌曲的时候，需要刷新底部控制器，和音频详细页面的数据
        List<AudioBean> musicList = BaseAppHelper.get().getMusicList();
        if(musicList.size()>0){
            int mPlayPosition;
            if (BaseAppHelper.get().getMusicService().getPlayingMusic() != null &&
                    BaseAppHelper.get().getMusicService().getPlayingMusic().getType() == AudioBean.Type.LOCAL) {
                mPlayPosition = BaseAppHelper.get().getMusicService().getPlayingPosition();
            } else {
                mPlayPosition = 0;
            }
            onChangeImpl(musicList.get(mPlayPosition));
        }else {
            onChangeImpl(BaseAppHelper.get().getMusicService().getPlayingMusic());
        }
    }

    /**
     * 当关闭锁屏页面(锁屏页面为栈顶页面)的时候，会返回该MainActivity
     * 那么则需要刷新页面数据
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fl_play_bar) {
            showPlayingFragment();
        } else if (i == R.id.iv_play_bar_list) {
            showListDialog();
        } else if (i == R.id.iv_play_bar_play) {
            if (BaseAppHelper.get().getMusicService().isDefault()) {
                if (BaseAppHelper.get().getMusicList().size() > 0) {
                    int mPlayPosition;
                    if (BaseAppHelper.get().getMusicService().getPlayingMusic() != null &&
                            BaseAppHelper.get().getMusicService().getPlayingMusic().getType() == AudioBean.Type.LOCAL) {
                        mPlayPosition = BaseAppHelper.get().getMusicService().getPlayingPosition();
                    } else {
                        mPlayPosition = 0;
                    }
                    BaseAppHelper.get().getMusicService().play(BaseAppHelper.get().getMusicList().get(mPlayPosition));
                } else {
                    ToastUtils.showToast("请检查是否有音乐");
                }
            } else {
                BaseAppHelper.get().getMusicService().playPause();
            }
        } else if (i == R.id.iv_play_bar_next) {
            BaseAppHelper.get().getMusicService().next();
        } else {
        }
    }

    /**
     * 从通知栏点击进入音频播放详情页面
     */
    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constant.EXTRA_NOTIFICATION)) {
            showPlayingFragment();
            setIntent(new Intent());
        }
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
            mPlayFragment = PlayMusicFragment.newInstance("Main");
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
        if(mPlayFragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(0, R.anim.fragment_slide_down);
            ft.hide(mPlayFragment);
            ft.commitAllowingStateLoss();
            isPlayFragmentShow = false;
        }
    }


    /**
     * 初始化服务播放音频播放进度监听器
     * 这个是要是通过监听即时更新主页面的底部控制器视图
     * 同时还要同步播放详情页面mPlayFragment的视图
     */
    public void initPlayServiceListener() {
        if (BaseAppHelper.get().getMusicService() == null) {
            return;
        }
        BaseAppHelper.get().getMusicService().setOnPlayEventListener(new OnPlayerEventListener() {
            /**
             * 切换歌曲
             * 主要是切换歌曲的时候需要及时刷新界面信息
             */
            @Override
            public void onChange(AudioBean music) {
                onChangeImpl(music);
                if (mPlayFragment != null && mPlayFragment.isAdded()) {
                    mPlayFragment.onChange(music);
                }
            }

            /**
             * 继续播放
             * 主要是切换歌曲的时候需要及时刷新界面信息，比如播放暂停按钮
             */
            @Override
            public void onPlayerStart() {
                ivPlayBarPlay.setSelected(true);
                if (mPlayFragment != null && mPlayFragment.isAdded()) {
                    mPlayFragment.onPlayerStart();
                }
            }

            /**
             * 暂停播放
             * 主要是切换歌曲的时候需要及时刷新界面信息，比如播放暂停按钮
             */
            @Override
            public void onPlayerPause() {
                ivPlayBarPlay.setSelected(false);
                if (mPlayFragment != null && mPlayFragment.isAdded()) {
                    mPlayFragment.onPlayerPause();
                }
            }

            /**
             * 更新进度
             * 主要是播放音乐或者拖动进度条时，需要更新进度
             */
            @Override
            public void onUpdateProgress(int progress) {
                pbPlayBar.setProgress(progress);
                if (mPlayFragment != null && mPlayFragment.isAdded()) {
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
        Bitmap cover = CoverLoader.getInstance().loadThumbnail(music);
        ivPlayBarCover.setImageBitmap(cover);
        tvPlayBarTitle.setText(music.getTitle());
        tvPlayBarArtist.setText(music.getArtist());
        ivPlayBarPlay.setSelected(BaseAppHelper.get().getMusicService().isPlaying() || BaseAppHelper.get().getMusicService().isPreparing());
        //更新进度条
        pbPlayBar.setMax((int) music.getDuration());
        pbPlayBar.setProgress((int) BaseAppHelper.get().getMusicService().getCurrentPosition());

        /*点击MainActivity中的控制器，如何更新musicFragment中的mLocalMusicFragment呢？*/
        if (mMusicFragment != null && mMusicFragment.isAdded()) {
            mMusicFragment.onItemPlay();
        }
    }


    public void showListDialog() {
        final List<AudioBean> musicList = BaseAppHelper.get().getMusicList();
        final BottomDialogFragment dialog = new BottomDialogFragment();
        dialog.setFragmentManager(getSupportFragmentManager());
        dialog.setViewListener(new BottomDialogFragment.ViewListener() {
            @Override
            public void bindView(View v) {
                RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
                TextView tvPlayType = v.findViewById(R.id.tv_play_type);
                TextView tvCollect = v.findViewById(R.id.tv_collect);
                ImageView ivClose = v.findViewById(R.id.iv_close);

                recyclerView.setLayoutManager(new LinearLayoutManager(MusicActivity.this));
                final DialogMusicListAdapter mAdapter = new DialogMusicListAdapter(MusicActivity.this, musicList);
                recyclerView.setAdapter(mAdapter);
                mAdapter.updatePlayingPosition(BaseAppHelper.get().getMusicService());
                final RecycleViewItemLine line = new RecycleViewItemLine(MusicActivity.this, LinearLayout.HORIZONTAL,
                        SizeUtils.dp2px(1), MusicActivity.this.getResources().getColor(R.color.grayLine));
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
                        } else if (i == R.id.tv_collect) {
                            ToastUtils.showRoundRectToast("收藏，后期在做");

                        } else if (i == R.id.iv_close) {
                            dialog.dismissDialogFragment();

                        } else {
                        }
                    }
                };
                tvPlayType.setOnClickListener(listener);
                tvCollect.setOnClickListener(listener);
                ivClose.setOnClickListener(listener);
            }
        });
        dialog.setLayoutRes(R.layout.dialog_bottom_list_view);
        dialog.setDimAmount(0.5f);
        dialog.setTag("BottomDialogFragment");
        dialog.setCancelOutside(true);
        //这个高度可以自己设置，十分灵活
        dialog.setHeight(ScreenUtils.getScreenHeight() * 6 / 10);
        dialog.show();
    }

}
