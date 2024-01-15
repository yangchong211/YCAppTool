package com.yc.mediascanner;

import android.content.ContentResolver;
import android.provider.MediaStore;

public class AudioScanner<T> extends BaseScanner<T> {

    public AudioScanner(ContentResolver resolver, MediaStoreHelper.Decoder<T> decoder) {
        super(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, resolver, decoder);
    }

}
