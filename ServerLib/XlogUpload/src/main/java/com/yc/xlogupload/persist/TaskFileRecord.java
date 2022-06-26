package com.yc.xlogupload.persist;


import androidx.room.Entity;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;


@SuppressWarnings("EqualsReplaceableByObjectsCall")
@Entity(tableName = "TaskFileRecord",
        primaryKeys = {"taskId", "file"}
)
@RestrictTo(RestrictTo.Scope.LIBRARY)

public class TaskFileRecord {

    @NonNull
    private String taskId;
    @NonNull
    private String file;

    public TaskFileRecord(@NonNull String taskId, @NonNull String file) {
        this.taskId = taskId;
        this.file = file;
    }

    @NonNull
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(@NonNull String taskId) {
        this.taskId = taskId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "TaskFileRecord{" +
                "taskId='" + taskId + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}