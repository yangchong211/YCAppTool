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
package org.yczbj.ycrefreshviewlib.utils;


import android.app.Application;
import android.content.Context;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/4/22
 *     desc  : 工具类
 *     revise:
 * </pre>
 */
public final class RecyclerUtils {

    public static void checkContent(Context context){
        if (context==null){
            throw new NullPointerException("context is not null");
        }
        if (context instanceof Application){
            throw new UnsupportedOperationException("context is not application");
        }
    }

}
