package com.yc.popup;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211/YCDialog
 *     time  : 2017/1/5
 *     desc  : 注意事项
 *             1.必须手动给popupWindow设置宽度和高度，否则popupWindow不显示
 *             2.在有些手机上面，全屏展示的时候底部会留白，其实是因为StatusBar的高度没有计算进去，需要我们自己手动计算出去
 *             3.设置宽度和高度问题比较麻烦：
 *             解决方法1：将布局文件中的match_parent都变成wrap_content，或者对长宽加以限制。设置popupwindow的固定长宽。但是有时候写布局又很难避过。
 *             解决办法2：
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCDialog
 * </pre>
 */
public class WrapPopupWindow extends PopupWindow {

    @Override
    public void showAsDropDown(View anchor) {
        handlerHeight(anchor);
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        handlerHeight(anchor);
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        handlerHeight(anchor);
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    /**
     * 解决高度无法自适应的问题
     */
    private void handlerHeight(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int heightPixels = anchor.getResources().getDisplayMetrics().heightPixels;
            int h = heightPixels - rect.bottom + getStatusBarHeight(anchor.getContext());
            setHeight(h);
        }
    }

    /**
     * 获取状态栏高度
     * @param context       context
     * @return              状态栏高度
     */
    private int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    /**
     * 获取状态栏的高度
     */
    @Deprecated
    private int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

}
