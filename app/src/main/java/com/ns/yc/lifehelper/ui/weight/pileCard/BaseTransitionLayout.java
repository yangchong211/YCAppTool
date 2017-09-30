package com.ns.yc.lifehelper.ui.weight.pileCard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class BaseTransitionLayout extends FrameLayout {

    public BaseTransitionLayout(Context context) {
        this(context, null);
    }

    public BaseTransitionLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTransitionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addViewWhenFinishInflate();
    }

    public abstract void addViewWhenFinishInflate();

    public abstract void firstInit(String info);

    public abstract void onAnimationEnd();

    public abstract void duringAnimation(float rate);

    public abstract void saveNextPosition(int position, String info);
}
