package com.ycbjie.library.loader;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.loader.ImageLoader;
import com.ycbjie.library.R;
import com.ycbjie.library.base.glide.GlideApp;

import java.io.File;

/**
 * Created by PC on 2017/9/26.
 * 作者：PC
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        GlideApp.with(activity)
                .load(Uri.fromFile(new File(path)))
                .error(R.mipmap.default_image)
                .placeholder(R.mipmap.default_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {}

}
