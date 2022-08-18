package com.yc.downloadhelper;

import android.net.Uri;

public interface IDownloadlister {
    void success(Uri uri);
    void fail(int code,String message);
}
