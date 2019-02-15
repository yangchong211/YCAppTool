package com.ycbjie.video.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ycbjie.library.apt.ContentActivity;
import com.ycbjie.video.R;
import com.ycbjie.library.apt.ContentView;

@ContentView(R.layout.activity_test_video)
public class TestActivity extends ContentActivity {

    //@ContentView(R.layout.activity_test_video) 这种使用是错误的
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv_video = findViewById(R.id.tv_video);
        tv_video.setOnClickListener(v -> startActivity(
                new Intent(TestActivity.this,VideoActivity.class)));
    }

}
