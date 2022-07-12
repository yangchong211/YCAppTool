package com.yc.widgetbusiness;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.yc.apptextview.DrawableTextView;

public class DrawableTextActivity extends AppCompatActivity {

    LinearLayout parent;

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
        parent = findViewById(R.id.ll_drawable_text);

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
