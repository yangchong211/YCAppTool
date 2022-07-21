package com.yc.expandlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCWidgetLib
 *     time  : 2019/04/24
 *     desc  : 自定义折叠布局
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCWidgetLib
 * </pre>
 */
public class ExpandLayout extends LinearLayout {

    private View layoutView;
    /**
     * 控件总高度
     */
    private int viewHeight;
    /**
     * 折叠时高度
     */
    private int expandHeight;
    /**
     * true表示折叠，false表示不折叠也就是伸展
     */
    private boolean isExpand;
    private long animationDuration;
    private boolean lock;
    private ValueAnimator animation;
    /**
     * 是否正在执行动画
     */
    private boolean isAnimate = false;

    public ExpandLayout(Context context) {
        this(context, null);
    }

    public ExpandLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * @param ev 触摸事件
     * @return 执行动画的过程中屏蔽事件传递
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //执行动画的过程中屏蔽事件传递
        return isAnimate;
    }

    private void initView() {
        layoutView = this;
        isExpand = false;
        animationDuration = 300;
    }

    /**
     * 初始化操作
     *
     * @param isExpand     初始状态是否折叠
     * @param expandHeight 折叠时高度
     */
    public void initExpand(boolean isExpand, int expandHeight) {
        this.isExpand = isExpand;
        setViewDimensions(expandHeight);
    }

    /**
     * 设置动画时间，暴露给外部调用
     *
     * @param animationDuration 动画时间
     */
    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    /**
     * 获取总高度
     */
    private void setViewDimensions(final int expandHeight) {
        this.expandHeight = expandHeight;
        final View childAt = getChildAt(0);
        layoutView.post(new Runnable() {
            @Override
            public void run() {
                int measuredHeight = childAt.getMeasuredHeight();
                viewHeight = layoutView.getMeasuredHeight();
                ExpandLogUtils.d("获取内容布局" + viewHeight + "-----" + expandHeight + "----" + measuredHeight);
                setViewHeight(layoutView, isExpand ? viewHeight : expandHeight);
            }
        });
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //至少要有一个子布局
        int childCount = getChildCount();
        if (childCount < 1) {
            throw new RuntimeException("ExpandLayout only accept childs more than 1!!");
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //销毁动画资源
        clearAnim();
    }

    /**
     * 设置view的高度
     *
     * @param view   view
     * @param height height
     */
    private void setViewHeight(View view, int height) {
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        //调用requestLayout刷新布局
        view.requestLayout();
    }

    /**
     * 切换动画实现
     */
    private void animateToggle(long animationDuration) {
        if (expandHeight == 0) {
            throw new NullPointerException("u should init expand first");
        }
        clearAnim();
        if (isExpand) {
            ExpandLogUtils.d("切换动画实现" + viewHeight + "-----" + expandHeight + "----折叠");
            animation = ValueAnimator.ofFloat(expandHeight, viewHeight);
        } else {
            ExpandLogUtils.d("切换动画实现" + viewHeight + "-----" + expandHeight + "----展开");
            animation = ValueAnimator.ofFloat(viewHeight, expandHeight);
        }
        animation.setDuration(animationDuration);
        animation.setStartDelay(animationDuration);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) (float) animation.getAnimatedValue();
                ExpandLogUtils.d("onAnimationUpdate----" + "-----" + value);
                setViewHeight(layoutView, value);
                /*if (value == viewHeight || value == 0) {
                    lock = false;
                }*/
            }
        });
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimate = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                isAnimate = false;
                if (listener != null) {
                    listener.onToggleExpand(isExpand);
                    lock = false;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimate = false;
                if (listener != null) {
                    listener.onToggleExpand(isExpand);
                    lock = false;
                }
            }
        });
        animation.start();
        lock = true;
    }

    /**
     * 清除动画资源
     */
    private void clearAnim() {
        if (animation != null) {
            animation.cancel();
            animation = null;
        }
    }

    /**
     * 查看控件是折叠还是展开状态
     *
     * @return true表示展开
     */
    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 折叠view
     */
    public void collapse() {
        isExpand = false;
        animateToggle(animationDuration);
    }

    /**
     * 展开view
     */
    public void expand() {
        isExpand = true;
        animateToggle(animationDuration);
    }

    /**
     * 这个是置反操作
     */
    public void toggleExpand() {
        if (lock) {
            return;
        }
        if (isExpand) {
            collapse();
        } else {
            expand();
        }
    }

    public interface OnToggleExpandListener {
        /**
         * 折叠或者展开操作后的监听
         *
         * @param isExpand 控件是折叠还是展开状态
         */
        void onToggleExpand(boolean isExpand);
    }

    private OnToggleExpandListener listener;

    /**
     * 设置折叠和展开的监听listener事件
     *
     * @param listener listener
     */
    public void setOnToggleExpandListener(OnToggleExpandListener listener) {
        this.listener = listener;
    }

}
