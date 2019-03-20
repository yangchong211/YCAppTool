package com.ycbjie.other.ui.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.weight.CustomScrollView;
import com.ycbjie.other.R;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/2
 *     desc  :
 *     revise:
 * </pre>
 */
public class ScrollActivity extends BaseActivity {

    private AppBarLayout appBar;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private CustomScrollView scrollView;
    private int state;
    private ImageView ivImage;
    private int height;
    private int width;
    private int top;

    @Override
    public int getContentView() {
        return R.layout.activity_scroll_view;
    }

    @Override
    public void initView() {
        appBar = findViewById(R.id.appbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        toolbar = findViewById(R.id.toolbar);
        scrollView = findViewById(R.id.scrollView);
        ivImage = findViewById(R.id.iv_image);

        StateAppBar.setStatusBarColorForCollapsingToolbar(this,
                appBar, collapsingToolbar, toolbar,
                ContextCompat.getColor(this, R.color.priority_yellow));

        ivImage.post(() -> {
            height = ivImage.getHeight();
            width = ivImage.getWidth();
            top = ivImage.getTop();
            LogUtils.e("CustomScrollView"+"++++++"+height+"-----"+width+"-----"+top);
        });
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
            int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset == 0) {
                if (state != Constant.STATES.EXPANDED) {
                    //修改状态标记为展开
                    state = Constant.STATES.EXPANDED;
                    Log.e("CustomScrollView","修改状态标记为展开");
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)
                            ivImage.getLayoutParams();
                    layoutParams.height = 240;
                    layoutParams.width = 240;
                    layoutParams.topMargin = 300;
                    ivImage.setLayoutParams(layoutParams);
                }
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            } else if (Math.abs(verticalOffset) >= totalScrollRange) {
                if (state != Constant.STATES.COLLAPSED) {
                    //修改状态标记为折叠
                    state = Constant.STATES.COLLAPSED;
                    Log.e("CustomScrollView","修改状态标记为折叠");
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)
                            ivImage.getLayoutParams();
                    layoutParams.height = 80;
                    layoutParams.width = 80;
                    layoutParams.topMargin = 120;
                    ivImage.setLayoutParams(layoutParams);
                }
            } else {
                if (state != Constant.STATES.INTERMEDIATE) {
                    //修改状态标记为中间
                    state = Constant.STATES.INTERMEDIATE;
                    //代码设置是否拦截事件
                    Log.e("CustomScrollView","修改状态标记为中间");
                }
                Log.e("CustomScrollView","中间-------------杨"+verticalOffset);
                Log.e("CustomScrollView","中间+++++++++++++杨"+totalScrollRange);
                int absVerticalOffset = Math.abs(verticalOffset);
                int startOffset = absVerticalOffset;
                int height = totalScrollRange - absVerticalOffset;
                Log.e("CustomScrollView","中间间距杨"+height);
                //正常 240*240   top  300
                //缩放到最小 80*80       top   60
                float scale = height/(totalScrollRange*1.0f);
                Log.e("CustomScrollView","比例大小"+scale);

                int spacing1 = 240-80;
                int spacing2 = 300-120;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)
                        ivImage.getLayoutParams();
                //指的是手指滑动
                if (startOffset > Math.abs(verticalOffset)){
                    //从下到上滑动，图片时放大并且向下移动
                    int mHeight = (int) (80 + spacing1*scale);
                    int mWeight = (int) (80 + spacing1*scale);
                    int mTop = (int) (120 + spacing2*scale);
                    Log.e("CustomScrollView","宽高大小"+mHeight);
                    layoutParams.height = mHeight;
                    layoutParams.width = mWeight;
                    layoutParams.topMargin = mTop;
                }else {
                    //从上到下滑动，图片是缩小并且向上移动
                    int mHeight = (int) (width - spacing1*(1-scale));
                    int mWeight = (int) (width - spacing1*(1-scale));
                    int mTop = (int) (top - spacing2*(1-scale));
                    Log.e("CustomScrollView","宽高大小"+mHeight);
                    layoutParams.height = mHeight;
                    layoutParams.width = mWeight;
                    layoutParams.topMargin = mTop;
                }
                ivImage.setLayoutParams(layoutParams);
            }
            scrollView.setStates(state);
        });
    }



    @Override
    public void initData() {

    }


}
