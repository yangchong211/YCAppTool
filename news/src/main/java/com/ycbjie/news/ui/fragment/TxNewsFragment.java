package com.ycbjie.news.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseFragment;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.news.R;
import com.ycbjie.news.api.ConstantTxApi;
import com.ycbjie.news.api.TxNewsModel;
import com.ycbjie.news.model.TxNewsBean;
import com.ycbjie.news.ui.activity.TxNewsActivity;
import com.ycbjie.news.ui.adapter.TxNewsAdapter;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/21
 *     desc  : 天行新闻页面
 *     revise:
 * </pre>
 */
public class TxNewsFragment extends BaseFragment {


    private static final String TYPE = "TxNewsFragment";
    private YCRefreshView recyclerView;
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
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position>-1 && adapter.getAllData().size()>position){
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(Constant.URL,adapter.getAllData().get(position).getUrl());
                    bundle1.putString(Constant.TITLE,adapter.getAllData().get(position).getDescription());
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle1);
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
                    .subscribe(new Observer<TxNewsBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

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
                                    int code = ExceptionUtils.CODE_SHOW_TOAST;
                                    ExceptionUtils.serviceException(code);
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

                        @Override
                        public void onError(Throwable e) {
                            if(recyclerView!=null){
                                recyclerView.showError();
                                recyclerView.setErrorView(R.layout.view_custom_empty_data);
                            }
                            ExceptionUtils.handleException(e);
                        }

                        @Override
                        public void onComplete() {
                            if(recyclerView!=null){
                                recyclerView.showRecycler();
                            }
                        }
                    });
        }
    }

}
