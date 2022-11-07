package com.yc.window.inter;

import android.view.View;

import com.yc.window.FloatWindow;
import com.yc.window.config.ParamsConfig;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 自定义悬浮窗接口
 *     revise:
 * </pre>
 */
public interface IFloatView {

    /**
     * 设置大小
     *
     * @param width  宽
     * @param height 高
     * @return 返回FloatWindow
     */
    FloatWindow setSize(int width, int height);

    /**
     * 设置显示的位置
     *
     * @param gravity 显示
     * @param xOffset x偏移
     * @param yOffset y偏移
     * @return 返回FloatWindow
     */
    FloatWindow setGravity(int gravity, int xOffset, int yOffset);

    /**
     * 设置window参数配置
     *
     * @param paramsConfig 参数配置
     * @return 返回FloatWindow
     */
    FloatWindow setParamsConfig(ParamsConfig paramsConfig);

    /**
     * 展示悬浮窗
     *
     * @param anchorView 依附的view
     */
    void showAsDropDown(View anchorView);

    /**
     * 展示悬浮窗
     *
     * @param anchorView  依附的view
     * @param showGravity 展示位置
     */
    void showAsDropDown(View anchorView, int showGravity);

    /**
     * 展示悬浮窗
     *
     * @param anchorView  依附的view
     * @param showGravity 展示位置
     * @param xOff        x轴偏移
     * @param yOff        y轴偏移
     */
    void showAsDropDown(View anchorView, int showGravity, int xOff, int yOff);

    /**
     * 展示悬浮窗
     */
    void show();

    /**
     * 销毁悬浮窗
     */
    void dismiss();

}
