package com.ns.yc.lifehelper.ui.data.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.utils.PicassoImageLoader;
import com.yc.cn.ycgallerylib.gallery.GalleryImageView;
import com.yc.cn.ycgallerylib.gallery.loader.DefaultImageLoader;
import com.yc.cn.ycgallerylib.gallery.loader.DefaultVideoLoader;
import com.yc.cn.ycgallerylib.gallery.loader.MediaLoader;
import com.yc.cn.ycgallerylib.gallery.model.MediaInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/7
 * 描    述：画廊
 * 修订历史：
 * ================================================
 */
public class GalleryImageActivity extends BaseActivity {

    @Bind(R.id.gallery_view)
    GalleryImageView galleryView;

    private static final ArrayList<String> images = new ArrayList<>(Arrays.asList(
            "http://img1.goodfon.ru/original/1920x1080/d/f5/aircraft-jet-su-47-berkut.jpg",
            "http://www.dishmodels.ru/picture/glr/13/13312/g13312_7657277.jpg",
            "http://img2.goodfon.ru/original/1920x1080/b/c9/su-47-berkut-c-37-firkin.jpg"
    ));
    private static final String movieUrl = "http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_1mb.mp4";


    @Override
    public int getContentView() {
        return R.layout.activity_gallery_image;
    }

    @Override
    public void initView() {
        initGallery();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    private void initGallery() {
        List<MediaInfo> infos = new ArrayList<>(images.size());
        for (String url : images) infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));

        galleryView
                .setThumbnailSize(SizeUtils.dp2px(100))
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(R.drawable.wallpaper1)))
                .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(toBitmap(R.drawable.wallpaper7))))
                .addMedia(MediaInfo.mediaLoader(new MediaLoader() {
                    @Override
                    public boolean isImage() {
                        return true;
                    }

                    @Override
                    public void loadMedia(Context context, ImageView imageView, MediaLoader.SuccessCallback callback) {
                        imageView.setImageBitmap(toBitmap(R.drawable.wallpaper3));
                        callback.onSuccess();
                    }

                    @Override
                    public void loadThumbnail(Context context, ImageView thumbnailView, MediaLoader.SuccessCallback callback) {
                        thumbnailView.setImageBitmap(toBitmap(R.drawable.wallpaper3));
                        callback.onSuccess();
                    }
                }))
                .addMedia(MediaInfo.mediaLoader(new DefaultVideoLoader(movieUrl, R.drawable.placeholder_video)))
                .addMedia(infos);
    }


    private Bitmap toBitmap(int image) {
        return ((BitmapDrawable) getResources().getDrawable(image)).getBitmap();
    }

}
