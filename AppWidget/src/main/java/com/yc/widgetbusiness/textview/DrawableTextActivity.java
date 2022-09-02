package com.yc.widgetbusiness.textview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.yc.apptextview.DrawableTextView;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;
import com.yc.widgetbusiness.R;

public class DrawableTextActivity extends AppCompatActivity {

    LinearLayout parent;
    private DrawableTextView tvDrawable1;
    private DrawableTextView tvDrawable2;
    private DrawableTextView tvDrawable3;
    private DrawableTextView tvDrawable4;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, DrawableTextActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_text);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        parent = findViewById(R.id.ll_drawable_text);



        tvDrawable1 = findViewById(R.id.tv_drawable_1);
        tvDrawable2 = findViewById(R.id.tv_drawable_2);
        tvDrawable3 = findViewById(R.id.tv_drawable_3);
        tvDrawable4 = findViewById(R.id.tv_drawable_4);

        tvDrawable1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("点击文字");
            }
        });

        tvDrawable2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("点击文字");
            }
        });

        //显示半遮盖的图片，半遮盖的文字，因为DrawableTextView只是简单的对画布做了位移操作
        DrawableTextView drawableTextView = new DrawableTextView(this);
        drawableTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        drawableTextView.setDrawable(DrawableTextView.RIGHT,
                ContextCompat.getDrawable(this, R.mipmap.camera), 50, 50);
        drawableTextView.setText("帖子");
        parent.addView(drawableTextView);
    }
}
