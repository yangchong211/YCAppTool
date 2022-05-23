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

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/4/28
 *     desc  : 自定义FixDataObserver
 *     revise: 当插入数据的时候，需要
 * </pre>
 */
public class FixDataObserver extends RecyclerView.AdapterDataObserver {

    private RecyclerView recyclerView;
    public FixDataObserver(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
            RecyclerArrayAdapter adapter = (RecyclerArrayAdapter) recyclerView.getAdapter();
            //获取footer的数量
            int footerCount = adapter.getFooterCount();
            //获取所有item的数量，包含header和footer
            int count = adapter.getCount();
            //如果footer大于0，并且
            if (footerCount > 0 && count == itemCount) {
                recyclerView.scrollToPosition(0);
            }
        }
    }
}
