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

package org.yczbj.ycrefreshviewlib.inter;

import android.view.View;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/4/28
 *     desc  : 数据处理和加载监听接口
 *     revise:
 * </pre>
 */
public interface InterEventDelegate {

    /**
     * 添加数据
     * @param length            长度
     */
    void addData(int length);

    /**
     * 清除数据
     */
    void clear();

    /**
     * 停止加载更多
     */
    void stopLoadMore();

    /**
     * 暂停加载更多
     */
    void pauseLoadMore();

    /**
     * 恢复加载更多
     */
    void resumeLoadMore();

    /**
     * 设置加载更多监听
     * @param view                  view
     * @param listener              listener
     */
    void setMore(View view, OnMoreListener listener);

    /**
     * 设置没有更多监听
     * @param view                  view
     * @param listener              listener
     */
    void setNoMore(View view, OnNoMoreListener listener);

    /**
     * 设置加载更多错误监听
     * @param view                  view
     * @param listener              listener
     */
    void setErrorMore(View view, OnErrorListener listener);

    /**
     * 设置加载更多监听
     * @param res                   res
     * @param listener              listener
     */
    void setMore(int res, OnMoreListener listener);

    /**
     * 设置没有更多监听
     * @param res                   res
     * @param listener              listener
     */
    void setNoMore(int res, OnNoMoreListener listener);

    /**
     * 设置加载更多错误监听
     * @param res                   res
     * @param listener              listener
     */
    void setErrorMore(int res, OnErrorListener listener);

}
