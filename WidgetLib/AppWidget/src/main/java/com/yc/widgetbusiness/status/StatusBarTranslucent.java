package com.yc.widgetbusiness.status;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.statusbar.bar.StateAppBar;

import cn.ycbjie.ycstatusbar.R;


public class StatusBarTranslucent extends AppCompatActivity {

    boolean isHide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_translucent);
        StateAppBar.translucentStatusBar(this, false);

        findViewById(R.id.toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHide = !isHide;
                StateAppBar.translucentStatusBar(StatusBarTranslucent.this, isHide);
            }
        });
    }
}
