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

import android.content.Context;
import android.view.View;
import android.view.ViewStub;

import androidx.annotation.LayoutRes;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/7/6
 *     desc  : ViewStubLayout
 *     revise:
 * </pre>
 */
public abstract class AbsViewStubLayout {

    /**
     * ViewStub用来加载网络异常，空数据等页面
     */
    private ViewStub mLayoutVs;
    /**
     * View用来加载正常视图页面
     */
    private View mContentView;

    protected void initLayout(Context context , @LayoutRes int layoutResId) {
        mLayoutVs = new ViewStub(context);
        mLayoutVs.setLayoutResource(layoutResId);
    }

    protected ViewStub getLayoutVs() {
        return mLayoutVs;
    }

    protected void setView(View contentView) {
        mContentView = contentView;
    }

    /**
     * 设置数据
     * @param objects           数据
     */
    abstract void setData(Object... objects);
}
