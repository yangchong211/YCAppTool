package com.yc.ycvideoplayer.video.tiktok;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.yc.statusbar.bar.StateAppBar;
import com.yc.toolutils.AppLogUtils;

import com.yc.videosurface.MeasureHelper;
import com.yc.ycvideoplayer.ConstantVideo;
import com.yc.ycvideoplayer.R;

import com.yc.pagerlib.pager.VerticalViewPager;
import com.yc.videocache.cache.PreloadManager;
import com.yc.videocache.cache.ProxyVideoCacheManager;

import com.yc.video.config.VideoInfoBean;
import com.yc.video.player.VideoPlayer;
import com.yc.video.tool.PlayerUtils;
import com.yc.video.ui.view.BasisVideoController;
import java.util.ArrayList;
import java.util.List;



/**
 * 模仿抖音短视频，使用VerticalViewPager实现，实现了预加载功能
 */

public class TikTok2Activity extends AppCompatActivity {

    /**
     * 当前播放位置
     */
    private int mCurPos;
    private List<VideoInfoBean> mVideoList = new ArrayList<>();
    private Tiktok2Adapter mTiktok2Adapter;
    private VerticalViewPager mViewPager;
    private PreloadManager mPreloadManager;
    private VideoPlayer mVideoPlayer;
    private BasisVideoController mController;

    private static final String KEY_INDEX = "index";

    public static void start(Context context, int index) {
        Intent i = new Intent(context, TikTok2Activity.class);
        i.putExtra(KEY_INDEX, index);
        context.startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoPlayer != null) {
            mVideoPlayer.release();
        }
        mPreloadManager.removeAllPreloadTask();
        //清除缓存，实际使用可以不需要清除，这里为了方便测试
        ProxyVideoCacheManager.clearAllCache(this);
    }

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
    public void onBackPressed() {
        if (mVideoPlayer == null || !mVideoPlayer.onBackPressed()) {
            super.onBackPressed();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiktok2);
        StateAppBar.translucentStatusBar(this, true);
        initFindViewById();
        initListener();
        initView();
    }

    private void initFindViewById() {
        mViewPager = findViewById(R.id.vvp);

    }

    private void initListener() {

    }

    protected void initView() {
        initViewPager();
        initVideoView();
        mPreloadManager = PreloadManager.getInstance(this);
        addData(null);
        Intent extras = getIntent();
        int index = extras.getIntExtra(KEY_INDEX, 0);
        mViewPager.setCurrentItem(index);

        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                startPlay(index);
            }
        });
    }

    private void initVideoView() {
        mVideoPlayer = new VideoPlayer(this);
        mVideoPlayer.setLooping(true);
        //以下只能二选一，看你的需求
        mVideoPlayer.setRenderViewFactory(TikTokRenderViewFactory.create());
        mController = new BasisVideoController(this);
        mVideoPlayer.setController(mController);
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.vvp);
        mViewPager.setOffscreenPageLimit(4);
        mTiktok2Adapter = new Tiktok2Adapter(mVideoList);
        mViewPager.setAdapter(mTiktok2Adapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            private int mCurItem;

            /**
             * VerticalViewPager是否反向滑动
             */
            private boolean mIsReverseScroll;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position == mCurItem) {
                    return;
                }
                mIsReverseScroll = position < mCurItem;
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == mCurPos) {
                    return;
                }
                startPlay(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == VerticalViewPager.SCROLL_STATE_DRAGGING) {
                    mCurItem = mViewPager.getCurrentItem();
                }

                if (state == VerticalViewPager.SCROLL_STATE_IDLE) {
                    mPreloadManager.resumePreload(mCurPos, mIsReverseScroll);
                } else {
                    mPreloadManager.pausePreload(mCurPos, mIsReverseScroll);
                }
            }
        });

//        RecyclerView recyclerView = new RecyclerView(this);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            /**
//             * 是否反向滑动
//             */
//            private boolean mIsReverseScroll;
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy>0){
//                    //表示下滑
//                    mIsReverseScroll = false;
//                } else {
//                    //表示上滑
//                    mIsReverseScroll = true;
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == VerticalViewPager.SCROLL_STATE_IDLE) {
//                    mPreloadManager.resumePreload(mCurPos, mIsReverseScroll);
//                } else {
//                    mPreloadManager.pausePreload(mCurPos, mIsReverseScroll);
//                }
//            }
//        });
    }

    private void startPlay(int position) {
        int count = mViewPager.getChildCount();
        for (int i = 0; i < count; i ++) {
            View itemView = mViewPager.getChildAt(i);
            Tiktok2Adapter.ViewHolder viewHolder = (Tiktok2Adapter.ViewHolder) itemView.getTag();
            if (viewHolder.mPosition == position) {
                mVideoPlayer.release();
                PlayerUtils.removeViewFormParent(mVideoPlayer);

                VideoInfoBean tiktokBean = mVideoList.get(position);
                String playUrl = mPreloadManager.getPlayUrl(tiktokBean.getVideoUrl());
                AppLogUtils.i("startPlay: " + "position: " + position + "  url: " + playUrl);
                mVideoPlayer.setUrl(playUrl);
                mVideoPlayer.setScreenScaleType(MeasureHelper.PlayerScreenScaleType.SCREEN_SCALE_16_9);
                mController.addControlComponent(viewHolder.mTikTokView, true);
                viewHolder.mPlayerContainer.addView(mVideoPlayer, 0);
                mVideoPlayer.start();
                mCurPos = position;
                break;
            }
        }
    }

    public void addData(View view) {
        mVideoList.addAll(ConstantVideo.getVideoList());
        mTiktok2Adapter.notifyDataSetChanged();
    }

}
