package com.ns.yc.lifehelper.weight.imageView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 圆形ImageView组件 点击事件不会穿透
 * 
 * @author kymjs(kymjs123@gmail.com)
 */
public class NoteItemCircleView extends CircleImageView {

    public NoteItemCircleView(Context context) {
        super(context);
    }

    public NoteItemCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoteItemCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }
}