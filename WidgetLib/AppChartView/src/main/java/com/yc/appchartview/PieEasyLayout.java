package com.yc.appchartview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
public class PieEasyLayout extends FrameLayout {

    private final Context context;
    private PieChartView pieChartView;
    private LinearLayout llListLayout;
    private TextView tvMaxPercent;
    private TextView tvMaxPercentTitle;

    public PieEasyLayout(@NonNull Context context) {
        this(context, null);
    }

    public PieEasyLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieEasyLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
            float dimension1 = this.getResources().getDimension(R.dimen.textSize28);
            float dimension2 = this.getResources().getDimension(R.dimen.textSize10);
            SpannableStringBuilder spannableString = getSpan(String.valueOf(maxPercent),
                    "%" ,dimension1,dimension2);
            tvMaxPercent.setText(spannableString);
            tvMaxPercentTitle.setText(title);
        }
        if (pieChartView!=null){
            pieChartView.setData(pieDataList);
        }
        if (llListLayout!=null){
            llListLayout.removeAllViews();
            for (int i=0 ; i<pieDataList.size() ; i++){
                View view = LayoutInflater.from(context).inflate(
                        R.layout.item_desk_pie_text, null,false);
                TextView tvDeskPieTitle = view.findViewById(R.id.tv_desk_pie_title);
                TextView tvDeskPiePercent = view.findViewById(R.id.tv_desk_pie_percent);
                TextView tvDeskPieCount = view.findViewById(R.id.tv_desk_pie_count);
                tvDeskPieTitle.setText(pieDataList.get(i).getTitle());
                tvDeskPieTitle.setTextColor(Color.parseColor(pieDataList.get(i).getGetColor()));

                int percent = (int) (pieDataList.get(i).getPercentage() * 100);
                float dimension1 = this.getResources().getDimension(R.dimen.textSize19);
                float dimension2 = this.getResources().getDimension(R.dimen.textSize10);
                SpannableStringBuilder spannableString1 = getSpan(String.valueOf(percent),"%" ,dimension1,dimension2);
                tvDeskPiePercent.setText(spannableString1);
                SpannableStringBuilder spannableString2 = getSpan("50","次" ,dimension1,dimension2);
                tvDeskPieCount.setText(spannableString2);
                llListLayout.addView(view);
            }
        }
    }

    private SpannableStringBuilder getSpan(String text1 , String text2 , float dimension1, float dimension2) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(String.valueOf(text1));
        AbsoluteSizeSpan absoluteSizeSpan1 = new AbsoluteSizeSpan((int) dimension1);
        int length1 = spannableString.toString().length();
        spannableString.setSpan(absoluteSizeSpan1, 0, length1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan, 0, length1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(
                Color.parseColor("#000000"));
        spannableString.setSpan(colorSpan1, 0, length1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        spannableString.append(text2);
        int length2 = spannableString.toString().length();
        AbsoluteSizeSpan absoluteSizeSpan2 = new AbsoluteSizeSpan((int) dimension2);
        spannableString.setSpan(absoluteSizeSpan2, length1, length2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(
                Color.parseColor("#626466"));
        spannableString.setSpan(colorSpan2, length1, length2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
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

}
