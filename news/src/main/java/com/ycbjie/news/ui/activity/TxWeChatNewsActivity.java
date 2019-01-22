package com.ycbjie.news.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.web.view.WebViewQQActivity;
import com.ycbjie.news.R;
import com.ycbjie.news.contract.TxWeChatContract;
import com.ycbjie.news.model.WeChatBean;
import com.ycbjie.news.presenter.TxWeChatPresenter;
import com.ycbjie.news.ui.adapter.TxWeChatAdapter;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import java.util.List;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : 微信新闻
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_NEW_WX_ACTIVITY)
public class TxWeChatNewsActivity extends BaseActivity implements View.OnClickListener ,TxWeChatContract.View{


    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private YCRefreshView recyclerView;
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
        return R.layout.base_easy_recycle_bar;
    }

    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recyclerView);
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
                            .setId(adapter.getAllData().get(position).getUrl())
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
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
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
                    ToastUtils.showToast("网络不可用");
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
                    ToastUtils.showToast("网络不可用");
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtils.showToast("网络不可用");
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
                    ToastUtils.showToast("网络不可用");
                }
            }
        });
    }


    @Override
    public void setErrorView() {
        recyclerView.setErrorView(R.layout.view_custom_data_error);
        recyclerView.showError();
        LinearLayout ll_error_view = recyclerView.findViewById(R.id.ll_error_view);
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
        LinearLayout ll_set_network = recyclerView.findViewById(R.id.ll_set_network);
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
