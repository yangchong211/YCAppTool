package com.yc.photo.internal.utils;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class SpacePhotoItemLine extends RecyclerView.ItemDecoration  {

    /**
     * 列数
     */
    private int mSpanCount;
    /**
     * 间距
     */
    private int mSpacing;
    /**
     * 是否设置底部间距
     */
    private boolean mIncludeEdge;

    public SpacePhotoItemLine(int spanCount, int spacing, boolean includeEdge) {
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
        this.mIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        RecyclerView.Adapter adapter = parent.getAdapter();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (adapter==null || layoutManager==null){
            return;
        }
        int column = position % mSpanCount;
        if (mIncludeEdge) {
            outRect.left = mSpacing - column * mSpacing / mSpanCount;
            outRect.right = (column + 1) * mSpacing / mSpanCount;
            if (position < mSpanCount) {
                outRect.top = mSpacing;
            }
            outRect.bottom = mSpacing;
        } else {
            outRect.left = column * mSpacing / mSpanCount;
            outRect.right = mSpacing - (column + 1) * mSpacing / mSpanCount;
            if (position >= mSpanCount) {
                outRect.top = mSpacing;
            }
        }
    }

}
