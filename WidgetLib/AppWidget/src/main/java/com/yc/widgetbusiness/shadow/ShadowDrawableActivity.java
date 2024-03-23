package com.yc.widgetbusiness.shadow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.yc.shadow.drawable.ShadowTool;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toolutils.size.AppSizeUtils;
import com.yc.widgetbusiness.R;

public class ShadowDrawableActivity extends AppCompatActivity {

    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, ShadowDrawableActivity.class);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow_drawable);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);

        ShadowTool.setShadowDrawable(text1, Color.parseColor("#2979FF"), AppSizeUtils.dp2px(8),
                Color.parseColor("#992979FF"), AppSizeUtils.dp2px(6), 0, 0);
        ShadowTool.setShadowDrawable(text2, Color.parseColor("#7C4DFF"), AppSizeUtils.dp2px(8),
                Color.parseColor("#997C4DFF"), AppSizeUtils.dp2px(6), AppSizeUtils.dp2px(3), AppSizeUtils.dp2px(3));

        ShadowTool.setShadowDrawable(text3, 1, Color.parseColor("#2979FF"),
                0, Color.parseColor("#aa536DFE"), AppSizeUtils.dp2px(10), 0, 0);
        ShadowTool.setShadowDrawable(text4, 1, Color.parseColor("#7C4DFF"),
                AppSizeUtils.dp2px(8), Color.parseColor("#992979FF"), AppSizeUtils.dp2px(6), AppSizeUtils.dp2px(3), AppSizeUtils.dp2px(3));
    }

}
