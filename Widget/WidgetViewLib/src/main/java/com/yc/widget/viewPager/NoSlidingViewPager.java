package com.yc.widget.viewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;



/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/3/22
 * 描    述：禁止左右滑动
 * 修订历史：
 * ================================================
 */
public class NoSlidingViewPager extends ViewPager {

    public NoSlidingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //去掉ViewPager默认的滑动效果， 不消费事件
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //不让拦截事件
        return false;
    }

}
