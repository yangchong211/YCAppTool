package com.yc.logging.util;

import android.support.annotation.RestrictTo;
import com.yc.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ReportUtils {

    private static final String PARAM_NETWORKTYPE = "networkType";
    private static final String PARAM_STATUSCODE = "statusCode";
    private static final String PARAM_ERROR_MSG = "errorMsg";
    private static final String PARAM_result = "result";
    private static final String PARAM_CATCHTYPE = "catchType";
    private static final String PARAM_SLICE_ID = "sliceid";
    private static final String PARAM_TASK_ID = "taskid";
    private static final String PARAM_INTENT_ACTION = "intentAction";
    private static final String PARAM_INTENT_EXTRA = "intentExtra";


    public static void reportProgramError(String name, Throwable err) {

    }

    public static void reportUploadTaskResult(boolean success, String net, String taskId, String err) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(PARAM_result, success ? 1 : 0);
        attrs.put(PARAM_NETWORKTYPE, net);
        attrs.put(PARAM_TASK_ID, taskId);
        attrs.put(PARAM_ERROR_MSG, err);
        attrs.put("serverUrl", LoggerFactory.getConfig().getServerHost());
    }

    public static void reportQueryTaskResult(String result) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(PARAM_result, result);
        attrs.put("serverUrl", LoggerFactory.getConfig().getServerHost());
    }

    public static void reportUploadSliceResult(boolean success, String net, long fileLength,
            int sliceId, long sliceAt, long length, String taskId, int code, String msg, String fileName) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(PARAM_result, success ? 1 : 0);
        attrs.put(PARAM_NETWORKTYPE, net);
        attrs.put(PARAM_STATUSCODE, code);
        attrs.put(PARAM_SLICE_ID, sliceId);
        attrs.put(PARAM_ERROR_MSG, msg);
        attrs.put(PARAM_TASK_ID, taskId);
        attrs.put("fileLength", fileLength);
        attrs.put("sliceLength", length);
        attrs.put("sliceAt", sliceAt);
        attrs.put("serverUrl", LoggerFactory.getConfig().getServerHost());
        attrs.put("file", fileName);
    }

    public static void reportUploadFileTreeResult(boolean success, String net, String taskId, String response) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(PARAM_result, success ? 1 : 0);
        attrs.put(PARAM_NETWORKTYPE, net);
        attrs.put("response", response);
        attrs.put(PARAM_TASK_ID, taskId);
        attrs.put("serverUrl", LoggerFactory.getConfig().getServerHost());
    }

    public static void reportReceivePush(String action, String extra) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put(PARAM_INTENT_ACTION, action);
        attrs.put(PARAM_INTENT_EXTRA, extra);
    }

    public static void reportRequest(String url, Map<String, Object> param, String response) {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("url", url);
        attrs.put("param", param.toString());
        attrs.put("response", response);
    }

}
