package com.yc.notifymessage;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 上划隐藏的布局
 *     revise:
 * </pre>
 */
public class NotifyContainerView extends FrameLayout {

    private static final int SLOP = 10;
    private float mLastY;
    /**
     * 是否上滑隐藏布局
     */
    private boolean mIsCollapsible;
    /**
     * 是否消费触摸事件
     */
    private boolean mIsConsumeTouchEvent;
    /**
     * 消失监听listener
     */
    private OnDismissListener mOnDismissListener;

    public NotifyContainerView(@NonNull Context context) {
        this(context,null);
    }

    public NotifyContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NotifyContainerView(@NonNull Context context,
                        @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCollapsible(boolean collapsible) {
        mIsCollapsible = collapsible;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    /**
     * 分发事件：使用对象	Activity、ViewGroup、View
     * @param event                         event
     * @return                              返回值
     * true： 消费事件；事件不会往下传递；后续事件（Move、Up）会继续分发到该View
     * false：不消费事件；事件不会往下传递；将事件回传给父控件的onTouchEvent()处理；Activity例外：返回false=消费事件
     *        后续事件（Move、Up）会继续分发到该View(与onTouchEvent()区别）
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!mIsCollapsible) {
            return super.dispatchTouchEvent(event);
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastY = event.getY();
                mIsConsumeTouchEvent = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsConsumeTouchEvent) {
                    break;
                }
                float mCurrentY = event.getY();
                if (mLastY - mCurrentY > SLOP) {
                    View child = getChildAt(0);
                    if (child != null) {
                        if (mOnDismissListener != null) {
                            mOnDismissListener.onDismiss();
                        }
                        mIsConsumeTouchEvent = true;
                        //表示自己消费事件
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsConsumeTouchEvent = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Nullable
    public Activity getActivity() {
        return getContext() instanceof Activity ? (Activity) getContext() : null;
    }
}
