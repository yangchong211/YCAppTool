package com.yc.imageserver.transformations;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/9
 *     desc  : 自定义切割圆形Transformation
 *     revise:
 * </pre>
 */
public class RoundedCornersTransformation extends BitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID = "jp.wasabeef.glide.transformations.RoundedCornersTransformation." + VERSION;

    public enum CornerType {
        ALL,
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        TOP, BOTTOM, LEFT, RIGHT,
        OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT,
        DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
    }

    private int radius;
    private int diameter;
    private int margin;
    private CornerType cornerType;

    public RoundedCornersTransformation(int radius, int margin) {
        this(radius, margin, CornerType.ALL);
    }

    public RoundedCornersTransformation(int radius, int margin, CornerType cornerType) {
        this.radius = radius;
        this.diameter = this.radius * 2;
        this.margin = margin;
        this.cornerType = cornerType;
    }

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();

        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(true);

        setCanvasBitmapDensity(toTransform, bitmap);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //Bitmap:构造shader使用的bitmap
        //tileX：X轴方向的TileMode，
        //tileY:Y轴方向的TileMode。
        //BitmapShader总是从左上角开始绘制，利用Canvas绘制特定形状，可以获得特定形状的图形，类似圆形，椭圆，矩形等。
        //Shader.TileMode类型的参数包括CLAMP、MIRROR和REPEAT3个可选值
        //其中，CLAMP为使用边界颜色来填充剩余的空间；MIRROR为采用镜像方式；REPEAT为采用重复方式
        BitmapShader bitmapShader = new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
        drawRoundRect(canvas, paint, width, height);
        return bitmap;
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        float right = width - margin;
        float bottom = height - margin;

        switch (cornerType) {
            case ALL:
                canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
                break;
            case TOP_LEFT:
                drawTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case TOP_RIGHT:
                drawTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_LEFT:
                drawBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_RIGHT:
                drawBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case TOP:
                drawTopRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM:
                drawBottomRoundRect(canvas, paint, right, bottom);
                break;
            case LEFT:
                drawLeftRoundRect(canvas, paint, right, bottom);
                break;
            case RIGHT:
                drawRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_LEFT:
                drawOtherTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_RIGHT:
                drawOtherTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_LEFT:
                drawOtherBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_RIGHT:
                drawOtherBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_LEFT:
                drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_RIGHT:
                drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom);
                break;
            default:
                canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
                break;
        }
    }

    private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, margin + diameter), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, margin + radius, bottom), paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
    }

    private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, margin + diameter), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
        canvas.drawRect(new RectF(right - radius, margin + radius, right, bottom), paint);
    }

    private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, margin + diameter, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin, margin + diameter, bottom - radius), paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
    }

    private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, bottom - diameter, right, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
        canvas.drawRect(new RectF(right - radius, margin, right, bottom - radius), paint);
    }

    private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, right, margin + diameter), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin, margin + radius, right, bottom), paint);
    }

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, right, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin, margin, right, bottom - radius), paint);
    }

    private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
    }

    private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, bottom), radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
    }

    private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, right, bottom), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, bottom), radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom - radius), paint);
    }

    private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, bottom), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, right, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom - radius), paint);
    }

    private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, right, margin + diameter), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, bottom), radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, right - radius, bottom), paint);
    }

    private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right,
                                               float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, right, margin + diameter), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin + radius, margin + radius, right, bottom), paint);
    }

    private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right,
                                                  float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, margin + diameter), radius,
                radius, paint);
        canvas.drawRoundRect(new RectF(right - diameter, bottom - diameter, right, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, right - diameter, bottom), paint);
        canvas.drawRect(new RectF(margin + diameter, margin, right, bottom - radius), paint);
    }

    private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right,
                                                   float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, margin + diameter), radius,
                radius, paint);
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, margin + diameter, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom - radius), paint);
        canvas.drawRect(new RectF(margin + radius, margin + radius, right, bottom), paint);
    }

    @Override
    public String toString() {
        return "RoundedTransformation(radius=" + radius + ", margin=" + margin + ", diameter="
                + diameter + ", cornerType=" + cornerType.name() + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RoundedCornersTransformation &&
                ((RoundedCornersTransformation) o).radius == radius &&
                ((RoundedCornersTransformation) o).diameter == diameter &&
                ((RoundedCornersTransformation) o).margin == margin &&
                ((RoundedCornersTransformation) o).cornerType == cornerType;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + radius * 10000 + diameter * 1000 + margin * 100 + cornerType.ordinal() * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius + diameter + margin + cornerType).getBytes(CHARSET));
    }
}
