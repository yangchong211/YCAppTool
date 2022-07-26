package com.yc.appchartview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     GitHub : https://github.com/yangchong211/YCWidgetLib
 *     time  : 2018/11/9
 *     desc  : 圆形图表控件
 *     revise:
 * </pre>
 */
public class PieChartLayout extends FrameLayout {

    private final Context context;

    public PieChartLayout(@NonNull Context context) {
        this(context, null);
    }

    public PieChartLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_pie_chart_view, this, true);
        initFindViewById(view);
    }

    private void initFindViewById(View view) {

    }
}
