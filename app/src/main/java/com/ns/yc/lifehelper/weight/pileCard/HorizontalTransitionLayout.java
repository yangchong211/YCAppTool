package com.ns.yc.lifehelper.weight.pileCard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;


public class HorizontalTransitionLayout extends BaseTransitionLayout {

    private TextView textView1, textView2;

    protected int currentPosition = -1;
    protected int nextPosition = -1;

    private int leftMargin = 50;
    private float textSize = 22;
    private int textColor = Color.BLACK;
    private int leftDistance = 50;
    private int rightDistance = 450;

    public HorizontalTransitionLayout(Context context) {
        this(context, null);
    }

    public HorizontalTransitionLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalTransitionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.scene);
        leftMargin = a.getDimensionPixelSize(R.styleable.scene_leftMargin, leftMargin);
        textSize = a.getFloat(R.styleable.scene_textSize, textSize);
        textColor = a.getColor(R.styleable.scene_textColor, textColor);
        leftDistance = a.getDimensionPixelSize(R.styleable.scene_leftDistance, leftDistance);
        rightDistance = a.getDimensionPixelSize(R.styleable.scene_rightDistance, rightDistance);
        a.recycle();
    }

    @Override
    public void addViewWhenFinishInflate() {
        textView1 = new TextView(getContext());
        textView1.setGravity(Gravity.CENTER_VERTICAL);
        textView1.setTextSize(textSize);
        textView1.setTextColor(textColor);
        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lp1.setMargins(leftMargin, 0, 0, 0);
        addView(textView1, lp1);

        textView2 = new TextView(getContext());
        textView2.setGravity(Gravity.CENTER_VERTICAL);
        textView2.setTextSize(textSize);
        textView2.setTextColor(textColor);
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        lp2.setMargins(leftMargin, 0, 0, 0);
        addView(textView2, lp2);
    }

    @Override
    public void firstInit(String text) {
        this.textView1.setText(text);
        currentPosition = 0;
    }

    @Override
    public void onAnimationEnd() {
        currentPosition = nextPosition;
        TextView tmp = textView1;
        textView1 = textView2;
        textView2 = tmp;
    }

    /**
     * rate从零到1
     */
    @Override
    public void duringAnimation(float rate) {
        textView1.setAlpha(1 - rate);
        textView2.setAlpha(rate);

        if (nextPosition > currentPosition) {
            textView1.offsetLeftAndRight((int) (leftMargin - leftDistance * rate - textView1.getLeft()));
            textView2.offsetLeftAndRight((int) (leftMargin + rightDistance * (1 - rate) - textView2.getLeft()));
        } else {
            textView1.offsetLeftAndRight((int) (leftMargin + rightDistance * rate - textView1.getLeft()));
            textView2.offsetLeftAndRight((int) (leftMargin * rate - textView2.getLeft()));
        }
    }

    @Override
    public void saveNextPosition(int position, String text) {
        this.nextPosition = position;
        this.textView2.setText(text);
        this.textView2.setAlpha(0);
    }
}
