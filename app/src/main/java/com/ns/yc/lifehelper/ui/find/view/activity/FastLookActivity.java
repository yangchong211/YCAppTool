package com.ns.yc.lifehelper.ui.find.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.find.contract.FastLookContract;
import com.ns.yc.lifehelper.ui.find.presenter.FastLookPresenter;
import com.ns.yc.lifehelper.ui.find.view.adapter.FastLookAdapter;
import com.ns.yc.lifehelper.ui.other.myNews.wxNews.model.bean.WxNewsDetailBean;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;
import com.pedaily.yc.ycdialoglib.toast.CustomToast;

import java.util.ArrayList;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/14
 * 描    述：快看页面
 * 修订历史：
 * ================================================
 */
public class FastLookActivity extends BaseActivity implements FastLookContract.View, View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;

    private FastLookAdapter adapter;
    private FastLookContract.Presenter presenter = new FastLookPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle_list;
    }


    @Override
    public void initView() {
        initToolBar();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("快速阅览");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }


    @Override
    public void initData() {
        presenter.readCache();
        recyclerView.showProgress();
        presenter.getData(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FastLookAdapter(this);
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        recyclerView.setAdapter(adapter);

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        presenter.getData(true);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    CustomToast.normal(FastLookActivity.this,"网络不可用").show();
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
                    CustomToast.normal(FastLookActivity.this,"网络不可用").show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    CustomToast.normal(FastLookActivity.this,"网络不可用").show();
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
                    presenter.getData(false);
                } else {
                    recyclerView.setRefreshing(false);
                    CustomToast.normal(FastLookActivity.this,"网络不可用").show();
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    @Override
    public void setRefreshData(WxNewsDetailBean wxNewsDetailBean) {
        if(wxNewsDetailBean.getResult()!=null && wxNewsDetailBean.getResult().getList()!=null
                && wxNewsDetailBean.getResult().getList().size()>0){
            adapter.clear();
            adapter.addAll(wxNewsDetailBean.getResult().getList());
            adapter.notifyDataSetChanged();
            recyclerView.showRecycler();
            presenter.cacheData(wxNewsDetailBean.getResult().getList());
        } else {
            recyclerView.showEmpty();
            recyclerView.setEmptyView(R.layout.view_custom_empty_data);
        }
    }

    /**
     * 加载更多数据
     */
    @Override
    public void setLoadMore(WxNewsDetailBean wxNewsDetailBean) {
        if(wxNewsDetailBean.getResult()!=null && wxNewsDetailBean.getResult().getList()!=null
                && wxNewsDetailBean.getResult().getList().size()>0){
            adapter.addAll(wxNewsDetailBean.getResult().getList());
            adapter.notifyDataSetChanged();
        } else {
            adapter.stopMore();
        }
    }

    /**
     * 加载错误
     */
    @Override
    public void setError() {
        recyclerView.showError();
        recyclerView.setErrorView(R.layout.view_custom_data_error);
    }

    /**
     * 读取缓存，刷新数据
     */
    @Override
    public void readCacheData(ArrayList<WxNewsDetailBean.ResultBean.ListBean> list) {
        if(adapter!=null){
            adapter.clear();
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
            recyclerView.setRefreshing(false);
        }
    }


}
