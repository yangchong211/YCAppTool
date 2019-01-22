package com.ycbjie.library.loader;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ns.yc.ycphotolib.inter.PhotoLoader;
import com.ycbjie.library.R;
import com.ycbjie.library.base.glide.GlideApp;

import java.io.File;


public class Loader extends PhotoLoader {

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        GlideApp.with(context)
                .load(Uri.fromFile(new File(path)))
                .error(R.drawable.bg_stack_blur_default)
                .placeholder(R.drawable.bg_stack_blur_default)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, @DrawableRes Integer resId, ImageView imageView) {
        imageView.setImageResource(resId);
    }

}
