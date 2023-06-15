package com.yc.sceondary;

import android.content.Context;
import android.view.View;

import com.yc.sceondary.present.PresentationImpl;


public final class DisplayManager {

    private PresentationImpl presentation;
    private static volatile DisplayManager singleton = null;

    public static DisplayManager getInstance(){
        if(singleton == null){
            synchronized (DisplayManager.class){
                if(singleton == null){
                    singleton = new DisplayManager();
                }
            }
        }
        return singleton;
    }

    private DisplayManager() {

    }

    private void init(Context context){
        presentation = new PresentationImpl();
        presentation.initView(context);
    }

    public void showDisplay(Context context ,int layout) {
        if (presentation == null) {
            init(context);
        }
        if (presentation != null) {
            presentation.showView(layout);
        }
    }

    public void showDisplay(View view) {
        if (presentation == null) {
            init(view.getContext());
        }
        if (presentation != null) {
            presentation.showView(view);
        }
    }

    public void dismissDisplay(Context context) {
        if (presentation == null) {
            init(context);
        }
        if (presentation != null) {
            presentation.destroyView(context);
        }
    }
}
