package com.yc.slideview;

import android.animation.ValueAnimator;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/7/3
 * 描    述：ViewHolder抽取类
 * 修订历史：
 * 项目地址：https://github.com/yangchong211/YCSlideView
 * ================================================
 */
public abstract class SlideViewHolder extends RecyclerView.ViewHolder implements Slide {

    private static final int DURATION_OPEN = 300;
    private static final int DURATION_CLOSE = 150;
    //默认正常偏移的量为50
    private static final int NORMAL_OFFSET = 70;
    private SlideAnimationHelper mSlideAnimationHelper;
    private OpenUpdateListener mOpenUpdateListener;
    private CloseUpdateListener mCloseUpdateListener;
    private int mOffset;

    public SlideViewHolder(View itemView) {
        super(itemView);
        mOffset = SlideAnimationHelper.getOffset(itemView.getContext(), NORMAL_OFFSET);
        mSlideAnimationHelper = new SlideAnimationHelper();
    }

    protected void setOffset(int offset) {
        mOffset = SlideAnimationHelper.getOffset(itemView.getContext(), offset);
    }

    public int getOffset() {
        return mOffset;
    }

    //keep change state
    public void onBindSlide(View targetView) {
        switch (mSlideAnimationHelper.getState()) {
            case SlideAnimationHelper.STATE_CLOSE:
                targetView.scrollTo(0, 0);
                onBindSlideClose(SlideAnimationHelper.STATE_CLOSE);
                break;
            case SlideAnimationHelper.STATE_OPEN:
                targetView.scrollTo(-mOffset, 0);
                doAnimationSetOpen(SlideAnimationHelper.STATE_OPEN);
                break;
            default:
                break;
        }
    }

    /**
     * 关闭时调用的方法
     */
    @Override
    public void slideOpen() {
        if (mOpenUpdateListener == null) {
            mOpenUpdateListener = new OpenUpdateListener();
        }
        mSlideAnimationHelper.openAnimation(DURATION_OPEN, mOpenUpdateListener);
    }

    /**
     * 打开时调用的方法
     */
    @Override
    public void slideClose() {
        if (mCloseUpdateListener == null) {
            mCloseUpdateListener = new CloseUpdateListener();
        }
        mSlideAnimationHelper.closeAnimation(DURATION_CLOSE, mCloseUpdateListener);
    }

    //以下几个方法子类必须继承
    public abstract void doAnimationSet(int offset, float fraction);

    public abstract void onBindSlideClose(int state);

    public abstract void doAnimationSetOpen(int state);


    private class OpenUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float fraction = animation.getAnimatedFraction();
            int endX = (int) (-mOffset * fraction);
            doAnimationSet(endX, fraction);
        }
    }


    private class CloseUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float fraction = animation.getAnimatedFraction();
            fraction = 1.0f - fraction;
            int endX = (int) (-mOffset * fraction);
            doAnimationSet(endX, fraction);
        }
    }

}
