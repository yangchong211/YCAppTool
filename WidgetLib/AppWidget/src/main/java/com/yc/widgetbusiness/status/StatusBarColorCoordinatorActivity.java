package com.yc.widgetbusiness.status;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.yc.statusbar.bar.StateAppBar;


import cn.ycbjie.ycstatusbar.R;

public class StatusBarColorCoordinatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_color_coordinator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StatusBarColorCoordinatorLayout");

        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }
}
