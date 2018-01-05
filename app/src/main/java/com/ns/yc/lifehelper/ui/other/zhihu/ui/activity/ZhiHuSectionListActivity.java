package com.ns.yc.lifehelper.ui.other.zhihu.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewAnimActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.contract.ZhiHuSectionListContract;
import com.ns.yc.lifehelper.ui.other.zhihu.model.bean.ZhiHuSectionChildBean;
import com.ns.yc.lifehelper.ui.other.zhihu.presenter.ZhiHuSectionListPresenter;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.adapter.ZhiHuSectionListAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/12/4
 * 描    述：知乎日报模块        主题
 * 修订历史：
 * ================================================
 */
public class ZhiHuSectionListActivity extends BaseActivity implements View.OnClickListener , ZhiHuSectionListContract.View{

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private ZhiHuSectionListAdapter adapter;
    private int id;

    private ZhiHuSectionListContract.Presenter presenter = new ZhiHuSectionListPresenter(this);
    private String title;

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
        return R.layout.base_refresh_recycle_bar;
    }

    @Override
    public void initView() {
        initToolBar();
        initIntentData();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("知乎专栏");
    }

    private void initIntentData() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(adapter.getAllData().size()>position && position>-1){
                    presenter.insertReadToDB(adapter.getAllData().get(position).getId());
                    adapter.setReadState(position, true);
                    adapter.notifyItemChanged(position);
                    Intent intent = new Intent();
                    intent.setClass(ZhiHuSectionListActivity.this, WebViewAnimActivity.class);
                    intent.putExtra("id", adapter.getAllData().get(position).getId());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                                ZhiHuSectionListActivity.this).toBundle());
                    }else {
                        startActivity(intent);
                    }
                }
            }
        });
    }


    @Override
    public void initData() {
        recyclerView.showProgress();
        presenter.getSectionChildData(id);
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
        adapter = new ZhiHuSectionListAdapter(this);
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    presenter.getSectionChildData(id);
                } else {
                    recyclerView.setRefreshing(false);
                    ToastUtils.showShort("网络不可用");
                }
            }
        });
    }


    @Override
    public void setView(ZhiHuSectionChildBean zhiHuSectionChildBean) {
        if(adapter!=null){
            adapter.clear();
        }else {
            adapter = new ZhiHuSectionListAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.addAll(zhiHuSectionChildBean.getStories());
        adapter.notifyDataSetChanged();
        recyclerView.showRecycler();
    }


    @Override
    public void setEmptyView() {
        recyclerView.setEmptyView(R.layout.view_custom_empty_data);
        recyclerView.showEmpty();
    }


    @Override
    public void setErrorView() {
        recyclerView.setErrorView(R.layout.view_custom_data_error);
        recyclerView.showError();
        LinearLayout ll_error_view = (LinearLayout) recyclerView.findViewById(R.id.ll_error_view);
        ll_error_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
    }


    @Override
    public void setNetworkErrorView() {
        recyclerView.setErrorView(R.layout.view_custom_network_error);
        recyclerView.showError();
        LinearLayout ll_set_network = (LinearLayout) recyclerView.findViewById(R.id.ll_set_network);
        ll_set_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NetworkUtils.isConnected()){
                    initData();
                }else {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            }
        });
    }


}
