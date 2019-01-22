package com.ycbjie.library.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * <pre>
 *     @author duanzheng
 *     blog  :
 *     time  : 2018/5/28.
 *     desc  : 工具类
 *     revise:
 * </pre>
 */
public class LogUtils {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void printJson(String tag, String msg) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                //最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                message = jsonObject.toString(4);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        if(isGoodJson(message)){
            printLine(tag, true);
            String[] lines = message.split(LINE_SEPARATOR);
            for (String line : lines) {
                Log.e(tag, "║ " + line);
            }
            printLine(tag, false);
        }
    }

    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.e(tag, "Http---------------------");
        } else {
            Log.e(tag, "Http---------------------");
        }
    }

    /**
     * 判断是否是json
     * @param json                      json
     * @return
     */
    private static boolean isGoodJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        } catch (JsonParseException e) {
            return false;
        }
    }


}
