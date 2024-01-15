package com.yc.mediascanner;

import android.content.ContentResolver;
import android.provider.MediaStore;

public class ImagesScanner<T> extends BaseScanner<T> {
    public ImagesScanner(ContentResolver resolver, MediaStoreHelper.Decoder<T> decoder) {
        super(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, resolver, decoder);
    }
}
