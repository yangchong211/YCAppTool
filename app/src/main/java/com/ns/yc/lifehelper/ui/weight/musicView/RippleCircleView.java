package com.ns.yc.lifehelper.ui.weight.musicView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/24
 * 描    述：豆瓣音乐识别音乐震动效果
 * 修订历史：
 * ================================================
 */
public class RippleCircleView extends View {

    private RippleAnimationView rippleAnimationView;

    public RippleCircleView(Context context) {
        super(context);
    }

    public RippleCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RippleCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RippleCircleView(RippleAnimationView rippleAnimationView, Context context) {
        super(context);
        this.rippleAnimationView = rippleAnimationView;
        this.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = (Math.min(getWidth(), getHeight())) / 2;
        canvas.drawCircle(radius, radius, radius - rippleAnimationView.rippleStrokeWidth, rippleAnimationView.paint);
    }
}
