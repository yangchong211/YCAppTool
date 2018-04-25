package com.ns.yc.lifehelper.utils;

import android.graphics.Color;
import android.view.View;

import com.ns.yc.ycutilslib.rippleLayout.MaterialRippleLayout;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : 工具类
 *     revise: v1.4 17年6月8日
 *             v1.5 17年10月3日修改
 * </pre>
 */
public class AppToolUtils {



    /**
     * 设置水波纹效果
     * @param view          view视图
     */
    public static void setRipper(View view){
        MaterialRippleLayout.on(view)
                .rippleColor(Color.parseColor("#999999"))
                .rippleAlpha(0.2f)
                .rippleHover(true)
                .create();
    }



}
