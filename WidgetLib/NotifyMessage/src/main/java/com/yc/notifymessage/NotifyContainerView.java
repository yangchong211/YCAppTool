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
    private boolean mIsCollapsible;
    private boolean mIsConsumeTouchEvent;
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
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsConsumeTouchEvent = false;
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Nullable
    public Activity getActivity() {
        return getContext() instanceof Activity ? (Activity) getContext() : null;
    }
}
