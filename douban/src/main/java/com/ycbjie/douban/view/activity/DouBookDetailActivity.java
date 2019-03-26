package com.ycbjie.douban.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pedaily.yc.ycdialoglib.loading.ViewLoading;
import com.ycbjie.douban.R;
import com.ycbjie.douban.api.DouBanModel;
import com.ycbjie.douban.bean.DouBookDetailBean;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.utils.image.ImageUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣读书页面
 *     revise:
 * </pre>
 */
public class DouBookDetailActivity extends BaseActivity {


    private TextView tvBookSummary;
    private TextView tvIntro;
    private TextView tvCatalog;
    private AppBarLayout appBar;
    private ImageView ivBookPhoto;
    private TextView tvBookDirectors;
    private TextView tvBookRatingRate;
    private TextView tvBookRatingNumber;
    private TextView tvOneCasts;
    private TextView tvBookGenres;
    private Toolbar toolbar;
    private TextView tvName;
    private TextView tvCasts;
    private int state;
    private String id;
    private String alt;
    private NestedScrollView scrollView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_book_detail;
    }

    @Override
    public void initView() {
        initFindViewById();
        initToolBar();
        initIntent();
    }

    private void initFindViewById() {
        tvBookSummary = findViewById(R.id.tv_book_summary);
        tvIntro = findViewById(R.id.tv_intro);
        tvCatalog = findViewById(R.id.tv_catalog);
        appBar = findViewById(R.id.app_bar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ImageView ivBgImage = findViewById(R.id.iv_bg_image);
        LinearLayout llBookDetail = findViewById(R.id.ll_book_detail);
        ivBookPhoto = findViewById(R.id.iv_book_photo);
        tvBookDirectors = findViewById(R.id.tv_book_directors);
        tvBookRatingRate = findViewById(R.id.tv_book_rating_rate);
        tvBookRatingNumber = findViewById(R.id.tv_book_rating_number);
        tvOneCasts = findViewById(R.id.tv_one_casts);
        tvBookGenres = findViewById(R.id.tv_book_genres);
        toolbar = findViewById(R.id.toolbar);
        tvName = findViewById(R.id.tv_name);
        tvCasts = findViewById(R.id.tv_casts);
        scrollView = findViewById(R.id.scrollView);
    }

    @SuppressLint("SetTextI18n")
    private void initIntent() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String image = intent.getStringExtra("image");
        String average = intent.getStringExtra("average");
        String numRaters = intent.getStringExtra("numRaters");
        String pubDate = intent.getStringExtra("pubdate");
        String publisher = intent.getStringExtra("publisher");
        id = intent.getStringExtra("id");
        alt = intent.getStringExtra("alt");

        tvName.setText(title);
        tvCasts.setText("作者："+ author);
        ImageUtils.loadImgByPicasso(DouBookDetailActivity.this,image,ivBookPhoto);
        tvBookDirectors.setText(author);
        tvBookRatingNumber.setText(numRaters+"人评分");
        tvBookRatingRate.setText("评分："+average);
        tvOneCasts.setText(pubDate);
        tvBookGenres.setText(publisher);
    }

    @Override
    public void initListener() {
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                if (state != Constant.STATES.EXPANDED) {
                    //修改状态标记为展开
                    state = Constant.STATES.EXPANDED;
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (state != Constant.STATES.COLLAPSED) {
                    //修改状态标记为折叠
                    state = Constant.STATES.COLLAPSED;
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTheme));
            } else {
                if (state != Constant.STATES.INTERMEDIATE) {
                    //修改状态标记为中间
                    state = Constant.STATES.INTERMEDIATE;
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            }
        });
    }

    @Override
    public void initData() {
        ViewLoading.show(this);
        getBookDetailData(id);
    }

    private void initToolBar() {
        toolbar.inflateMenu(R.menu.movie_menu_detail);
        toolbar.setOnMenuItemClickListener(item -> {
            int i = item.getItemId();
            if (i == R.id.movie_about) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.URL,alt);
                bundle.putString(Constant.TITLE,"");
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle);
            }
            return true;
        });
        toolbar.setNavigationOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        });
    }

    private void getBookDetailData(String id) {
        DouBanModel model = DouBanModel.getInstance();
        model.getBookDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DouBookDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DouBookDetailBean bookDetailBean) {
                        if(bookDetailBean!=null){
                            tvBookSummary.setText(bookDetailBean.getSummary());
                            tvIntro.setText(bookDetailBean.getAuthor_intro());
                            tvCatalog.setText(bookDetailBean.getCatalog());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {
                        ViewLoading.dismiss(DouBookDetailActivity.this);
                    }
                });
    }

}
