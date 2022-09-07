package com.yc.ycvideoplayer.video.list;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.toolutils.AppLogUtils;
import com.yc.ycvideoplayer.ConstantVideo;

import com.yc.ycvideoplayer.R;
import com.yc.video.config.ConstantKeys;
import com.yc.video.config.VideoInfoBean;
import com.yc.video.player.SimpleStateListener;
import com.yc.video.player.VideoViewManager;
import com.yc.video.player.VideoPlayer;
import com.yc.video.tool.PlayerUtils;
import com.yc.video.ui.view.BasisVideoController;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通的列表播放
 */
public class RecyclerViewFragment extends Fragment {

    protected List<VideoInfoBean> mVideos = new ArrayList<>();
    protected VideoRecyclerViewAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLinearLayoutManager;

    protected VideoPlayer mVideoView;
    protected BasisVideoController mController;

    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    protected void initView(View view) {
        initVideoView();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new VideoRecyclerViewAdapter(mVideos);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(int position) {
                startPlay(position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                AppLogUtils.i("addOnChildAttachStateChangeListener-----AttachedToWindow---"+view);
            }

            /**
             * 当适配器创建的view（即列表项view）被窗口分离（即滑动离开了当前窗口界面）就会被调用
             *
             * @param view
             */
            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                AppLogUtils.i("addOnChildAttachStateChangeListener-----DetachedFromWindow---"+view);
                FrameLayout playerContainer = view.findViewById(R.id.player_container);
                View v = playerContainer.getChildAt(0);
                if (v != null && v == mVideoView && !mVideoView.isFullScreen()) {
                    releaseVideoView();
                }
            }
        });
    }

    protected void initVideoView() {
        mVideoView = new VideoPlayer(context);
        mVideoView.setOnStateChangeListener(new SimpleStateListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == ConstantKeys.CurrentState.STATE_IDLE) {
                    PlayerUtils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new BasisVideoController(context);
        mVideoView.setController(mController);
    }

    protected void initData() {
        List<VideoInfoBean> videoList = ConstantVideo.getVideoList();
        mVideos.addAll(videoList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        pause();
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    protected void pause() {
        releaseVideoView();
    }

    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    protected void resume() {
        if (mLastPos == -1)
            return;
        //恢复上次播放的位置
        startPlay(mLastPos);
    }

    /**
     * 开始播放
     * @param position 列表位置
     */
    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        VideoInfoBean videoBean = mVideos.get(position);
        mVideoView.setUrl(videoBean.getVideoUrl());
        View itemView = mLinearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoRecyclerViewAdapter.VideoHolder viewHolder = (VideoRecyclerViewAdapter.VideoHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        PlayerUtils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(mVideoView, "list");
        mVideoView.start();
        mCurPos = position;
    }

    private void releaseVideoView() {
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        mVideoView.release();
        if(getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }
}
