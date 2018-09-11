package com.ns.yc.lifehelper.ui.other.douban.douBook.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseLazyFragment;
import com.ns.yc.lifehelper.ui.other.douban.douBook.contract.DouBookContract;
import com.ns.yc.lifehelper.ui.other.douban.douBook.presenter.DouBookPresenter;
import com.ns.yc.lifehelper.ui.other.douban.douBook.view.DouBookActivity;
import com.ns.yc.lifehelper.ui.other.douban.douBook.view.activity.DouBookDetailActivity;
import com.ns.yc.lifehelper.ui.other.douban.douBook.view.adapter.DouBookAdapter;
import com.ns.yc.lifehelper.ui.other.douban.douBook.model.DouBookBean;
import com.ns.yc.lifehelper.api.http.douban.DouBookModel;
import com.ns.yc.lifehelper.weight.manager.FullyGridLayoutManager;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：豆瓣读书页面
 * 修订历史：
 * ================================================
 */
public class DouBookFragment extends BaseLazyFragment implements DouBookContract.View{


    private static final String TYPE = "DouBookFragment";
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private DouBookActivity activity;
    private String mType;
    private DouBookAdapter adapter;

    private DouBookContract.Presenter presenter = new DouBookPresenter(this);

    public static DouBookFragment newInstance(String param1) {
        DouBookFragment fragment = new DouBookFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (DouBookActivity) context;
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
            Bundle arguments = getArguments();
            mType = arguments.getString(TYPE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.subscribe();
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
                Intent intent = new Intent(activity, DouBookDetailActivity.class);
                intent.putExtra("title",adapter.getAllData().get(position).getTitle());
                intent.putExtra("author",adapter.getAllData().get(position).getAuthor().get(0));
                intent.putExtra("image",adapter.getAllData().get(position).getImage());
                intent.putExtra("average",adapter.getAllData().get(position).getRating().getAverage());
                intent.putExtra("numRaters",String.valueOf(adapter.getAllData().get(position).getRating().getNumRaters()));
                intent.putExtra("pubdate",adapter.getAllData().get(position).getPubdate());
                intent.putExtra("publisher",adapter.getAllData().get(position).getPublisher());
                intent.putExtra("id",adapter.getAllData().get(position).getId());
                intent.putExtra("alt",adapter.getAllData().get(position).getAlt());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onLazyLoad() {
        recyclerView.showProgress();
        getTopMovieData(mType , 0 , 30);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new FullyGridLayoutManager(activity, 3));
        adapter = new DouBookAdapter(activity);
        recyclerView.setAdapter(adapter);
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        getTopMovieData(mType, adapter.getAllData().size(), adapter.getAllData().size() + 21);
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
                    getTopMovieData(mType , 0 , 30);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getTopMovieData(String mType , final int start, int count) {
        DouBookModel model = DouBookModel.getInstance();
        model.getHotMovie(mType,start,count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DouBookBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.showError();
                        recyclerView.setErrorView(R.layout.view_custom_empty_data);
                    }

                    @Override
                    public void onNext(DouBookBean bookBean) {
                        if(adapter==null){
                            adapter = new DouBookAdapter(activity);
                        }

                        if(start==0){
                            if(bookBean!=null && bookBean.getBooks()!=null && bookBean.getBooks().size()>0){
                                adapter.clear();
                                adapter.addAll(bookBean.getBooks());
                                adapter.notifyDataSetChanged();
                            } else {
                                recyclerView.showEmpty();
                                recyclerView.setEmptyView(R.layout.view_custom_empty_data);
                            }
                        } else {
                            if(bookBean!=null && bookBean.getBooks()!=null && bookBean.getBooks().size()>0){
                                adapter.addAll(bookBean.getBooks());
                                adapter.notifyDataSetChanged();
                            } else {
                                adapter.stopMore();
                            }
                        }
                    }
                });
    }


    private void getData(){
        Observable.interval(2,1, TimeUnit.SECONDS)
                .doOnNext(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {

                    }
                }).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {

            }
        });
    }


}
