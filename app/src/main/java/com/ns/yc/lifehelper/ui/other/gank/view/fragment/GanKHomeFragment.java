package com.ns.yc.lifehelper.ui.other.gank.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.base.mvp1.BaseLazyFragment;
import com.ns.yc.lifehelper.ui.other.gank.bean.CategoryResult;
import com.ns.yc.lifehelper.ui.other.gank.bean.GanKFavorite;
import com.ns.yc.lifehelper.ui.other.gank.contract.GanKHomeFContract;
import com.ns.yc.lifehelper.ui.other.gank.presenter.GanKHomeFPresenter;
import com.ns.yc.lifehelper.ui.other.gank.view.activity.GanKHomeActivity;
import com.ns.yc.lifehelper.ui.other.gank.view.activity.GanKWebActivity;
import com.ns.yc.lifehelper.ui.other.gank.view.adapter.GanKHomeAdapter;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营
 * 修订历史：
 * ================================================
 */
public class GanKHomeFragment extends BaseLazyFragment implements GanKHomeFContract.View {

    private static final String TYPE = "type";
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private GanKHomeActivity activity;
    private String mType;
    private GanKHomeAdapter adapter;
    private GanKHomeFContract.Presenter presenter = new GanKHomeFPresenter(this);

    public static GanKHomeFragment getInstance(String param) {
        GanKHomeFragment fragment = new GanKHomeFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (GanKHomeActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView() {
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(adapter.getAllData().size()>position && position>=0){
                    CategoryResult.ResultsBean mData = adapter.getAllData().get(position);
                    Intent intent = new Intent(activity, GanKWebActivity.class);
                    intent.putExtra("url",mData.url);
                    intent.putExtra("title",mData.desc);

                    GanKFavorite favorite = new GanKFavorite();
                    favorite.setAuthor(mData.who);
                    favorite.setData(mData.publishedAt);
                    favorite.setTitle(mData.desc);
                    favorite.setType(mData.type);
                    favorite.setUrl(mData.url);
                    favorite.setGankID(mData._id);
                    intent.putExtra("favorite",favorite);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void initData() {

    }


    @Override
    public void onLazyLoad() {
        showSwipeLoading();
        presenter.getData(true);
    }


    /**
     * 开始刷新，loading
     */
    @Override
    public void showSwipeLoading() {
        recyclerView.showProgress();
    }

    /**
     * 隐藏刷新，loading
     */
    @Override
    public void hideSwipeLoading() {
        recyclerView.showRecycler();
    }

    /**
     * 没有数据
     */
    @Override
    public void showNoData() {
        recyclerView.showEmpty();
    }

    /**
     * 网络错误
     */
    @Override
    public void showNetError() {
        recyclerView.showError();
    }

    /**
     * 获取类型
     */
    @Override
    public String getDataType(){
        return this.mType;
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshData(CategoryResult categoryResult) {
        adapter.clear();
        adapter.addAll(categoryResult.results);
        adapter.notifyDataSetChanged();
    }

    /**
     * 加载更多数据
     */
    @Override
    public void moreData(CategoryResult categoryResult) {
        adapter.addAll(categoryResult.results);
        adapter.notifyDataSetChanged();
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new GanKHomeAdapter(activity);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        recyclerView.setAdapter(adapter);

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        presenter.getData(false);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    ToastUtil.showToast(activity,"网络不可用");
                }
            }

            @Override
            public void onMoreClick() {

            }
        });

        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtil.showToast(activity,"网络不可用");
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtil.showToast(activity,"网络不可用");
                }
            }
        });

        //设置错误
        adapter.setError(R.layout.view_recycle_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });

        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    presenter.getData(true);
                } else {
                    recyclerView.setRefreshing(false);
                    ToastUtil.showToast(activity,"网络不可用");
                }
            }
        });
    }


}
