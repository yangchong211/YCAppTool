package com.ycbjie.gank.view.activity;

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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ycbjie.gank.R;
import com.ycbjie.gank.contract.GanKHomeAContract;
import com.ycbjie.gank.presenter.GanKHomeAPresenter;
import com.ycbjie.gank.view.fragment.GanKHomeFragment;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.adapter.BasePagerAdapter;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.utils.MDTintUtil;
import com.ycbjie.library.utils.animation.AnimatorUtils;

import java.util.ArrayList;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/14
 *     desc  : 干货集中营
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_GANK_ACTIVITY)
public class GanKHomeActivity extends BaseActivity implements View.OnClickListener , GanKHomeAContract.View{

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView ivHomeBanner;
    private AppCompatImageView ivHomeSetting;
    private Toolbar tlHomeToolbar;
    private TabLayout tabLayout;
    private LinearLayout llHomeSearch;
    private AppCompatImageView ivHomeCollection;
    private ViewPager vpHomeCategory;
    private FloatingActionButton fabHomeRandom;

    private GanKHomeAContract.Presenter presenter = new GanKHomeAPresenter(this);

    /**
     * CollapsingToolbarLayout 折叠状态
     */
    private int state;
    private ObjectAnimator mAnimator;


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
        presenter.unSubscribe();
        super.onDestroy();
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
        initFindViewId();
        StateAppBar.setStatusBarColorForCollapsingToolbar(
                this, appbar, collapsingToolbar,
                tlHomeToolbar, ContextCompat.getColor(this, R.color.colorPrimary));
        setFabDynamicState();
        initFragments();
    }

    private void initFindViewId() {
        appbar = findViewById(R.id.appbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        ivHomeBanner = findViewById(R.id.iv_home_banner);
        ivHomeSetting = findViewById(R.id.iv_home_setting);
        tlHomeToolbar = findViewById(R.id.tl_home_toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        llHomeSearch = findViewById(R.id.ll_home_search);
        ivHomeCollection = findViewById(R.id.iv_home_collection);
        vpHomeCategory = findViewById(R.id.vp_home_category);
        fabHomeRandom = findViewById(R.id.fab_home_random);
    }

    @Override
    public void initListener() {
        llHomeSearch.setOnClickListener(this);
        fabHomeRandom.setOnClickListener(this);
        ivHomeSetting.setOnClickListener(this);
        ivHomeCollection.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_home_search) {
            startActivity(GanKSearchActivity.class);
            //点击按钮，获取随机图片
        } else if (i == R.id.fab_home_random) {
            presenter.getRandomBanner();
        } else if (i == R.id.iv_home_setting) {
            ARouterUtils.navigation(ARouterConstant.ACTIVITY_APP_SETTING_ACTIVITY);
        } else if (i == R.id.iv_home_collection){
            ARouterUtils.navigation(ARouterConstant.ACTIVITY_GANK_KNOWLEDGE_ACTIVITY);
        }
    }

    /**
     * 设置图片
     */
    @Override
    public void setBanner(String imgUrl) {
        Picasso.with(GanKHomeActivity.this)
                .load(imgUrl)
                .into(ivHomeBanner, new Callback() {
                    @Override
                    public void onSuccess() {
                        int colorPrimary = Utils.getApp().getResources()
                                .getColor(R.color.colorTheme);
                        // 设置 FabButton 的背景色
                        setFabButtonColor(colorPrimary);
                        enableFabButton();
                        stopBannerLoadingAnim();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }


    /**
     * 开始加载动画
     */
    @Override
    public void startBannerLoadingAnim() {
        fabHomeRandom.setImageResource(R.drawable.ic_gank_loading);
        mAnimator = AnimatorUtils.setObjectAnimator(fabHomeRandom,
                "rotation",0,360,800,0);
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
        ToastUtils.showToast("图片加载失败");
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
        appbar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                if (state != Constant.STATES.EXPANDED) {
                    // 修改状态标记为展开
                    state = Constant.STATES.EXPANDED;
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (state != Constant.STATES.COLLAPSED) {
                    fabHomeRandom.hide();
                    // 修改状态标记为折叠
                    state = Constant.STATES.COLLAPSED;
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                            appbar.getLayoutParams();
                    layoutParams.height = SizeUtils.dp2px(240);
                    appbar.setLayoutParams(layoutParams);
                }
            } else {
                if (state != Constant.STATES.INTERMEDIATE) {
                    if (state == Constant.STATES.COLLAPSED) {
                        fabHomeRandom.show();
                    }
                    // 修改状态标记为中间
                    state = Constant.STATES.INTERMEDIATE;
                }
            }
        });
    }

    private void initFragments() {
        String[] mTabs = {"Android", "iOS", "前端", "瞎推荐", "拓展资源","App"};
        ArrayList<String> mTitles = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        for (String mTab : mTabs) {
            mTitles.add(mTab);
            mFragments.add(GanKHomeFragment.getInstance(mTab));
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitles);
        vpHomeCategory.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpHomeCategory.setOffscreenPageLimit(mFragments.size());
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpHomeCategory);
    }


}
