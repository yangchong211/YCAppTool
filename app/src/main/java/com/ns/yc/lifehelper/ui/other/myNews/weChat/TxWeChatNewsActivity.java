package com.ns.yc.lifehelper.ui.other.myNews.weChat;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.ConstantTxApi;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewQQActivity;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.adapter.TxWeChatAdapter;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.bean.WeChatBean;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.model.WeChatModel;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/4
 * 描    述：微信新闻
 * 修订历史：
 * ================================================
 */
public class TxWeChatNewsActivity extends BaseActivity implements View.OnClickListener {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        getNews(num,page);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(TxWeChatNewsActivity.this));
        adapter = new TxWeChatAdapter(TxWeChatNewsActivity.this);
        recyclerView.setAdapter(adapter);

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        page++;
                        getNews(num,page);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    Toast.makeText(TxWeChatNewsActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TxWeChatNewsActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(TxWeChatNewsActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    getNews(num,page);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(TxWeChatNewsActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getNews(int num, final int page) {
        WeChatModel model = WeChatModel.getInstance(TxWeChatNewsActivity.this);
        model.getTxNews(ConstantTxApi.TX_KEY,num,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeChatBean>() {
                    @Override
                    public void onCompleted() {
                        if(recyclerView!=null){
                            recyclerView.showRecycler();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(recyclerView!=null){
                            recyclerView.showError();
                            recyclerView.setErrorView(R.layout.view_custom_empty_data);
                        }
                    }

                    @Override
                    public void onNext(WeChatBean weChatBean) {
                        if(adapter==null){
                            adapter = new TxWeChatAdapter(TxWeChatNewsActivity.this);
                        }

                        if(page==1){
                            if(weChatBean!=null && weChatBean.getNewslist()!=null && weChatBean.getNewslist().size()>0){
                                adapter.clear();
                                adapter.addAll(weChatBean.getNewslist());
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.showEmpty();
                                recyclerView.setEmptyView(R.layout.view_custom_empty_data);
                            }
                        } else {
                            if(weChatBean!=null && weChatBean.getNewslist()!=null && weChatBean.getNewslist().size()>0){
                                adapter.addAll(weChatBean.getNewslist());
                                adapter.notifyDataSetChanged();
                            } else {
                                adapter.stopMore();
                            }
                        }
                    }
                });
    }

}
