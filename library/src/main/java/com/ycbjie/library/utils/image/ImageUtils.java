package com.ycbjie.library.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ycbjie.library.R;
import com.ycbjie.library.base.glide.GlideApp;
import com.ycbjie.library.weight.CircleTransform;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/7/14
 * 描    述：图片加载工具类
 * 修订历史：
 * ================================================
 */
public class ImageUtils {

    /**
     * 加载图片
     */
    public static void loadImgByPicasso(Context context , String path, ImageView target) {
        if(target==null){
            return;
        }
        if(path!=null && path.length()>0){
            Picasso.with(context)
                    .load(path)
                    .into(target);
        }
    }

    /**
     * 加载图片
     */
    public static void loadImgByPicasso(Context context , int path, ImageView target) {
        if(target==null){
            return;
        }
        if(path!=0){
            Picasso.with(context)
                    .load(path)
                    .placeholder(path)
                    .into(target);
        }
    }


    /**
     * 加载本地图片
     * @param context       上下文
     * @param path          路径
     * @param target        view
     */
    public static void loadImgByPicassoWithCircle(Context context , int path, ImageView target) {
        if(target==null){
            return;
        }
        if(path>0){
            Picasso.with(context)
                    .load(path)
                    .placeholder(R.drawable.avatar_default)
                    .transform(new CircleTransform())
                    .into(target);
        }
    }


    /**
     * 加载人物，机构logo时，加载失败时显示默认图片
     * @param path          路径
     * @param resId         加载失败时，默认图片
     * @param target        控件
     */
    public static void loadImgByPicassoPerson(Context context , String path, int resId, ImageView target) {
        if(target==null){
            return;
        }
        if(path!=null && path.length()>0){
            Picasso.with(context)
                    .load(path)
                    .error(resId)
                    .placeholder(resId)
                    .transform(new CircleTransform())
                    .into(target);
        }
    }

    /**
     * 加载图片
     * @param resId         string
     * @param target        控件
     */
    public static void loadImgByPicasso(Context context , String path , int resId, ImageView target) {
        if(target==null){
            return;
        }
        if(path!=null && path.length()>0){
            Picasso.with(context)
                    .load(path)
                    .placeholder(resId)
                    .error(resId)
                    .into(target);
        }else {
            Picasso.with(context)
                    .load(resId)
                    .placeholder(resId)
                    .error(resId)
                    .into(target);
        }
    }

    /**
     * 加载图片
     * @param resId         string
     * @param target        控件
     */
    public static void loadImgByPicasso(Context context , String path , int resId,
                                        ImageView target, Callback callback) {
        if(target==null){
            return;
        }
        if(path!=null && path.length()>0){
            Picasso.with(context)
                    .load(path)
                    .placeholder(resId)
                    .error(resId)
                    .into(target,callback);
        }else {
            Picasso.with(context)
                    .load(resId)
                    .placeholder(resId)
                    .error(resId)
                    .into(target,callback);
        }
    }



    /**
     * 加载图片
     * @param resId         int
     * @param target        控件
     */
    public static void loadImgByPicasso(Context context , int path , int resId, ImageView target) {
        if(target==null){
            return;
        }
        if(path>0){
            Picasso.with(context)
                    .load(path)
                    .placeholder(resId)
                    .error(resId)
                    .into(target);
        }else {
            Picasso.with(context)
                    .load(resId)
                    .placeholder(resId)
                    .error(resId)
                    .into(target);
        }
    }

    /**
     * 加载图片bitmap
     * @param resId         int
     * @param target        控件
     */
    public static void loadImgByPicasso(Context context , Bitmap bitmap , int resId, ImageView target) {
        if(target==null){
            return;
        }
        if(bitmap!=null){
            target.setImageBitmap(bitmap);
        }else {
            Picasso.with(context)
                    .load(resId)
                    .placeholder(resId)
                    .error(resId)
                    .into(target);
        }
    }


    /**------------------------------Glide加载图片--------------------------------------------------**/


    /**
     * 将gif图转换为静态图
     */
    public static void loadImgByGlide(Context context , String url, int resId ,ImageView imageView) {
        if(imageView==null){
            return;
        }
        if(url!=null && url.length()>0){
            GlideApp.with(context)
                    .asDrawable()
                    .load(url)
                    .placeholder(resId)
                    .error(resId)
                    .into(imageView);
        }else {
            GlideApp.with(context)
                    .asDrawable()
                    .load(resId)
                    .placeholder(resId)
                    .error(resId)
                    .into(imageView);
        }
    }

    /**
     * 将gif图转换为静态图
     */
    public static void loadImgByGlideSize(Context context , String url, int resId ,ImageView imageView) {
        if(imageView==null){
            return;
        }
        if(url!=null && url.length()>0){
            GlideApp.with(context)
                    .asDrawable()
                    .load(url)
                    .placeholder(resId)
                    .error(resId)
                    .into(imageView);
        }else {
            GlideApp.with(context)
                    .asDrawable()
                    .load(resId)
                    .placeholder(resId)
                    .error(resId)
                    .into(imageView);
        }
    }

    /**
     * 将gif图转换为静态图
     */
    public static void loadImgByGlide(Context context , int url, int resId ,ImageView imageView) {
        if(imageView==null){
            return;
        }
        GlideApp.with(context)
                .asDrawable()
                .load(url)
                .placeholder(resId)
                .error(resId)
                .into(imageView);
    }


    public static void loadImageWithRound(Context context, String url,
                                          @DrawableRes int defaultImg, ImageView iv, int radius) {
        if (TextUtils.isEmpty(url) || context == null) {
            return;
        }
        GlideApp.with(context)
                .load(url)
                .placeholder(defaultImg)
                .error(defaultImg)
                .apply(bitmapTransform(new RoundedCornersTransformation(radius, 0,
                        RoundedCornersTransformation.CornerType.ALL)))
                .into(iv);
    }


    /**
     * 加载带有圆角的矩形图片  用glide处理
     *
     * @param path   路径
     * @param round  圆角半径
     * @param resId  加载失败时的图片
     * @param target 控件
     */
    public static void loadImageWithRound(final Context activity, String path,
                                                 final int round, int resId, final ImageView target) {
        if (path != null && path.length() > 0) {
            GlideApp.with(activity)
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
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory
                                    .create(activity.getResources(), resource);
                            //设置绘制位图时要应用的角半径
                            circularBitmapDrawable.setCornerRadius(round);
                            target.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }


    public static Bitmap loadBitmapFromView(View v) {
        v.measure(0, 0);
        int w = v.getMeasuredWidth();
        int h = v.getMeasuredHeight();
        if (w <= 0 || h <= 0) {
            DisplayMetrics metric = new DisplayMetrics();
            w = metric.widthPixels;// 屏幕宽度（像素）
            h = metric.heightPixels;// 屏幕高度（像素）
        }
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        //如果不设置canvas画布为白色，则生成透明
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }


}
