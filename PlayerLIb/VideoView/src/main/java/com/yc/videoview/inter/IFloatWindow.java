package com.yc.videoview.inter;

import android.view.View;

import com.yc.videoview.tool.FloatScreenType;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : 定义悬浮Window接口类
 *     revise:
 * </pre>
 */
public interface IFloatWindow {

    void show();

    void hide();

    int getX();

    int getY();

    void updateX(int x);

    void updateX(@FloatScreenType.screenType int screenType, float ratio);

    void updateY(int y);

    void updateY(@FloatScreenType.screenType int screenType, float ratio);

    View getView();

    void dismiss();
}
