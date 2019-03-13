package com.ycbjie.library.weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public final class FlingBehavior extends AppBarLayout.Behavior {

    /**
     * 是否处于惯性滑动状态
     */
    private boolean isFlinging = false;

    public FlingBehavior() {

    }

    public FlingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl,
                                   View target, int type) {
        //如果不是惯性滑动,让他可以执行紧贴操作
        if (!isFlinging) {
            super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        }
    }
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  AppBarLayout child, View target, int dx,
                                  int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        //type==1时处于非惯性滑动
        if (type == 1) {
            isFlinging = false;
        }
    }
    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout,
                                 @NonNull AppBarLayout child, @NonNull View target,
                                 float velocityX, float velocityY, boolean consumed) {
        //惯性滑动的时候设置为true
        isFlinging = true;
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }
}
