package com.ns.yc.lifehelper.ui.test.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PC on 2017/11/1.
 * 作者：PC
 */

public class CacheSoftRef {

    //首先定义一个HashMap,保存引用对象
    private Map<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

    //再来定义一个方法，保存Bitmap的软引用到HashMap
    public void addBitmapToCache(String path) {
        //强引用的Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //软引用的Bitmap对象
        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
        //添加该对象到Map中使其缓存
        imageCache.put(path, softBitmap);
    }

    //获取的时候，可以通过SoftReference的get()方法得到Bitmap对象
    public Bitmap getBitmapByPath(String path) {
        //从缓存中取软引用的Bitmap对象
        SoftReference<Bitmap> softBitmap = imageCache.get(path);
        //判断是否存在软引用
        if (softBitmap == null) {
            return null;
        }
        //通过软引用取出Bitmap对象，如果由于内存不足Bitmap被回收，将取得空，如果未被回收，
        //则可重复使用，提高速度。
        Bitmap bitmap = softBitmap.get();
        return bitmap;
    }

}
