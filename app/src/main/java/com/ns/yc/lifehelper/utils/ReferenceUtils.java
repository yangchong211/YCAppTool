package com.ns.yc.lifehelper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.blankj.utilcode.util.Utils;

import java.lang.ref.SoftReference;


public class ReferenceUtils {

    /**
     * 使用弱引用避免加载背景图和默认图时导致内存泄漏崩溃
     * @param image             图片
     * @param view              视图控件
     */
    public static void setSoftReference(int image , ImageView view){
        if(view!=null){
            Bitmap bitmap = BitmapFactory.decodeResource(Utils.getApp().getResources(), image);
            Drawable drawable = new BitmapDrawable(bitmap);
            SoftReference<Drawable> drawableSoftReference = new SoftReference<>(drawable);
            //注意，一定要增加判断
            if(drawableSoftReference != null) {
                view.setBackground(drawableSoftReference.get());
            }else {
                view.setImageResource(image);
            }
        }
    }


}
