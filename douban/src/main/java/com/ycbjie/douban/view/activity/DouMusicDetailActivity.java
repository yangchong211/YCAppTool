package com.ycbjie.douban.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ns.yc.ycutilslib.scrollView.ReboundScrollView;
import com.pedaily.yc.ycdialoglib.loading.ViewLoading;
import com.ycbjie.douban.R;
import com.ycbjie.douban.api.DouBanModel;
import com.ycbjie.douban.bean.DouMusicDetailBean;
import com.ycbjie.douban.weight.CustomChangeBounds;
import com.ycbjie.library.base.glide.GlideApp;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.http.ExceptionUtils;
import com.ycbjie.library.utils.image.ImageUtils;

import cn.ycbjie.ycstatusbarlib.StatusBarUtils;
import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/22
 *     desc  : 豆瓣音乐详情页面
 *     revise:
 * </pre>
 */
public class DouMusicDetailActivity extends BaseActivity {

    private ReboundScrollView nsvScrollview;
    private ImageView imgMusicItemBg;
    private ImageView ivMusicPhoto;
    private TextView tvMusicName;
    private TextView tvMusicAltTitle;
    private TextView tvMusicAverage;
    private TextView tvMusicNumRaters;
    private TextView tvMusicSummary;
    private TextView tvTxt;
    private RecyclerView xrvList;
    private ImageView ivTitleHeadBg;
    private Toolbar titleToolBar;


    private String id = "";
    private String title = "";
    private int imageBgHeight;
    /**
     * 在多大范围内变色
     */
    private int slidingDistance;
    private String image = "";

    @Override
    protected void onResume() {
        super.onResume();
        xrvList.setFocusable(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_dou_music_detail;
    }

    @Override
    public void initView() {
        initFindViewById();
        initIntentData();
        setTitleBar();
        setMotion();
        setPicture();
        initSlideShapeTheme();
    }

    private void initFindViewById() {
        nsvScrollview = findViewById(R.id.nsv_scrollview);
        imgMusicItemBg = findViewById(R.id.img_music_item_bg);
        ivMusicPhoto = findViewById(R.id.iv_music_photo);
        tvMusicName = findViewById(R.id.tv_music_name);
        tvMusicAltTitle = findViewById(R.id.tv_music_alt_title);
        tvMusicAverage = findViewById(R.id.tv_music_average);
        tvMusicNumRaters = findViewById(R.id.tv_music_numRaters);
        tvMusicSummary = findViewById(R.id.tv_music_summary);
        tvTxt = findViewById(R.id.tv_txt);
        xrvList = findViewById(R.id.xrv_list);
        ivTitleHeadBg = findViewById(R.id.iv_title_head_bg);
        titleToolBar = findViewById(R.id.title_tool_bar);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        ViewLoading.show(this);
        getMusicDetailData(id);
    }

    @SuppressLint("SetTextI18n")
    private void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            id = intent.getStringExtra("id");
            title = intent.getStringExtra("alt_title");
            String name = intent.getStringExtra("title");
            image = intent.getStringExtra("image");
            String average = intent.getStringExtra("average");
            String numRaters = intent.getStringExtra("numRaters");
            ImageUtils.loadImgByPicasso(DouMusicDetailActivity.this, image,ivMusicPhoto);
            tvMusicName.setText(name);
            tvMusicAltTitle.setText(title);
            tvMusicAverage.setText("评分："+average);
            tvMusicNumRaters.setText("评论数："+numRaters);
            tvMusicSummary.setText("");
        }
    }


    private void setTitleBar() {
        setSupportActionBar(titleToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
        }
        titleToolBar.setTitle(title);
        titleToolBar.setNavigationOnClickListener(v -> onBackPressed());
    }


    /**
     * 设置自定义 Shared Element切换动画
     */
    private void setMotion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //定义ArcMotion
            ArcMotion arcMotion = new ArcMotion();
            arcMotion.setMinimumHorizontalAngle(50f);
            arcMotion.setMinimumVerticalAngle(50f);
            //插值器，控制速度
            Interpolator interpolator = AnimationUtils.loadInterpolator(
                    this, android.R.interpolator.fast_out_slow_in);

            //实例化自定义的ChangeBounds
            CustomChangeBounds changeBounds = new CustomChangeBounds();
            changeBounds.setPathMotion(arcMotion);
            changeBounds.setInterpolator(interpolator);
            changeBounds.addTarget(ivMusicPhoto);
            //将切换动画应用到当前的Activity的进入和返回
            getWindow().setSharedElementEnterTransition(changeBounds);
            getWindow().setSharedElementReturnTransition(changeBounds);
        }
    }

    /**
     * 高斯背景图和一般图片
     */
    private void setPicture() {
        ImageUtils.loadImgByGlide(this,image,R.drawable.image_default,ivMusicPhoto);
        ImageUtils.loadImgByGlide(this,image,R.drawable.bg_stack_blur_default,imgMusicItemBg);
    }


    /**
     * 初始化滑动渐变
     */
    private void initSlideShapeTheme() {
        setImgHeaderBg();

        // toolbar的高度
        int toolbarHeight = titleToolBar.getLayoutParams().height;
        // toolbar+状态栏的高度
        final int headerBgHeight = toolbarHeight + StatusBarUtils.getStatusBarHeight(this);

        // 使背景图向上移动到图片的最底端，保留toolbar+状态栏的高度
        ivTitleHeadBg.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams params = ivTitleHeadBg.getLayoutParams();
        ViewGroup.MarginLayoutParams ivTitleHeadBgParams = (ViewGroup.MarginLayoutParams) ivTitleHeadBg.getLayoutParams();
        int marginTop = params.height - headerBgHeight;
        ivTitleHeadBgParams.setMargins(0, -marginTop, 0, 0);
        ivTitleHeadBg.setImageAlpha(0);

        // 为头部是View的界面设置状态栏透明
        StateAppBar.translucentStatusBar(this, true);
        ViewGroup.LayoutParams imgItemBgparams = imgMusicItemBg.getLayoutParams();
        // 获得高斯图背景的高度
        imageBgHeight = imgItemBgparams.height;

        // 监听改变透明度
        initScrollViewListener();
    }

    /**
     * 加载titleBar背景,加载后将背景设为透明
     */
    private void setImgHeaderBg() {
        GlideApp.with(this)
                .load(image)
                .error(R.drawable.bg_stack_blur_default)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        titleToolBar.setBackgroundColor(Color.TRANSPARENT);
                        ivTitleHeadBg.setImageAlpha(0);
                        ivTitleHeadBg.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(ivTitleHeadBg);
    }



    private void initScrollViewListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nsvScrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                /**
                 * 滚动监听
                 * @param v                 第一个参数NestedScrollView v:是NestedScrollView的对象
                 * @param scrollX           第二个参数:scrollX是目前的（滑动后）的X轴坐标
                 * @param scrollY           第三个参数:ScrollY是目前的（滑动后）的Y轴坐标
                 * @param oldScrollX        第四个参数:oldScrollX是之前的（滑动前）的X轴坐标
                 * @param oldScrollY        第五个参数:oldScrollY是之前的（滑动前）的Y轴坐标
                 */
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY,
                                           int oldScrollX, int oldScrollY) {
                    //滑动后-滑动前>0  屏幕向下滑动
                    if (scrollY-oldScrollY>0){
                        //向下滑动
                        LogUtils.d("setOnScrollChangeListener" + "向下滑动");
                    }else {
                        //向上滑动
                        LogUtils.d("setOnScrollChangeListener" + "向上滑动");
                    }
                    scrollChangeHeader(scrollY);
                }
            });
        }
        int titleBarAndStatusHeight = (int) (DouMusicDetailActivity.this.getResources().getDimension(R.dimen.nav_bar_height)
                + StatusBarUtils.getStatusBarHeight(this));
        slidingDistance = imageBgHeight - titleBarAndStatusHeight - (int) (DouMusicDetailActivity.this.getResources().getDimension(R.dimen.nav_bar_height_more));
    }


    /**
     * 根据页面滑动距离改变Header透明度方法
     */
    private void scrollChangeHeader(int scrolledY) {
        if (scrolledY < 0) {
            scrolledY = 0;
        }
        float alpha = Math.abs(scrolledY) * 1.0f / (slidingDistance);
        Drawable drawable = ivTitleHeadBg.getDrawable();
        if (drawable != null) {
            if (scrolledY <= slidingDistance) {
                // title部分的渐变
                drawable.mutate().setAlpha((int) (alpha * 255));
                ivTitleHeadBg.setImageDrawable(drawable);
            } else {
                drawable.mutate().setAlpha(255);
                ivTitleHeadBg.setImageDrawable(drawable);
            }
        }
    }

    /**
     * 获取网络数据
     */
    private void getMusicDetailData(String id) {
        DouBanModel model = DouBanModel.getInstance();
        model.getMusicDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DouMusicDetailBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DouMusicDetailBean douMusicDetailBean) {
                        if(douMusicDetailBean!=null){
                            tvMusicSummary.setText(douMusicDetailBean.getSummary());
                            tvTxt.setVisibility(View.VISIBLE);
                            tvTxt.setText(douMusicDetailBean.getAttrs().getTracks().get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ExceptionUtils.handleException(e);
                    }

                    @Override
                    public void onComplete() {
                        ViewLoading.dismiss(DouMusicDetailActivity.this);
                    }
                });
    }

}
