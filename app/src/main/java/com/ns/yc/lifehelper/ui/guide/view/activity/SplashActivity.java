package com.ns.yc.lifehelper.ui.guide.view.activity;

import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.adapter.GuideViewPagerAdapter;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.weight.DotView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/5/18
 * 描    述：启动页面
 * 修订历史：
 * ================================================
 */
public class SplashActivity extends BaseActivity {

    @Bind(R.id.vp_pager)
    ViewPager vpPager;
    @Bind(R.id.ll_dot)
    LinearLayout llDot;
    @Bind(R.id.btn_go)
    Button btnGo;
    private ArrayList<View> views;
    private DotView[] dotViews;
    private int currentIndex;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        if(SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_FIRST_SPLASH,true)){
            initSplashView();
            initSplashListener();
        } else {
            startActivity(GuideActivity.class);
            finish();
        }
    }

    @Override
    public void initListener() {
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SelectFollowActivity.class);
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }


    private void initSplashView() {
        //实例化各个界面的布局对象
        View view1 = View.inflate(this, R.layout.activity_splash_view, null);
        View view2 = View.inflate(this, R.layout.activity_splash_view, null);
        View view3 = View.inflate(this, R.layout.activity_splash_view, null);
        View view4 = View.inflate(this, R.layout.activity_splash_view, null);

        ((ImageView) view1.findViewById(R.id.iv_image)).setImageResource(R.drawable.screenshot_1);
        ((ImageView) view2.findViewById(R.id.iv_image)).setImageResource(R.drawable.screenshot_2);
        ((ImageView) view3.findViewById(R.id.iv_image)).setImageResource(R.drawable.screenshot_3);
        ((ImageView) view4.findViewById(R.id.iv_image)).setImageResource(R.drawable.screenshot_4);

        views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
    }


    private void initSplashListener() {
        GuideViewPagerAdapter adapter = new GuideViewPagerAdapter(views);
        vpPager.setAdapter(adapter);
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setCurrentDot(position);
                if (position + 1 == vpPager.getAdapter().getCount()) {
                    if (btnGo.getVisibility() != View.VISIBLE) {
                        btnGo.setVisibility(View.VISIBLE);
                        btnGo.startAnimation(AnimationUtils.loadAnimation(Utils.getContext(), android.R.anim.fade_in));
                    }
                } else {
                    if (btnGo.getVisibility() != View.GONE) {
                        btnGo.setVisibility(View.GONE);
                        btnGo.startAnimation(AnimationUtils.loadAnimation(Utils.getContext(), android.R.anim.fade_out));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        dotViews = new DotView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dotViews[i] = (DotView) llDot.getChildAt(i);
        }
        currentIndex = 0;
        dotViews[currentIndex].setIsSelected(true);
    }


    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1 || currentIndex == position) {
            return;
        }
        dotViews[currentIndex].setIsSelected(false);
        dotViews[position].setIsSelected(true);
        currentIndex = position;
    }

    /**
     * 屏蔽返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
