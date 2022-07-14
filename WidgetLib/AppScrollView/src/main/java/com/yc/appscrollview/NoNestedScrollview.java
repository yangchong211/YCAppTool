package com.yc.appscrollview;

import android.content.Context;
import androidx.core.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;



/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/6/23
 * 描    述：ScrollView屏蔽 滑动事件
 * 修订历史：
 * ================================================
 */
public class NoNestedScrollview extends NestedScrollView {

    private int downX;
    private int downY;
    private int mTouchSlop;

    public NoNestedScrollview(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public NoNestedScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public NoNestedScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    private NestedScrollViewListener scrollViewListener = null;
    public void setScrollViewListener(NestedScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }
    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scrollViewListener!=null){
            scrollViewListener.onScroll(getScrollY());
        }
    }
}
