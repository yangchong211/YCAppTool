package org.yczbj.ycrefreshviewlib.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class DiffItemCallBack<T> extends DiffUtil.ItemCallback<T> {

    @Override
    public boolean areItemsTheSame(@NonNull T t, @NonNull T t1) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(@NonNull T t, @NonNull T t1) {
        return false;
    }
}
