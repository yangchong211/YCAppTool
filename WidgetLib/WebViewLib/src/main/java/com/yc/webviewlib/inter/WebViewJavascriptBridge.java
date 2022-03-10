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
package com.yc.webviewlib.inter;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : js接口
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public interface WebViewJavascriptBridge {

	/**
	 * native调用js方法
	 * @param handlerName
	 */
	void callHandler(String handlerName);
	void callHandler(String handlerName, String data);
	void callHandler(String handlerName, String data, CallBackFunction callBack);

	/**
	 * js调用native方法
	 * @param handlerName
	 * @param handler
	 */
	void registerHandler(String handlerName, BridgeHandler handler);
	void unregisterHandler(String handlerName);

}
