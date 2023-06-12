package com.yc.sceondary;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

public interface IPresentation {

    void initView(Context context);

    void showView(int layout);
    void showView(View view);

    boolean hasSecondScreen();

    void destroyView(Context context);

    SurfaceView getSurfaceView();
}
