package com.ycbjie.other.ui.activity;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.Utils;
import com.yc.cn.ycgallerylib.gallery.GalleryImageView;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;

import java.util.ArrayList;
import java.util.List;

import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;
/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/11
 *     desc  : 画廊页面
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_OTHER_GALLERY_ACTIVITY)
public class ImageGalleryActivity extends BaseActivity {

    private GalleryImageView scrollGalleryView;
    private TextView tag;

    @Override
    public int getContentView() {
        return R.layout.activity_image_gallery;
    }

    @Override
    public void initView() {
        StateAppBar.setStatusBarColor(this,
                ContextCompat.getColor(this, com.ycbjie.library.R.color.black));
        scrollGalleryView = findViewById(R.id.scroll_gallery_view);
        tag = findViewById(R.id.tag);
    }

    @Override
    public void initListener() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData() {
        final TypedArray array = Utils.getApp().getResources().obtainTypedArray(R.array.image_girls);
        final List<Bitmap> images = new ArrayList<>();
        for(int a=0 ; a<array.length() ; a++){
            int resourceId = array.getResourceId(a, R.drawable.ic_data_picture);
            Bitmap bitmap = toBitmap(resourceId);
            images.add(bitmap);
        }
        array.recycle();
        tag.setText(1+  "/" + images.size());
        scrollGalleryView
                //设置viewPager底部缩略图大小尺寸
                .setThumbnailSize(200)
                //设置是否支持缩放
                .setZoom(true)
                //设置缩放的倍数
                .setZoomSize(2)
                //设置是否隐藏底部缩略图
                .hideThumbnails(false)
                //设置FragmentManager
                .setFragmentManager(getSupportFragmentManager())
                //添加滑动事件，也可以不用添加
                .addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPageSelected(int position) {
                        scrollGalleryView.setCurrentItem(position);
                        tag.setText((position + 1) + "/" + images.size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                })
                .addBitmap(images);
    }



    private Bitmap toBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }

}
