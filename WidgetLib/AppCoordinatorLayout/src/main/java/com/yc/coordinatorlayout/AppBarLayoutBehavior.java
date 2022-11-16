package com.yc.coordinatorlayout;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.google.android.material.appbar.AppBarLayout;

import java.lang.reflect.Field;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/03/13
 *     desc  : 自定义Behavior
 *     revise: 解决appbarLayout若干问题
 *              1）快速滑动appbarLayout会出现回弹
 *              2）快速滑动appbarLayout到折叠状态下，立马下滑，会出现抖动的问题
 *              3）滑动appbarLayout，无法通过手指按下让其停止滑动
 * </pre>
 */
public class AppBarLayoutBehavior extends AppBarLayout.Behavior {

    /*
     * CoordinatorLayout向上fling滚动无法被外部中断
     *     CoordinatorLayout和子View的联动时通过CoordinatorLayout.Behavior实现的。
     *     问题就在这里。HeaderBehavior的onTouchEvent中使用Scroller实现了fling操作，
     *     但是没有通过NestedScrolling API对外开放，也就说一旦HeaderBehavior的fling动作形成，无法由外部主动中断。
     * RecyclerView向下fling滚动
     *     与AppBarLayout同层级的RecyclerView可以通过升级过的NestedScrolling API对AppBarLayout产生影响，
     *     比如RecyclerView向下fling时滑动到item 0之后，如果AppBarLayout可以滑动时会给AppBarLayout
     *     施加一个同样向下的fling动作,以此形成一个连贯的下滑fling。
     *     那么问题来了。当HeaderBehavior产生的向上的fling没有结束时，RecyclerView又送来向下的fling，抖动就产生了。
     */


    private static final String TAG = "AppbarLayoutBehavior";
    private static final int TYPE_FLING = 1;
    private boolean isFlinging;
    private boolean shouldBlockNestedScroll;

    public AppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写分发事件，也就是说当手指抬起的时候，停止fling事件
     * @param parent
     * @param child
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        LogUtil.d(TAG, "onInterceptTouchEvent:" + child.getTotalScrollRange());
        shouldBlockNestedScroll = isFlinging;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //手指触摸屏幕的时候停止fling事件
                stopAppbarLayoutFling(child);
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    /**
     * 反射获取私有的flingRunnable 属性，考虑support 28以后变量名修改的问题
     * @return Field
     * @throws
     */
    private Field getFlingRunnableField() throws NoSuchFieldException {
        Class<?> superclass = this.getClass().getSuperclass();
        try {
            // support design 27及一下版本
            Class<?> headerBehaviorType = null;
            if (superclass != null) {
                headerBehaviorType = superclass.getSuperclass();
            }
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("mFlingRunnable");
            }else {
                return null;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            // 可能是28及以上版本
            Class<?> headerBehaviorType = superclass.getSuperclass().getSuperclass();
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("flingRunnable");
            } else {
                return null;
            }
        }
    }

    /**
     * 反射获取私有的scroller 属性，考虑support 28以后变量名修改的问题
     * @return Field
     * @throws
     */
    private Field getScrollerField() throws NoSuchFieldException {
        Class<?> superclass = this.getClass().getSuperclass();
        try {
            // support design 27及一下版本
            Class<?> headerBehaviorType = null;
            if (superclass != null) {
                headerBehaviorType = superclass.getSuperclass();
            }
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("mScroller");
            }else {
                return null;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            // 可能是28及以上版本
            Class<?> headerBehaviorType = superclass.getSuperclass().getSuperclass();
            if (headerBehaviorType != null) {
                return headerBehaviorType.getDeclaredField("scroller");
            }else {
                return null;
            }
        }
    }

    /**
     * 停止appbarLayout的fling事件
     * @param appBarLayout
     */
    private void stopAppbarLayoutFling(AppBarLayout appBarLayout) {
        //通过反射拿到HeaderBehavior中的flingRunnable变量
        try {
            Field flingRunnableField = getFlingRunnableField();
            Field scrollerField = getScrollerField();
            if (flingRunnableField != null) {
                flingRunnableField.setAccessible(true);
            }
            if (scrollerField != null) {
                scrollerField.setAccessible(true);
            }
            Runnable flingRunnable = null;
            if (flingRunnableField != null) {
                flingRunnable = (Runnable) flingRunnableField.get(this);
            }
            OverScroller overScroller = null;
            if (scrollerField != null) {
                overScroller = (OverScroller) scrollerField.get(this);
            }
            //移除flingRunnable
            if (flingRunnable != null) {
                LogUtil.d(TAG, "存在flingRunnable");
                appBarLayout.removeCallbacks(flingRunnable);
                flingRunnableField.set(this, null);
            }
            //移除动画
            if (overScroller != null && !overScroller.isFinished()) {
                overScroller.abortAnimation();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child,
                                       View directTargetChild, View target,
                                       int nestedScrollAxes, int type) {
        LogUtil.d(TAG, "onStartNestedScroll");
        //开始滚动的时候调用该方法
        stopAppbarLayoutFling(child);
        return super.onStartNestedScroll(parent, child, directTargetChild, target,
                nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  AppBarLayout child, View target,
                                  int dx, int dy, int[] consumed, int type) {
        LogUtil.d(TAG, "onNestedPreScroll:" + child.getTotalScrollRange()
                + " ,dx:" + dx + " ,dy:" + dy + " ,type:" + type);
        //type返回1时，表示当前target处于非touch的滑动，
        //该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动
        //子类还未结束其自身的fling
        //所以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
        if (type == TYPE_FLING) {
            isFlinging = true;
        }
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
                               View target, int dxConsumed, int dyConsumed, int
                                       dxUnconsumed, int dyUnconsumed, int type) {
        LogUtil.d(TAG, "onNestedScroll: target:" + target.getClass() + " ,"
                + child.getTotalScrollRange() + " ,dxConsumed:"
                + dxConsumed + " ,dyConsumed:" + dyConsumed + " " + ",type:" + type);
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                    dyConsumed, dxUnconsumed, dyUnconsumed, type);
        }
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl,
                                   View target, int type) {
        LogUtil.d(TAG, "onStopNestedScroll");
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        //滚动停止后需要回执所有的状态
        isFlinging = false;
        shouldBlockNestedScroll = false;
    }

    private static class LogUtil{
        static void d(String tag, String string){
            Log.d(tag,string);
        }
    }

}
