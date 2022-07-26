package com.yc.appchartview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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
    private PieChartView pieChartView;
    private LinearLayout llListLayout;

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
        pieChartView = view.findViewById(R.id.pie_chart_view);
        llListLayout = view.findViewById(R.id.ll_list_layout);
    }

    public void setData(ArrayList<PieFlagData> pieDataList) {
        if (pieChartView!=null){
            pieChartView.setData(pieDataList);
        }
        if (llListLayout!=null){
            llListLayout.removeAllViews();
            for (int i=0 ; i<pieDataList.size() ; i++){
                TextView textView = new TextView(context);
                textView.setText(pieDataList.get(i).getTitle());
                llListLayout.addView(textView);
            }
        }
    }
}
