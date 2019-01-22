package com.ycbjie.library.utils;

import android.support.annotation.ColorRes;

import com.blankj.utilcode.util.Utils;

public class ColorUtils {

    public static int getColor(@ColorRes int id){
        int color = Utils.getApp().getResources().getColor(id);
        return color;
    }

}
