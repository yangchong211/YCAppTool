package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.SubHomeTopAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.BooksByCats;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.SubHomeTopBean;
import com.ns.yc.lifehelper.api.http.bookReader.BookReaderModel;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
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
public class SubHomeTopRankActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private String id = "";
    private String title = "";
    private SubHomeTopAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle_bar;
    }

    @Override
    public void initView() {
        initToolBar();
        initIntentData();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("排行榜");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            id = intent.getStringExtra("id");
            String str = intent.getStringExtra("title");
            title = str.split(" ")[0];
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SubHomeTopRankActivity.this,ReadBookDetailActivity.class);
                intent.putExtra("id",adapter.getAllData().get(position)._id);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        getData(id);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(SubHomeTopRankActivity.this));
        adapter = new SubHomeTopAdapter(SubHomeTopRankActivity.this);
        recyclerView.setAdapter(adapter);
        final RecycleViewItemLine line = new RecycleViewItemLine(SubHomeTopRankActivity.this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        //加载更多
        /*adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {

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
        });*/

        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(SubHomeTopRankActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(SubHomeTopRankActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    getData(id);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(SubHomeTopRankActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getData(String id) {
        BookReaderModel model = BookReaderModel.getInstance(SubHomeTopRankActivity.this);
        model.getRanking(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SubHomeTopBean>() {
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
