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

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/3/13
 *     desc  : 上拉加载更多异常监听
 *     revise:
 * </pre>
 */
public interface OnErrorListener {

    /**
     * 上拉加载，加载更多数据异常展示，这个方法可以暂停或者停止加载数据
     */
    void onErrorShow();
    /**
     * 这个方法是点击加载更多数据异常展示布局的操作，比如恢复加载更多等等
     */
    void onErrorClick();

}
