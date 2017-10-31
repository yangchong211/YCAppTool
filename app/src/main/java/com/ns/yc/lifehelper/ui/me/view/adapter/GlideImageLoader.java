package com.ns.yc.lifehelper.ui.me.view.adapter;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.loader.ImageLoader;
import com.ns.yc.lifehelper.R;

import java.io.File;

/**
 * Created by PC on 2017/9/26.
 * 作者：PC
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        /*Picasso.with(activity)                            //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(R.drawable.bg_stack_blur_default)           //设置错误图片
                .placeholder(R.drawable.bg_stack_blur_default)     //设置占位图片
                .into(imageView);*/

        Glide.with(activity)                             //配置上下文
                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
                .error(R.mipmap.default_image)           //设置错误图片
                .placeholder(R.mipmap.default_image)     //设置占位图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {}

}
