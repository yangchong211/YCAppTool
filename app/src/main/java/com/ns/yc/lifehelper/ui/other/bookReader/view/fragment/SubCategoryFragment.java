package com.ns.yc.lifehelper.ui.other.bookReader.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.SubCategoryAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderCategoryList;
import com.ns.yc.lifehelper.ui.other.bookReader.model.BookReaderModel;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.ReadBookDetailActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.SubCategoryListActivity;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.RxUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：22017/9/21
 * 描    述：小说阅读器分类list页面
 * 修订历史：
 * ================================================
 */

public class SubCategoryFragment extends BaseFragment {


    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private SubCategoryListActivity activity;
    private String name = "";
    private String minor = "";
    private String gender = "";
    private String type = "";
    private SubCategoryAdapter adapter;

    protected int start = 0;
    protected int limit = 20;

    public static SubCategoryFragment newInstance(String name, String minor, String gender, @Constant.CateType String type) {
        SubCategoryFragment fragment = new SubCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("minor", minor);
        bundle.putString("gender", gender);
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SubCategoryListActivity) context;
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            name = arguments.getString("name");
            minor = arguments.getString("minor");
            gender = arguments.getString("gender");
            type = arguments.getString("type");
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
                    Intent intent = new Intent(activity,ReadBookDetailActivity.class);
                    intent.putExtra("id",adapter.getAllData().get(position).get_id());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        getCategoryData(gender, name, minor, type, start, limit);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new SubCategoryAdapter(activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshing(false);
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        start = adapter.getAllData().size() + limit;
                        getCategoryData(gender, name, minor, type, start, limit);
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
                    getCategoryData(gender, name, minor, type, start, limit);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getCategoryData(String gender, String name, String minor, String type, final int start, int limit) {
        String key = AppUtil.createCacheKey("category-list", gender, type, name, minor, start, limit);
        BookReaderModel model = BookReaderModel.getInstance(activity);
        Observable<ReaderCategoryList> fromNetWork = model.getBooksByCats(gender, type, name, minor, start, limit)
                .compose(RxUtil.<ReaderCategoryList>rxCacheListHelper(key));
        Observable.concat(RxUtil.rxCreateDiskObservable(key, ReaderCategoryList.class), fromNetWork)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReaderCategoryList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.showError();
                    }

                    @Override
                    public void onNext(ReaderCategoryList booksByCats) {
                        if(booksByCats!=null){
                            if(start==0){
                                adapter.clear();
                                adapter.addAll(booksByCats.getBooks());
                                adapter.notifyDataSetChanged();
                                recyclerView.setRefreshing(false);
                            }else {
                                adapter.addAll(booksByCats.getBooks());
                                adapter.notifyDataSetChanged();
                            }
                            recyclerView.showRecycler();
                        }
                    }
                });
    }

}
