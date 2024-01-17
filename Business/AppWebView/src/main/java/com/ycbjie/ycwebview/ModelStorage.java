package com.ycbjie.ycwebview;


import android.graphics.Bitmap;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/18
 *     desc  : 数据缓冲区，替代intent传递大数据方案
 *     revise:
 * </pre>
 */
public class ModelStorage {

    private Bitmap bitmap;

    public static ModelStorage getInstance(){
        return SingletonHolder.instance;
    }

    private static class SingletonHolder{
        private static final ModelStorage instance = new ModelStorage();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
