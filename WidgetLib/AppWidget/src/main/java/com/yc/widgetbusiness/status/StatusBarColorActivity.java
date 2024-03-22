package com.yc.widgetbusiness.status;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.yc.statusbar.bar.StateAppBar;

import cn.ycbjie.ycstatusbar.R;

public class StatusBarColorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_color);

        StateAppBar.setStatusBarColor(this,
                ContextCompat.getColor(this, R.color.colorAccent));

        initSearch();
    }

    private TextView tvSearchStart;
    private TextView tvSearchOpen;
    private void initSearch() {
        tvSearchStart = findViewById(R.id.tv_search_start);
        tvSearchOpen = findViewById(R.id.tv_search_open);

    }
}
