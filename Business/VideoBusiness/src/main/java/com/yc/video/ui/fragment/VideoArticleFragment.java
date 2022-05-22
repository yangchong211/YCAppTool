package com.yc.video.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.yc.library.base.mvp.BackLazyFragment;
import com.yc.toastutils.ToastUtils;
import com.yc.video.R;
import com.yc.video.contract.VideoArticleContract;
import com.yc.video.model.MultiNewsArticleDataBean;
import com.yc.video.presenter.VideoArticlePresenter;
import com.yc.video.ui.activity.VideoActivity;
import com.yc.video.ui.adapter.VideoArticleAdapter;

import org.yczbj.ycrefreshviewlib.inter.OnErrorListener;
import org.yczbj.ycrefreshviewlib.inter.OnMoreListener;
import org.yczbj.ycrefreshviewlib.inter.OnNoMoreListener;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import org.yczbj.ycrefreshviewlib.view.YCRefreshView;
import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;

import java.util.List;
import java.util.Random;
import java.util.zip.CRC32;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 视频播放页面
 *     revise:
 * </pre>
 */
public class VideoArticleFragment extends BackLazyFragment implements VideoArticleContract.View {

    private VideoArticleContract.Presenter presenter = new VideoArticlePresenter(this);
    private static final String TYPE = "VideoArticleFragment";
    private VideoActivity activity;
    private String mType;
    private YCRefreshView recyclerView;
    protected boolean canLoadMore = false;
    private VideoArticleAdapter adapter;
    private VideoPlayerFragment videoPlayerFragment;
    private boolean isPlayFragmentShow = false;

    public static VideoArticleFragment newInstance(String param) {
        VideoArticleFragment fragment = new VideoArticleFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (VideoActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }



//    @Override
//    public boolean interceptBackPressed() {
//        if (activity.backHandled) {
//            if (videoPlayerFragment != null && isPlayFragmentShow) {
//                hidePlayingFragment();
//                VideoPlayerManager.instance().onBackPressed();
//                return true;
//            }else {
//                return false;
//            }
//        }
//        return false;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onLazyLoad() {
        recyclerView.showProgress();
        presenter.getVideoData(mType);
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecycleView();
    }

    @Override
    public void initListener() {
//        recyclerView.setRefreshListener(() -> {
//            if (NetworkUtils.isConnected()) {
//                presenter.doRefresh();
//            } else {
//                recyclerView.setRefreshing(false);
//                ToastUtils.showRoundRectToast("网络不可用");
//            }
//        });
        recyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (canLoadMore) {
                    canLoadMore = false;
                    presenter.doLoadMoreData();
                }
            }
        });
        adapter.setOnItemClickListener(position -> {
            if (adapter.getAllData().size()>position){
                String videoId = adapter.getAllData().get(position).getVideo_id();
                String url = getVideoContentApi(videoId);
                showPlayingFragment(url);
            }
        });
    }

    @Override
    public void initData() {

    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new VideoArticleAdapter(activity);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                1, Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        recyclerView.setAdapter(adapter);

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        presenter.doLoadMoreData();
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    ToastUtils.showRoundRectToast("网络不可用");
                }
            }

            @Override
            public void onMoreClick() {

            }
        });

        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, new OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtils.showRoundRectToast("网络不可用");
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtils.showRoundRectToast("网络不可用");
                }
            }
        });

        //设置错误
        adapter.setError(R.layout.view_recycle_error, new OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });
    }

    @Override
    public void showRecyclerView() {
        recyclerView.showRecycler();
    }

    @Override
    public void setDataView(List<MultiNewsArticleDataBean> dataList) {
        adapter.addAll(dataList);
        adapter.notifyDataSetChanged();
        canLoadMore = true;
        recyclerView.getRecyclerView().stopScroll();
    }

    @Override
    public void showErrorView() {
        recyclerView.showError();
    }


    private static String getVideoContentApi(String videoId) {
        String VIDEO_HOST = "https://ib.365yg.com";
        String VIDEO_URL = "/video/urls/v/1/toutiao/mp4/%s?r=%s";
        String r = getRandom();
        String s = String.format(VIDEO_URL, videoId, r);
        // 将/video/urls/v/1/toutiao/mp4/{videoId}?r={Math.random()} 进行crc32加密
        CRC32 crc32 = new CRC32();
        crc32.update(s.getBytes());
        String crcString = crc32.getValue() + "";
        String url = VIDEO_HOST + s + "&s=" + crcString;
        return url;
    }

    private static String getRandom() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }


    /**
     * 展示页面
     */
    private void showPlayingFragment(String url) {
        if (isPlayFragmentShow) {
            return;
        }
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (videoPlayerFragment == null) {
            videoPlayerFragment = VideoPlayerFragment.showFragment(url);
            ft.add(android.R.id.content, videoPlayerFragment);
        } else {
            ft.show(videoPlayerFragment);
            videoPlayerFragment.updateUrl(url);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }

    /**
     * 隐藏页面
     */
    private void hidePlayingFragment() {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(videoPlayerFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
        VideoPlayerManager.instance().releaseVideoPlayer();
    }

    public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {

        private int lastItemCount;

        public abstract void onLoadMore();

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            int itemCount;
            int lastPosition;
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                itemCount = layoutManager.getItemCount();
                lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
            } else {
                LogUtils.e("OnLoadMoreListener", "The OnLoadMoreListener only support LinearLayoutManager");
                return;
            }
            if (lastItemCount != itemCount && lastPosition == itemCount - 1) {
                lastItemCount = itemCount;
                this.onLoadMore();
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(1)) {
                        this.onLoadMore();
                    }
                }
            }
        }

    }

}
