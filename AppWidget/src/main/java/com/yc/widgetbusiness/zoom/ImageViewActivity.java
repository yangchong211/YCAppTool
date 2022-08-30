package com.yc.widgetbusiness.zoom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.widgetbusiness.R;
import com.yc.zoomimagelib.ZoomImageInfo;
import com.yc.zoomimagelib.ZoomImageUtils;
import com.yc.zoomimagelib.ZoomImageView;


public class ImageViewActivity extends AppCompatActivity {

    ImageView img;
    ZoomImageView photoView;
    ZoomImageInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_view);

        img = (ImageView) findViewById(R.id.img);
        photoView = (ZoomImageView) findViewById(R.id.photoview);
        photoView.enable();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfo = ZoomImageUtils.getImageViewInfo(img);
                img.setVisibility(View.GONE);
                photoView.setVisibility(View.VISIBLE);
                photoView.animaFrom(mInfo);
            }
        });

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoView.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        photoView.setVisibility(View.GONE);
                        img.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (photoView.getVisibility() == View.VISIBLE) {
            photoView.animaTo(mInfo, new Runnable() {
                @Override
                public void run() {
                    photoView.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                }
            });
        } else {
            super.onBackPressed();
        }
    }
}