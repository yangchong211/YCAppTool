package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.http.bookReader.BookReaderModel;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderSubjectBean;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.ReaderSubjectAdapter;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.rx.RxUtil;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/18
 * 描    述：小说阅读器主题书单
 * 修订历史：
 * ================================================
 */
public class ReaderSubjectFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private ReaderSubjectActivity activity;
    private int tab;

    public String duration = "";
    public String sort = "";
    private ReaderSubjectAdapter adapter;
    private String currendTag;
    private int start = 0;
    private int limit = 10;

    public static ReaderSubjectFragment newInstance(String tag, int tab) {
        ReaderSubjectFragment fragment = new ReaderSubjectFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        args.putInt("tab", tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ReaderSubjectActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public void onDestroyView() {
        //EventBus.getDefault().unregister(this);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            tab = arguments.getInt("tab");
            switch (tab) {
                case 0:
                    duration = "last-seven-days";
                    sort = "collectorCount";
                    break;
                case 1:
                    duration = "all";
                    sort = "created";
                    break;
                case 2:
                default:
                    duration = "all";
                    sort = "collectorCount";
                    break;
            }
        }
    }


    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView() {
        //EventBus.getDefault().register(this);
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(adapter.getAllData().size()>position && position>=0){
                    ReaderSubjectBean.BookListsBean bookListsBean = adapter.getAllData().get(position);
                    Intent intent = new Intent(activity,ReadBookDetailSubjectActivity.class);
                    intent.putExtra("bookListsBean",bookListsBean);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        getData();
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ReaderSubjectAdapter(activity);
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), activity.getResources().getColor(R.color.grayLine));
        recyclerView.addItemDecoration(line);
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        start = start + 10;
                        getData();
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
                    getData();
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void getData() {
        String sex = "male";
        getBookLists(duration, sort, start, limit, currendTag, sex);
    }

    /**
     * 访问网络
     * @param duration
     * @param sort
     * @param start
     * @param limit
     * @param currendTag
     * @param sex
     */
    private void getBookLists(String duration, String sort, final int start, int limit, String currendTag, String sex) {
        String key = AppUtil.createCacheKey("book-lists", duration, sort, start + "", limit + "", currendTag, sex);
        BookReaderModel model = BookReaderModel.getInstance(activity);
        Observable<ReaderSubjectBean> bookLists = model.getBookLists(duration, sort, start + "", limit + "", currendTag, sex);
        Observable<ReaderSubjectBean> compose = bookLists.compose(RxUtil.<ReaderSubjectBean>rxCacheListHelper(key));
        Observable.concat(RxUtil.rxCreateDiskObservable(key, ReaderSubjectBean.class), compose)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReaderSubjectBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.showError();
                    }

                    @Override
                    public void onNext(ReaderSubjectBean readerSubjectBean) {
                        if(readerSubjectBean!=null){
                            if(start==0){
                                recyclerView.showProgress();
                                adapter.clear();
                                adapter.addAll(readerSubjectBean.getBookLists());
                                adapter.notifyDataSetChanged();
                                recyclerView.setRefreshing(false);
                            }else {
                                recyclerView.showProgress();
                                adapter.addAll(readerSubjectBean.getBookLists());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

}
