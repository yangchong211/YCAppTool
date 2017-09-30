package com.ns.yc.lifehelper.ui.weight.pileCard;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ns.yc.lifehelper.R;


public class FadeTransitionImageView extends BaseTransitionLayout {

    private ImageView imageView1, imageView2;

    protected int currentPosition = -1;
    protected int nextPosition = -1;

    public FadeTransitionImageView(Context context) {
        this(context, null);
    }

    public FadeTransitionImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FadeTransitionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.scene);
        a.recycle();
    }

    @Override
    public void addViewWhenFinishInflate() {
        imageView1 = new ImageView(getContext());
        ViewGroup.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imageView1, lp1);

        imageView2 = new ImageView(getContext());
        LayoutParams lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imageView2, lp2);
    }


    @Override
    public void firstInit(String url) {
        Glide.with(getContext()).load(url).into(imageView1);
        currentPosition = 0;
    }

    @Override
    public void onAnimationEnd() {
        currentPosition = nextPosition;
        ImageView tmp = imageView1;
        imageView1 = imageView2;
        imageView2 = tmp;
    }

    /**
     * rate从零到1
     */
    @Override
    public void duringAnimation(float rate) {
        imageView1.setAlpha(1 - rate);
        imageView2.setAlpha(rate);
    }

    @Override
    public void saveNextPosition(int position, String url) {
        this.nextPosition = position;
        Glide.with(getContext()).load(url).into(imageView2);
    }
}