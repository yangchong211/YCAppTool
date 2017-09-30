package com.ns.yc.lifehelper.ui.weight.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**Created by yangchong on 2016/9/22.
 * 禁止左右滑动ViewPager
 */
public class NoSlidingViewPager extends ViewPager {

    public NoSlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
