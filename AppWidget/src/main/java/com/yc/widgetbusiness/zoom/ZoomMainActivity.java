package com.yc.widgetbusiness.zoom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.yc.statusbar.bar.StateAppBar;
import com.yc.widgetbusiness.R;


public class ZoomMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_main);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
    }

    public void img(View view) {
        startActivity(new Intent(this, ImgActivity.class));
    }

    public void viewpager(View view) {
        startActivity(new Intent(this, ViewPagerActivity.class));
    }

    public void imgclick(View view) {
        startActivity(new Intent(this, ImgClickActivity.class));
    }

    public void photobrowse(View view) {
        startActivity(new Intent(this, PhotoBrowseActivity.class));
    }

    public void imageview(View view) {
        startActivity(new Intent(this, ImageViewActivity.class));
    }
}
