package com.ns.yc.lifehelper.weight.pdfview;

import android.graphics.Bitmap;

public interface BitmapContainer {

    Bitmap get(int position);
    void remove(int position);
    void clear();
}