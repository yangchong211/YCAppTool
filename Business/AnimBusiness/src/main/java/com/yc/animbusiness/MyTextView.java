package com.yc.animbusiness;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.yc.animation.utils.LoggerUtil;


/**
 * @author 杨充
 * @date 2017/6/13 17:50
 */
public class MyTextView extends AppCompatTextView {

    private static final String TAG = "MyTextView";

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScale(float scale) {
        LoggerUtil.d(TAG, "setScale: " + scale);
        setScaleX(scale);
        setScaleY(scale);
    }
}
