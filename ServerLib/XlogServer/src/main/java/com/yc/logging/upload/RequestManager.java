package com.yc.logging.upload;

import androidx.annotation.RestrictTo;
import android.util.Pair;
import com.yc.logging.config.LoggerContext;
import com.yc.logging.LoggerFactory;
import com.yc.logging.BuildConfig;
import com.yc.logging.upload.persist.TaskRecord;
import com.yc.logging.util.Debug;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yc.toolutils.AppStringUtils;

import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;



@RestrictTo(RestrictTo.Scope.LIBRARY)
class RequestManager {

    private static final String API_UPLOAD_LOGFILE = "catch/log/slice_upload";
    private static final String API_UPLOAD_STATUS = "catch/log/upload/status";
    private static final String API_UPLOAD_FILETREE = "catch/upload_tree";
    private static final String API_QUERY_TASK = "catch/log/query_incomplete_task";

    private static final String PARAM_API = "api";
    private static final String PARAM_TOKEN = "token";
    private static final String API_VERSION = "1";
    private static final String PARAM_APPNAME = "appname";
    private static final String PARAM_NETWORKTYPE = "networkType";
    private static final String PARAM_OS = "os";
    private static final String OS_ANDROID = "android";
    private static final String PARAM_FILE = "file";
    private static final String PARAM_TASKID = "taskid";
    private static final String PARAM_TIME_STAMP = "ts";
    private static final String PARAM_FILE_LENGTH = "filelength";
    private static final String PARAM_SLICE_ID = "sliceid";
    private static final String PARAM_SLICE_AT = "sliceAt";
    public static final String OMG = "omg";


    public static final int RESPONSE_SUCCESS_CODE = 0; //服务端成功接收
    public static final int RESPONSE_ERROR_CODE = -1; //请求错误，如"ret":-1, "msg":"multipart: NextPart: EOF",重试
    public static final int RESPONSE_SLICE_REPEAT_CODE = -2; //日志分片重复上传,上传下一片
    public static final int RESPONSE_OTHER_ERROR_CODE = -3; //其他错误类型，不需要重试

    public static final int EXCEPTION_CODE = -4; //端上请求异常，如不支持协议
    public static final String RESPONSE_RET = "ret";

    static OkHttpClient sHttpClient;

    static {
        sHttpClient = new OkHttpClient.Builder()
                .connectTimeout(150, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
                .readTimeout(150, TimeUnit.SECONDS)
                .build();
    }

    public static void uploadTaskStatus(String taskId, int status, String message) {
        long ts = System.currentTimeMillis();
        String url = LoggerFactory.getConfig().getServerHost() + API_UPLOAD_STATUS;
        HashMap<String, Object> param = new HashMap<>();
        param.put("status", status);
        param.put("ts", ts);
        param.put("taskId", taskId);
        //param.put("sdk_ver", BuildConfig.VERSION_NAME);

        RequestBody formBody = new FormBody.Builder()
                .add("status", String.valueOf(status))
                .add("message", message)
                .add("ts", String.valueOf(ts))
                //.add("sdk_ver", BuildConfig.VERSION_NAME)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("taskId", taskId)
                .post(formBody)
                .build();
        try {
            Response response = sHttpClient.newCall(request).execute();
            response.body().close();
        } catch (IOException e) {
            Debug.e("uploadTaskStatus error", e);
        }
    }

    /**
     * 上传压缩后的Log文件
     * <p>
     * post请求参数，见http://wiki.intra.xiaojukeji.com/pages/viewpage.action?pageId=84181311
     * <p>
     * Header： token/taskid
     * Body:   api/ts/uid/phone/appname/os/file
     */
    static RequestResult<String> uploadSectionFile(String taskId, File zipFile, String networkType,
            int sliceId, long startPos, long endPos) {
        long ts = System.currentTimeMillis();

        RequestResult<String> requestResult = new RequestResult<>();
        HashMap<String, Object> param = new HashMap<>();
        param.put(PARAM_TASKID, taskId);
        param.put(PARAM_OS, OS_ANDROID);
        param.put(PARAM_API, API_VERSION);
        param.put(PARAM_TIME_STAMP, String.valueOf(ts));
        param.put(PARAM_APPNAME, LoggerContext.getDefault().getPackageName());
        param.put(PARAM_FILE_LENGTH, zipFile.length());
        param.put(PARAM_SLICE_ID, sliceId);
        param.put(PARAM_SLICE_AT, startPos);
        //param.put("sdk_ver", BuildConfig.VERSION_NAME);
        param.put("slice_length", endPos - startPos);

        String url = LoggerFactory.getConfig().getServerHost() + API_UPLOAD_LOGFILE;

        boolean result = false;
        String msg;
        long contentLen = endPos - startPos;
        FileRequestBody fileRequestBody = FileRequestBody.create(zipFile, startPos, contentLen);
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(PARAM_FILE, zipFile.getName(), fileRequestBody)
                .addFormDataPart(PARAM_OS, OS_ANDROID)
                .addFormDataPart(PARAM_API, API_VERSION)
                .addFormDataPart(PARAM_TIME_STAMP, String.valueOf(ts))
                .addFormDataPart(PARAM_APPNAME, LoggerContext.getDefault().getPackageName())
                .addFormDataPart(PARAM_FILE_LENGTH, String.valueOf(zipFile.length()))
                .addFormDataPart(PARAM_SLICE_ID, String.valueOf(sliceId))
                .addFormDataPart(PARAM_SLICE_AT, String.valueOf(startPos))
                //.addFormDataPart("sdk_ver", BuildConfig.VERSION_NAME)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header(PARAM_TASKID, taskId)
                .header(PARAM_SLICE_ID, String.valueOf(sliceId))
                .header(PARAM_SLICE_AT, String.valueOf(startPos))
                .post(formBody)
                .build();

        try {
            Response response = sHttpClient.newCall(request).execute();
            ResponseBody body = response.body();
            String responseString = body.string();
            msg = responseString;
            int status = response.code();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(responseString);
                status = jsonObject.optInt(RESPONSE_RET, -1);
                switch (status) {
                    case RESPONSE_SUCCESS_CODE:
                    case RESPONSE_SLICE_REPEAT_CODE:
                        result = true;
                        break;
                    case RESPONSE_ERROR_CODE:
                        result = false;
                        break;
                    default:
                        break;
                }
            }
            requestResult.setCode(status);

            body.close();
        } catch (Exception e) {
            Debug.e("uploadSectionFile error", e);
            msg = e.getMessage();
        }
        requestResult.setSuccess(result).setMsg(msg);
        return requestResult;
    }

    static Pair<TaskRecord, String> queryTask(String phoneNum) {
        long ts = System.currentTimeMillis();
        String url = LoggerFactory.getConfig().getServerHost() + API_QUERY_TASK;

        HashMap<String, Object> param = new HashMap<>();
        param.put("phone", phoneNum);
        param.put("ts", ts);
        //param.put("sdk_ver", BuildConfig.VERSION_NAME);

        RequestBody formBody = new FormBody.Builder()
                .add("phone", phoneNum)
                .add("ts", String.valueOf(ts))
                .add("appName", LoggerContext.getDefault().getPackageName())
                .add("osType", "android")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        TaskRecord record = null;
        String rawResp = null;
        try {
            Response response = sHttpClient.newCall(request).execute();
            ResponseBody body = response.body();
            rawResp = body.string();
            if (response.isSuccessful()) {
                JsonElement element = new JsonParser().parse(rawResp);
                JsonObject object = element.getAsJsonObject();
                JsonElement data = object.get("data");
                if (data != null) {
                    record = TaskRecord.fromJson(data);
                }
            }
            body.close();
        } catch (IOException e) {
            Debug.e("queryTask error", e);
        }
        return new Pair<>(record, rawResp);
    }


    public static void uploadFileTree(String taskId, String networkType, FileTree fileTree) {
        long ts = System.currentTimeMillis();
        String url = LoggerFactory.getConfig().getServerHost() + API_UPLOAD_FILETREE;

        RequestBody formBody = new FormBody.Builder()
                .add(PARAM_APPNAME, LoggerContext.getDefault().getPackageName())
                .add(PARAM_TIME_STAMP, String.valueOf(ts))
                .add(PARAM_NETWORKTYPE, networkType)
                //.add("sdk_ver", BuildConfig.VERSION_NAME)
                .add("taskid", taskId)
                .add("content", fileTree.toJson())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header(PARAM_TOKEN, AppStringUtils.MD5(OMG + ts + PARAM_API))
                .post(formBody)
                .build();
        try {
            Response response = sHttpClient.newCall(request).execute();
            ResponseBody body = response.body();
            String resp = body.string();
            body.close();
        } catch (IOException e) {
            Debug.e("uploadFileTree error", e);
        }
    }
}
