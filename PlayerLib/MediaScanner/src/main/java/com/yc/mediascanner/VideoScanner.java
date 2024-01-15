package com.yc.mediascanner;

import android.content.ContentResolver;
import android.provider.MediaStore;

public class VideoScanner<T> extends BaseScanner<T> {

    public VideoScanner(ContentResolver resolver, MediaStoreHelper.Decoder<T> decoder) {
        super(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, resolver, decoder);
    }

}
