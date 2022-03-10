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

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/10
 *     desc  : 自定义消息Message实体类
 *     revise: demo地址：https://github.com/yangchong211/YCWebView
 * </pre>
 */
public final class WebJsMessage {

    /**
     * callbackId
     * 回调id
     */
    private String callbackId;
    /**
     * responseId
     * 响应id
     */
    private String responseId;
    /**
     * responseData
     * 响应内容
     */
    private String responseData;
    /**
     * data of message
     * 消息内容
     */
    private String data;
    /**
     * name of handler
     * 消息名称
     */
    private String handlerName;

    private final static String CALLBACK_ID_STR = "callbackId";
    private final static String RESPONSE_ID_STR = "responseId";
    private final static String RESPONSE_DATA_STR = "responseData";
    private final static String DATA_STR = "data";
    private final static String HANDLER_NAME_STR = "handlerName";

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(CALLBACK_ID_STR, getCallbackId());
            jsonObject.put(DATA_STR, getData());
            jsonObject.put(HANDLER_NAME_STR, getHandlerName());
            String data = getResponseData();
            if (TextUtils.isEmpty(data)) {
                jsonObject.put(RESPONSE_DATA_STR, data);
            } else {
                jsonObject.put(RESPONSE_DATA_STR, new JSONTokener(data).nextValue());
            }
            jsonObject.put(RESPONSE_DATA_STR, getResponseData());
            jsonObject.put(RESPONSE_ID_STR, getResponseId());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WebJsMessage toObject(String jsonStr) {
        WebJsMessage m = new WebJsMessage();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            m.setHandlerName(jsonObject.has(HANDLER_NAME_STR) ? jsonObject.getString(HANDLER_NAME_STR) : null);
            m.setCallbackId(jsonObject.has(CALLBACK_ID_STR) ? jsonObject.getString(CALLBACK_ID_STR) : null);
            m.setResponseData(jsonObject.has(RESPONSE_DATA_STR) ? jsonObject.getString(RESPONSE_DATA_STR) : null);
            m.setResponseId(jsonObject.has(RESPONSE_ID_STR) ? jsonObject.getString(RESPONSE_ID_STR) : null);
            m.setData(jsonObject.has(DATA_STR) ? jsonObject.getString(DATA_STR) : null);
            return m;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return m;
    }

    public static List<WebJsMessage> toArrayList(String jsonStr) {
        List<WebJsMessage> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                WebJsMessage m = new WebJsMessage();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                m.setHandlerName(jsonObject.has(HANDLER_NAME_STR) ? jsonObject.getString(HANDLER_NAME_STR) : null);
                m.setCallbackId(jsonObject.has(CALLBACK_ID_STR) ? jsonObject.getString(CALLBACK_ID_STR) : null);
                m.setResponseData(jsonObject.has(RESPONSE_DATA_STR) ? jsonObject.getString(RESPONSE_DATA_STR) : null);
                m.setResponseId(jsonObject.has(RESPONSE_ID_STR) ? jsonObject.getString(RESPONSE_ID_STR) : null);
                m.setData(jsonObject.has(DATA_STR) ? jsonObject.getString(DATA_STR) : null);
                list.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
