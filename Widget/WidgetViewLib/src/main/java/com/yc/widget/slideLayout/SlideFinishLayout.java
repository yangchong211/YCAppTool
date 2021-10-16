package com.yc.widget.slideLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * 自定义可以滑动的RelativeLayout, 类似于IOS的滑动删除页面效果，当我们要使用
 * 此功能的时候，需要将该Activity的顶层布局设置为SlideFinishLayout，
 */
public class SlideFinishLayout extends RelativeLayout {

    private final String TAG = SlideFinishLayout.class.getName();

    /**
     * SlideFinishLayout布局的父布局
     */
    private ViewGroup mParentView;

    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 按下点的X坐标
     */
    private int downX;
    /**
     * 按下点的Y坐标
     */
    private int downY;
    /**
     * 临时存储X坐标
     */
    private int tempX;
    /**
     * 滑动类
     */
    private Scroller mScroller;
    /**
     * SlideFinishLayout的宽度
     */
    private int viewWidth;
    /**
     * 记录是否正在滑动
     */
    private boolean isSlide;
    /**
     * 是否开启左侧切换事件
     */
    private boolean enableLeftSlideEvent = true;
    /**
     * 是否开启右侧切换事件
     */
    private boolean enableRightSlideEvent = true;
    /**
     * 按下时范围(处于这个范围内就启用切换事件，目的是使当用户从左右边界点击时才响应)
     */
    private int size ;
    /**
     * 是否拦截触摸事件
     */
    private boolean isIntercept = false;
    /**
     * 是否可切换
     */
    private boolean canSwitch;
    /**
     * 左侧切换
     */
    private boolean isSwitchFromLeft = false;
    /**
     * 右侧侧切换
     */
    private boolean isSwitchFromRight = false;


    public SlideFinishLayout(Context context) {
        super(context);
        init(context);
    }
    public SlideFinishLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }
    public SlideFinishLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Log.i(TAG, "设备的最小滑动距离:" + mTouchSlop);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 获取SlideFinishLayout所在布局的父布局
            mParentView = (ViewGroup) this.getParent();
            viewWidth = this.getWidth();
            size = viewWidth;
        }
        Log.i(TAG, "viewWidth=" + viewWidth);
    }


    public void setEnableLeftSlideEvent(boolean enableLeftSlideEvent) {
        this.enableLeftSlideEvent = enableLeftSlideEvent;
    }


    public void setEnableRightSlideEvent(boolean enableRightSlideEvent) {
        this.enableRightSlideEvent = enableRightSlideEvent;
    }


    /**
     * 设置OnSlideFinishListener, 在onSlideFinish()方法中finish Activity
     * @param onSlideFinishListener         onSlideFinishListener
     */
    private OnSlideFinishListener onSlideFinishListener;
    public void setOnSlideFinishListener(OnSlideFinishListener onSlideFinishListener) {
        this.onSlideFinishListener = onSlideFinishListener;
    }


    /**
     * 是否拦截事件，如果不拦截事件，对于有滚动的控件的界面将出现问题(相冲突)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float downX = ev.getRawX();
        Log.i(TAG, "downX =" + downX + ",viewWidth=" + viewWidth);
        if(enableLeftSlideEvent && downX < size){
            Log.e(TAG, "downX 在左侧范围内 ,拦截事件");
            isIntercept = true;
            isSwitchFromLeft = true;
            isSwitchFromRight = false;
            return false;
        }else if(enableRightSlideEvent && downX > (viewWidth - size)){
            Log.i(TAG, "downX 在右侧范围内 ,拦截事件");
            isIntercept = true;
            isSwitchFromRight = true;
            isSwitchFromLeft = false;
            return true;
        }else{
            Log.i(TAG, "downX 不在范围内 ,不拦截事件");
            isIntercept = false;
            isSwitchFromLeft = false;
            isSwitchFromRight = false;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //不拦截事件时 不处理
        if(!isIntercept){
            Log.d(TAG,"false------------");
            return false;
        }
        Log.d(TAG,"true-----------");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) event.getRawX();
                downY = (int) event.getRawY();
                Log.d(TAG,"downX---"+downX+"downY---"+downY);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int deltaX = tempX - moveX;
                tempX = moveX;
                if (Math.abs(moveX - downX) > mTouchSlop && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                    isSlide = true;
                }
                Log.e(TAG, "scroll deltaX=" + deltaX);
                //左侧滑动
                if(enableLeftSlideEvent){
                    if (moveX - downX >= 0 && isSlide) {
                        mParentView.scrollBy(deltaX, 0);
                    }
                }
                //右侧滑动
                if(enableRightSlideEvent){
                    if (moveX - downX <= 0 && isSlide) {
                        mParentView.scrollBy(deltaX, 0);
                    }
                }
                Log.i(TAG + "/onTouchEvent", "mParentView.getScrollX()=" + mParentView.getScrollX());
                break;
            case MotionEvent.ACTION_UP:
                isSlide = false;
                //mParentView.getScrollX() <= -viewWidth / 2  ==>指左侧滑动
                //mParentView.getScrollX() >= viewWidth / 2   ==>指右侧滑动
                if (mParentView.getScrollX() <= -viewWidth / 2 || mParentView.getScrollX() >= viewWidth / 2) {
                    canSwitch = true;
                    if(isSwitchFromLeft){
                        scrollToRight();
                    }

                    if(isSwitchFromRight){
                        scrollToLeft();
                    }
                } else {
                    scrollOrigin();
                    canSwitch = false;
                }
                break;
            default:
                break;
        }
        return true;
    }


    /**
     * 滚动出界面至右侧
     */
    private void scrollToRight() {
        final int delta = (viewWidth + mParentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta + 1, 0, Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动出界面至左侧
     */
    private void scrollToLeft() {
        final int delta = (viewWidth - mParentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        //此处就不可用+1，也不卡直接用delta
        mScroller.startScroll(mParentView.getScrollX(), 0, delta - 1, 0, Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int delta = mParentView.getScrollX();
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }



    @Override
    public void computeScroll(){
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished()) {
                if (onSlideFinishListener != null && canSwitch) {
                    //回调，左侧切换事件
                    if(isSwitchFromLeft){
                        onSlideFinishListener.onSlideBack();
                    }
                    //右侧切换事件
                    if(isSwitchFromRight){
                        onSlideFinishListener.onSlideForward();
                    }
                }
            }
        }
    }


    public interface OnSlideFinishListener {
        void onSlideBack();
        void onSlideForward();
    }

}
