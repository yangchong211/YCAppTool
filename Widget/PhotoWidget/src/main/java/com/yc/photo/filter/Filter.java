package com.yc.photo.filter;

import android.content.Context;

import com.yc.photo.MimeType;
import com.yc.photo.internal.entity.IncapableCause;
import com.yc.photo.internal.entity.Item;

import java.util.Set;

@SuppressWarnings("unused")
public abstract class Filter {
    /**
     * Convenient constant for a minimum value.
     */
    public static final int MIN = 0;
    /**
     * Convenient constant for a maximum value.
     */
    public static final int MAX = Integer.MAX_VALUE;
    /**
     * Convenient constant for 1024.
     */
    public static final int K = 1024;

    /**
     * Against what mime types this filter applies.
     */
    protected abstract Set<MimeType> constraintTypes();

    /**
     * Invoked for filtering each item.
     *
     * @return null if selectable, {@link IncapableCause} if not selectable.
     */
    public abstract IncapableCause filter(Context context, Item item);

    /**
     * Whether an {@link Item} need filtering.
     */
    protected boolean needFiltering(Context context, Item item) {
        for (MimeType type : constraintTypes()) {
            if (type.checkType(context.getContentResolver(), item.getContentUri())) {
                return true;
            }
        }
        return false;
    }
}
