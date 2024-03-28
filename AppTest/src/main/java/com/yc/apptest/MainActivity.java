package com.yc.apptest;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.looperthread.heart.HeartManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.yc.mmaplog.test.MmapLogActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HeartManager.getInstance().setCrash(false);
        findViewById(R.id.tv_view1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MmapLogActivity.startActivity(MainActivity.this);
            }
        });

        Map<String ,String> map = new HashMap<>();
        Set<String> strings = map.keySet();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);

        }
    }
}
