package com.ns.yc.lifehelper.ui.other.myNews.txNews.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantTxApi;
import com.ns.yc.lifehelper.base.mvp.BaseFragment;
import com.ns.yc.lifehelper.ui.webView.view.WebViewActivity;
import com.ns.yc.lifehelper.ui.other.myNews.txNews.TxNewsActivity;
import com.ns.yc.lifehelper.ui.other.myNews.txNews.adapter.TxNewsAdapter;
import com.ns.yc.lifehelper.ui.other.myNews.txNews.bean.TxNewsBean;
import com.ns.yc.lifehelper.api.http.news.TxNewsModel;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：天行新闻页面
 * 修订历史：
 * ================================================
 */
public class TxNewsFragment extends BaseFragment {


    private static final String TYPE = "";
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private TxNewsActivity activity;
    private String mType;
    private int num = 10;
    private TxNewsAdapter adapter;

    public static TxNewsFragment newInstance(String param1) {
        TxNewsFragment fragment = new TxNewsFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (TxNewsActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(TYPE);
        }

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
                if(position>-1 && adapter.getAllData().size()>position){
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("url",adapter.getAllData().get(position).getUrl());
                    intent.putExtra("name",adapter.getAllData().get(position).getDescription());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        getTxNews(mType , num);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new TxNewsAdapter(activity);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(5), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        recyclerView.setAdapter(adapter);

        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        getTxNews(mType, adapter.getAllData().size() + num);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    getTxNews(mType , num);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getTxNews(String mType, final int num) {
        TxNewsModel model = TxNewsModel.getInstance(activity);
        Observable<TxNewsBean> txNews = null;
        if(mType.equals("social")){
            txNews = model.getTxNews(ConstantTxApi.TX_KEY, num);
        }else if(mType.equals("guonei")){
            txNews =model.getTxGnNews(ConstantTxApi.TX_KEY, num);
        }else if(mType.equals("huabian")){
            txNews =model.getTxHbNews(ConstantTxApi.TX_KEY, num);
        }else if(mType.equals("tiyu")){
            txNews =model.getTxTyNews(ConstantTxApi.TX_KEY, num);
        }else if(mType.equals("nba")){
            txNews =model.getTxNbaNews(ConstantTxApi.TX_KEY, num);
        }else if(mType.equals("startup")){
            txNews =model.getTxSuNews(ConstantTxApi.TX_KEY, num);
        }else if(mType.equals("military")){
            txNews =model.getTxMiNews(ConstantTxApi.TX_KEY, num);
        }else if(mType.equals("travel")){
            txNews =model.getTxTrNews(ConstantTxApi.TX_KEY, num);
        }else if(mType.equals("health")){
            txNews =model.getTxHeNews(ConstantTxApi.TX_KEY, num);
        }else if(mType.equals("qiwen")){
            txNews =model.getTxQwNews(ConstantTxApi.TX_KEY, num);
        }

        if(txNews!=null){
            txNews.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<TxNewsBean>() {
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
                        public void onNext(TxNewsBean txNewsBean) {
                            if(adapter==null){
                                adapter = new TxNewsAdapter(activity);
                            }

                            if(num==10){
                                if(txNewsBean!=null && txNewsBean.getNewslist()!=null && txNewsBean.getNewslist().size()>0){
                                    adapter.clear();
                                    adapter.addAll(txNewsBean.getNewslist());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    recyclerView.showEmpty();
                                    recyclerView.setEmptyView(R.layout.view_custom_empty_data);
                                }
                            } else {
                                if(txNewsBean!=null && txNewsBean.getNewslist()!=null && txNewsBean.getNewslist().size()>0){
                                    adapter.addAll(txNewsBean.getNewslist());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    adapter.stopMore();
                                }
                            }
                        }
                    });
        }
    }

}
