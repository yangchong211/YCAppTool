package com.ns.yc.lifehelper.ui.me.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.ycphotolib.inter.PhotoLoader;
import com.squareup.picasso.Picasso;

import java.io.File;


public class Loader extends PhotoLoader {

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        //Picasso.with(context).load(path).into(imageView);

        Picasso.with(context)                                       //配置上下文
                .load(Uri.fromFile(new File(path)))                 //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(R.drawable.bg_stack_blur_default)           //设置错误图片
                .placeholder(R.drawable.bg_stack_blur_default)     //设置占位图片
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, @DrawableRes Integer resId, ImageView imageView) {
        imageView.setImageResource(resId);
    }

}
