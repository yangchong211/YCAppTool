package com.yc.imageserver.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.yc.toolutils.context.AppToolUtils;
import com.yc.imageserver.R;
import com.yc.imageserver.transformations.CornerGlideTransform;
import com.yc.imageserver.transformations.RoundedCornersTransformation;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/9
 *     desc  : 图片加载工具类
 *     revise:
 * </pre>
 */
public final class GlideImageUtils {

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
     * 低内存的条件下清除缓存
     * @param context                       上下文
     */
    public static void clear(@NonNull Context context){
        if (context==null){
            return;
        }
        Glide.get(context).clearMemory();
    }

    /**
     * 程序在内存清理的时候执行
     * @param context                       上下文
     * @param level                         级别
     */
    public static void clear(@NonNull Context context ,int i , int level){
        if (context==null){
            return;
        }
        if (level == i){
            Glide.get(context).clearMemory();
        }
        Glide.get(context).trimMemory(level);
    }

    /**
     * 加载本地图片，默认没有加载loading
     * @param context                       上下文
     * @param drawableId                    图片
     * @param imageView                     控件
     */
    public static void loadImageLocal(@NonNull Context context ,@DrawableRes int drawableId,
                                      ImageView imageView){
        loadImageLocal(context, drawableId, imageView ,true);
    }

    /**
     * 加载本地图片，默认没有加载loading
     * @param context                       上下文
     * @param bitmap                        图片
     * @param imageView                     控件
     */
    public static void loadImageLocal(@NonNull Context context ,Bitmap bitmap,
                                      ImageView imageView){
        if (context!=null && imageView!=null){
            Glide.with(context)
                    .load(bitmap)
                    .placeholder(getPlaceholderImage(context))
                    .error(getErrorImage(context))
                    .into(imageView);
        }
    }

    /**
     * 加载本地图片
     * @param context                       上下文
     * @param drawableId                    图片
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageLocal(@NonNull Context context ,@DrawableRes int drawableId,
                                      ImageView imageView , boolean isLoad){
        if (context!=null && imageView!=null){
            isLoad =false;

            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory.Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                Glide.with(context)
                        .load(drawableId)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(drawableId)
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context))
                        .into(imageView);
            }
        }
    }

    /**
     * 加载本地图片
     * @param context                       上下文
     * @param drawableId                    图片
     * @param imageView                     控件
     */
    public static void loadImageLocal(@NonNull Context context ,@DrawableRes int drawableId,
                                      ImageView imageView , CallBack callBack){
        if (context!=null && imageView!=null){
            Glide.with(context)
                    .asBitmap()
                    .load(drawableId)
                    .placeholder(getPlaceholderImage(context))
                    .error(getErrorImage(context))
                    .addListener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Bitmap> target, boolean isFirstResource) {
                            callBack.onLoadFailed();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model,
                                                       Target<Bitmap> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            callBack.onResourceReady(resource);
                            return false;
                        }
                    })
                    .into(imageView);
        }
    }

    /**
     * 加载网络图片，默认没有加载loading
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageNet(@NonNull Context context ,String url,
                                    ImageView imageView){
        loadImageNet(context, url, imageView ,true);
    }

    /**
     * 加载网络图片
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageNet(@NonNull Context context ,String url,
                                      ImageView imageView , boolean isLoad){
        loadImageNet(context, url, getPlaceholderImage(context), imageView, isLoad);
    }

    /**
     * 加载网络图片
     * @param context                       上下文
     * @param url                           图片url
     * @param placeholder                   预加载图片
     * @param imageView                     控件
     */
    public static void loadImageNet(@NonNull Context context , String url, Drawable placeholder,
                                    ImageView imageView){
        loadImageNet(context, url, placeholder, getErrorImage(context), imageView, false);
    }

    /**
     * 加载网络图片
     * @param context                       上下文
     * @param url                           图片url
     * @param placeholder                   预加载图片
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageNet(@NonNull Context context , String url, Drawable placeholder,
                                    ImageView imageView , boolean isLoad){
        loadImageNet(context, url, placeholder, getErrorImage(context), imageView, isLoad);
    }

    /**
     * 加载网络图片
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param placeholder                   预加载图片
     */
    public static void loadImageNet(@NonNull Context context , String url,ImageView imageView ,
                                    Drawable placeholder,  Drawable errorholder){
        loadImageNet(context, url, placeholder, errorholder,imageView,  true);
    }

    /**
     * 加载网络图片，可以自定义设置加载失败的图片
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param error                         自定义设置加载失败的图片
     */
    public static void loadImageNet(@NonNull Context context ,String url, @DrawableRes int error, ImageView imageView ){
        loadImageNet(context, url, error,imageView,  false);
    }

    /**
     * 加载网络图片
     * @param context                       上下文
     * @param url                           图片url
     * @param placeholder                   预加载图片
     * @param errorholder                   错误图片
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageNet(@NonNull Context context , String url, Drawable placeholder, Drawable errorholder,
                                    ImageView imageView , boolean isLoad){
        boolean isEmpty = checkNullUrl(context, url, imageView);
        if (isEmpty){
            return;
        }
        isLoad =false;
        if (isLoad){
            DrawableCrossFadeFactory drawableCrossFadeFactory =
                    new DrawableCrossFadeFactory.Builder(300)
                            .setCrossFadeEnabled(true)
                            .build();
            Glide.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .placeholder(placeholder == null ? getPlaceholderImage(context) : placeholder)
                    .error(errorholder)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(url)
                    .placeholder(placeholder == null ? getPlaceholderImage(context) : placeholder)
                    .error(errorholder)
                    .into(imageView);
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
    public static void loadImageNet(@NonNull Context context ,String url, @DrawableRes int error,
                                    ImageView imageView , boolean isLoad ){
        boolean isEmpty = checkNullUrl(context, url, imageView);
        if (isEmpty){
            return;
        }
        isLoad =false;

        if (isLoad){
            DrawableCrossFadeFactory drawableCrossFadeFactory =
                    new DrawableCrossFadeFactory.Builder(300)
                            .setCrossFadeEnabled(true)
                            .build();
            Glide.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .placeholder(getPlaceholderImage(context))
                    .error(error)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(url)
                    .placeholder(getPlaceholderImage(context))
                    .error(error)
                    .into(imageView);
        }
    }

    /**
     * 加载网络图片，可以自定义设置加载失败的图片
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param error                         自定义设置加载失败的图片
     */
    public static void loadImageNet(@NonNull Context context ,String url, @DrawableRes int error,
                                    ImageView imageView , CallBack callBack ){
        boolean isEmpty = checkNullUrl(context, url, imageView);
        if (isEmpty){
            return;
        }
        Glide.with(context)
                .asBitmap()
                .load(url)
                .placeholder(getPlaceholderImage(context))
                .error(error)
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Bitmap> target, boolean isFirstResource) {
                        callBack.onLoadFailed();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model,
                                                   Target<Bitmap> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        callBack.onResourceReady(resource);
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 加载网络图片，并且设置切割圆形，默认没有加载loading
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageRound(@NonNull Context context ,String url, ImageView imageView){
        loadImageRound(context, url, imageView ,false);
    }

    /**
     * 加载网络图片，并且设置切割圆形
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageRound(@NonNull Context context ,String url,
                                      ImageView imageView , boolean isLoad){
        loadImageRound(context, url, imageView, getPlaceholderImage(context), getErrorImage(context), isLoad);
    }

    /**
     * 加载网络图片，并且设置切割圆形
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param errorDrawable                 设置错误的默认图片
     */
    public static void loadImageRound(@NonNull Context context ,String url,
                                      ImageView imageView, Drawable placeholderImage, Drawable errorDrawable){
        loadImageRound(context, url, imageView, placeholderImage, errorDrawable, true);
    }


    /**
     * 加载网络图片，并且设置切割圆形
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param errorDrawable                 设置错误的默认图片
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageRound(@NonNull Context context ,String url,
                                      ImageView imageView, Drawable placeholderImage, Drawable errorDrawable, boolean isLoad){
        boolean isEmpty = checkNullUrl(context, url, imageView);
        if (isEmpty){
            return;
        }
        RequestOptions requestOptions = RequestOptions.circleCropTransform();
        //RequestOptions requestOptions = RequestOptions.bitmapTransform(new CircleCrop());
        isLoad =false;

        if (isLoad){
            DrawableCrossFadeFactory drawableCrossFadeFactory =
                    new DrawableCrossFadeFactory.Builder(300)
                            .setCrossFadeEnabled(true)
                            .build();
            Glide.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .placeholder(placeholderImage)
                    .error(errorDrawable)
                    .apply(requestOptions)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(url)
                    .placeholder(placeholderImage)
                    .error(errorDrawable)
                    .apply(requestOptions)
                    .into(imageView);
        }
    }

    /**
     * 加载本地图片，并且设置切割圆形
     * @param context                       上下文
     * @param img                           图片
     * @param imageView                     控件
     */
    public static void loadImageRound(@NonNull Context context ,@DrawableRes int img, ImageView imageView){
        loadImageRound(context, img, imageView , true);
    }

    /**
     * 加载本地图片，并且设置切割圆形
     * @param context                       上下文
     * @param img                           图片
     * @param imageView                     控件
     */
    public static void loadImageRound(@NonNull Context context ,@DrawableRes int img,
                                      ImageView imageView , boolean isLoad){
        loadImageRound(context, img, imageView, getErrorImage(context), isLoad);
    }

    /**
     * 加载本地图片，并且设置切割圆形
     * @param context                       上下文
     * @param img                           图片
     * @param imageView                     控件
     * @param errorDrawable                 设置失败的加载图片
     * @param isLoad
     */
    public static void loadImageRound(@NonNull Context context ,@DrawableRes int img,
                                      ImageView imageView, Drawable errorDrawable, boolean isLoad){
        if (context!=null && imageView!=null){
            RequestOptions requestOptions = RequestOptions.circleCropTransform();
            //RequestOptions requestOptions = RequestOptions.bitmapTransform(new CircleCrop());
            isLoad =false;

            if (isLoad){
                DrawableCrossFadeFactory drawableCrossFadeFactory =
                        new DrawableCrossFadeFactory.Builder(300)
                                .setCrossFadeEnabled(true)
                                .build();
                Glide.with(context)
                        .load(img)
                        .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                        .placeholder(getPlaceholderImage(context))
                        .error(errorDrawable)
                        .apply(requestOptions)
                        .into(imageView);
            } else {
                Glide.with(context)
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
    public static void loadImageCorner(@NonNull Context context ,String url, ImageView imageView ){
        int corner = Dp2Px(context, 8);
        loadImageCorner(context, url, imageView, true , corner);
    }

    /**
     * 加载网络图片，并且设置切割圆角，可以设置圆角半径
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCorner(@NonNull Context context ,String url, ImageView imageView , int corner){
        loadImageCorner(context, url, imageView, true , Dp2Px(context, corner));
    }

    /**
     * 加载网络图片，并且设置切割圆角，可以设置圆角半径
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param corner                        圆角
     * @param width                         宽度
     * @param height                        高度
     */
    public static void loadImageCorner(@NonNull Context context ,String url, ImageView imageView, int corner,
                                       int width, int height){
        boolean isEmpty = checkNullUrl(context, url, imageView);
        if (isEmpty){
            return;
        }
        MultiTransformation<Bitmap> multiTransformation =
                new MultiTransformation<>(
                        new CenterCrop(),
                        new RoundedCornersTransformation(corner,
                                0, RoundedCornersTransformation.CornerType.ALL));
        Glide.with(context)
                .load(url)
                .override(width,height)
                .apply(RequestOptions.bitmapTransform(multiTransformation)
                        .placeholder(getPlaceholderImage(context))
                        .error(getErrorImage(context)))
                .into(imageView);
    }

    /**
     * 加载网络图片，并且设置切割圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageCorner(@NonNull Context context ,String url,
                                       ImageView imageView , boolean isLoad ){
        int corner = Dp2Px(context, 8);
        loadImageCorner(context, url, imageView, isLoad , corner);
    }

    /**
     * 加载网络图片，并且设置切割圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageCorner(@NonNull Context context ,String url,
                                       ImageView imageView , boolean isLoad , int corner){
        boolean isEmpty = checkNullUrl(context, url, imageView);
        if (isEmpty){
            return;
        }
        MultiTransformation<Bitmap> multiTransformation =
                new MultiTransformation<>(
                        new CenterCrop(),
                        new RoundedCornersTransformation(corner,
                                0, RoundedCornersTransformation.CornerType.ALL));
        isLoad =false;

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

    /**
     * 加载网络图片，并且设置切割圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param errorDrawable                 设置错误的默认图片
     */
    public static void loadImageCorner(@NonNull Context context ,String url,
                                       ImageView imageView, Drawable placeholderImage, Drawable errorDrawable){
        int corner = Dp2Px(context, 4);
        loadImageCorner(context,url,imageView,placeholderImage,errorDrawable,corner,true);
    }
    /**
     * 加载网络图片，并且设置切割圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param errorDrawable                 设置错误的默认图片
     */
    public static void loadImageCorner(@NonNull Context context ,String url,
                                       ImageView imageView, Drawable placeholderImage, Drawable errorDrawable, int cor){
        int corner = Dp2Px(context, cor);
        loadImageCorner(context,url,imageView,placeholderImage,errorDrawable,corner,true);
    }

    /**
     * 加载网络图片，并且设置切割圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     * @param errorDrawable                 设置错误的默认图片
     * @param isLoad                        是否显示加载动画
     */
    public static void loadImageCorner(@NonNull Context context ,String url,
                                      ImageView imageView, Drawable placeholderImage, Drawable errorDrawable,
                                       int corner,boolean isLoad){
        boolean isEmpty = checkNullUrl(context, url, imageView);
        if (isEmpty){
            return;
        }
        MultiTransformation<Bitmap> multiTransformation =
                new MultiTransformation<>(
                        new CenterCrop(),
                        new RoundedCornersTransformation(corner,
                                0, RoundedCornersTransformation.CornerType.ALL));
        isLoad =false;

        if (isLoad){
            DrawableCrossFadeFactory drawableCrossFadeFactory =
                    new DrawableCrossFadeFactory
                            .Builder(300)
                            .setCrossFadeEnabled(true)
                            .build();
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(multiTransformation)
                            .placeholder(placeholderImage)
                            .error(errorDrawable))
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(multiTransformation)
                            .placeholder(placeholderImage)
                            .error(errorDrawable))
                    .into(imageView);
        }
    }


    /**
     * 加载网络图片，并且设置切割圆角，切割下边两个圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCornerBottom(@NonNull Context context ,String url,
                                       ImageView imageView  , int corner ){
        loadImageCorner(context, url, imageView,true, corner ,
                true , true ,false ,false);
    }

    /**
     * 加载网络图片，并且设置切割圆角，切割左边两个圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCornerRight(@NonNull Context context ,String url,
                                          ImageView imageView  , int corner ){
        loadImageCorner(context, url, imageView,true, corner ,
                true , false ,true ,false);
    }

    /**
     * 加载网络图片，并且设置切割圆角，切割左边两个圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCornerRight(@NonNull Context context ,String url,
                                            ImageView imageView  ,Drawable placeholderImage,
                                            Drawable errorDrawable, int corner ){
        loadImageCorner(context, url, imageView,true, placeholderImage, errorDrawable,corner ,
                true , false ,true ,false);
    }

    /**
     * 加载网络图片，并且设置切割圆角，切割左边两个圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCornerLeft(@NonNull Context context ,String url,
                                           ImageView imageView  , Drawable placeholderImage,
                                           Drawable errorDrawable,int corner ){
        loadImageCorner(context, url, imageView,true,placeholderImage, errorDrawable,corner ,
                false , true ,false ,true);
    }


    /**
     * 加载网络图片，并且设置切割圆角，切割左边两个圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCornerLeft(@NonNull Context context ,String url,
                                           ImageView imageView  , int corner ){
        loadImageCorner(context, url, imageView,true, corner ,
                false , true ,false ,true);
    }

    /**
     * 加载网络图片，并且设置切割圆角，切割上边两个圆角
     * @param context                       上下文
     * @param url                           图片url
     * @param imageView                     控件
     */
    public static void loadImageCornerTop(@NonNull Context context ,String url,
                                       ImageView imageView  , int corner ){
        loadImageCorner(context, url, imageView,true, corner ,
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
    public static void loadImageCorner(@NonNull Context context , String url,
                                       ImageView imageView , boolean isLoad , int corner ,
                                       boolean top , boolean bottom , boolean left , boolean right){
        boolean isEmpty = checkNullUrl(context, url, imageView);
        if (isEmpty){
            return;
        }
        context.getApplicationContext();
        CornerGlideTransform multiTransformation = new CornerGlideTransform(context, corner);
        //只是绘制左上角和右上角
        multiTransformation.setExceptCorner(top,bottom, left, right);
        isLoad =false;

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
    public static void loadImageCorner(@NonNull Context context , String url,
                                       ImageView imageView , boolean isLoad , Drawable placeholderImage,
                                       Drawable errorDrawable,int corner ,
                                       boolean top , boolean bottom , boolean left , boolean right){
        boolean isEmpty = checkNullUrl(context, url, imageView);
        if (isEmpty){
            return;
        }
        context.getApplicationContext();
        CornerGlideTransform multiTransformation = new CornerGlideTransform(context, corner);
        //只是绘制左上角和右上角
        multiTransformation.setExceptCorner(top,bottom, left, right);
        isLoad =false;

        if (isLoad){
            DrawableCrossFadeFactory drawableCrossFadeFactory =
                    new DrawableCrossFadeFactory
                            .Builder(300)
                            .setCrossFadeEnabled(true)
                            .build();
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(multiTransformation)
                            .placeholder(placeholderImage)
                            .error(errorDrawable))
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.bitmapTransform(multiTransformation)
                            .placeholder(placeholderImage)
                            .error(errorDrawable))
                    .into(imageView);
        }
    }


    /**
     * 预加载默认图
     * icon_place_choose                车系综述默认图，预加载图像
     * icon_place_car                   车系车型列表，预加载图
     * icon_place_sale                  销售默认图像，预加载图像
     * icon_place_user                  用户默认图像，预加载图像
     * icon_place_author                作者默认图像，预加载图像
     * icon_place_banner                首页轮播图预加载图像
     * icon_place_vertical              竖图，4：3，预加载图像
     * icon_place_video                 横向视频预加载图
     * icon_place_picture               一鹿有车预加载图像
     */


    /**
     * 获取默认的加载失败的图片
     * @param context                       上下文
     * @return                              返回drawable资源
     */
    public static Drawable getErrorImage(@NonNull Context context){
        Drawable drawable = context.getResources().getDrawable(R.drawable.shape_bg_glide_load);
        return drawable;
    }

    /**
     * 获取默认的预加载图片
     * @param context                       上下文
     * @return                              返回drawable资源
     */
    public static Drawable getPlaceholderImage(@NonNull Context context){
        Drawable drawable = context.getResources().getDrawable(R.drawable.shape_bg_glide_load);
        return drawable;
    }

    /**
     * 检测是否为null
     * @param view                                  view
     */
    private static boolean checkNullUrl(Context context , String url, ImageView view){
        if (url==null || context==null || view==null){
            return true;
        } else {
            if (context instanceof Activity){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (((Activity) context).isDestroyed() || ((Activity) context).isFinishing()){
                        return true;
                    }
                } else {
                    if (((Activity) context).isFinishing()){
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public interface CallBack{
        void onLoadFailed();
        void onResourceReady(Bitmap resource);
    }

    private static int Dp2Px(Context context, float dp) {
        if (context==null){
            //避免空指针异常
            context = AppToolUtils.getApp();
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
