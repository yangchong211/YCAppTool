package com.yc.logging.upload;

import androidx.annotation.RestrictTo;
import android.text.TextUtils;
import com.yc.logging.annotation.KeepClass;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * 获取文件目录树
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
@KeepClass
public class GetTreeTask {


    //push时间和客户端接收到的最大时间差,超过该值,忽略消息
    public static final long MAX_MESSAGE_TIME_DELTA = 20 * 1000;

    /**
     * {
     * "timestamp":"1432331241000",
     * "task_id":"a3f0e19e-6ccd-425e-8737-4b9170f145a2",
     * "event_type":100,
     * "event_id":"bamai_get_tree"
     * }
     */


    @SerializedName("event_id")
    private String eventId;

    @SerializedName("event_type")
    private int eventType;

    @SerializedName("task_id")
    private String taskId;

    /**
     * 服务器发消息时间
     * <p>
     * 设 clientTimestamp = 客户端接受到该消息的时间, 单位是s
     * <p>
     * 当且仅当 clientTimestamp - serverPushTimestamp < 5s 时,才执行该任务
     */
    @SerializedName("timestamp")
    private long serverPushTimestamp;


    public boolean hasTaskId() {
        return !TextUtils.isEmpty(taskId);
    }

    public String getTaskId() {
        return taskId;
    }

    public long getPushTimestamp() {
        return serverPushTimestamp * 1000;
    }


    public static GetTreeTask parseGetTreeTask(String extra) {
        GetTreeTask getTreeTask = null;
        try {
            Gson gson = new Gson();
            getTreeTask = gson.fromJson(extra, GetTreeTask.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getTreeTask;
    }
}
