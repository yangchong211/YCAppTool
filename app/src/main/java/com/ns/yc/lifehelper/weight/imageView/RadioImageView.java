package com.ns.yc.lifehelper.weight.imageView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/1
 * 描    述：一个能保持比例的 ImageView
 * 修订历史：TODO: 暂时只支持维持宽度适应高度
 * ================================================
 */
public class RadioImageView extends ImageView {

    private int originalWidth;
    private int originalHeight;

    public RadioImageView(Context context) {
        super(context);
    }

    public RadioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOriginalSize(int originalWidth, int originalHeight) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (originalWidth > 0 && originalHeight > 0) {
            float ratio = (float) originalWidth / (float) originalHeight;

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            // TODO: 现在只支持固定宽度
            if (width > 0) {
                height = (int) ((float) width / ratio);
            }

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
