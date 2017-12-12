package com.ns.yc.lifehelper.base;

import android.content.Context;
import android.widget.ImageView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.youth.banner.loader.ImageLoader;


public class BannerImageStrLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        ImageUtils.loadImgByPicasso(context, (String) path, R.drawable.image_default ,imageView);
    }

}
