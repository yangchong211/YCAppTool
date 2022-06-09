package com.yc.videoview.inter;

import android.view.View;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2021/12/21
 *     desc  : 悬浮view接口
 *     revise:
 * </pre>
 */
public interface IFloatView {

    /**
     * 设置大小
     * @param width                 宽
     * @param height                高
     */
    void setSize(int width, int height);

    /**
     * 设置view
     * @param view                  view
     */
    void setView(View view);

    /**
     * 设置是否显示
     * @param gravity               显示
     * @param xOffset               x偏移
     * @param yOffset               y偏移
     */
    void setGravity(int gravity, int xOffset, int yOffset);

    /**
     * 初始化
     */
    void init();

    /**
     * 销毁
     */
    void dismiss();

}
