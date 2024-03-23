package com.yc.common.image;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yc.compress.utils.AppCompress;
import com.yc.compress.utils.CompressConfig;
import com.yc.toolutils.store.AppFileUriUtils;
import com.yc.common.R;
import com.yc.compress.luban.Luban;
import com.yc.compress.luban.OnCompressListener;
import com.yc.compress.image.AppBitmapUtils;
import com.yc.compress.image.ImageSaveUtils;
import com.yc.compress.image.PicCalculateUtils;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.roundcorner.view.RoundTextView;
import com.yc.toolutils.intent.AppIntentUtils;
import com.yc.toolutils.system.AppLogUtils;
import com.yc.toolutils.appinfo.AppWindowUtils;

import java.io.File;

public class ImageActivity extends BaseActivity implements View.OnClickListener {

    private RoundTextView tvView1;
    private RoundTextView tvView2;
    private RoundTextView tvView3;
    private RoundTextView tvView4;
    private RoundTextView tvView5;
    private RoundTextView tvView6;
    private RoundTextView tvView7;
    private RoundTextView tvView8;
    private RoundTextView tvView9;
    private RoundTextView tvView10;
    private RoundTextView tvView11;
    private RoundTextView tvView12;
    private ImageView ivImageView;
    private ImageView ivImageView2;

    @Override
    public int getContentView() {
        return R.layout.activity_base_text_view;
    }

    @Override
    public void initView() {
        tvView1 = findViewById(R.id.tv_view_1);
        tvView2 = findViewById(R.id.tv_view_2);
        tvView3 = findViewById(R.id.tv_view_3);
        tvView4 = findViewById(R.id.tv_view_4);
        tvView5 = findViewById(R.id.tv_view_5);
        tvView6 = findViewById(R.id.tv_view_6);
        tvView7 = findViewById(R.id.tv_view_7);
        tvView8 = findViewById(R.id.tv_view_8);
        tvView9 = findViewById(R.id.tv_view_9);
        tvView10 = findViewById(R.id.tv_view_10);
        tvView11 = findViewById(R.id.tv_view_11);
        tvView12 = findViewById(R.id.tv_view_12);
        ivImageView = findViewById(R.id.iv_image_view);
        ivImageView2 = findViewById(R.id.iv_image_view2);
    }

    @Override
    public void initListener() {
        tvView1.setOnClickListener(this);
        tvView2.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tvView1.setText("1.打开手机相册");
        tvView2.setText("2.调用自定义相册");
        tvView3.setVisibility(View.GONE);
        tvView4.setVisibility(View.GONE);
        tvView5.setVisibility(View.GONE);
        tvView6.setVisibility(View.GONE);
        tvView7.setVisibility(View.GONE);
        tvView8.setVisibility(View.GONE);
        tvView9.setVisibility(View.GONE);
        tvView10.setVisibility(View.GONE);
        tvView11.setVisibility(View.GONE);
        tvView12.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v == tvView1) {
            //Intent photoIntent = AppIntentUtils.getPhotoIntent();
            Intent photoIntent = AppIntentUtils.getPhotoIntent2();
            startActivityForResult(photoIntent, 1024);
        } else if (v == tvView2) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1024) {
            Uri uri = data.getData();
            File file = AppFileUriUtils.uri2File(this, uri);
            String path = file.getPath();
            AppLogUtils.d("FileActivity : 回调" , file.getPath() + "  " +file.getName());
            //Glide.with(ImageActivity.this).load(uri).into(ivImageView);
            CompressConfig.Builder builder = CompressConfig.builder();
            builder.setMaxPixel(AppWindowUtils.getRealScreenHeight(this));
            CompressConfig compressConfig = builder.build();
            AppCompress.getInstance().setCompressConfig(compressConfig);
            Bitmap bitmap = AppCompress.getInstance().compressSizePath(path);
            int byteCount = bitmap.getByteCount();
            AppLogUtils.d("FileActivity : 回调 , 字节大小 , " , (byteCount/1024)+"kb");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                int allocationByteCount = bitmap.getAllocationByteCount();
                AppLogUtils.d("FileActivity : 回调 , 字节大小 , " , (allocationByteCount/1024)+"kb");
            }
            //int orientation = PicCalculateUtils.getOrientation(AppBitmapUtils.bitmap2Bytes(bitmap));
            //Bitmap rotatingImage = AppBitmapUtils.rotatingImage(bitmap, orientation);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                //读取图片属性：旋转的角度
                int degree = PicCalculateUtils.readPictureDegree(this, path);
                //旋转Bitmap的角度，获取一张新的图片
                Bitmap rotatingImage = AppBitmapUtils.rotatingImage(bitmap, degree);
                ivImageView.setImageBitmap(rotatingImage);
            }


            Luban.with(this).load(path).setCompressListener(new OnCompressListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(File file) {
                    Glide.with(ImageActivity.this).load(file).into(ivImageView2);

                    //把图片加入到系统图库中
                    ImageSaveUtils.insertImage(ImageActivity.this,file);
                }

                @Override
                public void onError(Throwable e) {

                }
            }).launch();
        }
    }

}
