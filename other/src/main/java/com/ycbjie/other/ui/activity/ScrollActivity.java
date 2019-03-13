package com.ycbjie.other.ui.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewParent;

import com.blankj.utilcode.util.LogUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.weight.CustomScrollView;
import com.ycbjie.other.R;

public class ScrollActivity extends BaseActivity {

    private CoordinatorLayout coordinator;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private CustomScrollView scrollView;
    private Constant.CollapsingToolbarLayoutState state;
    @Override
    public int getContentView() {
        return R.layout.activity_scroll_view;
    }

    @Override
    public void initView() {
        coordinator = findViewById(R.id.coordinator);
        appBar = findViewById(R.id.appbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        scrollView = findViewById(R.id.scrollView);

    }

    @Override
    public void initListener() {
        ViewParent parent = scrollView.getParent();
        if (parent instanceof CoordinatorLayout){
            LogUtils.e("initListener"+"------");
        }else {
            LogUtils.e("initListener"+"++++++");
        }
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                if (state != Constant.CollapsingToolbarLayoutState.EXPANDED) {
                    //修改状态标记为展开
                    state = Constant.CollapsingToolbarLayoutState.EXPANDED;
                    //coordinator.requestDisallowInterceptTouchEvent(false);
                    Log.e("CustomScrollView","修改状态标记为展开");
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                if (state != Constant.CollapsingToolbarLayoutState.COLLAPSED) {
                    //修改状态标记为折叠
                    state = Constant.CollapsingToolbarLayoutState.COLLAPSED;
                    //coordinator.requestDisallowInterceptTouchEvent(true);
                    Log.e("CustomScrollView","修改状态标记为折叠");
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                //请求所有父控件不要拦截Touch事件
            } else {
                if (state != Constant.CollapsingToolbarLayoutState.INTERNEDIATE) {
                    //修改状态标记为中间
                    state = Constant.CollapsingToolbarLayoutState.INTERNEDIATE;
                    //代码设置是否拦截事件
                    //coordinator.requestDisallowInterceptTouchEvent(true);
                    Log.e("CustomScrollView","修改状态标记为中间");
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            }
        });
        scrollView.setListener(new CustomScrollView.ScrollListener() {
            @Override
            public int onScrollListener() {
                switch (state){
                    case INTERNEDIATE:
                        return CustomScrollView.STATES.INTERMEDIATE;
                    case COLLAPSED:
                        return CustomScrollView.STATES.COLLAPSED;
                    case EXPANDED:
                        return CustomScrollView.STATES.EXPANDED;
                    default:
                        return CustomScrollView.STATES.INTERMEDIATE;
                }
            }
        });
    }

    @Override
    public void initData() {

    }

}
