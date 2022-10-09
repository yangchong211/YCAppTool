package com.yc.widgetbusiness.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.statusbar.bar.StateAppBar;
import com.yc.toolutils.BitmapUtils;
import com.yc.widgetbusiness.R;


public class ImageViewActivity extends AppCompatActivity {

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, ImageViewActivity.class);
            context.startActivity(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        ImageView iv_image1 = findViewById(R.id.iv_image1);
        ImageView iv_image7 = findViewById(R.id.iv_image7);
        //给图片设置灰色显示
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        iv_image1.setColorFilter(filter);

        Bitmap bitmap = BitmapUtils.getBitmap(R.drawable.bg_kites_min);
        Bitmap greyBitmap = BitmapUtils.greyBitmap(bitmap);
        iv_image7.setImageBitmap(greyBitmap);
    }
}
