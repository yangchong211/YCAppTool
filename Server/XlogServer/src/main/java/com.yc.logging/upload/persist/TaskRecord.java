package com.yc.logging.upload.persist;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;
import com.yc.logging.annotation.KeepClass;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

@RestrictTo(RestrictTo.Scope.LIBRARY)
@Entity(tableName = "TaskRecord")
@KeepClass
public class TaskRecord {

    @PrimaryKey
    @NonNull
    @SerializedName("task_id")
    private String taskId;

    @SerializedName("log_path")
    private String logPath;

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    @SerializedName("buffers")
    private String buffers;

    private transient String rawData;

    public TaskRecord(@NonNull String taskId) {
        this.taskId = taskId;
    }

    @NonNull
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(@NonNull String taskId) {
        this.taskId = taskId;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static TaskRecord fromJson(String json) {
        TaskRecord record = new Gson().fromJson(json, TaskRecord.class);
        record.setRawData(json);
        return record;
    }

    public static TaskRecord fromJson(JsonElement json) {
        return new Gson().fromJson(json, TaskRecord.class);
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(taskId) && !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime);
    }

    public String getBuffers() {
        return buffers;
    }

    public void setBuffers(String buffers) {
        if (TextUtils.isEmpty(buffers)) return;
        this.buffers = buffers;
    }

    public TaskRecord setRawData(String rawData) {
        this.rawData = rawData;
        return this;
    }

    public String getRawData() {
        return rawData;
    }

    @Override
    public String toString() {
        return "TaskRecord{" +
                "taskId='" + taskId + '\'' +
                ", logPath='" + logPath + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", buffers='" + buffers + '\'' +
                '}';
    }
}