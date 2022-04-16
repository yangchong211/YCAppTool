package com.ycbjie.android;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yc.toolutils.logger.AppLogUtils;
import com.ycbjie.android.tools.utils.OnceInvokeUtils;

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


    }

    public String getPageTag(){
        return null;
    }

}
