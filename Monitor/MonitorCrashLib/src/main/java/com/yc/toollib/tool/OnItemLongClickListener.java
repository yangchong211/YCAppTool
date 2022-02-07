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

package com.yc.toollib.tool;

import android.view.View;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/4/28
 *     desc  : item中长按点击监听接口
 *     revise:
 * </pre>
 */
public interface OnItemLongClickListener {
    /**
     * item中长按点击监听接口
     * @param position              索引
     * @return
     */
    boolean onItemLongClick(View view, int position);
}
