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

package org.yczbj.ycrefreshviewlib.observer;

import androidx.recyclerview.widget.RecyclerView;

import org.yczbj.ycrefreshviewlib.view.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/4/28
 *     desc  : 自定义AdapterDataObserver
 *     revise:
 * </pre>
 */
public class ViewDataObserver extends RecyclerView.AdapterDataObserver {

    private YCRefreshView recyclerView;
    private RecyclerArrayAdapter adapter;

    /**
     * 构造方法
     * @param recyclerView                  recyclerView
     */
    public ViewDataObserver(YCRefreshView recyclerView) {
        this.recyclerView = recyclerView;
        if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
            adapter = (RecyclerArrayAdapter) recyclerView.getAdapter();
        }
    }

    /**
     * 判断是否是header或者footer
     * @param position                      索引
     * @return                              true表示是header或者footer
     */
    private boolean isHeaderFooter(int position) {
        return adapter != null && (position < adapter.getHeaderCount()
                || position >= adapter.getHeaderCount() + adapter.getCount());
    }


    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        //header&footer不会有移动操作
        update();
    }

    @Override
    public void onChanged() {
        super.onChanged();
        //header&footer不会引起changed
        update();
    }


    /**
     * 自动更改Container的样式
     */
    private void update() {
        int count;
        if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
            count = ((RecyclerArrayAdapter) recyclerView.getAdapter()).getCount();
        } else {
            count = recyclerView.getAdapter().getItemCount();
        }
        if (count == 0) {
            recyclerView.showEmpty();
        } else {
            recyclerView.showRecycler();
        }
    }

}