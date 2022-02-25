
package com.yc.photo.listener;

import android.net.Uri;
import androidx.annotation.NonNull;

import java.util.List;

public interface OnSelectedListener {
    /**
     * @param uriList the selected item {@link Uri} list.
     * @param pathList the selected item file path list.
     */
    void onSelected(@NonNull List<Uri> uriList, @NonNull List<String> pathList);
}
