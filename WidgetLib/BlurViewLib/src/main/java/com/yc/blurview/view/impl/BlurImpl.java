package com.yc.blurview.view.impl;

import android.content.Context;
import android.graphics.Bitmap;

public interface BlurImpl {

	boolean prepare(Context context, Bitmap buffer, float radius);

	void release();

	void blur(Bitmap input, Bitmap output);

}
