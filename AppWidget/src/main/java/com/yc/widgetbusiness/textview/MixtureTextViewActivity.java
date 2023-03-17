package com.yc.widgetbusiness.textview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.yc.customwidget.AlignImageSpan;
import com.yc.customwidget.RoundBackgroundColorSpan;
import com.yc.customwidget.RoundedBackgroundSpan;
import com.yc.customwidget.SpannableUtils;
import com.yc.customwidget.VerticalImageSpan;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;
import com.yc.widgetbusiness.R;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/3/11
 *     desc  : 图文混排
 *     revise:
 * </pre>
 */
public class MixtureTextViewActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv10;
    private TextView tv11;
    private TextView tv12;
    private TextView tv13;
    private TextView tv14;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, MixtureTextViewActivity.class);
            context.startActivity(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mixture_text);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        initView();
        initListener();
        initData();
    }

    public void initView() {
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tv6 = findViewById(R.id.tv_6);
        tv7 = findViewById(R.id.tv_7);
        tv8 = findViewById(R.id.tv_8);
        tv9 = findViewById(R.id.tv_9);
        tv10 = findViewById(R.id.tv_10);
        tv11 = findViewById(R.id.tv_11);
        tv12 = findViewById(R.id.tv_12);
        tv13 = findViewById(R.id.tv_13);
        tv14 = findViewById(R.id.tv_14);
    }

    public void initListener() {

    }

    public void initData() {
        initSetView();
    }

    private void initSetView() {
        mode1();
        mode2();
        mode3();
        mode4();
        mode5();
        mode6();
        mode7();
        mode8();
        mode9();
        mode10();
        mode11();
        mode12();
        mode13();
        mode14();
    }

    private void mode14() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影IV已经开始暴走了暗影IV已经开始暴走了");
        //Drawable drawable = getResources().getDrawable(R.drawable.study_shape_oval);
        ImageSpan imageSpan1 = new ImageSpan(this,R.drawable.pie_chart_shape_oval);
        spannableString.setSpan(imageSpan1, 0, 0, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ImageSpan imageSpan2 = new ImageSpan(this,R.drawable.pie_chart_shape_oval);
        spannableString.setSpan(imageSpan2, 3, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ImageSpan imageSpan3 = new ImageSpan(this,R.drawable.pie_chart_shape_oval);
        spannableString.setSpan(imageSpan3, 7, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ImageSpan imageSpan4 = new ImageSpan(this,R.drawable.pie_chart_shape_oval);
        spannableString.setSpan(imageSpan4, 11, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv14.setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——图片
     */
    private void mode1() {
        SpannableString spannableString = new
                SpannableString("      暗影IV已经开始暴走了暗影IV已经开始暴走了暗影IV已经开始暴走了");

        Drawable drawable = this.getResources().getDrawable(R.mipmap.camera);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan1 = new AlignImageSpan(drawable);
        //也可以这样
        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        //drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        //ImageSpan imageSpan1 = new ImageSpan(drawable);
        //将index为6、7的字符用图片替代
        //https://www.jianshu.com/p/2650357f7547
        //setSpan插入内容的时候，起始位置不替换，会替换起始位置到终止位置间的内容，含终止位置。
        //Spanned.SPAN_EXCLUSIVE_EXCLUSIVE模式用来控制是否同步设置新插入的内容与start/end 位置的字体样式，
        //此处没设置具体字体，所以可以随意设置
        spannableString.setSpan(imageSpan1, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


        Drawable drawable2 = this.getResources().getDrawable(R.mipmap.camera);
        drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
        ImageSpan imageSpan2 = new AlignImageSpan(drawable2);
        spannableString.setSpan(imageSpan2, 2, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv1.setText(spannableString);
    }


    /**
     * 使用SpannableString设置样式——字体颜色
     */
    private void mode2() {
        SpannableString spannableString = new SpannableString("暗影IV已经开始暴走了暗影IV已经开始暴走了");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(
                ContextCompat.getColor(this,R.color.colorPrimary));
        spannableString.setSpan(colorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(
                ContextCompat.getColor(this,R.color.colorAccent));
        spannableString.setSpan(colorSpan2, 9, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan3 = new ForegroundColorSpan(
                ContextCompat.getColor(this,R.color.colorPrimary));
        spannableString.setSpan(colorSpan3, 11, spannableString.length()-1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv2.setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——字体颜色
     */
    private void mode3() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影IV");
        spannableString.append("已经开始暴走了");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                ToastUtils.showRoundRectToast("请不要点我");
            }
        };
        spannableString.setSpan(clickableSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        ForegroundColorSpan colorSpan = new ForegroundColorSpan(
                ContextCompat.getColor(this,R.color.colorAccent));
        spannableString.setSpan(colorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        //tv3.setTextColor(this.getResources().getColor(R.color.bar_color));
        //tv3.setHighlightColor(Color.TRANSPARENT);
        tv3.setMovementMethod(LinkMovementMethod.getInstance());
        tv3.setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——背景颜色
     */
    private void mode4() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(" ");
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_red_point_shadow);
        drawable.setBounds(0, spannableString.length(), drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imageSpan1 = new AlignImageSpan(drawable);
        spannableString.setSpan(imageSpan1,0,spannableString.length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);


        spannableString.append("暗影IV已经开始暴走了暗影IV已经开始暴走了暗影IV已经开始暴走了暗影IV已经开始暴走了暗影IV已经开始暴走了 \n");
        BackgroundColorSpan bgColorSpan = new BackgroundColorSpan(Color.parseColor("#009ad6"));
        spannableString.setSpan(bgColorSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        ImageSpan imageSpan2 = new AlignImageSpan(drawable);
        int start = spannableString.length();
        spannableString.append(" ");
        int end = spannableString.length();
        spannableString.setSpan(imageSpan2,start,end,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.append("你就是个逗比 \n");

        tv4.setText(spannableString);

        //20.5    15
    }

    /**
     * 使用SpannableStringBuilder设置样式——字体大小
     */
    private void mode5() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影IV");
        AbsoluteSizeSpan absoluteSizeSpan1 = new AbsoluteSizeSpan((int) 24);
        int length1 = spannableString.toString().length();
        spannableString.setSpan(absoluteSizeSpan1,
                0, length1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //spannableString.append("\n");
        spannableString.append("已经开始暴走了");
        int length2 = spannableString.toString().length();
        AbsoluteSizeSpan absoluteSizeSpan2 = new AbsoluteSizeSpan((int) 14);
        spannableString.setSpan(absoluteSizeSpan2,
                length1, length2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv5.setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——粗体\斜体
     */
    private void mode6() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影IV已经开始暴走了");
        //setSpan可多次使用
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);//粗体
        spannableString.setSpan(styleSpan, 0, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        StyleSpan styleSpan2 = new StyleSpan(Typeface.ITALIC);//斜体
        spannableString.setSpan(styleSpan2, 3, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        StyleSpan styleSpan3 = new StyleSpan(Typeface.BOLD_ITALIC);//粗斜体
        spannableString.setSpan(styleSpan3, 6, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv6.setText(spannableString);
    }


    /**
     * 使用SpannableStringBuilder设置点击事件
     */
    private void mode7() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影IV已经开始暴走了");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                ToastUtils.showRoundRectToast("请不要点我");
            }
        };
        tv7.setMovementMethod(LinkMovementMethod.getInstance());
        spannableString.setSpan(clickableSpan, 5, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv7.setText(spannableString);
    }

    /**
     * 使用SpannableStringBuilder设置样式——图片
     */
//    private void mode8() {
//        SpannableStringBuilder spannableString = new SpannableStringBuilder();
//        spannableString.append("暗影IV已经开始暴走了");
//        ImageSpan imageSpan = new ImageSpan(this, R.mipmap.ic_launcher);
//        //也可以这样
//        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
//        //drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        //ImageSpan imageSpan1 = new ImageSpan(drawable);
//        //将index为6、7的字符用图片替代
//        spannableString.setSpan(imageSpan, 6, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        tv8.setText(spannableString);
//    }

    /**
     * 使用SpannableStringBuilder设置样式——图片
     */
    private void mode8() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影IV已经开始暴走了暗影IV已经开始暴走了暗影IV已经开始暴走了");
        spannableString.append("*");
        //ImageSpan imageSpan = new ImageSpan(this, R.mipmap.ic_launcher);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        VerticalImageSpan imageSpan = new VerticalImageSpan(drawable, 9, 0);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                ToastUtils.showRoundRectToast("吐司");
            }
        };

        //想要ClickableSpan 触发点击事件
        tv8.setMovementMethod(LinkMovementMethod.getInstance());
        spannableString.setSpan(imageSpan, spannableString.length()-1,
                spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.setSpan(clickableSpan, spannableString.length()-1,
                spannableString.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv8.setText(spannableString);
    }



    /**
     * 使用SpannableStringBuilder设置样式——下划线
     */
    private void mode9() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("暗影IV已经开始暴走了");
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, 0, 8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv9.setText(spannableString);
    }


    private void mode10() {
        String name = "name";
        String title = "title";
        SpannableString spannableString= new SpannableString(name+title);
        spannableString.setSpan(new RoundBackgroundColorSpan(
                        Color.parseColor("#12DBD1"),Color.parseColor("#FFFFFF")),
                0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv10.setText(spannableString);
    }


    private void mode11() {
        String[] tags = {"精华", "活动","推荐"};

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        for (String tag : tags) {
            String thisTag = " " + tag + " ";
            stringBuilder.append(thisTag);
            RoundedBackgroundSpan span;
            if("活动".equals(tag)){
                span= new RoundedBackgroundSpan(ContextCompat.getColor(this,R.color.colorAccent), Color.WHITE);
            }else if ("推荐".equals(tag)){
                span= new RoundedBackgroundSpan(ContextCompat.getColor(this,R.color.colorPrimary), Color.WHITE);
            }else{
                span= new RoundedBackgroundSpan(ContextCompat.getColor(this,R.color.colorPrimaryDark), Color.WHITE);
            }
            stringBuilder.setSpan(span, stringBuilder.length() - thisTag.length(), stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.append(" ");
        }
        stringBuilder.append("王宝强凌晨宣布离婚，妻子劈腿经纪人。");
        tv11.setText(stringBuilder);
    }

    private void mode12() {
        SpannableStringBuilder string = SpannableUtils.appendString(
                this, "名字", "放假啊打扫房间");
        tv12.setText(string);
    }


    private void mode13() {
        SpannableStringBuilder string = appendString(
                this, "名字", "放假啊打扫房间放假啊打扫房间放假啊打扫房间");
        tv13.setText(string);
    }



    /**
     * 字符串拼接
     * @param context                   上下文
     * @param beforeText                前面标签文件
     * @param afterText                 后面内容
     * @return
     */
    public SpannableStringBuilder appendString(
            Context context , String beforeText , String afterText){
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        String thisTag = " " + beforeText + " ";
        stringBuilder.append(thisTag);
        stringBuilder.append("  ");
        stringBuilder.append(afterText);
        RoundedBackgroundSpan span;

        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
        stringBuilder.setSpan(sizeSpan,0, thisTag.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span= new RoundedBackgroundSpan(ContextCompat.getColor(context,
                R.color.color_f25057), R.color.colorAccent);
        stringBuilder.setSpan(span, 0, thisTag.length(), Spanned.SPAN_USER_SHIFT);

        stringBuilder.append(" ");
        return stringBuilder;
    }



}
