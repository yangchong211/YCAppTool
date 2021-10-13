package com.yc.cn.cover.cover;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ycbjie.zoomimagelib.view.ZoomImageView;

import java.util.List;


public class CoverImageAdapter extends PagerAdapter {
    
    private List<Object> imageUrls;
    private Context mContext;

    public CoverImageAdapter(List<Object> imageUrls, Context mContext) {
        this.imageUrls = imageUrls;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Object image = imageUrls.get(position);
        final ZoomImageView photoView = new ZoomImageView(mContext);
        if (image instanceof Integer || image instanceof String){
            Glide.with(photoView.getContext())
                    .asDrawable()
                    //注意，这里拉取原始图片
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .load(image)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            if (resource!=null){
                                photoView.setImageDrawable(resource);
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                        }
                    });
        }
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
