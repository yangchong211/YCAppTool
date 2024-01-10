package com.yc.sceondary.present;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

public interface IPresentation {

    void initView(Context context);

    void showView(int layout);
    void showView(View view);

    boolean hasSecondScreen();
    void closeDisplay();
    void destroyView(Context context);

    SurfaceView getSurfaceView();
}
