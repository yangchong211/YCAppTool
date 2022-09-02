package com.yc.zoomimagelib;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCWidgetLib
 *     email  : yangchong211@163.com
 *     time  : 2018/7/10
 *     desc  : 旋转接口
 *     revise:
 * </pre>
 */
public interface OnRotateListener {
    /**
     * 旋转回调
     * @param degrees       角度
     * @param focusX        x
     * @param focusY        y
     */
    void onRotate(float degrees, float focusX, float focusY);
}
