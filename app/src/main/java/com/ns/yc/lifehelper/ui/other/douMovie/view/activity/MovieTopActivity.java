package com.ns.yc.lifehelper.ui.other.douMovie.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.douMovie.view.adapter.MovieTopAdapter;
import com.ns.yc.lifehelper.ui.other.douMovie.bean.DouHotMovieBean;
import com.ns.yc.lifehelper.ui.other.douMovie.model.TopMovieModel;
import com.ns.yc.lifehelper.ui.weight.manager.FullyGridLayoutManager;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：豆瓣电影评分榜页面
 * 修订历史：
 * ================================================
 */
public class MovieTopActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private MovieTopAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_refresh_recycle_bar;
    }

    @Override
    public void initView() {
        initToolBar();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("豆瓣电影评分榜");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<DouHotMovieBean.SubjectsBean.CastsBean> casts = adapter.getAllData().get(position).getCasts();
                StringBuilder sb = new StringBuilder();
                for(int a=0 ; a<casts.size() ; a++){
                    sb.append(casts.get(a).getName());
                    sb.append("/");
                }

                Intent intent = new Intent(MovieTopActivity.this, MovieDetailActivity.class);
                intent.putExtra("image",adapter.getAllData().get(position).getImages().getLarge());
                intent.putExtra("title",adapter.getAllData().get(position).getTitle());
                intent.putExtra("directors",adapter.getAllData().get(position).getDirectors().get(0).getName());
                intent.putExtra("casts",sb.toString());
                intent.putExtra("genres",adapter.getAllData().get(position).getGenres().get(0));
                intent.putExtra("rating",adapter.getAllData().get(position).getRating().getAverage());
                intent.putExtra("year",adapter.getAllData().get(position).getYear());
                intent.putExtra("id",adapter.getAllData().get(position).getId());
                intent.putExtra("collect_count",String.valueOf(adapter.getAllData().get(position).getCollect_count()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        getTopMovieData(0,21);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }

    private void initRecycleView() {
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setLayoutManager(new FullyGridLayoutManager(this, 3));
        adapter = new MovieTopAdapter(MovieTopActivity.this);
        recyclerView.setAdapter(adapter);
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if(NetworkUtils.isConnected()){
                    if(adapter.getAllData().size()>0){
                        getTopMovieData(adapter.getAllData().size(),adapter.getAllData().size()+21);
                    } else {
                        adapter.pauseMore();
                    }
                }else {
                    adapter.pauseMore();
                    Toast.makeText(MovieTopActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MovieTopActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(MovieTopActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    getTopMovieData(0,21);
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(MovieTopActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void getTopMovieData(final int start , int count) {
        TopMovieModel model = TopMovieModel.getInstance(this);
        model.getTopMovie(start,count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DouHotMovieBean>() {
                    @Override
                    public void onCompleted() {
                        recyclerView.showRecycler();
                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.showError();
                        recyclerView.setErrorView(R.layout.view_custom_empty_data);
                    }

                    @Override
                    public void onNext(DouHotMovieBean hotMovieBean) {
                        if (adapter == null) {
                            adapter = new MovieTopAdapter(MovieTopActivity.this);
                        }

                        if(start==0){
                            if(hotMovieBean != null && hotMovieBean.getSubjects() != null && hotMovieBean.getSubjects().size() > 0){
                                adapter.clear();
                                adapter.addAll(hotMovieBean.getSubjects());
                                adapter.notifyDataSetChanged();
                            }else {
                                recyclerView.showEmpty();
                                recyclerView.setEmptyView(R.layout.view_custom_empty_data);
                            }
                        } else {
                            if(hotMovieBean != null && hotMovieBean.getSubjects() != null && hotMovieBean.getSubjects().size() > 0){
                                adapter.addAll(hotMovieBean.getSubjects());
                                adapter.notifyDataSetChanged();
                            }else {
                                adapter.stopMore();
                            }
                        }
                    }
                });
    }

}
