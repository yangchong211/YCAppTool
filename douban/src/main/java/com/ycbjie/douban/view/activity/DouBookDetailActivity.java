package com.ycbjie.douban.view.activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ycbjie.douban.R;
import com.ycbjie.douban.api.DouBookModel;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.douban.bean.DouBookDetailBean;
import com.ycbjie.library.utils.ImageUtils;

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
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView ivBgImage;
    private LinearLayout llBookDetail;
    private ImageView ivBookPhoto;
    private TextView tvBookDirectors;
    private TextView tvBookRatingRate;
    private TextView tvBookRatingNumber;
    private TextView tvOneCasts;
    private TextView tvBookGenres;
    private Toolbar toolbar;
    private LinearLayout llToolBar;
    private TextView tvName;
    private TextView tvCasts;
    private Constant.CollapsingToolbarLayoutState state;
    private String id;
    private String alt;


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
        tvBookSummary = (TextView) findViewById(R.id.tv_book_summary);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvCatalog = (TextView) findViewById(R.id.tv_catalog);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ivBgImage = (ImageView) findViewById(R.id.iv_bg_image);
        llBookDetail = (LinearLayout) findViewById(R.id.ll_book_detail);
        ivBookPhoto = (ImageView) findViewById(R.id.iv_book_photo);
        tvBookDirectors = (TextView) findViewById(R.id.tv_book_directors);
        tvBookRatingRate = (TextView) findViewById(R.id.tv_book_rating_rate);
        tvBookRatingNumber = (TextView) findViewById(R.id.tv_book_rating_number);
        tvOneCasts = (TextView) findViewById(R.id.tv_one_casts);
        tvBookGenres = (TextView) findViewById(R.id.tv_book_genres);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        llToolBar = (LinearLayout) findViewById(R.id.ll_tool_bar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCasts = (TextView) findViewById(R.id.tv_casts);


        initToolBar();
        initIntent();
    }

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
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != Constant.CollapsingToolbarLayoutState.EXPANDED) {
                        //修改状态标记为展开
                        state = Constant.CollapsingToolbarLayoutState.EXPANDED;
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != Constant.CollapsingToolbarLayoutState.COLLAPSED) {
                        //修改状态标记为折叠
                        state = Constant.CollapsingToolbarLayoutState.COLLAPSED;
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                } else {
                    if (state != Constant.CollapsingToolbarLayoutState.INTERNEDIATE) {
                        //修改状态标记为中间
                        state = Constant.CollapsingToolbarLayoutState.INTERNEDIATE;
                    }
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                }
            }
        });
    }

    @Override
    public void initData() {
        getBookDetailData(id);
    }

    private void initToolBar() {
        toolbar.inflateMenu(R.menu.movie_menu_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.movie_about) {
                    Intent intent = new Intent(DouBookDetailActivity.this, MovieWebViewActivity.class);
                    intent.putExtra("alt", alt);
                    intent.putExtra("name", "");
                    startActivity(intent);

                } else {
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

    private void getBookDetailData(String id) {
        DouBookModel model = DouBookModel.getInstance();
        model.getHotMovie(id)
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
