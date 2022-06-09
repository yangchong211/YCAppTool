package com.yc.ycvideoplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/14
 * 描    述：图片加载工具类
 * 修订历史：
 * ================================================
 */
public class ImageUtil {


    /**
     * 将gif图转换为静态图
     * @param context
     * @param url
     * @param resId
     * @param imageView
     */
    public static void display(Context context , String url, int resId ,ImageView imageView) {
        if(imageView==null){
            return;
        }
        if(url!=null && url.length()>0){
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .placeholder(resId)
                    .error(resId)
                    .into(imageView);
        }else {
            Glide.with(context)
                    .asBitmap()
                    .load(resId)

                    .placeholder(resId)
                    .error(resId)
                    .into(imageView);
        }
    }


    /**
     * 加载带有圆角的矩形图片  用glide处理
     *
     * @param path   路径
     * @param round  圆角半径
     * @param resId  加载失败时的图片
     * @param target 控件
     */
    public static void loadImgByPicassoWithRound(final Context activity, String path, final int round, int resId, final ImageView target) {
        if (path != null && path.length() > 0) {
            Glide.with(activity)
                    .asBitmap()
                    .load(path)
                    .placeholder(resId)
                    .error(resId)
                    //设置缓存
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new BitmapImageViewTarget(target) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                            //设置圆角弧度
                            circularBitmapDrawable.setCornerRadius(round);
                            target.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }


}
