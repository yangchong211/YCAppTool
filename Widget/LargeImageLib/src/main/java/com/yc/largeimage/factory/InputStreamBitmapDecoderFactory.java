package com.yc.largeimage.factory;

import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamBitmapDecoderFactory implements BitmapDecoderFactory {
    private InputStream inputStream;

    public InputStreamBitmapDecoderFactory(InputStream inputStream) {
        super();
        this.inputStream = inputStream;
    }

    @Override
    public BitmapRegionDecoder made() throws IOException {
        return BitmapRegionDecoder.newInstance(inputStream, false);
    }

    @Override
    public int[] getImageInfo() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, new Rect(),options);
        return new int[]{options.outWidth, options.outHeight};
    }
}