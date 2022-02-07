package com.yc.imageserver.transformations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/7/18
 *     desc  : 自定义Transformation
 *     revise: 可以设置任意一个圆角
 * </pre>
 */
public class CornerGlideTransform implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;
    private float radius;
    private boolean exceptLeftTop, exceptRightTop, exceptLeftBottom, exceptRightBottom;

    /**
     * 是否设定某个特定圆角
     * @param leftTop                           左上角
     * @param rightTop                          右上角
     * @param leftBottom                        左下角
     * @param rightBottom                       左下角
     */
    public void setExceptCorner(boolean leftTop, boolean rightTop,
                                boolean leftBottom, boolean rightBottom) {
        this.exceptLeftTop = leftTop;
        this.exceptRightTop = rightTop;
        this.exceptLeftBottom = leftBottom;
        this.exceptRightBottom = rightBottom;
    }

    /**
     * 构造方法
     * @param context                           上下文
     * @param radius                            圆角大小
     */
    public CornerGlideTransform(Context context, float radius) {
        this.mBitmapPool = Glide.get(context).getBitmapPool();
        this.radius = radius;
        //作出一个限制
        if (radius<=0 || radius>100){
           this.radius = 10;
        }
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource,
                                      int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int finalWidth, finalHeight;
        //输出目标的宽高或高宽比例
        float ratio;
        //输出宽度>输出高度,求高宽比
        if (outWidth > outHeight) {
            ratio = (float) outHeight / (float) outWidth;
            finalWidth = source.getWidth();
            //固定原图宽度,求最终高度
            finalHeight = (int) ((float) source.getWidth() * ratio);
            //求出的最终高度>原图高度,求宽高比
            if (finalHeight > source.getHeight()) {
                ratio = (float) outWidth / (float) outHeight;
                finalHeight = source.getHeight();
                //固定原图高度,求最终宽度
                finalWidth = (int) ((float) source.getHeight() * ratio);
            }
        } else if (outWidth < outHeight) {
            //输出宽度 < 输出高度,求宽高比
            ratio = (float) outWidth / (float) outHeight;
            finalHeight = source.getHeight();
            //固定原图高度,求最终宽度
            finalWidth = (int) ((float) source.getHeight() * ratio);
            //求出的最终宽度 > 原图宽度,求高宽比
            if (finalWidth > source.getWidth()) {
                ratio = (float) outHeight / (float) outWidth;
                finalWidth = source.getWidth();
                finalHeight = (int) ((float) source.getWidth() * ratio);
            }
        } else {
            //输出宽度=输出高度
            finalHeight = source.getHeight();
            finalWidth = finalHeight;
        }

        //修正圆角
        this.radius *= (float) finalHeight / (float) outHeight;
        Bitmap outBitmap = this.mBitmapPool.get(finalWidth, finalHeight, Bitmap.Config.ARGB_8888);
        //注意，这个地方一定要判断一下非空
        if (outBitmap == null) {
            outBitmap = Bitmap.createBitmap(finalWidth, finalHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        //关联画笔绘制的原图bitmap
        BitmapShader shader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //计算中心位置,进行偏移
        int width = (source.getWidth() - finalWidth) / 2;
        int height = (source.getHeight() - finalHeight) / 2;
        if (width != 0 || height != 0) {
            Matrix matrix = new Matrix();
            matrix.setTranslate((float) (-width), (float) (-height));
            shader.setLocalMatrix(matrix);
        }

        paint.setShader(shader);
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0.0F, 0.0F,
                (float) canvas.getWidth(), (float) canvas.getHeight());
        //先绘制圆角矩形
        canvas.drawRoundRect(rectF, this.radius, this.radius, paint);

        //左上角不为圆角
        if (exceptLeftTop) {
            canvas.drawRect(0, 0, radius, radius, paint);
        }
        //右上角不为圆角
        if (exceptRightTop) {
            canvas.drawRect(canvas.getWidth() - radius, 0, canvas.getWidth(), radius, paint);
        }
        //左下角不为圆角
        if (exceptLeftBottom) {
            canvas.drawRect(0, canvas.getHeight() - radius, radius, canvas.getHeight(), paint);
        }
        //右下角不为圆角
        if (exceptRightBottom) {
            canvas.drawRect(canvas.getWidth() - radius, canvas.getHeight() -
                    radius, canvas.getWidth(), canvas.getHeight(), paint);
        }
        return BitmapResource.obtain(outBitmap, this.mBitmapPool);
    }

    /**
     * 重写该方法
     * @return              字符串
     */
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }

}
