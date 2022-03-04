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
package com.yc.webviewlib.base;

import java.io.Serializable;
import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 自定义RequestInfo实体类
 *     revise:
 * </pre>
 */
public class RequestInfo implements Serializable {

    public String url;

    public Map<String, String> headers;

    public RequestInfo(String url, Map<String, String> additionalHttpHeaders) {
        this.url = url;
        this.headers = additionalHttpHeaders;
    }

}
