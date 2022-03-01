package com.ycbjie.webviewlib.wv;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {

    private final static String CALLBACK_ID_STR = "callbackId";
    private final static String RESPONSE_ID_STR = "responseId";
    private final static String RESPONSE_DATA_STR = "responseData";
    private final static String DATA_STR = "data";
    private final static String HANDLER_NAME_STR = "handlerName";

    public static JSONObject messageToJsonObject(WvMessage message) {
        JSONObject jo = new JSONObject();
        try {
            if (message.callbackId != null) {
                jo.put(CALLBACK_ID_STR, message.callbackId);
            }
            if (message.data != null) {
                jo.put(DATA_STR, message.data);
            }
            if (message.handlerName != null) {
                jo.put(HANDLER_NAME_STR, message.handlerName);
            }
            if (message.responseId != null) {
                jo.put(RESPONSE_ID_STR, message.responseId);
            }
            if (message.responseData != null) {
                jo.put(RESPONSE_DATA_STR, message.responseData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public static WvMessage JsonObjectToMessage(JSONObject jo) {
        WvMessage message = new WvMessage();
        try {
            if (jo.has(CALLBACK_ID_STR)) {
                message.callbackId = jo.getString(CALLBACK_ID_STR);
            }
            if (jo.has(DATA_STR)) {
                message.data = jo.get(DATA_STR);
            }
            if (jo.has(HANDLER_NAME_STR)) {
                message.handlerName = jo.getString(HANDLER_NAME_STR);
            }
            if (jo.has(RESPONSE_ID_STR)) {
                message.responseId = jo.getString(RESPONSE_ID_STR);
            }
            if (jo.has(RESPONSE_DATA_STR)) {
                message.responseData = jo.get(RESPONSE_DATA_STR);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }


}
