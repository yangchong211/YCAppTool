package com.ns.yc.lifehelper.ui.other.gank.view.activity;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.me.view.activity.MeSettingActivity;
import com.ns.yc.lifehelper.ui.other.gank.callback.PicassoPalette;
import com.ns.yc.lifehelper.ui.other.gank.contract.GanKHomeAContract;
import com.ns.yc.lifehelper.ui.other.gank.presenter.GanKHomeAPresenter;
import com.ns.yc.lifehelper.ui.other.gank.view.fragment.GanKHomeFragment;
import com.ns.yc.lifehelper.utils.MDTintUtil;
import com.ns.yc.lifehelper.utils.animation.AnimatorUtils;
import com.ns.yc.lifehelper.utils.statusbar.GanKStatusBarUtil;
import com.pedaily.yc.ycdialoglib.toast.ToastUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：干货集中营
 * 修订历史：
 * ================================================
 */
public class GanKHomeActivity extends BaseActivity implements View.OnClickListener , GanKHomeAContract.View{

    @Bind(R.id.iv_home_banner)
    ImageView ivHomeBanner;
    @Bind(R.id.iv_home_setting)
    AppCompatImageView ivHomeSetting;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.tl_home_toolbar)
    Toolbar tlHomeToolbar;
    @Bind(R.id.ll_home_search)
    LinearLayout llHomeSearch;
    @Bind(R.id.iv_home_collection)
    AppCompatImageView ivHomeCollection;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.vp_home_category)
    ViewPager vpHomeCategory;
    @Bind(R.id.fab_home_random)
    FloatingActionButton fabHomeRandom;

    private GanKHomeAContract.Presenter presenter = new GanKHomeAPresenter(this);

    private CollapsingToolbarLayoutState state;             // CollapsingToolbarLayout 折叠状态
    private ObjectAnimator mAnimator;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,                                   // 完全展开
        COLLAPSED,                                  // 折叠
        INTERNEDIATE                                // 中间状态
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        if(mAnimator!=null && mAnimator.isRunning()){
            mAnimator.cancel();
        }
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mAnimator!=null) {
            mAnimator.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_gank_home_main;
    }

    @Override
    public void initView() {
        GanKStatusBarUtil.immersive(this);
        GanKStatusBarUtil.setPaddingSmart(this, ivHomeBanner);
        GanKStatusBarUtil.setPaddingSmart(this, tlHomeToolbar);
        setFabDynamicState();
        initFragments();
        initViewPager();
    }

    @Override
    public void initListener() {
        llHomeSearch.setOnClickListener(this);
        fabHomeRandom.setOnClickListener(this);
        ivHomeSetting.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_home_search:
                startActivity(GanKSearchActivity.class);
                break;
            case R.id.fab_home_random:                  //点击按钮，获取随机图片
                presenter.getRandomBanner();
                break;
            case R.id.iv_home_setting:
                startActivity(MeSettingActivity.class);
                break;
        }
    }

    /**
     * 设置图片
     */
    @Override
    public void setBanner(String imgUrl) {
        Picasso.with(GanKHomeActivity.this)
                .load(imgUrl)
                .into(ivHomeBanner, PicassoPalette.with(imgUrl, ivHomeBanner)
                        .intoCallBack(new PicassoPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(Palette palette) {
                                presenter.setThemeColor(palette,GanKHomeActivity.this);
                            }
                        }));
    }


    /**
     * 开始加载动画
     */
    @Override
    public void startBannerLoadingAnim() {
        fabHomeRandom.setImageResource(R.drawable.ic_gank_loading);
        mAnimator = AnimatorUtils.setObjectAnimator(fabHomeRandom,"rotation",0,360,800,0);
        mAnimator.start();

        //Animator mAnim = AnimatorInflater.loadAnimator(this, R.animator.animator_1_0);
        //mAnim.setTarget(fabHomeRandom);
        //mAnim.start();
    }

    /**
     * 停止加载动画
     */
    @Override
    public void stopBannerLoadingAnim() {
        fabHomeRandom.setImageResource(R.drawable.ic_gank_beauty);
        mAnimator.cancel();
        fabHomeRandom.setRotation(0);
    }

    /**
     * 设置可以点击
     */
    @Override
    public void enableFabButton() {
        fabHomeRandom.setEnabled(true);
    }

    /**
     * 设置不可点击
     */
    @Override
    public void disEnableFabButton() {
        fabHomeRandom.setEnabled(false);
    }


    /**
     * 设置加载图片失败
     */
    @Override
    public void errorImage() {
        ToastUtil.showToast(this,"图片加载失败");
    }

    /**
     * 设置 AppBarLayout 的背景色
     */
    @Override
    public void setAppBarLayoutColor(int appBarLayoutColor) {
        collapsingToolbar.setContentScrimColor(appBarLayoutColor);
        appbar.setBackgroundColor(appBarLayoutColor);
    }

    /**
     * 设置 FabButton 的背景色
     */
    @Override
    public void setFabButtonColor(int color) {
        MDTintUtil.setTint(fabHomeRandom, color);
    }

    /**
     * 缓存图片
     */
    @Override
    public void cacheImg(final String imgUrl) {
        // 预加载 提前缓存好的欢迎图
        Picasso.with(this)
                .load(imgUrl)
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        presenter.saveCacheImgUrl(imgUrl);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }


    /**
     * 根据 CollapsingToolbarLayout 的折叠状态，设置 FloatingActionButton 的隐藏和显示
     */
    private void setFabDynamicState() {
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED; // 修改状态标记为展开
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        fabHomeRandom.hide();
                        state = CollapsingToolbarLayoutState.COLLAPSED; // 修改状态标记为折叠
                        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
                        layoutParams.height = SizeUtils.dp2px(240);
                        appbar.setLayoutParams(layoutParams);
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                            fabHomeRandom.show();
                        }
                        state = CollapsingToolbarLayoutState.INTERNEDIATE; // 修改状态标记为中间
                    }
                }
            }
        });
    }

    private List<String> mTitles;
    private ArrayList<Fragment> mFragments;
    private String[] mTabs = {"Android", "iOS", "前端", "瞎推荐", "拓展资源","App"};
    private void initFragments() {
        mTitles = new ArrayList<>();
        mFragments = new ArrayList<>();
        for (int a = 0; a < mTabs.length; a++) {
            mTitles.add(mTabs[a]);
            mFragments.add(GanKHomeFragment.getInstance(mTabs[a]));
        }
    }


    private void initViewPager() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitles);
        vpHomeCategory.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpHomeCategory.setOffscreenPageLimit(5);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpHomeCategory);
    }


}
