package com.yc.lifehelper.component;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yc.apploglib.AppLogHelper;
import com.yc.banner.loader.ImageLoaderInterface;
import com.yc.banner.view.BannerView;
import com.yc.lifehelper.R;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;

import java.util.ArrayList;
import java.util.List;

public class BannerComponent implements InterItemView {

    private BannerView banner;

    @Override
    public View onCreateView(ViewGroup parent) {
        View headerView = View.inflate(parent.getContext(), R.layout.head_home_banner, null);
        AppLogHelper.d("banner on create view");
        return headerView;
    }

    @Override
    public void onBindView(View header) {
        banner = header.findViewById(R.id.banner);
        AppLogHelper.d("banner on bind view");
        initBanner();
    }

    /**
     * 初始化轮播图
     */
    private void initBanner() {
        if (banner!=null){
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
    }

    public BannerView getBannerView() {
        return banner;
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
}
