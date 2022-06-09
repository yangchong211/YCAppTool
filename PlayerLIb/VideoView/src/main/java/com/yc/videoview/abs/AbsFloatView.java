package com.yc.videoview.abs;

import com.yc.videoview.inter.IFloatView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : 抽象view类
 *     revise: 定义抽象方法和普通方法
 * </pre>
 */
public abstract class AbsFloatView implements IFloatView {

    /**
     * 更新x和y
     *
     * @param x x
     * @param y y
     */
    public void updateXY(int x, int y) {
    }

    /**
     * 更新x
     *
     * @param x x
     */
    public void updateX(int x) {
    }

    /**
     * 更新y
     *
     * @param y y
     */
    public void updateY(int y) {
    }

    /**
     * 获取x
     *
     * @return 默认是0
     */
    public int getX() {
        return 0;
    }

    /**
     * 获取y
     *
     * @return 默认是0
     */
    public int getY() {
        return 0;
    }
}
