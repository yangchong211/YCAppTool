package com.ycbjie.android;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.yczbj.ycrefreshviewlib.view.YCRefreshView;

import java.util.HashMap;

public class A {

    public void setName(){
        YCRefreshView ycRefreshView = new YCRefreshView(null);
        ycRefreshView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        HashMap<String,String> hashMap = new HashMap<>();

    }

    public String getPageTag(){
        return null;
    }

}
