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
package com.yc.webviewlib.bridge;

import android.content.Context;

import com.tencent.smtt.sdk.WebView;
import com.yc.webviewlib.utils.X5LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 工具类
 *     revise: final修饰，不可修改，避免反射攻击
 * </pre>
 */
public final class BridgeUtil {

	public final static String YY_OVERRIDE_SCHEMA = "yy://";
	/**
	 * 格式为   yy://return/
	 */
	public final static String YY_RETURN_DATA = YY_OVERRIDE_SCHEMA + "return/";
	/**
	 * 格式为   yy://return/_fetchQueue/
	 */
	private final static String YY_FETCH_QUEUE = YY_RETURN_DATA + "_fetchQueue/";
	private final static String EMPTY_STR = "";
	public final static String UNDERLINE_STR = "_";
	public  final static String SPLIT_MARK = "/";
	public final static String CALLBACK_ID_FORMAT = "JAVA_CB_%s";
	public final static String JS_HANDLE_MESSAGE_FROM_JAVA =
			"javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
	public final static String JS_FETCH_QUEUE_FROM_JAVA =
			"javascript:WebViewJavascriptBridge._fetchQueue();";
	public final static String JAVASCRIPT_STR = "javascript:";

	/**
	 * 例子 javascript:WebViewJavascriptBridge._fetchQueue(); --> _fetchQueue
	 * @param jsUrl				url
	 * @return					返回字符串，注意获取的时候判断空
	 */
	public static String parseFunctionName(String jsUrl){
		return jsUrl.replace("javascript:WebViewJavascriptBridge.", "")
				.replaceAll("\\(.*\\);", "");
	}

	/**
	 * 获取到传递信息的body值
	 * url = yy://return/_fetchQueue/[{"responseId":"JAVA_CB_2_3957",
	 * "responseData":"Javascript Says Right back aka!"}]
	 * @param url				url
	 * @return					返回字符串，注意获取的时候判断空
	 */
	public static String getDataFromReturnUrl(String url) {
		if(url.startsWith(YY_FETCH_QUEUE)) {
			// return = [{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
			return url.replace(YY_FETCH_QUEUE, EMPTY_STR);
		}

		// temp = _fetchQueue/[{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
		String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
		String[] functionAndData = temp.split(SPLIT_MARK);

		if(functionAndData.length >= 2) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < functionAndData.length; i++) {
				sb.append(functionAndData[i]);
			}
			// return = [{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
			return sb.toString();
		}
		return null;
	}

	// 获取到传递信息的方法
	// url = yy://return/_fetchQueue/[{"responseId":"JAVA_CB_1_360","responseData":"Javascript Says Right back aka!"}]
	public static String getFunctionFromReturnUrl(String url) {
		// temp = _fetchQueue/[{"responseId":"JAVA_CB_1_360","responseData":"Javascript Says Right back aka!"}]
		String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
		X5LogUtils.i("------BridgeUtil--------getFunctionFromReturnUrl--"+temp);
		String[] functionAndData = temp.split(SPLIT_MARK);
		if(functionAndData.length >= 1){
			// functionAndData[0] = _fetchQueue
			return functionAndData[0];
		}
		return null;
	}

	/**
	 * js 文件将注入为第一个script引用
	 * @param view WebView
	 * @param url url
	 */
	public static void webViewLoadJs(WebView view, String url){
		String js = "var newscript = document.createElement(\"script\");";
		js += "newscript.src=\"" + url + "\";";
		js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);";
		view.loadUrl("javascript:" + js);
	}

	/**
	 * 这里只是加载lib包中assets中的 WebViewJavascriptBridge.js
	 * @param view webview
	 * @param path 路径
	 */
    public static void webViewLoadLocalJs(WebView view, String path){
        String jsContent = assetFile2Str(view.getContext(), path);
		X5LogUtils.i("------BridgeUtil--------webViewLoadLocalJs--"+jsContent);
		if (jsContent==null){
			return;
		}
        view.loadUrl("javascript:" + jsContent);
    }

	/**
	 * 解析assets文件夹里面的代码,去除注释,取可执行的代码
	 * @param c 										context
	 * @param urlStr 									路径
	 * @return 											可执行代码
	 */
	public static String assetFile2Str(Context c, String urlStr){
		InputStream in = null;
		try{
			in = c.getAssets().open(urlStr);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuilder sb = new StringBuilder();
            do {
                line = bufferedReader.readLine();
				// 去除注释
                if (line != null && !line.matches("^\\s*\\/\\/.*")) {
                    sb.append(line);
                }
            } while (line != null);
            bufferedReader.close();
            in.close();
            return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
