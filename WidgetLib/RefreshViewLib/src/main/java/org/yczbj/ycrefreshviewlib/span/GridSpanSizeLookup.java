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

package org.yczbj.ycrefreshviewlib.span;

import androidx.recyclerview.widget.GridLayoutManager;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/2
 *     desc  : 自定义SpanSizeLookup
 *     revise:
 * </pre>
 */
public class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup{

    private int mMaxCount;
    private ArrayList<InterItemView> headers;
    private ArrayList<InterItemView> footers;
    private List<Object> mObjects;


    public GridSpanSizeLookup(int maxCount, ArrayList<InterItemView> headers,
                       ArrayList<InterItemView> footers, List<Object> objects){
        this.mMaxCount = maxCount;
        this.headers = headers;
        this.footers = footers;
        this.mObjects = objects;
    }

    /**
     * 该方法的返回值就是指定position所占的列数
     * @param position                      指定索引
     * @return                              列数
     */
    @Override
    public int getSpanSize(int position) {
        //如果有headerView，则
        if (headers.size()!=0){
            if (position<headers.size()) {
                return mMaxCount;
            }
        }
        //如果有footerView，则
        if (footers.size()!=0) {
            int i = position - headers.size() - mObjects.size();
            if (i >= 0) {
                return mMaxCount;
            }
        }
        return 1;
    }

}
