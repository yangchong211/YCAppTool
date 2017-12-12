package com.ns.yc.lifehelper.ui.other.douMusic.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ArcMotion;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.other.douMusic.bean.DouMusicDetailBean;
import com.ns.yc.lifehelper.ui.other.douMusic.model.DouMusicDetailModel;
import com.ns.yc.lifehelper.ui.weight.CustomChangeBounds;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.ns.yc.lifehelper.utils.statusbar.StatusBarUtils;
import com.ns.yc.ycutilslib.scrollView.MyNestedScrollView;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/22
 * 描    述：豆瓣音乐详情页面
 * 修订历史：
 * ================================================
 */
public class DouMusicDetailActivity extends BaseActivity {

    @Bind(R.id.img_music_item_bg)
    ImageView imgMusicItemBg;
    @Bind(R.id.iv_music_photo)
    ImageView ivMusicPhoto;
    @Bind(R.id.tv_music_name)
    TextView tvMusicName;
    @Bind(R.id.tv_music_alt_title)
    TextView tvMusicAltTitle;
    @Bind(R.id.tv_music_average)
    TextView tvMusicAverage;
    @Bind(R.id.tv_music_numRaters)
    TextView tvMusicNumRaters;
    @Bind(R.id.tv_music_summary)
    TextView tvMusicSummary;
    @Bind(R.id.ll_music_item)
    LinearLayout llMusicItem;
    @Bind(R.id.ll_Header_view)
    LinearLayout llHeaderView;
    @Bind(R.id.tv_txt)
    TextView tvTxt;
    @Bind(R.id.xrv_list)
    RecyclerView xrvList;
    @Bind(R.id.nsv_scrollview)
    MyNestedScrollView nsvScrollview;
    @Bind(R.id.iv_title_head_bg)
    ImageView ivTitleHeadBg;
    @Bind(R.id.title_tool_bar)
    Toolbar titleToolBar;
    @Bind(R.id.rl_title_head)
    RelativeLayout rlTitleHead;

    private String id = "";
    private String title = "";
    // 这个是高斯图背景的高度
    private int imageBgHeight;
    // 在多大范围内变色
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
        initIntentData();
        setTitleBar();
        setMotion();
        setPicture();
        initSlideShapeTheme();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        getMusicDetailData(id);
    }

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
        titleToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
            Interpolator interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in);

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
        Glide.with(this)
                .load(image)
                .into(ivMusicPhoto);

        // "14":模糊度；"3":图片缩放3倍后再进行模糊
        Glide.with(this)
                .load(image)
                .error(R.drawable.bg_stack_blur_default)
                .placeholder(R.drawable.bg_stack_blur_default)
                .crossFade(500)
                .bitmapTransform(new BlurTransformation(this, 14, 3))
                .into(imgMusicItemBg);
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
        StatusBarUtils.setTranslucentForImageView(this, 0, titleToolBar);

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
        Glide.with(this)
                .load(image)
                .error(R.drawable.bg_stack_blur_default)
                .bitmapTransform(new BlurTransformation(this, 14, 3))// 设置高斯模糊
                .listener(new RequestListener<String, GlideDrawable>() {//监听加载状态
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        titleToolBar.setBackgroundColor(Color.TRANSPARENT);
                        ivTitleHeadBg.setImageAlpha(0);
                        ivTitleHeadBg.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(ivTitleHeadBg);
    }



    private void initScrollViewListener() {
        // 为了兼容api23以下
        nsvScrollview.setOnMyScrollChangeListener(new MyNestedScrollView.ScrollInterface() {
            @Override
            public void onScrollChange(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollChangeHeader(scrollY);
            }
        });

        int titleBarAndStatusHeight = (int) (DouMusicDetailActivity.this.getResources().getDimension(R.dimen.nav_bar_height) + StatusBarUtils.getStatusBarHeight(this));
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
     * @param id
     */
    private void getMusicDetailData(String id) {
        DouMusicDetailModel model = new DouMusicDetailModel(this);
        model.getMusicDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DouMusicDetailBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DouMusicDetailBean douMusicDetailBean) {
                        if(douMusicDetailBean!=null){
                            tvMusicSummary.setText(douMusicDetailBean.getSummary());
                            tvTxt.setVisibility(View.VISIBLE);
                            tvTxt.setText(douMusicDetailBean.getAttrs().getTracks().get(0));
                        }
                    }
                });
    }

}
