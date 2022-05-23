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
package org.yczbj.ycrefreshviewlib.view;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;

import java.io.Closeable;

public final class InnerRecycledViewPool extends RecyclerView.RecycledViewPool {

    private static final String TAG = "InnerRecycledViewPool";

    private static final int DEFAULT_MAX_SIZE = 5;

    /*
     * Wrapped InnerPool
     */
    private RecyclerView.RecycledViewPool mInnerPool;


    private SparseIntArray mScrapLength = new SparseIntArray();
    private SparseIntArray mMaxScrap = new SparseIntArray();

    /**
     * Wrap an existing pool
     *
     * @param pool
     */
    public InnerRecycledViewPool(RecyclerView.RecycledViewPool pool) {
        this.mInnerPool = pool;
    }


    public InnerRecycledViewPool() {
        this(new RecyclerView.RecycledViewPool());
    }

    /**
     * destroyViewHolder
     */
    @Override
    public void clear() {
        for (int i = 0, size = mScrapLength.size(); i < size; i++) {
            int viewType = mScrapLength.keyAt(i);
            RecyclerView.ViewHolder holder = mInnerPool.getRecycledView(viewType);
            while (holder != null) {
                destroyViewHolder(holder);
                holder = mInnerPool.getRecycledView(viewType);
            }
        }

        mScrapLength.clear();
        super.clear();
    }

    /**
     * 设置丢弃前要在池中持有的视图持有人的最大数量
     * @param viewType                  type
     * @param max                       max数量
     */
    @Override
    public void setMaxRecycledViews(int viewType, int max) {
        // When viewType is changed, because can not get items in wrapped pool,
        // destroy all the items for the viewType
        RecyclerView.ViewHolder holder = mInnerPool.getRecycledView(viewType);
        while (holder != null) {
            destroyViewHolder(holder);
            holder = mInnerPool.getRecycledView(viewType);
        }

        // change maxRecycledViews
        this.mMaxScrap.put(viewType, max);
        this.mScrapLength.put(viewType, 0);
        mInnerPool.setMaxRecycledViews(viewType, max);
    }

    /**
     * 返回给定视图类型的RecycledViewPool所持有的当前视图数
     * @param viewType                  type
     * @return
     */
    @Override
    public int getRecycledViewCount(int viewType) {
        return super.getRecycledViewCount(viewType);
    }


    /**
     * 从池中获取指定类型的ViewHolder，如果没有指定类型的ViewHolder，则获取{@Codenull}
     * @param viewType                  type
     * @return
     */
    @Override
    public RecyclerView.ViewHolder getRecycledView(int viewType) {
        RecyclerView.ViewHolder holder = mInnerPool.getRecycledView(viewType);
        if (holder != null) {
            int scrapHeapSize = mScrapLength.indexOfKey(viewType) >= 0 ? this.mScrapLength.get(viewType) : 0;
            if (scrapHeapSize > 0)
                mScrapLength.put(viewType, scrapHeapSize - 1);
        }

        return holder;
    }


    /**
     * 获取当前池中的所有项大小
     * @return                          size
     */
    public int size() {
        int count = 0;
        for (int i = 0, size = mScrapLength.size(); i < size; i++) {
            int val = mScrapLength.valueAt(i);
            count += val;
        }
        return count;
    }


    /**
     * 向池中添加一个废视图保存器。如果那个ViewHolder类型的池已经满了，它将立即被丢弃。
     * @param scrap                     scrap
     */
    @Override
    @SuppressWarnings("unchecked")
    public void putRecycledView(RecyclerView.ViewHolder scrap) {
        int viewType = scrap.getItemViewType();

        if (mMaxScrap.indexOfKey(viewType) < 0) {
            // does't contains this viewType, initial scrap list
            mMaxScrap.put(viewType, DEFAULT_MAX_SIZE);
            setMaxRecycledViews(viewType, DEFAULT_MAX_SIZE);
        }

        // get current heap size
        int scrapHeapSize = mScrapLength.indexOfKey(viewType) >= 0 ?
                this.mScrapLength.get(viewType) : 0;

        if (this.mMaxScrap.get(viewType) > scrapHeapSize) {
            // if exceed current heap size
            mInnerPool.putRecycledView(scrap);
            mScrapLength.put(viewType, scrapHeapSize + 1);
        } else {
            // destroy viewHolder
            destroyViewHolder(scrap);
        }
    }


    private void destroyViewHolder(RecyclerView.ViewHolder holder) {
        View view = holder.itemView;
        // if view inherits {@link Closeable}, cal close method
        if (view instanceof Closeable) {
            try {
                ((Closeable) view).close();
            } catch (Exception e) {
                Log.w(TAG, Log.getStackTraceString(e), e);
            }
        }
        if (holder instanceof Closeable) {
            try {
                ((Closeable) holder).close();
            } catch (Exception e) {
                Log.w(TAG, Log.getStackTraceString(e), e);
            }
        }
    }
}
