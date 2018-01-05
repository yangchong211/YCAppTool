package com.ns.yc.lifehelper.ui.other.douMovie.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.listener.OnListItemClickListener;
import com.ns.yc.lifehelper.ui.other.douMovie.view.adapter.MovieDetailAdapter;
import com.ns.yc.lifehelper.ui.other.douMovie.bean.DouMovieDetailBean;
import com.ns.yc.lifehelper.ui.other.douMovie.model.DetailMovieModel;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import com.ns.yc.lifehelper.utils.ImageUtils;

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
 * 描    述：豆瓣电影详情页面
 * 修订历史：
 * ================================================
 */
public class MovieDetailActivity extends BaseActivity {

    @Bind(R.id.tv_content_title)
    TextView tvContentTitle;
    @Bind(R.id.tv_content_about)
    TextView tvContentAbout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.iv_bg_image)
    ImageView ivBgImage;
    @Bind(R.id.iv_header_photo)
    ImageView ivHeaderPhoto;
    @Bind(R.id.tv_header_rating_rate)
    TextView tvHeaderRatingRate;
    @Bind(R.id.tv_header_rating_number)
    TextView tvHeaderRatingNumber;
    @Bind(R.id.tv_header_directors)
    TextView tvHeaderDirectors;
    @Bind(R.id.tv_header_casts)
    TextView tvHeaderCasts;
    @Bind(R.id.tv_header_genres)
    TextView tvHeaderGenres;
    @Bind(R.id.tv_header_day)
    TextView tvHeaderDay;
    @Bind(R.id.tv_header_city)
    TextView tvHeaderCity;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_casts)
    TextView tvCasts;
    @Bind(R.id.ll_tool_bar)
    LinearLayout llToolBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    private String id = "";

    private CollapsingToolbarLayoutState state;
    private String alt;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTER
    }

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
        initToolBar();
        initIntent();
    }

    private void initToolBar() {
        toolbar.inflateMenu(R.menu.movie_menu_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.movie_about:
                        if(alt!=null && alt.length()>0){
                            Intent intent = new Intent(MovieDetailActivity.this,MovieWebViewActivity.class);
                            intent.putExtra("alt",alt);
                            intent.putExtra("name","");
                            startActivity(intent);
                        }
                        break;
                }
                return true;
            }
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
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;  //修改状态标记为展开
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED; //修改状态标记为折叠
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                } else {
                    if (state != CollapsingToolbarLayoutState.INTER) {
                        state = CollapsingToolbarLayoutState.INTER;     //修改状态标记为中间
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                }
            }
        });
    }

    @Override
    public void initData() {
        getMovieDetailData(id);
    }

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
        DetailMovieModel model = DetailMovieModel.getInstance(this);
        model.getHotMovie(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DouMovieDetailBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

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
                            adapter.setOnItemClickListener(new OnListItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(MovieDetailActivity.this,MovieWebViewActivity.class);
                                    intent.putExtra("alt",movieDetailBean.getCasts().get(position).getAlt());
                                    intent.putExtra("name",movieDetailBean.getCasts().get(position).getName());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }

}
