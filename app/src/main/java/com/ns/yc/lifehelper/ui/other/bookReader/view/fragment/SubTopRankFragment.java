package com.ns.yc.lifehelper.ui.other.bookReader.view.fragment;

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
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.SubTopRankAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.BooksByCats;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.SubHomeTopBean;
import com.ns.yc.lifehelper.ui.other.bookReader.model.BookReaderModel;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.ReadBookDetailActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.SubTopRankActivity;
import com.ns.yc.lifehelper.ui.weight.itemLine.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/19
 * 描    述：小说阅读器排行版
 * 修订历史：
 * ================================================
 */
public class SubTopRankFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;
    private SubTopRankActivity activity;
    private String mType;
    private SubTopRankAdapter adapter;

    public static SubTopRankFragment newInstance(String id) {
        SubTopRankFragment fragment = new SubTopRankFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SubTopRankActivity) context;
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
            mType = arguments.getString("id");
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
                if(adapter.getAllData().size()>position && position>=0){
                    Intent intent = new Intent(activity,ReadBookDetailActivity.class);
                    intent.putExtra("id",adapter.getAllData().get(position)._id);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {
        getData(mType);
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new SubTopRankAdapter(activity);
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        recyclerView.setRefreshing(false);
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        getData(mType);
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
                    getData(mType);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(activity, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getData(String mType) {
        BookReaderModel model = BookReaderModel.getInstance(activity);
        model.getRanking(mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SubHomeTopBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SubHomeTopBean subHomeTopBean) {
                        if(subHomeTopBean!=null){
                            List<SubHomeTopBean.RankingBean.BooksBean> books = subHomeTopBean.getRanking().getBooks();

                            BooksByCats cats = new BooksByCats();
                            cats.books = new ArrayList<>();
                            for (SubHomeTopBean.RankingBean.BooksBean bean : books) {
                                cats.books.add(new BooksByCats.BooksBean(bean.get_id(), bean.getCover(), bean.getTitle(),
                                        bean.getAuthor(), bean.cat, bean.getShortIntro(), bean.getLatelyFollower(), bean.getRetentionRatio()));
                            }

                            adapter.clear();
                            adapter.addAll(cats.books);
                        }
                    }
                });
    }


}
