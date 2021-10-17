package com.ycbjie.zoomimagelib.utils;


import android.graphics.Matrix;
import android.graphics.RectF;
import android.widget.ImageView;

import com.ycbjie.zoomimagelib.pool.MatrixPool;
import com.ycbjie.zoomimagelib.pool.RectPool;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/30
 *     desc  : 工具类
 *     revise:
 * </pre>
 */
public class MathUtils {

    /**
     * 矩阵对象池
     */
    private static MatrixPool mMatrixPool = new MatrixPool(16);

    /**
     * 获取矩阵对象
     */
    public static Matrix matrixTake() {
        return mMatrixPool.take();
    }

    /**
     * 获取某个矩阵的copy
     */
    public static Matrix matrixTake(Matrix matrix) {
        Matrix result = mMatrixPool.take();
        if (matrix != null) {
            result.set(matrix);
        }
        return result;
    }

    /**
     * 归还矩阵对象
     */
    public static void matrixGiven(Matrix matrix) {
        mMatrixPool.given(matrix);
    }

    /**
     * 矩形对象池
     */
    private static RectPool mRectFPool = new RectPool(16);

    /**
     * 获取矩形对象
     */
    public static RectF rectFTake() {
        return mRectFPool.take();
    }

    /**
     * 按照指定值获取矩形对象
     */
    public static RectF rectFTake(float left, float top, float right, float bottom) {
        RectF result = mRectFPool.take();
        result.set(left, top, right, bottom);
        return result;
    }

    /**
     * 获取某个矩形的副本
     */
    public static RectF rectFTake(RectF rectF) {
        RectF result = mRectFPool.take();
        if (rectF != null) {
            result.set(rectF);
        }
        return result;
    }

    /**
     * 归还矩形对象
     */
    public static void rectFGiven(RectF rectF) {
        mRectFPool.given(rectF);
    }

    /**
     * 获取两点之间距离
     *
     * @param x1 点1
     * @param y1 点1
     * @param x2 点2
     * @param y2 点2
     * @return 距离
     */
    public static float getDistance(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 获取两点的中点
     *
     * @param x1 点1
     * @param y1 点1
     * @param x2 点2
     * @param y2 点2
     * @return float[]{x, y}
     */
    public static float[] getCenterPoint(float x1, float y1, float x2, float y2) {
        return new float[]{(x1 + x2) / 2f, (y1 + y2) / 2f};
    }

    /**
     * 获取矩阵的缩放值
     *
     * @param matrix 要计算的矩阵
     * @return float[]{scaleX, scaleY}
     */
    public static float[] getMatrixScale(Matrix matrix) {
        if (matrix != null) {
            float[] value = new float[9];
            matrix.getValues(value);
            return new float[]{value[0], value[4]};
        } else {
            return new float[2];
        }
    }

    /**
     * 计算点除以矩阵的值
     *
     * matrix.mapPoints(unknownPoint) -> point
     * 已知point和matrix,求unknownPoint的值.
     *
     * @param point
     * @param matrix
     * @return unknownPoint
     */
    public static float[] inverseMatrixPoint(float[] point, Matrix matrix) {
        if (point != null && matrix != null) {
            float[] dst = new float[2];
            //计算matrix的逆矩阵
            Matrix inverse = matrixTake();
            matrix.invert(inverse);
            //用逆矩阵变换point到dst,dst就是结果
            inverse.mapPoints(dst, point);
            //清除临时变量
            matrixGiven(inverse);
            return dst;
        } else {
            return new float[2];
        }
    }

    /**
     * 计算两个矩形之间的变换矩阵
     *
     * unknownMatrix.mapRect(to, from)
     * 已知from矩形和to矩形,求unknownMatrix
     *
     * @param from
     * @param to
     * @param result unknownMatrix
     */
    public static void calculateRectTranslateMatrix(RectF from, RectF to, Matrix result) {
        if (from == null || to == null || result == null) {
            return;
        }
        if (from.width() == 0 || from.height() == 0) {
            return;
        }
        result.reset();
        result.postTranslate(-from.left, -from.top);
        result.postScale(to.width() / from.width(), to.height() / from.height());
        result.postTranslate(to.left, to.top);
    }

    /**
     * 计算图片在某个ImageView中的显示矩形
     *
     * @param container ImageView的Rect
     * @param srcWidth 图片的宽度
     * @param srcHeight 图片的高度
     * @param scaleType 图片在ImageView中的ScaleType
     * @param result 图片应该在ImageView中展示的矩形
     */
    public static void calculateScaledRectInContainer(RectF container, float srcWidth, float srcHeight, ImageView.ScaleType scaleType, RectF result) {
        if (container == null || result == null) {
            return;
        }
        if (srcWidth == 0 || srcHeight == 0) {
            return;
        }
        //默认scaleType为fit center
        if (scaleType == null) {
            scaleType = ImageView.ScaleType.FIT_CENTER;
        }
        result.setEmpty();
        if (ImageView.ScaleType.FIT_XY.equals(scaleType)) {
            result.set(container);
        } else if (ImageView.ScaleType.CENTER.equals(scaleType)) {
            Matrix matrix = matrixTake();
            RectF rect = rectFTake(0, 0, srcWidth, srcHeight);
            matrix.setTranslate((container.width() - srcWidth) * 0.5f, (container.height() - srcHeight) * 0.5f);
            matrix.mapRect(result, rect);
            rectFGiven(rect);
            matrixGiven(matrix);
            result.left += container.left;
            result.right += container.left;
            result.top += container.top;
            result.bottom += container.top;
        } else if (ImageView.ScaleType.CENTER_CROP.equals(scaleType)) {
            Matrix matrix = matrixTake();
            RectF rect = rectFTake(0, 0, srcWidth, srcHeight);
            float scale;
            float dx = 0;
            float dy = 0;
            if (srcWidth * container.height() > container.width() * srcHeight) {
                scale = container.height() / srcHeight;
                dx = (container.width() - srcWidth * scale) * 0.5f;
            } else {
                scale = container.width() / srcWidth;
                dy = (container.height() - srcHeight * scale) * 0.5f;
            }
            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
            matrix.mapRect(result, rect);
            rectFGiven(rect);
            matrixGiven(matrix);
            result.left += container.left;
            result.right += container.left;
            result.top += container.top;
            result.bottom += container.top;
        } else if (ImageView.ScaleType.CENTER_INSIDE.equals(scaleType)) {
            Matrix matrix = matrixTake();
            RectF rect = rectFTake(0, 0, srcWidth, srcHeight);
            float scale;
            float dx;
            float dy;
            if (srcWidth <= container.width() && srcHeight <= container.height()) {
                scale = 1f;
            } else {
                scale = Math.min(container.width() / srcWidth, container.height() / srcHeight);
            }
            dx = (container.width() - srcWidth * scale) * 0.5f;
            dy = (container.height() - srcHeight * scale) * 0.5f;
            matrix.setScale(scale, scale);
            matrix.postTranslate(dx, dy);
            matrix.mapRect(result, rect);
            rectFGiven(rect);
            matrixGiven(matrix);
            result.left += container.left;
            result.right += container.left;
            result.top += container.top;
            result.bottom += container.top;
        } else if (ImageView.ScaleType.FIT_CENTER.equals(scaleType)) {
            Matrix matrix = matrixTake();
            RectF rect = rectFTake(0, 0, srcWidth, srcHeight);
            RectF tempSrc = rectFTake(0, 0, srcWidth, srcHeight);
            RectF tempDst = rectFTake(0, 0, container.width(), container.height());
            matrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.CENTER);
            matrix.mapRect(result, rect);
            rectFGiven(tempDst);
            rectFGiven(tempSrc);
            rectFGiven(rect);
            matrixGiven(matrix);
            result.left += container.left;
            result.right += container.left;
            result.top += container.top;
            result.bottom += container.top;
        } else if (ImageView.ScaleType.FIT_START.equals(scaleType)) {
            Matrix matrix = matrixTake();
            RectF rect = rectFTake(0, 0, srcWidth, srcHeight);
            RectF tempSrc = rectFTake(0, 0, srcWidth, srcHeight);
            RectF tempDst = rectFTake(0, 0, container.width(), container.height());
            matrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.START);
            matrix.mapRect(result, rect);
            rectFGiven(tempDst);
            rectFGiven(tempSrc);
            rectFGiven(rect);
            matrixGiven(matrix);
            result.left += container.left;
            result.right += container.left;
            result.top += container.top;
            result.bottom += container.top;
        } else if (ImageView.ScaleType.FIT_END.equals(scaleType)) {
            Matrix matrix = matrixTake();
            RectF rect = rectFTake(0, 0, srcWidth, srcHeight);
            RectF tempSrc = rectFTake(0, 0, srcWidth, srcHeight);
            RectF tempDst = rectFTake(0, 0, container.width(), container.height());
            matrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.END);
            matrix.mapRect(result, rect);
            rectFGiven(tempDst);
            rectFGiven(tempSrc);
            rectFGiven(rect);
            matrixGiven(matrix);
            result.left += container.left;
            result.right += container.left;
            result.top += container.top;
            result.bottom += container.top;
        } else {
            result.set(container);
        }
    }

}
