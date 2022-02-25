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
package com.yc.ycstatelib;

import android.view.View;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/7/6
 *     desc  : 为状态View显示隐藏监听事件，写成接口
 *     revise:
 * </pre>
 */
public interface OnShowHideViewListener {

    /**
     * show
     * @param view                  view
     * @param id                    view对应id
     */
    void onShowView(View view, int id);

    /**
     * hide
     * @param view                  view
     * @param id                    view对应id
     */
    void onHideView(View view, int id);

}
