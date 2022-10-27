package com.yc.widgetbusiness.banner;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.apploglib.AppLogHelper;
import com.yc.banner.loader.ImageLoaderInterface;
import com.yc.banner.view.BannerView;
import com.yc.widgetbusiness.R;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends AppCompatActivity {

    private BannerView banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_vp);
        banner = findViewById(R.id.banner);
        initBanner();
    }


    /**
     * 初始化轮播图
     */
    private void initBanner() {
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.bg_small_autumn_tree_min);
        list.add(R.drawable.bg_small_kites_min);
        list.add(R.drawable.bg_small_leaves_min);
        list.add(R.drawable.bg_small_tulip_min);
        list.add(R.drawable.bg_small_tree_min);
        banner.setAnimationDuration(3000);
        banner.setDelayTime(4000);
        banner.setImages(list);
        banner.setImageLoader(new BannerImageLoader());
        banner.start();
    }

    public static class BannerImageLoader implements ImageLoaderInterface<ImageView> {

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

    @Override
    protected void onPause() {
        super.onPause();
        if (banner!=null){
            banner.startAutoPlay();
        }
        AppLogHelper.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (banner!=null){
            banner.stopAutoPlay();
        }
        AppLogHelper.d("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (banner!=null){
            banner.releaseBanner();
        }
        AppLogHelper.d("onDestroy");
    }

}
