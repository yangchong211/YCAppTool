package com.ycbjie.douban.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ycbjie.douban.R;
import com.ycbjie.douban.api.DouBanModel;
import com.ycbjie.douban.bean.DouMovieDetailBean;
import com.ycbjie.douban.view.adapter.MovieDetailAdapter;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.utils.image.ImageUtils;

import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;



/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣电影详情页面
 *     revise:
 * </pre>
 */
public class MovieDetailActivity extends BaseActivity {

    private TextView tvContentTitle;
    private TextView tvContentAbout;
    private RecyclerView recyclerView;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView ivBgImage;
    private ImageView ivHeaderPhoto;
    private TextView tvHeaderRatingRate;
    private TextView tvHeaderRatingNumber;
    private TextView tvHeaderDirectors;
    private TextView tvHeaderCasts;
    private TextView tvHeaderGenres;
    private TextView tvHeaderDay;
    private TextView tvHeaderCity;
    private Toolbar toolbar;
    private LinearLayout llToolBar;
    private TextView tvName;
    private TextView tvCasts;
    private String id = "";

    private int state;
    private String alt;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_movie_detail;
    }

    @Override
    public void initView() {


        tvContentTitle = (TextView) findViewById(R.id.tv_content_title);
        tvContentAbout = (TextView) findViewById(R.id.tv_content_about);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ivBgImage = (ImageView) findViewById(R.id.iv_bg_image);
        ivHeaderPhoto = (ImageView) findViewById(R.id.iv_header_photo);
        tvHeaderRatingRate = (TextView) findViewById(R.id.tv_header_rating_rate);
        tvHeaderRatingNumber = (TextView) findViewById(R.id.tv_header_rating_number);
        tvHeaderDirectors = (TextView) findViewById(R.id.tv_header_directors);
        tvHeaderCasts = (TextView) findViewById(R.id.tv_header_casts);
        tvHeaderGenres = (TextView) findViewById(R.id.tv_header_genres);
        tvHeaderDay = (TextView) findViewById(R.id.tv_header_day);
        tvHeaderCity = (TextView) findViewById(R.id.tv_header_city);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        llToolBar = (LinearLayout) findViewById(R.id.ll_tool_bar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCasts = (TextView) findViewById(R.id.tv_casts);

        initToolBar();
        initIntent();
    }

    private void initToolBar() {
        toolbar.inflateMenu(R.menu.movie_menu_detail);
        toolbar.setOnMenuItemClickListener(item -> {
            int i = item.getItemId();
            if (i == R.id.movie_about) {
                if (alt != null && alt.length() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.URL,alt);
                    bundle.putString(Constant.TITLE,"");
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle);
                }
            }
            return true;
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void initListener() {
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                if (state != Constant.STATES.EXPANDED) {
                    state = Constant.STATES.EXPANDED;
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (state != Constant.STATES.COLLAPSED) {
                    state = Constant.STATES.COLLAPSED;
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTheme));
            } else {
                if (state != Constant.STATES.INTERMEDIATE) {
                    state = Constant.STATES.INTERMEDIATE;
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            }
        });
    }

    @Override
    public void initData() {
        getMovieDetailData(id);
    }

    @SuppressLint("SetTextI18n")
    private void initIntent() {
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String title = intent.getStringExtra("title");
        String directors = intent.getStringExtra("directors");
        String casts = intent.getStringExtra("casts");
        String genres = intent.getStringExtra("genres");
        String rating = intent.getStringExtra("rating");
        String year = intent.getStringExtra("year");
        id = intent.getStringExtra("id");
        String collect_count = intent.getStringExtra("collect_count");


        ImageUtils.loadImgByPicasso(MovieDetailActivity.this,image,ivHeaderPhoto);
        tvName.setText(title);
        tvCasts.setText("主演："+casts);

        tvHeaderRatingRate.setText(rating);
        tvHeaderRatingNumber.setText(collect_count);
        tvHeaderDirectors.setText(directors);
        tvHeaderCasts.setText("主演："+casts);
        tvHeaderGenres.setText("类型："+ genres);
        tvHeaderDay.setText("上映日期："+year);
        tvHeaderCity.setText("制片国家/地区：");
    }

    private void getMovieDetailData(String id) {
        DouBanModel model = DouBanModel.getInstance();
        Observable<DouMovieDetailBean> hotMovie = model.getMovieDetail(id);
        hotMovie
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DouMovieDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final DouMovieDetailBean movieDetailBean) {
                        if(movieDetailBean!=null){
                            List<String> aka = movieDetailBean.getAka();
                            StringBuilder sb = new StringBuilder();
                            for(int a=0 ; a<aka.size() ; a++){
                                sb.append(aka.get(a));
                                sb.append("/");
                            }
                            tvContentTitle.setText(sb.toString());
                            tvContentAbout.setText(movieDetailBean.getSummary());
                            alt = movieDetailBean.getAlt();

                            recyclerView.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this));
                            final RecycleViewItemLine line = new RecycleViewItemLine(MovieDetailActivity.this, LinearLayout.HORIZONTAL,
                                    SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
                            recyclerView.addItemDecoration(line);
                            final MovieDetailAdapter adapter = new MovieDetailAdapter(movieDetailBean.getCasts(),MovieDetailActivity.this);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener((view, position) -> {
                                Bundle bundle = new Bundle();
                                bundle.putString(Constant.URL,movieDetailBean.getCasts().get(position).getAlt());
                                bundle.putString(Constant.TITLE,movieDetailBean.getCasts().get(position).getName());
                                ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle);
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
