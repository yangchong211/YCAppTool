package com.yc.shadow.drawable;

import android.view.View;

import androidx.core.view.ViewCompat;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCWidgetLib
 *     time  : 2019/04/24
 *     desc  : 不用改动代码情况下设置阴影
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCWidgetLib
 * </pre>
 */
public final class ShadowTool {

    public static void setShadowBgForView(View view, ShadowConfig config) {
        if (view == null || config == null) {
            return;
        }
        //关闭硬件加速
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, config.builder());
    }

    /**
     * 为指定View添加阴影
     * @param view 目标View
     * @param shapeRadius View的圆角
     * @param shadowColor 阴影的颜色
     * @param shadowRadius 阴影的宽度
     * @param offsetX 阴影水平方向的偏移量
     * @param offsetY 阴影垂直方向的偏移量
     */
    public static void setShadowDrawable(View view, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowConfig shadowConfig = new ShadowConfig();
        shadowConfig
                .setRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        ShadowDrawable drawable = shadowConfig.builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    /**
     * 为指定View设置带阴影的背景
     * @param view 目标View
     * @param bgColor View背景色
     * @param shapeRadius View的圆角
     * @param shadowColor 阴影的颜色
     * @param shadowRadius 阴影的宽度
     * @param offsetX 阴影水平方向的偏移量
     * @param offsetY 阴影垂直方向的偏移量
     */
    public static void setShadowDrawable(View view, int bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowConfig shadowConfig = new ShadowConfig();
        shadowConfig.setColor(bgColor).setRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        ShadowDrawable drawable = shadowConfig.builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    /**
     * 为指定View设置指定形状并带阴影的背景
     * @param view 目标View
     * @param shape View的形状 取值可为：GradientDrawable.RECTANGLE， GradientDrawable.OVAL， GradientDrawable.RING
     * @param bgColor View背景色
     * @param shapeRadius View的圆角
     * @param shadowColor 阴影的颜色
     * @param shadowRadius 阴影的宽度
     * @param offsetX 阴影水平方向的偏移量
     * @param offsetY 阴影垂直方向的偏移量
     */
    public static void setShadowDrawable(View view, int shape, int bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowConfig shadowConfig = new ShadowConfig();
        shadowConfig.setColor(bgColor)
                .setRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        ShadowDrawable drawable = shadowConfig.builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

}
