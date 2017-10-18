package com.ns.yc.lifehelper.ui.other.bookReader.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantBookReader;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.event.BookReaderSelectionEvent;
import com.ns.yc.lifehelper.ui.other.bookReader.activity.BookReaderReviewActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.adapter.ReaderReviewAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderBookReviewList;
import com.ns.yc.lifehelper.ui.other.bookReader.model.BookReaderModel;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.EventBusUtils;
import com.ns.yc.lifehelper.utils.RxUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/27
 * 描    述：书评区
 * 修订历史：
 * ================================================
 */
public class BookReaderReviewFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private BookReaderReviewActivity activity;
    private static final String TYPE = "type";
    private String mType;
    private String sort = ConstantBookReader.SortType.DEFAULT;
    private String type = ConstantBookReader.BookType.ALL;
    private String distillate = ConstantBookReader.Distillate.ALL;


    private ReaderReviewAdapter adapter;
    protected int start = 0;
    protected int limit = 20;

    public static BookReaderReviewFragment newInstance(String param) {
        BookReaderReviewFragment fragment = new BookReaderReviewFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param);
        fragment.setArguments(args);
        return fragment;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BookReaderReviewActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBusUtils.unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initCategoryList(BookReaderSelectionEvent event) {
        recyclerView.setRefreshing(true);
        sort = event.sort;
        type = event.type;
        distillate = event.distillate;
        start = 0;
        getData(sort, type, distillate, start, limit);
    }


    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView() {
        //注意此处不要传递activity，否则崩溃
        EventBusUtils.register(this);
        initRecycleView();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        recyclerView.showProgress();
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ReaderReviewAdapter(activity);
        recyclerView.setAdapter(adapter);
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        start = start + adapter.getAllData().size();
                        getData(sort, type, distillate, start, limit);
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
                    start = 0;
                    getData(sort, type, distillate, start, limit);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getData(final String sort, final String type, final String distillate, final int start, final int limit) {
        String key = AppUtil.createCacheKey("book-review-list", sort, type, distillate, start, limit);
        BookReaderModel model = BookReaderModel.getInstance(activity);
        Observable<ReaderBookReviewList> fromNetWork = model.getBookReviewList("all", sort, type, start + "", limit + "", distillate)
                .compose(RxUtil.<ReaderBookReviewList>rxCacheListHelper(key));
        Observable observable = RxUtil.rxCreateDiskObservable(key, ReaderBookReviewList.class);

        //依次检查disk、network
        Subscription rxSubscription = Observable.concat(observable, fromNetWork)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReaderBookReviewList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ReaderBookReviewList list) {
                        boolean isRefresh = start == 0 ? true : false;
                        if(isRefresh){
                            recyclerView.setRefreshing(true);
                        }else {
                            recyclerView.setRefreshing(false);
                        }

                        if(adapter==null){
                            adapter = new ReaderReviewAdapter(activity);
                        }

                        if(start==0){
                            if(list.isOk()){
                                if(list.getReviews()!=null && list.getReviews().size()>0){
                                    recyclerView.showRecycler();
                                    adapter.clear();
                                    adapter.addAll(list.getReviews());
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setRefreshing(false);
                                }else {
                                    recyclerView.setRefreshing(false);
                                }
                            }
                        }else {
                            if(list.isOk()){
                                if(list.getReviews()!=null && list.getReviews().size()>0){
                                    recyclerView.showRecycler();
                                    adapter.addAll(list.getReviews());
                                    adapter.notifyDataSetChanged();
                                }else {
                                    adapter.stopMore();
                                }
                            }
                        }
                    }
                });
    }

}
