package com.yc.appchartview;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private TextView tvMaxPercent;
    private TextView tvMaxPercentTitle;

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
        tvMaxPercent = view.findViewById(R.id.tv_max_percent);
        tvMaxPercentTitle = view.findViewById(R.id.tv_max_percent_title);
    }

    public void setData(ArrayList<PieFlagData> pieDataList) {
        if (tvMaxPercent!=null && tvMaxPercentTitle!=null){
            PieFlagData max = max(pieDataList);
            int maxPercent = (int) (max.getPercentage() * 100);
            String title = max.getTitle();
            SpannableStringBuilder spannableString = new SpannableStringBuilder();
            spannableString.append(String.valueOf(maxPercent));
            float dimension1 = this.getResources().getDimension(R.dimen.textSize28);
            AbsoluteSizeSpan absoluteSizeSpan1 = new AbsoluteSizeSpan((int) dimension1);
            int length1 = spannableString.toString().length();
            spannableString.setSpan(absoluteSizeSpan1, 0, length1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableString.append("%");
            int length2 = spannableString.toString().length();
            float dimension2 = this.getResources().getDimension(R.dimen.textSize10);
            AbsoluteSizeSpan absoluteSizeSpan2 = new AbsoluteSizeSpan((int) dimension2);
            spannableString.setSpan(absoluteSizeSpan2, length1, length2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tvMaxPercent.setText(spannableString);
            tvMaxPercentTitle.setText(title);
        }
        if (pieChartView!=null){
            pieChartView.setData(pieDataList);
        }
        if (llListLayout!=null){
            llListLayout.removeAllViews();
            for (int i=0 ; i<pieDataList.size() ; i++){
                TextView textView = new TextView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,0,0,dip2px(context,20));
                textView.setLayoutParams(layoutParams);
                textView.setTextSize(10);

                SpannableStringBuilder spannableString = new SpannableStringBuilder();
                spannableString.append(pieDataList.get(i).getTitle());
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor(pieDataList.get(i).getGetColor()));
                spannableString.setSpan(colorSpan, 0, spannableString.toString().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                spannableString.append("\n");

                ForegroundColorSpan blackSpan = new ForegroundColorSpan(
                        Color.parseColor("#000000"));
                int percent = (int) (pieDataList.get(i).getPercentage() * 100);
                int percentStartLength = spannableString.length();
                spannableString.append(String.valueOf(percent));
                int percentEndLength = spannableString.length();
                spannableString.setSpan(blackSpan, percentStartLength, percentEndLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


                spannableString.append("%");
                spannableString.append("/");
                spannableString.append("50");
                spannableString.append("次");
                textView.setText(spannableString);
                llListLayout.addView(textView);
            }
        }
    }

    public static PieFlagData max(ArrayList<PieFlagData> list) {
        //排序
        Collections.sort(list, new Comparator<PieFlagData>() {
            @Override
            public int compare(PieFlagData data1, PieFlagData data2) {
                try {
                    //根据百分比排序
                    float lastModified01 = data1.getPercentage();
                    float lastModified02 = data2.getPercentage();
                    if (lastModified01 > lastModified02) {
                        return -1;
                    } else {
                        return 1;
                    }
                } catch (Exception e) {
                    return 1;
                }
            }
        });
        return list.get(list.size() - 1);
    }


    public static int dip2px(Context ctx, float dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public static int sp2px(Context context, final float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
