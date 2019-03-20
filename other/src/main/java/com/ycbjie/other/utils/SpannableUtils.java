package com.ycbjie.other.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.ycbjie.other.R;

/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2018/6/11.
 *     desc  : 字符串拼接
 *     revise:
 *     参考博客：https://blog.csdn.net/a214024475/article/details/53261122
 * </pre>
 */
public class SpannableUtils {

    private static final int beforeColor = Color.parseColor("#009ad6");
    private static final int afterColor = Color.parseColor("#009ad6");

    @SuppressLint("Range")
    public static SpannableStringBuilder appendStringAndString(String beforeText , String afterText,
                                                               int beforeSize, int afterSize){
        //创建SpannableStringBuilder，并添加前面文案
        SpannableStringBuilder builder = new SpannableStringBuilder(beforeText);
        //设置前面的字体颜色
        builder.setSpan(new ForegroundColorSpan(beforeColor),
                0, beforeText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置前面的字体大小
        builder.setSpan(new AbsoluteSizeSpan(beforeSize, true),
                0, beforeText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        //追加后面文案
        builder.append(afterText);
        //设置后面的字体颜色
        builder.setSpan(new ForegroundColorSpan(afterColor),
                beforeText.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置后面的字体大小
        builder.setSpan(new AbsoluteSizeSpan(afterSize, true),
                beforeText.length(), builder.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return builder;
    }


    public static SpannableStringBuilder appendString(
            Context context , String beforeText , String afterText){
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        String thisTag = " " + beforeText + " ";
        stringBuilder.append(thisTag);
        stringBuilder.append("  ");
        stringBuilder.append(afterText);
        RoundedBackgroundSpan span;
        span= new RoundedBackgroundSpan(ContextCompat.getColor(context,R.color.colorAccent), Color.WHITE);
        stringBuilder.setSpan(span, 0, thisTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append(" ");
        return stringBuilder;
    }


}
