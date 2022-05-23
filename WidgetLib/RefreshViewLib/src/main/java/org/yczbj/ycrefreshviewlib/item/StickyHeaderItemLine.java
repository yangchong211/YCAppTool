/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.yczbj.ycrefreshviewlib.item;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.utils.RefreshLogUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/2
 *     desc  : list条目的分割线【粘贴头部】
 *     revise:
 * </pre>
 */
public class StickyHeaderItemLine extends RecyclerView.ItemDecoration {

    private static final long NO_HEADER_ID = -1L;
    private Map<Long, RecyclerView.ViewHolder> mHeaderCache;
    private IStickyHeaderAdapter mAdapter;
    private boolean mRenderInline;
    private boolean mIncludeHeader = false;

    public interface IStickyHeaderAdapter<T extends RecyclerView.ViewHolder> {
        long getHeaderId(int position);
        T onCreateHeaderViewHolder(ViewGroup parent);
        void onBindHeaderViewHolder(T viewholder, int position);
    }


    public StickyHeaderItemLine(IStickyHeaderAdapter adapter) {
        this(adapter, false);
    }


    @SuppressLint("UseSparseArrays")
    public StickyHeaderItemLine(IStickyHeaderAdapter adapter, boolean renderInline) {
        mAdapter = adapter;
        mHeaderCache = new HashMap<>();
        //mHeaderCache = new LongSparseArray<>();
        mRenderInline = renderInline;
    }

    public void setIncludeHeader(boolean mIncludeHeader) {
        this.mIncludeHeader = mIncludeHeader;
    }


    /**
     * 调用的是getItemOffsets会被多次调用，在layoutManager每次测量可摆放的view的时候回调用一次，
     * 在当前状态下需要摆放多少个view这个方法就会回调多少次。
     * @param outRect                   核心参数，这个rect相当于item摆放的时候设置的margin，
     *                                  rect的left相当于item的marginLeft，
     *                                  rect的right相当于item的marginRight
     * @param view                      当前绘制的view，可以用来获取它在adapter中的位置
     * @param parent                    recyclerView
     * @param state                     状态，用的很少
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int headerHeight = 0;
        if (!mIncludeHeader){
            if (parent.getAdapter() instanceof RecyclerArrayAdapter){
                int headerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getHeaderCount();
                int footerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getFooterCount();
                int dataCount = ((RecyclerArrayAdapter) parent.getAdapter()).getCount();
                if (position<headerCount){
                    return;
                }
                if (position>=headerCount+dataCount){
                    return ;
                }
                if (position>=headerCount){
                    position-=headerCount;
                }
            }
        }
        boolean hasHeader = hasHeader(position);
        boolean showHeaderAboveItem = showHeaderAboveItem(position);
        if (position != RecyclerView.NO_POSITION && hasHeader && showHeaderAboveItem) {
            View header = getHeader(parent, position).itemView;
            headerHeight = getHeaderHeightForLayout(header);
        }
        RefreshLogUtils.d("StickyItemLine------headerHeight---"+headerHeight);
        outRect.set(0, headerHeight, 0, 0);
    }

    private boolean showHeaderAboveItem(int itemAdapterPosition) {
        if (itemAdapterPosition == 0) {
            return true;
        }
        return mAdapter.getHeaderId(itemAdapterPosition - 1)
                != mAdapter.getHeaderId(itemAdapterPosition);
    }


    public void clearHeaderCache() {
        mHeaderCache.clear();
    }


    public View findHeaderViewUnder(float x, float y) {
        for (RecyclerView.ViewHolder holder : mHeaderCache.values()) {
            final View child = holder.itemView;
            final float translationX = ViewCompat.getTranslationX(child);
            final float translationY = ViewCompat.getTranslationY(child);
            if (x >= child.getLeft() + translationX && x <= child.getRight() + translationX &&
                    y >= child.getTop() + translationY && y <= child.getBottom() + translationY) {
                return child;
            }
        }
        return null;
    }

    /**
     * 判断是否有header
     * @param position                  索引
     * @return
     */
    private boolean hasHeader(int position) {
        return mAdapter.getHeaderId(position) != NO_HEADER_ID;
    }


    private RecyclerView.ViewHolder getHeader(RecyclerView parent, int position) {
        final long key = mAdapter.getHeaderId(position);
        if (mHeaderCache.containsKey(key)) {
            return mHeaderCache.get(key);
        } else {
            final RecyclerView.ViewHolder holder = mAdapter.onCreateHeaderViewHolder(parent);
            final View header = holder.itemView;
            //noinspection unchecked
            mAdapter.onBindHeaderViewHolder(holder, position);
            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(),
                    View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(),
                    View.MeasureSpec.UNSPECIFIED);
            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(),
                    header.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(),
                    header.getLayoutParams().height);
            header.measure(childWidth, childHeight);
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
            mHeaderCache.put(key, holder);
            return holder;
        }
    }

    /**
     * 绘制分割线
     * ItemDecoration的onDrawOver方法是在RecyclerView的draw方法中调用的
     * 同样传入的是RecyclerView的canvas，这时候onLayout已经调用，所以此时绘制的内容会覆盖item。
     * @param canvas                    canvas用来绘制的画板
     * @param parent                    recyclerView
     * @param state                     状态，用的很少
     */
    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent,
                           @NonNull RecyclerView.State state) {
        if (parent.getAdapter() == null){
            return;
        }
        final int count = parent.getChildCount();
        long previousHeaderId = -1;
        for (int layoutPos = 0; layoutPos < count; layoutPos++) {
            final View child = parent.getChildAt(layoutPos);
            int adapterPos = parent.getChildAdapterPosition(child);
            if (!mIncludeHeader){
                if (parent.getAdapter() instanceof RecyclerArrayAdapter){
                    int headerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getHeaderCount();
                    int footerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getFooterCount();
                    int dataCount = ((RecyclerArrayAdapter) parent.getAdapter()).getCount();
                    if (adapterPos<headerCount){
                        continue;
                    }
                    if (adapterPos>=headerCount+dataCount){
                        continue ;
                    }
                    if (adapterPos>=headerCount){
                        adapterPos-=headerCount;
                    }
                }
            }

            if (adapterPos != RecyclerView.NO_POSITION && hasHeader(adapterPos)) {
                long headerId = mAdapter.getHeaderId(adapterPos);
                if (headerId != previousHeaderId) {
                    previousHeaderId = headerId;
                    View header = getHeader(parent, adapterPos).itemView;
                    canvas.save();
                    final int left = child.getLeft();
                    final int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);
                    canvas.translate(left, top);
                    header.setTranslationX(left);
                    header.setTranslationY(top);
                    header.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }


    private int getHeaderTop(RecyclerView parent, View child, View header,
                             int adapterPos, int layoutPos) {
        int headerHeight = getHeaderHeightForLayout(header);
        int top = ((int) child.getY()) - headerHeight;
        if (layoutPos == 0) {
            final int count = parent.getChildCount();
            final long currentId = mAdapter.getHeaderId(adapterPos);
            // find next view with header and compute the offscreen push if needed
            for (int i = 1; i < count; i++) {
                int adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i));
                if (adapterPosHere != RecyclerView.NO_POSITION) {
                    long nextId = mAdapter.getHeaderId(adapterPosHere);
                    if (nextId != currentId) {
                        final View next = parent.getChildAt(i);
                        final int offset = ((int) next.getY()) - (headerHeight +
                                getHeader(parent, adapterPosHere).itemView.getHeight());
                        if (offset < 0) {
                            return offset;
                        } else {
                            break;
                        }
                    }
                }
            }
            top = Math.max(0, top);
        }
        return top;
    }

    private int getHeaderHeightForLayout(View header) {
        return mRenderInline ? 0 : header.getHeight();
    }


}
