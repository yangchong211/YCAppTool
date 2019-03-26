package com.ycbjie.douban.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.douban.R;
import com.ycbjie.douban.api.DouBanModel;
import com.ycbjie.douban.bean.DouHotMovieBean;
import com.ycbjie.douban.view.adapter.MovieTopAdapter;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.weight.FullyGridLayoutManager;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣电影评分榜页面
 *     revise:
 * </pre>
 */

@Route(path = ARouterConstant.ACTIVITY_DOU_TOP_ACTIVITY)
public class MovieTopActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    YCRefreshView recyclerView;
    private MovieTopAdapter adapter;

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
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recyclerView);
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
        int i = view.getId();
        if (i == R.id.ll_title_menu) {
            finish();

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
        DouBanModel model = DouBanModel.getInstance();
        model.getTopMovie(start,count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DouHotMovieBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

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

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.showError();
                        recyclerView.setErrorView(R.layout.view_custom_empty_data);
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {
                        recyclerView.showRecycler();
                    }
                });
    }

}
