package com.yc.baseclasslib.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : 封装ViewPager
 *     revise:
 * </pre>
 */
public class ScrollViewPager extends ViewPager {

    private boolean scrollable = true;

    /**
     * 设置是否支持滚动
     * @param scrollable        是否滚动
     */
    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    public ScrollViewPager(Context context) {
        super(context);
    }

    public ScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.scrollable) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            }
            return super.onTouchEvent(ev);
        } else {
            //去掉ViewPager默认的滑动效果， 不消费事件
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.scrollable) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            }
            return super.onInterceptTouchEvent(ev);
        } else {
            //不让拦截事件
            return false;
        }
    }

    /**
     * ViewPager有预加载机制，即默认情况下当前页面左右两侧的1个页面会被加载
     * 以方便用户滑动切换到相邻的界面时，可以更加顺畅的显示出来。
     * 可以设置预加载页面数量，当前页面相邻的limit个页面会被预加载进内存。
     * @param limit     数量，默认是1
     */
    @Override
    public void setOffscreenPageLimit(int limit) {
        super.setOffscreenPageLimit(limit);
    }

}
