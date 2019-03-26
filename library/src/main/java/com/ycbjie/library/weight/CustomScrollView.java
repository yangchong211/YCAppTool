package com.ycbjie.library.weight;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.animation.SpringAnimation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ycbjie.library.constant.Constant;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/03/13
 *     desc  : 自定义具有弹簧效果的控件
 *     revise: 1.支持滑动松手后控件带有弹簧效果
 *             2.可以设置下滑滑动的最大位移，超过最大值不让滑动
 *             3.设置view移动位移是滑动距离的比例相同，该比例可以设置
 *             4.页面销毁时，清除动画
 *             5.支持动态设置阻尼回弹次数，目前设置是2次，可以设置回弹速度
 *             6.需要解决和父布局的滑动冲突
 *             7.当滑动到顶部或者底部的时候，让父View消费事件
 * </pre>
 */
public class CustomScrollView extends NestedScrollView {

    /**
     * 手指开始移动的y
     */
    private float startDragY;
    /**
     * 阻尼弹簧动画
     */
    private SpringAnimation springAnim;
    /**
     * 设置支持滑动最大的位移
     */
    private static final int MAX_MOVE = 400;
    /**
     * ScrollView的子View， 也是ScrollView的唯一一个子View
     */
    private View contentView;
    private float downX;
    private float downY;
    /**
     * 状态，折叠控件
     */
    private int mState = Constant.STATES.EXPANDED;
    public void setStates(int state) {
        mState = state;
    }


    public CustomScrollView(@NonNull Context context) {
        this(context,null);
    }

    public CustomScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnimation();
    }

    private void initAnimation() {
        springAnim = new SpringAnimation(this, SpringAnimation.TRANSLATION_Y, 0);
        //刚度 默认1200 值越大回弹的速度越快
        //Stiffness刚度(劲度/弹性)，刚度越大，形变产生的里也就越大，体现在效果上就是运动越快
        springAnim.getSpring().setStiffness(400.0f);
        //阻尼 默认0.5 值越小，回弹之后来回的次数越多
        springAnim.getSpring().setDampingRatio(0.50f);
        //设置时间
        springAnim.setStartVelocity(2000);
    }

    /***
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后.
     * 即使子类覆盖了 onFinishInflate方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
        }
        super.onFinishInflate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //当view销毁的时候，需要关闭动画资源
        if (springAnim!=null){
            springAnim.cancel();
            springAnim = null;
        }
    }

    /**
     * 分发事件
     * 根据内部拦截状态，向其child或者自己分发事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                //如果滑动到了最底部，就允许继续向上滑动，可以加载那个更多【listView，下一页等】
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - downX;
                float dy = ev.getY() - downY;
                boolean allowParentTouchEvent;
                if (Math.abs(dy) > Math.abs(dx)) {
                    if (dy > 0) {
                        //位于顶部时下拉，让父View消费事件
                        allowParentTouchEvent = isTop();
                    } else {
                        //位于底部时上拉，让父View消费事件
                        allowParentTouchEvent = isBottom();
                    }
                } else {
                    //水平方向滑动
                    allowParentTouchEvent = true;
                }
                getParent().requestDisallowInterceptTouchEvent(!allowParentTouchEvent);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("NewApi")
    public boolean isTop() {
        return !canScrollVertically(-1);
    }

    public boolean isBottom() {
        return !canScrollVertically(1);
    }

    /**
     * 触摸事件
     * 如果返回结果为false表示不消费该事件，并且也不会截获接下来的事件序列，事件会继续传递
     * 如果返回为true表示当前View消费该事件，阻止事件继续传递
     *
     * 在这里要强调View的OnTouchListener。如果View设置了该监听，那么OnTouch()将会回调。
     * 如果返回为true那么该View的OnTouchEvent将不会在执行 这是因为设置的OnTouchListener执行时的优先级要比onTouchEvent高。
     * 优先级：OnTouchListener > onTouchEvent > onClickListener
     * @param event
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (mState==Constant.STATES.EXPANDED){
            //只有当控件折叠后继续滑动，才触发走触摸事件监听
            switch (e.getAction()) {
                //手指移动【主要是分为两种：向上滑动，向下滑动】
                case MotionEvent.ACTION_MOVE:
                    if (getScrollY() <= 0) {
                        //顶部下拉
                        if (startDragY == 0) {
                            startDragY = e.getRawY();
                        }
                        // 滑动距离
                        int deltaY = (int) (e.getRawY() - startDragY);
                        if (deltaY > 0 && deltaY < MAX_MOVE) {
                            Log.e("CustomScrollView","onTouchEvent-----------------"+deltaY);
                            //设置view下滑的位移是手指滑动距离位移的三分之一
                            setTranslationY((e.getRawY() - startDragY));
                            return true;
                        } else if (deltaY>MAX_MOVE){
                            Log.e("CustomScrollView","onTouchEvent-----------------"+deltaY);
                            setTranslationY(MAX_MOVE);
                            return true;
                        } else {
                            Log.e("CustomScrollView","onTouchEvent-----------------"+deltaY);
                            springAnim.cancel();
                            setTranslationY(0);
                        }
                    } else if ((getScrollY() + getHeight()) >= contentView.getMeasuredHeight()) {
                        //底部上拉
                        if (startDragY == 0) {
                            //时时y坐标
                            startDragY = e.getRawY();
                        }
                        // 滑动距离
                        int deltaY = (int) (e.getRawY() - startDragY);
                        if (deltaY < 0) {
                            Log.e("CustomScrollView","1-----------------"+deltaY);
                            setTranslationY((e.getRawY() - startDragY) / 3);
                            return true;
                        } else {
                            Log.e("CustomScrollView","2-----------------"+deltaY);
                            springAnim.cancel();
                            setTranslationY(0);
                        }
                    }
                    break;
                //手指抬起
                case MotionEvent.ACTION_UP:
                    //手指滑动结束
                case MotionEvent.ACTION_CANCEL:
                    if (getTranslationY() != 0) {
                        //当手指抬起的时候，就开始动画
                        springAnim.start();
                    }
                    startDragY = 0;
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(e);
    }

    /**
     * 拦截事件
     * 默认实现是返回false，也就是默认不拦截任何事件
     *
     * 判断自己是否需要截取事件
     * 如果该方法返回为true，那么View将消费该事件，即会调用onTouchEvent()方法
     * 如果返回false,那么通过调用子View的dispatchTouchEvent()将事件交由子View来处理
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("onEvent","MyLinearLayout onInterceptTouchEvent");
        return super.onInterceptTouchEvent(ev);
    }

}
