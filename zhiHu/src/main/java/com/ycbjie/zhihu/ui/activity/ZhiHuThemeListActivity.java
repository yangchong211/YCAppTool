package com.ycbjie.zhihu.ui.activity;

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
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.zhihu.R;
import com.ycbjie.zhihu.contract.ZhiHuThemeListContract;
import com.ycbjie.zhihu.model.ZhiHuThemeChildBean;
import com.ycbjie.zhihu.presenter.ZhiHuThemeListPresenter;
import com.ycbjie.zhihu.ui.adapter.ZhiHuThemeListAdapter;
import com.ycbjie.zhihu.web.activity.WebViewAnimActivity;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/04/21
 *     desc  : 知乎日报模块        主题
 *     revise:
 * </pre>
 */
public class ZhiHuThemeListActivity extends BaseActivity implements View.OnClickListener ,
        ZhiHuThemeListContract.View{

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    YCRefreshView recyclerView;
    private ZhiHuThemeListAdapter adapter;
    private int id;

    private ZhiHuThemeListContract.Presenter presenter = new ZhiHuThemeListPresenter(this);

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
        return R.layout.base_easy_recycle_bar;
    }


    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recyclerView);
        initToolBar();
        initIntentData();
        initRecycleView();
    }


    private void initToolBar() {
        toolbarTitle.setText("知乎主题");
    }


    private void initIntentData() {
        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
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
                    intent.setClass(ZhiHuThemeListActivity.this, WebViewAnimActivity.class);
                    intent.putExtra("id", adapter.getAllData().get(position).getId());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
                                (ZhiHuThemeListActivity.this).toBundle());
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
        presenter.getThemeChildData(id);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();

        }
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ZhiHuThemeListAdapter(this);
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#e5e5e5"));
        recyclerView.addItemDecoration(line);
        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    presenter.getThemeChildData(id);
                } else {
                    recyclerView.setRefreshing(false);
                    ToastUtils.showShort("网络不可用");
                }
            }
        });
    }


    @Override
    public void setView(ZhiHuThemeChildBean zhiHuThemeChildBean) {
        if(adapter!=null){
            adapter.clear();
        }else {
            adapter = new ZhiHuThemeListAdapter(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.addAll(zhiHuThemeChildBean.getStories());
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
