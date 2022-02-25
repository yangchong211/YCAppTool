package com.yc.banner.loader;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/6/20
 *     desc  : 自定义轮播图接口
 *     revise:
 * </pre>
 */
public interface ImageLoaderInterface<T extends View> extends Serializable {

    /**
     * 给控件设置图片的操作放到这个方法中
     * @param context
     * @param path
     * @param imageView
     */
    void displayImage(Context context, Object path, T imageView);

    /**
     * 创建imageView控件
     * @param context               上下文
     * @return
     */
    T createImageView(Context context);
}
