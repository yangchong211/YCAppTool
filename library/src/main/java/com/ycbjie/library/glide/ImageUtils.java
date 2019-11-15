package com.ycbjie.library.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ycbjie.library.R;
import com.ycbjie.library.base.glide.GlideApp;
import com.ycbjie.library.glide.transformations.CornerGlideTransform;
import com.ycbjie.library.glide.transformations.RoundedCornersTransformation;
import com.ycbjie.library.glide.transformations.CircleTransform;


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
            // 屏幕宽度（像素）
            w = metric.widthPixels;
            // 屏幕高度（像素）
            h = metric.heightPixels;
        }
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        //如果不设置canvas画布为白色，则生成透明
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }




    /*
     * 注意，glide加载图片不要使用全局上下文。
     * 如果传入的是ApplicationContext，那么只有当应用程序被杀掉的时候，图片加载才会停止。
     * 注意with()方法中传入的实例会决定Glide加载图片的生命周期，
     * 如果传入的是Activity或者Fragment的实例，那么当这个Activity或Fragment被销毁的时候，图片加载也会停止。
     *
     *
     * glide加载图片源码    load(@Nullable String string)
     * @Nullable作用于函数参数或者返回值，标记参数或者返回值可以为空
     * @NonNull作用于函数参数或者返回值，标记参数或者返回值不可以为空
     * 将@Nullable作用在方法上,这样方法的返回值是允许为null的,但是可能会导致某些情况下的crash;
     */


    /**
     * 获取默认的加载失败的图片
     * @param context                       上下文
     * @return                              返回drawable资源
     */
    private static Drawable getErrorImage(Context context){
        Drawable drawable = context.getResources().getDrawable(R.drawable.shape_load_error_img);
        return drawable;
    }

    /**
     * 获取默认的预加载图片
     * @param context                       上下文
     * @return                              返回drawable资源
     */
    private static Drawable getPlaceholderImage(Context context){
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_loading);
        return drawable;
    }

    /**
     * 加载本地图片，默认没有加载loading
     * @param context                       上下文
     * @param drawableId                    图片
     * @param imageView                     控件
     */
    public static void loadImageLocal(Context context ,@DrawableRes int drawableId,
                                      ImageView imageView){
        loadImageLocal(context, drawableId, imageView ,false);
    }

    /**
     * 加载本地图片
     * @param context                       上下文
     * @param drawableId                    图片
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageLocal(Context context ,@DrawableRes int drawableId,
                                      ImageView imageView , boolean isLoad){
        if (context!=null && imageView!=null){
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory.Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                GlideApp.with(context)
                        .load(drawableId)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .into(imageView);
            } else {
                GlideApp.with(context)
                        .load(drawableId)
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .into(imageView);
            }
        }
    }

    /**
     * 加载网络图片，默认没有加载loading
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageNet(Context context ,String url,
                                    ImageView imageView){
        loadImageNet(context, url, imageView ,false);
    }

    /**
     * 加载网络图片
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageNet(Context context ,String url,
                                    ImageView imageView , boolean isLoad){
        if (context!=null && imageView!=null && url!=null){
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory.Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                GlideApp.with(context)
                        .load(url)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .into(imageView);
            } else {
                GlideApp.with(context)
                        .load(url)
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .into(imageView);
            }
        }
    }


    /**
     * 加载网络图片，可以自定义设置加载失败的图片
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     * @param error                         自定义设置加载失败的图片
     */
    public static void loadImageNet(Context context ,String url,
                                    ImageView imageView , boolean isLoad , @DrawableRes int error){
        if (context!=null && imageView!=null && url!=null){
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory.Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                GlideApp.with(context)
                        .load(url)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .placeholder(getPlaceholderImage(context))
                        .error(error)
                        .into(imageView);
            } else {
                GlideApp.with(context)
                        .load(url)
                        .placeholder(getPlaceholderImage(context))
                        .error(error)
                        .into(imageView);
            }
        }
    }

    /**
     * 加载网络图片，并且设置切割圆形，默认没有加载loading
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageRound(Context context ,String url, ImageView imageView){
        loadImageRound(context, url, imageView ,false);
    }

    /**
     * 加载网络图片，并且设置切割圆形
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageRound(Context context ,String url,
                                      ImageView imageView , boolean isLoad){
        if (context!=null && imageView!=null && url!=null){
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory.Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                GlideApp.with(context)
                        .load(url)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .apply(requestOptions)
                        .into(imageView);
            } else {
                GlideApp.with(context)
                        .load(url)
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .apply(requestOptions)
                        .into(imageView);
            }
        }
    }


    /**
     * 加载本地图片，并且设置切割圆形
     * @param context                       上下文
     * @param img                           图片
     * @param imageView                     控件
     */
    public static void loadImageRound(Context context ,@DrawableRes int img, ImageView imageView){

        loadImageRound(context, img, imageView , false);
    }

    /**
     * 加载网络图片，并且设置切割圆形
     * @param context                       上下文
     * @param img                           图片
     * @param imageView                     控件
     */
    public static void loadImageRound(Context context ,@DrawableRes int img,
                                      ImageView imageView , boolean isLoad){
        if (context!=null && imageView!=null){
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory.Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                GlideApp.with(context)
                        .load(img)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .apply(requestOptions)
                        .into(imageView);
            } else {
                GlideApp.with(context)
                        .load(img)
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .apply(requestOptions)
                        .into(imageView);
            }
        }
    }

    /**
     * 加载网络图片，并且设置切割圆角，默认圆角半径8dp
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCorner(Context context ,String url, ImageView imageView ){
        int corner = SizeUtils.dp2px(8);
        loadImageCorner(context, url, imageView, false , corner);
    }

    /**
     * 加载网络图片，并且设置切割圆角，可以设置圆角半径
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCorner(Context context ,String url, ImageView imageView , int corner){
        loadImageCorner(context, url, imageView, false  , SizeUtils.dp2px(corner));
    }

    /**
     * 加载网络图片，并且设置切割圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageCorner(Context context ,String url,
                                       ImageView imageView , boolean isLoad ){
        int corner = SizeUtils.dp2px(8);
        loadImageCorner(context, url, imageView, isLoad , corner);
    }

    /**
     * 加载网络图片，并且设置切割圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageCorner(Context context ,String url,
                                       ImageView imageView , boolean isLoad , int corner){
        if(context!=null && url!=null && imageView!=null){
            MultiTransformation<Bitmap> multiTransformation =
                    new MultiTransformation<>(
                            new CenterCrop(),
                            new RoundedCornersTransformation(corner,
                                    0, RoundedCornersTransformation.CornerType.ALL));
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory
                                .Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context))
                                .error(getErrorImage(context)))
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context))
                                .error(getErrorImage(context)))
                        .into(imageView);
            }
        }
    }

    /**
     * 加载网络图片，并且设置切割圆角，切割上下两个圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCornerTopAndBottom(Context context ,String url,
                                                   ImageView imageView  , int corner ){
        loadImageCorner(context, url, imageView,false, corner ,
                true , true ,false ,false);
    }

    /**
     * 加载网络图片，并且设置切割圆角，切割左右两个圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCornerLeftAndRight(Context context ,String url,
                                                   ImageView imageView  , int corner ){
        loadImageCorner(context, url, imageView,false, corner ,
                false , false ,true ,true);
    }

    /**
     * 加载网络图片，并且设置切割圆角，切割某个指定的圆角，比如设置图片上面一个圆角，两个圆角，都可以用该方法
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     * @param top                           顶部
     * @param bottom                        底部
     * @param left                          左边
     * @param right                         右边
     */
    public static void loadImageCorner(Context context , String url,
                                       ImageView imageView , boolean isLoad , int corner ,
                                       boolean top , boolean bottom , boolean left , boolean right){
        if(context!=null && url!=null && imageView!=null){
            CornerGlideTransform multiTransformation = new CornerGlideTransform(context, corner);
            //只是绘制左上角和右上角
            multiTransformation.setExceptCorner(top,bottom, left, right);
            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory
                                .Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context))
                                .error(getErrorImage(context)))
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(url)
                        .apply(RequestOptions.bitmapTransform(multiTransformation)
                                .placeholder(getPlaceholderImage(context))
                                .error(getErrorImage(context)))
                        .into(imageView);
            }
        }
    }

}
