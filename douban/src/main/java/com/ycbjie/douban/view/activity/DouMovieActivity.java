package com.ycbjie.douban.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.douban.R;
import com.ycbjie.douban.api.DouBanModel;
import com.ycbjie.douban.bean.DouHotMovieBean;
import com.ycbjie.douban.view.adapter.DouMovieAdapter;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

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
 *     desc  : 豆瓣电影页面
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_DOU_MOVIE_ACTIVITY)
public class DouMovieActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    YCRefreshView recyclerView;
    private DouMovieAdapter adapter;

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
        ARouterUtils.injectActivity(this);
        initFindViewById();
        initToolBar();
        initRecycleView();
    }

    private void initFindViewById() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initToolBar() {
        toolbarTitle.setText("豆瓣电影");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        adapter.setOnItemClickListener(position -> {
            List<DouHotMovieBean.SubjectsBean.CastsBean> casts = adapter.getAllData().get(position).getCasts();
            StringBuilder sb = new StringBuilder();
            for(int a=0 ; a<casts.size() ; a++){
                sb.append(casts.get(a).getName());
                sb.append("/");
            }
            Intent intent = new Intent(DouMovieActivity.this, MovieDetailActivity.class);
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
        });
    }


    @Override
    public void initData() {
        recyclerView.showProgress();
        getHotMovieData();
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        }
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(DouMovieActivity.this));
        final RecycleViewItemLine line = new RecycleViewItemLine(DouMovieActivity.this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        adapter = new DouMovieAdapter(DouMovieActivity.this);
        recyclerView.setAdapter(adapter);
        AddHeader();
        //刷新
        recyclerView.setRefreshListener(() -> {
            if (NetworkUtils.isConnected()) {
                if (adapter.getAllData().size() > 0) {
                    getHotMovieData();
                } else {
                    recyclerView.setRefreshing(false);
                }
            } else {
                recyclerView.setRefreshing(false);
                ToastUtils.showRoundRectToast("网络不可用");
            }
        });
    }


    private void AddHeader() {
        adapter.removeAllHeader();
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view = LayoutInflater.from(DouMovieActivity.this).inflate(
                        R.layout.head_dou_movie, parent, false);
                LinearLayout ll_movie_top = view.findViewById(R.id.ll_movie_top);
                ImageView iv_img = view.findViewById(R.id.iv_img);
                ImageUtils.loadImgByPicasso(DouMovieActivity.this, R.drawable.image_default,iv_img);
                ll_movie_top.setOnClickListener(view1 -> {
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_DOU_TOP_ACTIVITY);
                    //startActivity(MovieTopActivity.class);
                });
                iv_img.setOnClickListener(v -> {
                    //使用路由跳转到另一个模块中的页面
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_VIDEO_VIDEO);
                });
                return view;
            }

            @Override
            public void onBindView(View headerView) {

            }
        });
    }


    private void getHotMovieData() {
        DouBanModel model = DouBanModel.getInstance();
        model.getHotMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DouHotMovieBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DouHotMovieBean hotMovieBean) {
                        if (adapter == null) {
                            adapter = new DouMovieAdapter(DouMovieActivity.this);
                        }

                        if (hotMovieBean != null && hotMovieBean.getSubjects() != null && hotMovieBean.getSubjects().size() > 0) {
                            adapter.clear();
                            adapter.addAll(hotMovieBean.getSubjects());
                            adapter.notifyDataSetChanged();
                            recyclerView.showRecycler();
                        } else {
                            recyclerView.showEmpty();
                            recyclerView.setEmptyView(R.layout.view_custom_empty_data);
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

                    }
                });
    }


}
