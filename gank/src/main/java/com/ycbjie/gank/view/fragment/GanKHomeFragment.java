package com.ycbjie.gank.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.gank.R;
import com.ycbjie.gank.bean.bean.CategoryResult;
import com.ycbjie.gank.contract.GanKHomeFContract;
import com.ycbjie.gank.presenter.GanKHomeFPresenter;
import com.ycbjie.gank.view.activity.GanKHomeActivity;
import com.ycbjie.gank.view.adapter.GanKHomeAdapter;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseLazyFragment;
import com.ycbjie.library.constant.Constant;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/14
 *     desc  : 干货集中营详情页面
 *     revise:
 * </pre>
 */
public class GanKHomeFragment extends BaseLazyFragment implements GanKHomeFContract.View {

    private static final String TYPE = "type";
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
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(position -> {
            if(adapter.getAllData().size()>position && position>=0){
                CategoryResult.ResultsBean mData = adapter.getAllData().get(position);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.URL,mData.url);
                bundle.putString(Constant.TITLE,mData.desc);
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle);
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
                    ToastUtils.showRoundRectToast("网络不可用");
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
        recyclerView.setRefreshListener(() -> {
            if (NetworkUtils.isConnected()) {
                presenter.getData(true);
            } else {
                recyclerView.setRefreshing(false);
                ToastUtils.showRoundRectToast("网络不可用");
            }
        });
    }


}
