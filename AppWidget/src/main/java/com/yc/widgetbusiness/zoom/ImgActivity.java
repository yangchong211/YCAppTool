package com.yc.widgetbusiness.zoom;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.statusbar.bar.StateAppBar;
import com.yc.statusbar.utils.StatusBarUtils;
import com.yc.widgetbusiness.R;
import com.yc.zoomimagelib.ZoomImageView;


public class ImgActivity extends AppCompatActivity {

    ZoomImageView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        StateAppBar.setStatusBarColor(this, Color.parseColor("#252525"));

        mPhotoView = (ZoomImageView) findViewById(R.id.img1);
        mPhotoView.enable();

        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ImgActivity.this, "长按了", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

//        使用ImageLoader
//        ImaggeLoaderConfiguration configuration = ImageLoaderConfiguration
//                .createDefault(this);
//        ImageLoader.getInstance().init(configuration);
//        ImageLoader.etInstance().displayImage(url, (ImageView) findViewById(R.id.img1));

//        使用Glide加载的gif图片同样支持缩放功能
//        Glide.with(this)
//                .load(gif)
//                .crossFade()
//                .placeholder(R.mipmap.bbb)
//                .into(((PhotoView) findViewById(R.id.img1)));
    }
}
