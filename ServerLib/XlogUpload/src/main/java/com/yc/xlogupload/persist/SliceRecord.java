package com.yc.xlogupload.persist;


import androidx.room.Entity;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;


import java.util.Arrays;

@SuppressWarnings("EqualsReplaceableByObjectsCall")
@Entity(tableName = "SliceRecord",
        primaryKeys = {"taskId", "sliceId"}
)
@RestrictTo(RestrictTo.Scope.LIBRARY)

public class SliceRecord {

    @NonNull
    private String taskId;
    private int sliceId;
    private int sliceCount;
    private String file;
    private long startPos;
    private long endPos;
    private long fileSize;
    private int status = STATUS_UNSTARTED;
    private int uploadCount;

    public static final int STATUS_UNSTARTED = -1;
    public static final int STATUS_UPLOADING = 0;
    public static final int STATUS_FAILED = 1;

    public SliceRecord(@NonNull String taskId, int sliceCount, int sliceId, String file,
            long fileSize, long startPos, long endPos) {
        this.taskId = taskId;
        this.sliceCount = sliceCount;
        this.sliceId = sliceId;
        this.file = file;
        this.fileSize = fileSize;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public int getSliceId() {
        return sliceId;
    }

    public void setSliceId(int sliceId) {
        this.sliceId = sliceId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public void setEndPos(long endPos) {
        this.endPos = endPos;
    }

    public int getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(int uploadCount) {
        this.uploadCount = uploadCount;
    }

    public void increaseUploadCount() {
        this.uploadCount++;
    }

    public int getSliceCount() {
        return sliceCount;
    }

    public void setSliceCount(int sliceCount) {
        this.sliceCount = sliceCount;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SliceRecord that = (SliceRecord) o;
        return sliceId == that.sliceId &&
                startPos == that.startPos &&
                endPos == that.endPos &&
                status == that.status &&
                equals(taskId, that.taskId) &&
                equals(file, that.file);
    }

    private static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    @Override
    public int hashCode() {
        return hash(taskId, sliceId, file, startPos, endPos, status);
    }

    private static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return "SliceRecord{" +
                "taskId='" + taskId + '\'' +
                ", sliceId=" + sliceId +
                ", file='" + file + '\'' +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                ", status=" + status +
                ", uploadCount=" + uploadCount +
                '}';
    }
}