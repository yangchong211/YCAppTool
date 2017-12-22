package com.ns.yc.lifehelper.ui.other.myNews.weChat.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewQQActivity;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.contract.TxWeChatContract;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.model.bean.WeChatBean;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.presenter.TxWeChatPresenter;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.view.adapter.TxWeChatAdapter;
import com.pedaily.yc.ycdialoglib.toast.ToastUtil;

import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/4
 * 描    述：微信新闻
 * 修订历史：
 * ================================================
 */
public class TxWeChatNewsActivity extends BaseActivity implements
        View.OnClickListener ,TxWeChatContract.View{


    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private TxWeChatAdapter adapter;

    private int num = 10;
    private int page = 1;

    private TxWeChatContract.Presenter presenter = new TxWeChatPresenter(this);

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
        toolbarTitle.setText("微信精选新闻");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(int position) {
                if(position>-1 && adapter.getAllData().size()>position){
                    WebViewQQActivity.toWhere(new WebViewQQActivity.Builder()
                            .setContext(TxWeChatNewsActivity.this)
                            .setId(adapter.getAllData().get(position).getUrl())             //wechat API 没有id，用图片来做唯一数据库索引
                            .setImgUrl(adapter.getAllData().get(position).getPicUrl())
                            .setTitle(adapter.getAllData().get(position).getTitle())
                            .setUrl(adapter.getAllData().get(position).getUrl())
                            .setType(Constant.LikeType.TYPE_WE_CHAT));
                }
            }
        });
    }


    @Override
    public void initData() {
        recyclerView.showProgress();
        page = 1;
        presenter.getNews(num,page);
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
        adapter = new TxWeChatAdapter(this);
        recyclerView.setAdapter(adapter);

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        page++;
                        presenter.getNews(num,page);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    ToastUtil.showToast(TxWeChatNewsActivity.this,"网络不可用");
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
                    ToastUtil.showToast(TxWeChatNewsActivity.this,"网络不可用");
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtil.showToast(TxWeChatNewsActivity.this,"网络不可用");
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
                    page = 1;
                    presenter.getNews(num,page);
                } else {
                    recyclerView.setRefreshing(false);
                    ToastUtil.showToast(TxWeChatNewsActivity.this,"网络不可用");
                }
            }
        });
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


    @Override
    public void setView(List<WeChatBean.NewslistBean> newslist) {
        if(adapter!=null){
            adapter.clear();
        }else {
            adapter = new TxWeChatAdapter(this);
        }
        adapter.addAll(newslist);
        adapter.notifyDataSetChanged();
        recyclerView.showRecycler();
    }


    @Override
    public void setEmptyView() {
        recyclerView.setEmptyView(R.layout.view_custom_empty_data);
        recyclerView.showEmpty();
    }


    @Override
    public void setViewMore(List<WeChatBean.NewslistBean> newslist) {
        adapter.addAll(newslist);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void stopMore() {
        adapter.stopMore();
    }


}
