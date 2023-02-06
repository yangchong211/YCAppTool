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
package com.yc.video.ui.pip;
import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yc.video.controller.GestureVideoController;
import com.yc.video.ui.view.CustomCompleteView;
import com.yc.video.ui.view.CustomErrorView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/9
 *     desc  : 悬浮播放控制器
 *     revise:
 * </pre>
 */
public class CustomFloatController extends GestureVideoController {

    public CustomFloatController(@NonNull Context context) {
        super(context);
    }

    public CustomFloatController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView(Context context) {
        super.initView(context);
        addControlComponent(new CustomCompleteView(getContext()));
        addControlComponent(new CustomErrorView(getContext()));
        addControlComponent(new CustomFloatView(getContext()));
    }

    @Override
    public void destroy() {

    }
}
