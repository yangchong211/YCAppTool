package com.yc.lifehelper;

import android.content.Context;
import android.widget.ImageView;

import com.yc.banner.loader.ImageLoaderInterface;

public class BannerImageLoader implements ImageLoaderInterface<ImageView> {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setBackgroundResource((Integer) path);
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }
}
